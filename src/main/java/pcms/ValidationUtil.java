package pcms;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/** Validation utilities. */
public final class ValidationUtil {
    /** Prevent object creation. */
    private ValidationUtil() {
        // Empty
    }

    /** Make sure ID exists. */
    public static <T extends Model> int idExists(final List<T> cache, final T record) {
        final int index = Collections.binarySearch(cache, record, T::compare);
        if (index < 0) {
            throw new InvalidFieldException("id", "ID does not exist in database.");
        }
        return index;
    }

    /** Make sure record exists. */
    public static <T extends Model> T recordExists(final Repository<T> records, final String id) {
        final Optional<T> record = records.findWithId(id);
        if (record.isEmpty()) {
            throw new InvalidFieldException("id", 
                    String.format("Record with ID %s not found.", id));
        }
        return record.get();
    }

    /** Make sure field is not empty. */
    public static void notEmpty(final String label, final String x) {
        if (x.isEmpty()) {
            throw new InvalidFieldException(label, String.format("%s is required.", 
                    capitalize(label)));
        }
    }

    /** Make sure field is not empty. */
    public static void notEmpty(final String label, final char[] x) {
        if (x.length == 0) {
            throw new InvalidFieldException(label, String.format("%s is required.", 
                    capitalize(label)));
        }
    }

    /** Make sure x has minimum length of minLength. */
    public static String validMinLength(final String label, final String x, final int minLength) {
        if (x.length() < minLength) {
            throw new InvalidFieldException(label, String.format("%s length should be at least %d.",
                    capitalize(label), minLength));
        }
        return x;
    }

    /** Make sure x has username format. */
    public static String validUsernameFormat(final String label, final String x) {
        if (!x.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
            throw new InvalidFieldException(label, String.join("", capitalize(label),
                    " should start with alphabet, and can contain alphabets and numbers only."));
        }
        return x;
    }

    /** Make sure x has email format. */
    public static String validEmailFormat(final String label, final String x) {
        if (!x.matches(".+@.+\\..+")) {
            throw new InvalidFieldException(label, String.join("", capitalize(label),
                    " should have at least one '@' and '.' character."));
        }
        return x;
    }

    /** Make sure x has phone format. */
    public static String validPhoneFormat(final String label, final String x) {
        if (!x.matches("\\+?[0-9]{8,15}")) {
            throw new InvalidFieldException(label, String.join("", capitalize(label),
                    " should be 8 to 15 digits."));
        }
        return x;
    }

    /** Make sure x does not exists in cache. */
    public static <T, U> U notExists(
            final String label, 
            final U x, 
            final Collection<T> cache, 
            final Function<T, U> fieldGetter) {

        if (cache.stream().anyMatch(y -> fieldGetter.apply(y).equals(x))) {
            throw new InvalidFieldException(label, String.join("", capitalize(label), 
                    " has been used. Choose another one."));
        }
        return x;
    }

    /** Make sure x does not exists in cache, ignoring certain row. */
    public static <T, U, V> U notExists(
            final String label, 
            final U x, 
            final Collection<T> cache, 
            final Function<T, U> fieldGetter,
            final V ignore,
            final Function<T, V> ignoreGetter) {

        if (cache.stream()
                .filter(y -> !ignoreGetter.apply(y).equals(ignore))
                .anyMatch(y -> fieldGetter.apply(y).equals(x))) {

            throw new InvalidFieldException(label, String.join("", capitalize(label),
                    " has been used. Choose another one."));
        }
        return x;
    }

    private static String capitalize(final String x) {
        return x.substring(0, 1).toUpperCase(Locale.US) + x.substring(1);
    }
}
