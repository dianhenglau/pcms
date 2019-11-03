package pcms.user;

import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;

import java.util.List;
import pcms.Model;

/** Models user. */
public final class User implements Model {
    /** String constant for administrator. */
    private static final String ADMIN = "[Administrator]";
    /** String constant for product manager. */
    private static final String PM = "[Product Manager]";

    /** ID. */
    private final String id;
    /** Full name. */
    private final String fullName;
    /** Address. */
    private final String address;
    /** Email. */
    private final String email;
    /** Is administrator. */
    private final boolean isAdmin;
    /** Is product manager. */
    private final boolean isPM;
    /** Username. */
    private final String username;
    /** Password. */
    private final String password;
    /** Active. */
    private final boolean active;

    /** Construct from User.Builder. */
    private User(final Builder builder) {
        id = builder.id;
        fullName = builder.fullName;
        address = builder.address;
        email = builder.email;
        isAdmin = builder.isAdmin;
        isPM = builder.isPM;
        username = builder.username;
        password = builder.password;
        active = builder.active;
    }

    /** Construct from row. */
    public User(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 8) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }
        if (!fields.get(4).isEmpty()
                && !fields.get(4).equals(ADMIN)
                && !fields.get(4).equals(PM)
                && !fields.get(4).equals(ADMIN + PM)) {

            throw new IllegalArgumentException("Role invalid.");
        }
        if (!fields.get(7).equals("Active") && !fields.get(7).equals("Inactive")) {
            throw new IllegalArgumentException("Status invalid.");
        }

        id = fields.get(0);
        fullName = decode(fields.get(1));
        address = decode(fields.get(2));
        email = decode(fields.get(3));
        isAdmin = fields.get(4).contains(ADMIN);
        isPM = fields.get(4).contains(PM);
        username = decode(fields.get(5));
        password = decode(fields.get(6));
        active = fields.get(7).equals("Active");
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        return String.join(
                ",",
                id,
                encode(fullName),
                encode(address),
                encode(email),
                (isAdmin ? ADMIN : "") 
                + (isPM ? PM : ""),
                encode(username),
                encode(password),
                active ? "Active" : "Inactive");
    }

    /** Get id. */
    @Override
    public String getId() {
        return id;
    }

    /** Get fullName. */
    public String getFullName() {
        return fullName;
    }

    /** Get address. */
    public String getAddress() {
        return address;
    }

    /** Get email. */
    public String getEmail() {
        return email;
    }

    /** Is administrator. */
    public boolean isAdministrator() {
        return isAdmin;
    }

    /** Is product manager. */
    public boolean isProductManager() {
        return isPM;
    }

    /** Get username. */
    public String getUsername() {
        return username;
    }

    /** Get password. */
    public String getPassword() {
        return password;
    }

    /** Is active. */
    public boolean isActive() {
        return active;
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** ID. */
        private String id;
        /** Full name. */
        private String fullName;
        /** Address. */
        private String address;
        /** Email. */
        private String email;
        /** Is administrator. */
        private boolean isAdmin;
        /** Is product manager. */
        private boolean isPM;
        /** Username. */
        private String username;
        /** Password. */
        private String password;
        /** Is active. */
        private boolean active;

        /** Default constructor. */
        public Builder() {
            id = "";
            fullName = "";
            address = "";
            email = "";
            isAdmin = false;
            isPM = false;
            username = "";
            password = "";
            active = false;
        }

        /** Construct from user. */
        public Builder(final User user) {
            id = user.id;
            fullName = user.fullName;
            address = user.address;
            email = user.email;
            isAdmin = user.isAdmin;
            isPM = user.isPM;
            username = user.username;
            password = user.password;
            active = user.active;
        }

        /** Set id. */
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /** Set fullName. */
        public Builder withFullName(final String fullName) {
            this.fullName = fullName;
            return this;
        }

        /** Set address. */
        public Builder withAddress(final String address) {
            this.address = address;
            return this;
        }

        /** Set email. */
        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        /** Set isAdministrator. */
        public Builder withIsAdministrator(final boolean isAdministrator) {
            this.isAdmin = isAdministrator;
            return this;
        }

        /** Set isProductManager. */
        public Builder withIsProductManager(final boolean isProductManager) {
            this.isPM = isProductManager;
            return this;
        }

        /** Set username. */
        public Builder withUsername(final String username) {
            this.username = username;
            return this;
        }

        /** Set password. */
        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        /** Set isActive. */
        public Builder withIsActive(final boolean isActive) {
            this.active = isActive;
            return this;
        }

        /** Build user. */
        @Override
        public User build() {
            return new User(this);
        }
    }
}
