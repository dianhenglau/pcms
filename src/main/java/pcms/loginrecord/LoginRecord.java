package pcms.loginrecord;

import static pcms.CsvParsingUtil.splitIntoCols;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import pcms.Model;
import pcms.user.User;
import pcms.user.UserRepository;

/** Models login record. */
public final class LoginRecord implements Model {
    /** User repository which help to get user. */
    private static Optional<UserRepository> userRepository = Optional.empty();

    /** List of actions. */
    public enum Action {
        LOGIN, LOGOUT
    }

    /** ID. */
    private final String id;
    /** User ID. */
    private final String userId;
    /** Action. */
    private final Action action;
    /** Data and time. */
    private final Instant timestamp;

    /** Construct from builder. */
    private LoginRecord(final Builder builder) {
        id = builder.id;
        userId = builder.userId;
        action = builder.action;
        timestamp = builder.timestamp;
    }

    /** Construct from row. */
    public LoginRecord(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 4) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }
        id = fields.get(0);
        userId = fields.get(1);
        action = Action.valueOf(fields.get(2));
        timestamp = Instant.parse(fields.get(3));
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        return String.join(
                ",",
                id,
                userId,
                action.name(),
                timestamp.toString());
    }

    /** Get id. */
    @Override
    public String getId() {
        return id;
    }

    /** Get userId. */
    public String getUserId() {
        return userId;
    }

    /** Get action. */
    public Action getAction() {
        return action;
    }

    /** Get timestamp. */
    public Instant getTimestamp() {
        return timestamp;
    }

    /** Get user. */
    public User getUser() {
        return userRepository.get().findWithId(userId).get();
    }

    /** Set user repository. */
    public static void setUserRepository(final UserRepository repository) {
        userRepository = Optional.of(repository);
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** Temporary variable. */
        private String id;
        /** Temporary variable. */
        private String userId;
        /** Temporary variable. */
        private Action action;
        /** Temporary variable. */
        private Instant timestamp;

        /** Default constructor. */
        public Builder() {
            id = "";
            userId = "";
            action = Action.LOGIN;
            timestamp = Instant.ofEpochSecond(0);
        }

        /** Construct from login record. */
        public Builder(final LoginRecord loginRecord) {
            id = loginRecord.id;
            userId = loginRecord.userId;
            action = loginRecord.action;
            timestamp = loginRecord.timestamp;
        }

        /** Set ID. */
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /** Set user ID. */
        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        /** Set action. */
        public Builder withAction(final Action action) {
            this.action = action;
            return this;
        }

        /** Set timestamp. */
        public Builder withTimestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /** Build user. */
        @Override
        public LoginRecord build() {
            return new LoginRecord(this);
        }
    }
}
