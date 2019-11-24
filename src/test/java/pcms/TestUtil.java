package pcms;

import java.nio.file.Path;

/** Test utilities. */
final class TestUtil {
    /** Prevent object creation. */
    private TestUtil() {
        // Empty
    }

    /** Get test data path. */
    public static Path getDataPath(final String filename) {
        return Path.of("data", "test", filename);
    }

    /** Minimum length error message. */
    public static String minLenErrMsg(final String label, final int min) {
        return String.format("%s length should be at least %d.", label, min);
    }

    /** Duplicate error message. */
    public static String duplicateErrMsg(final String label) {
        return String.format("%s has been used. Choose another one.", label);
    }

    /** Key not found error message. */
    public static String keyNotFoundErrMsg(final String label) {
        return String.format("%s does not exist in database.", label);
    }

    /** Required error message. */
    public static String requiredErrMsg(final String label) {
        return String.format("%s is required.", label);
    }

    /** Username format error message. */
    public static String usernameFmtErrMsg(final String label) {
        return String.format("%s should start with alphabet, and can contain alphabets and "
                + "numbers only.", label);
    }

    /** Email format error message. */
    public static String emailFmtErrMsg(final String label) {
        return String.format("%s should have at least one '@' and '.' character.", label);
    }

    /** Phone format error message. */
    public static String phoneFmtErrMsg(final String label) {
        return String.format("%s should be 8 to 15 digits.", label);
    }

    /** Negative error message. */
    public static String negativeErrMsg(final String label) {
        return String.format("%s cannot be negative.", label);
    }

    /** Record not found error message. */
    public static String recordNotFoundErrMsg(final String id) {
        return String.format("Record with ID %s not found.", id);
    }

}
