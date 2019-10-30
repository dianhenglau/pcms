package pcms; // NOPMD

import static org.junit.jupiter.api.Assertions.*; // NOPMD
import static org.mockito.Mockito.*; // NOPMD

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import pcms.loginrecord.LoginRecord;
import pcms.loginrecord.LoginRecordRepository;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test login record repository. */
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
            when(userRepository.findWithId(any())).thenReturn(Optional.of(user));

            // Start testing.
            final LoginRecordRepository loginRecordRepository = new LoginRecordRepository(
                    filePath, userRepository);
            final LoginRecord.Builder builder = new LoginRecord.Builder()
                    .withFullName("Kristina Clarke")
                    .withAddress("17 Penn Street, Drexel Hill, PA 19026")
                    .withEmail("kristina@example.com")
                    .withIsAdministrator(false)
                    .withIsProductManager(true)
                    .withLoginRecordname("kristina")
                    .withPassword("kristina123")
                    .withIsActive(true);

            LoginRecord newLoginRecord = builder.build();
            newLoginRecord = loginRecordRepository.insert(newLoginRecord);
            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), newLoginRecord.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(builder.withLoginRecordname("").build());
            });
            assertEquals("loginRecordname", ex.getLabel()); // NOPMD
            assertEquals("LoginRecordname" + MSG1, ex.getMessage()); // NOPMD

            ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(builder.withLoginRecordname("name with space").build());
            });
            assertEquals("loginRecordname", ex.getLabel());
            assertEquals("LoginRecordname" + MSG2, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(builder.withLoginRecordname("5tart_with_number").build());
            });
            assertEquals("loginRecordname", ex.getLabel());
            assertEquals("LoginRecordname" + MSG2, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(builder.withLoginRecordname("contain_$ymbol").build());
            });
            assertEquals("loginRecordname", ex.getLabel());
            assertEquals("LoginRecordname" + MSG2, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate loginRecordname
                loginRecordRepository.insert(builder.withLoginRecordname("kristina").build());
            });
            assertEquals("loginRecordname", ex.getLabel());
            assertEquals("LoginRecordname" + MSG3, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                loginRecordRepository.insert(builder.withLoginRecordname("isabelle").withPassword("").build());
            });
            assertEquals("password", ex.getLabel());
            assertEquals("Password" + MSG1, ex.getMessage());
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
            final LoginRecordRepository loginRecordRepository = new LoginRecordRepository(filePath);

            LoginRecord newLoginRecord = new LoginRecord.Builder()
                    .withFullName("Barack Obama")
                    .withAddress("75 Peachtree Street, Lansdale, PA 19446")
                    .withEmail("barack@example.com")
                    .withIsAdministrator(false)
                    .withIsProductManager(true)
                    .withLoginRecordname("barack")
                    .withPassword("barack123")
                    .withIsActive(true)
                    .build();
            newLoginRecord = loginRecordRepository.insert(newLoginRecord);
            assertEquals(
                    String.join("\n", "2", newLoginRecord.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            fail(ex);
        }
    }

}
