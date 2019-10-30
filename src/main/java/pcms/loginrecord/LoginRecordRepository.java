package pcms.loginrecord;

import java.nio.file.Path;
import java.time.Instant;
import pcms.Repository;
import pcms.ValidationUtil;
import pcms.user.UserRepository;

/** Models login record repository. */
public final class LoginRecordRepository extends Repository<LoginRecord> {
    /** Helps to check foreign key. */
    private final UserRepository userRepository;

    /** Constructor. */
    public LoginRecordRepository(final Path dataPath, final UserRepository userRepository) {
        super(dataPath, id -> new LoginRecord.Builder().withId(id).build(), LoginRecord::new);
        this.userRepository = userRepository;
    }

    /** Insert login record. */
    @Override
    public LoginRecord insert(final LoginRecord loginRecord) {
        readFromFile();

        // Make sure user ID exists.
        ValidationUtil.recordExists(userRepository, loginRecord.getUserId());

        // Add to cache
        final LoginRecord newLoginRecord = new LoginRecord.Builder(loginRecord)
                .withId(String.format("L%05d", newId))
                .withTimestamp(Instant.now())
                .build();
        cache.add(newLoginRecord);
        newId += 1;

        writeToFile();
        return newLoginRecord;
    }

    /** Should not be called. */
    @Override
    public LoginRecord update(final LoginRecord loginRecord) {
        // Empty
        return new LoginRecord.Builder().build();
    }
}
