package pcms; // NOPMD

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON
import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;
import static pcms.CsvParsingUtil.splitIntoRows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Test CsvParsingUtil. */
class CsvParsingUtilTest {
    /** Test encode. */
    @Test 
    public void testEncode() {
        assertEquals("", encode(""));
        assertEquals("  ", encode("  "));
        assertEquals("a b", encode("a b"));
        assertEquals("hello", encode("hello"));
        assertEquals("123", encode("123"));
        assertEquals("\"\"\"Yellow!\"\"\"", encode("\"Yellow!\""));
        assertEquals("\"\"\"What's your problem?\"\", he asked.\"", 
                encode("\"What's your problem?\", he asked."));
        assertEquals("\"Hello\nWorld!\"", encode("Hello\nWorld!"));
        assertEquals("\"hello,world\"", encode("hello,world"));
        assertEquals("\"\"\"\"", encode("\""));
        assertEquals("\",\"", encode(","));
        assertEquals("\"\n\"", encode("\n"));
    }

    /** Test decode. */
    @Test
    public void testDecode() {
        assertEquals("", decode(""));
        assertEquals("   ", decode("   "));
        assertEquals("c d", decode("c d"));
        assertEquals("say", decode("say"));
        assertEquals("876", decode("876"));
        assertEquals("\"Red!\"", decode("\"\"\"Red!\"\"\""));
        assertEquals("\"Nothing\", she answered.",
                decode("\"\"\"Nothing\"\", she answered.\""));
        assertEquals("Goodbye\nWorld!", decode("\"Goodbye\nWorld!\""));
        assertEquals("goodbye,world", decode("\"goodbye,world\""));
        assertEquals("\"", decode("\"\"\"\""));
        assertEquals(",", decode("\",\""));
        assertEquals("\n", decode("\"\n\""));
    }

    /** Test split into rows. */
    @Test
    public void testSplitIntoRows() {
        try {
            final List<String> rows = splitIntoRows(Files.readString(
                    TestUtil.getDataPath("correct.csv"),
                    StandardCharsets.UTF_8));
            assertEquals(16, rows.size());
            assertEquals("1,2,3", rows.get(0));
            assertEquals("4,5,6", rows.get(1));
            assertEquals(",,", rows.get(2));
            assertEquals("17,18,19", rows.get(3));
            assertEquals("", rows.get(4));
            assertEquals("", rows.get(5));
            assertEquals("\"hello\",world", rows.get(6));
            assertEquals("goodbye,\"world\"", rows.get(7));
            assertEquals("\"\"\"what!?\"\", he asked\",nah", rows.get(8));
            assertEquals("\"\"\"goodbye\"\"\",\"\"\"\"", rows.get(9));
            assertEquals("\"wow,wah\", hey", rows.get(10));
            assertEquals("\"ha,hi,hu,he,ho\",\",\"", rows.get(11));
            assertEquals("\"say\nsomething\",\"hey\n\nhoh\"", rows.get(12));
            assertEquals("\"my\nlove\n\",oh", rows.get(13));
            assertEquals("if you're happy, and you know it", rows.get(14));
            assertEquals("clap your hand, clap, clap", rows.get(15));
        } catch (IOException ex) {
            fail(ex);
        }

        try {
            final String s = Files.readString(
                    TestUtil.getDataPath("separator_expected_error.csv"),
                    StandardCharsets.UTF_8);
            final Throwable ex = assertThrows(CsvFormatException.class, () -> {
                splitIntoRows(s);
            });
            assertEquals("Comma or newline are expected after closing double quote.", 
                    ex.getMessage());
        } catch (IOException ex) {
            fail(ex);
        }

        try {
            final String s = Files.readString(
                    TestUtil.getDataPath("closing_quote_expected_error.csv"),
                    StandardCharsets.UTF_8);
            final Throwable ex = assertThrows(CsvFormatException.class, () -> {
                splitIntoRows(s);
            });
            assertEquals("Missing closing double-quote.", ex.getMessage());
        } catch (IOException ex) {
            fail(ex);
        }

        try {
            final String s = Files.readString(
                    TestUtil.getDataPath("newline_at_eof_expected_error.csv"),
                    StandardCharsets.UTF_8);
            final Throwable ex = assertThrows(CsvFormatException.class, () -> {
                splitIntoRows(s);
            });
            assertEquals("File does not end with a separator.", ex.getMessage());
        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test split into columns. */
    @Test
    public void testSplitIntoCols() {
        try {
            final List<String> rows = splitIntoRows(Files.readString(
                    TestUtil.getDataPath("correct.csv"),
                    StandardCharsets.UTF_8));
            assertEquals(16, rows.size());

            List<String> cols = splitIntoCols(rows.get(0));
            assertEquals(3, cols.size());
            assertEquals("1", cols.get(0));
            assertEquals("2", cols.get(1));
            assertEquals("3", cols.get(2));

            cols = splitIntoCols(rows.get(2));
            assertEquals(3, cols.size());
            assertEquals("", cols.get(0));
            assertEquals("", cols.get(1));
            assertEquals("", cols.get(2));

            cols = splitIntoCols(rows.get(4));
            assertEquals(1, cols.size());
            assertEquals("", cols.get(0));

            cols = splitIntoCols(rows.get(6));
            assertEquals(2, cols.size());
            assertEquals("\"hello\"", cols.get(0));
            assertEquals("world", cols.get(1));

            cols = splitIntoCols(rows.get(8));
            assertEquals(2, cols.size());
            assertEquals("\"\"\"what!?\"\", he asked\"", cols.get(0));
            assertEquals("nah", cols.get(1));

            cols = splitIntoCols(rows.get(9));
            assertEquals(2, cols.size());
            assertEquals("\"\"\"goodbye\"\"\"", cols.get(0));
            assertEquals("\"\"\"\"", cols.get(1));

            cols = splitIntoCols(rows.get(10));
            assertEquals(2, cols.size());
            assertEquals("\"wow,wah\"", cols.get(0));
            assertEquals(" hey", cols.get(1));

            cols = splitIntoCols(rows.get(11));
            assertEquals(2, cols.size());
            assertEquals("\"ha,hi,hu,he,ho\"", cols.get(0));
            assertEquals("\",\"", cols.get(1));

            cols = splitIntoCols(rows.get(12));
            assertEquals(2, cols.size());
            assertEquals("\"say\nsomething\"", cols.get(0));
            assertEquals("\"hey\n\nhoh\"", cols.get(1));

            cols = splitIntoCols(rows.get(15));
            assertEquals(3, cols.size());
            assertEquals("clap your hand", cols.get(0));
            assertEquals(" clap", cols.get(1));
            assertEquals(" clap", cols.get(2));
        } catch (IOException ex) {
            fail(ex);
        }
    }
}
