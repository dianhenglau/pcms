package pcms.supplier;

import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;

import java.util.List;
import pcms.Model;

/** Models supplier. */
public final class Supplier implements Model {
    ///** Product repository which help to get products. */
    //private static Optional<ProductRepository> productRepository = Optional.empty();

    /** ID. */
    private final String id;
    /** Name. */
    private final String name;
    /** Email. */
    private final String email;
    /** Phone. */
    private final String phone;
    /** Address. */
    private final String address;
    /** Active. */
    private final boolean active;

    /** Construct from Supplier.Builder. */
    private Supplier(final Builder builder) {
        id = builder.id;
        name = builder.name;
        email = builder.email;
        phone = builder.phone;
        address = builder.address;
        active = builder.active;
    }

    /** Construct from row. */
    public Supplier(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 6) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }
        if (!fields.get(5).equals("Active") && !fields.get(5).equals("Inactive")) {
            throw new IllegalArgumentException("Status invalid.");
        }

        id = fields.get(0);
        name = decode(fields.get(1));
        email = decode(fields.get(2));
        phone = decode(fields.get(3));
        address = decode(fields.get(4));
        active = fields.get(5).equals("Active");
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        return String.join(
                ",",
                id,
                encode(name),
                encode(email),
                encode(phone),
                encode(address),
                active ? "Active" : "Inactive");
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

    /** Get email. */
    public String getEmail() {
        return email;
    }

    /** Get phone. */
    public String getPhone() {
        return phone;
    }

    /** Get address. */
    public String getAddress() {
        return address;
    }

    /** Is active. */
    public boolean isActive() {
        return active;
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** ID. */
        private String id;
        /** Name. */
        private String name;
        /** Email. */
        private String email;
        /** Phone. */
        private String phone;
        /** Address. */
        private String address;
        /** Is active. */
        private boolean active;

        /** Default constructor. */
        public Builder() {
            id = "";
            name = "";
            email = "";
            phone = "";
            address = "";
            active = false;
        }

        /** Construct from supplier. */
        public Builder(final Supplier supplier) {
            id = supplier.id;
            name = supplier.name;
            email = supplier.email;
            phone = supplier.phone;
            address = supplier.address;
            active = supplier.active;
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

        /** Set email. */
        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        /** Set phone. */
        public Builder withPhone(final String phone) {
            this.phone = phone;
            return this;
        }

        /** Set address. */
        public Builder withAddress(final String address) {
            this.address = address;
            return this;
        }

        /** Set isActive. */
        public Builder withIsActive(final boolean isActive) {
            this.active = isActive;
            return this;
        }

        /** Build supplier. */
        @Override
        public Supplier build() {
            return new Supplier(this);
        }
    }
}
