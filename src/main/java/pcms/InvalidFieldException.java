package pcms;

/** Invalid field exception. */
public class InvalidFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** Field label. */
    private final String label;

    /** Constructor. */
    public InvalidFieldException(final String label, final String message) {
        super(message);
        this.label = label;
    }

    /** Get label. */
    public String getLabel() {
        return label;
    }
}
