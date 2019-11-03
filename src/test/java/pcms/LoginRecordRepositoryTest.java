package pcms; // NOPMD

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.loginrecord.LoginRecord;
import pcms.loginrecord.LoginRecordRepository;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test login record repository. */
@ExtendWith(MockitoExtension.class)
class LoginRecordRepositoryTest {
    /** Row 1 of login_records.csv. */
    private static final String ROW1 = "L00001,U00001,LOGIN,1970-01-01T00:00:00Z";
    /** Row 2 of login_records.csv. */
    private static final String ROW2 = "L00002,U00002,LOGIN,1970-01-01T00:00:01Z";
    /** Row 3 of login_records.csv. */
    private static final String ROW3 = "L00003,U00001,LOGOUT,1970-01-01T00:00:03Z";
    /** Row 4 of login_records.csv. */
    private static final String ROW4 = "L00004,U00003,LOGIN,1970-01-01T00:00:04Z";
    /** Row 5 of login_records.csv. */
    private static final String ROW5 = "L00005,U00002,LOGOUT,1970-01-01T00:00:05Z";
    /** login_records.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(LoginRecordRepository.class));

        final LoginRecordRepository loginRecordRepository = new LoginRecordRepository(
                TestUtil.getDataPath("login_records.csv"), // NOPMD
                mock(UserRepository.class));
        assertNotNull(loginRecordRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("login_records_insert.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Mocking
            final User user = mock(User.class);
            final UserRepository userRepository = mock(UserRepository.class);
            when(userRepository.findWithId(any()))
                    .thenReturn(Optional.of(user))
                    .thenReturn(Optional.empty());

            // Start testing.
            final LoginRecordRepository loginRecordRepository = new LoginRecordRepository(
                    filePath, userRepository);
            final LoginRecord newLoginRecord = new LoginRecord.Builder()
                    .withUserId("U00003")
                    .withAction(LoginRecord.Action.LOGOUT)
                    .build();

            final LoginRecord result = loginRecordRepository.insert(newLoginRecord);
            verify(userRepository).findWithId("U00003");

            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), result.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(
                        new LoginRecord.Builder(newLoginRecord).withUserId("U00008").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals("Record with ID U00008 not found.", ex.getMessage());
            verify(userRepository).findWithId("U00008");
        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test with non existent file. */
    @Test
    public void testWithNonExistentFile() {
        try {
            final Path filePath = TestUtil.getDataPath("non_existent_file.csv");
            Files.deleteIfExists(filePath);

            // Mocking
            final User user = mock(User.class);
            final UserRepository userRepository = mock(UserRepository.class);
            when(userRepository.findWithId(any())).thenReturn(Optional.of(user));

            final LoginRecordRepository loginRecordRepository 
                    = new LoginRecordRepository(filePath, userRepository);

            final LoginRecord newLoginRecord = new LoginRecord.Builder()
                    .withUserId("U00001")
                    .withAction(LoginRecord.Action.LOGIN)
                    .build();
            final LoginRecord result = loginRecordRepository.insert(newLoginRecord);

            verify(userRepository).findWithId("U00001");
            assertEquals(
                    String.join("\n", "2", result.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            fail(ex);
        }
    }
}
