package pcms;

import java.util.ArrayList;
import java.util.List;

/** Parsing utilities. */
public final class CsvParsingUtil {
    /** Prevent object creation. */
    private CsvParsingUtil() {
        // Empty
    }

    /** Encode string. */
    public static String encode(final String s) {
        return s.indexOf('"') == -1 && s.indexOf('\n') == -1 && s.indexOf(',') == -1
                ? s
                : String.join("", "\"", s.replace("\"", "\"\""), "\"");
    }

    /** Decode string. */
    public static String decode(final String s) {
        return (s.startsWith("\"") ? s.substring(1, s.length() - 1) : s).replace("\"\"", "\"");
    }

    /** Split into rows. */
    public static List<String> splitIntoRows(final String s) {
        return split(s, '\n', true);
    }

    /** Split into columns. */
    public static List<String> splitIntoCols(final String s) {
        return split(s, ',', false);
    }

    /** Split using separator. Separator can be ',' or '\n' only. */
    private static List<String> split(
            final String s, 
            final char separator, 
            final boolean mustEndWithSeparator) {

        final ArrayList<String> result = new ArrayList<>();
        boolean isOpened = false;
        int marker;
        int i;

        for (marker = i = 0; i < s.length(); i++) {
            if (isOpened) {
                if (s.charAt(i) != '"') {
                    continue;
                }
                // Now current character is double quote.

                // If has next character and next character is double quote.
                if (i + 1 != s.length() && s.charAt(i + 1) == '"') {
                    i++;
                    continue;
                }
                // Now current character is closing double quote.

                isOpened = false;

                // If has next character and next character is not comma nor newline.
                if (i + 1 != s.length() && s.charAt(i + 1) != ',' && s.charAt(i + 1) != '\n') {
                    throw new CsvFormatException(
                            "Comma or newline are expected after closing double quote.");
                }
            } else {
                if (s.charAt(i) == '"') {
                    isOpened = true;
                } else if (s.charAt(i) == separator) {
                    result.add(s.substring(marker, i));
                    marker = i + 1;
                }
            }
        }

        if (isOpened) {
            throw new CsvFormatException("Missing closing double-quote.");
        }

        if (marker != i) {
            if (mustEndWithSeparator) {
                throw new CsvFormatException("File does not end with a separator.");
            } else {
                result.add(s.substring(marker, i));
            }
        }

        if ("".equals(s) || s.endsWith(",")) {
            result.add("");
        }

        return result;
    }
}
