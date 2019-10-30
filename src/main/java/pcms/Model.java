package pcms;

/** Model interface. */
public interface Model {
    /** Compare two record. */
    static int compare(final Model a, final Model b) {
        return a.getId().compareTo(b.getId());
    }

    /** Get id. */
    String getId();

    /** Convert to row. */
    String toRow();

    /** Builder interface. */
    interface Builder {
        /** Set id. */
        Builder withId(final String id);

        /** Build model. */
        Model build();
    }
}
