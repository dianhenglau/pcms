package pcms.category;

import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;

import java.util.List;
import pcms.Model;

/** Models category. */
public final class Category implements Model {
    /** ID. */
    private final String id;
    /** Name. */
    private final String name;
    /** Description. */
    private final String description;

    /** Construct from Category.Builder. */
    private Category(final Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
    }

    /** Construct from row. */
    public Category(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 3) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }

        id = fields.get(0);
        name = decode(fields.get(1));
        description = decode(fields.get(2));
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        return String.join(
                ",",
                id,
                encode(name),
                encode(description));
    }

    /** Get id. */
    @Override
    public String getId() {
        return id;
    }

    /** Get name. */
    public String getName() {
        return name;
    }

    /** Get description. */
    public String getDescription() {
        return description;
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** ID. */
        private String id;
        /** Name. */
        private String name;
        /** Description. */
        private String description;

        /** Default constructor. */
        public Builder() {
            id = "";
            name = "";
            description = "";
        }

        /** Construct from category. */
        public Builder(final Category category) {
            id = category.id;
            name = category.name;
            description = category.description;
        }

        /** Set id. */
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /** Set name. */
        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        /** Set description. */
        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        /** Build category. */
        @Override
        public Category build() {
            return new Category(this);
        }
    }
}
