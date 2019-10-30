package pcms;

/** CSV format exception. */
public class CsvFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    public CsvFormatException(final String message) {
        super(message);
    }
}
