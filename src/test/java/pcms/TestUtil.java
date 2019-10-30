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
}
