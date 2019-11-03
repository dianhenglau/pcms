package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test User Repository. */
class UserRepositoryTest {
    // CHECKSTYLE:OFF
    /** Row 1 of users.csv. */
    private static final String ROW1 = "U00001,Ernest Day,\"547 Hickory Street, Easley, SC 29640\",ernest@example.com,[Administrator],ernest,ernest123,Active";
    /** Row 2 of users.csv. */
    private static final String ROW2 = "U00002,Jamal Rowe,\"8610 Cherry Street, Batavia, OH 45103\",jamal@example.com,[Product Manager],jamal,jamal123,Active";
    /** Row 3 of users.csv. */
    private static final String ROW3 = "U00003,Bronwen Hills,\"999 Brown Road, San Pablo, CA 94806\",bronwen@example.com,[Administrator][Product Manager],bronwen,bronwen123,Active";
    /** Row 4 of users.csv. */
    private static final String ROW4 = "U00004,Louisa Mcintyre,\"57 Sutor Court, Kingston, NY 12401\",louisa@example.com,[Administrator],louisa,louisa123,Inactive";
    /** Row 5 of users.csv. */
    private static final String ROW5 = "U00005,Jake Lindsey,\"9 Broad Street, Pompano Beach, FL 33060\",jake@example.com,[Product Manager],jake,jake123,Active";
    /** users.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");
    // CHECKSTYLE:ON

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(UserRepository.class));

        final UserRepository userRepository = new UserRepository(
                TestUtil.getDataPath("users.csv"));
        assertNotNull(userRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("users_insert.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final UserRepository userRepository = new UserRepository(filePath);
            final User newUser = new User.Builder()
                    .withFullName("Kristina Clarke")
                    .withAddress("17 Penn Street, Drexel Hill, PA 19026")
                    .withEmail("kristina@example.com")
                    .withIsAdministrator(false)
                    .withIsProductManager(true)
                    .withUsername("kristina")
                    .withPassword("kristina123")
                    .withIsActive(true)
                    .build();

            final User result = userRepository.insert(newUser);
            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), result.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("").build());
            });
            assertEquals("username", ex.getLabel()); // NOPMD
            assertEquals(TestUtil.minLenErrMsg("Username", 1), ex.getMessage()); // NOPMD

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("name with space").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("5tart_with_number").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("contain_$ymbol").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate username
                userRepository.insert(
                        new User.Builder(newUser).withUsername("kristina").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.duplicateErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("abc").withPassword("").build());
            });
            assertEquals("password", ex.getLabel());
            assertEquals(TestUtil.minLenErrMsg("Password", 1), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("abc").withEmail("").build());
            });
            assertEquals("email", ex.getLabel()); // NOPMD
            assertEquals(TestUtil.requiredErrMsg("Email"), ex.getMessage()); // NOPMD

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withUsername("abc").withEmail("abcdef").build());
            });
            assertEquals("email", ex.getLabel());
            assertEquals(TestUtil.emailFmtErrMsg("Email"), ex.getMessage());
        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test update. */
    @Test
    public void testUpdate() {
        try {
            final Path filePath = TestUtil.getDataPath("users_update.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final UserRepository userRepository = new UserRepository(filePath);
            final User user = userRepository.findWithId("U00004").get();
            final User newUser = new User.Builder(user).withPassword("louisa456").build();

            userRepository.update(newUser);
            assertEquals(
                    CONTENT.replace("louisa123", "louisa456"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(new User.Builder().withId("U00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(new User.Builder(newUser).withUsername("").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.minLenErrMsg("Username", 1), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(
                        new User.Builder(newUser).withUsername("name with space").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(
                        new User.Builder(newUser).withUsername("5tart_with_number").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(
                        new User.Builder(newUser).withUsername("contain_$ymbol").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.usernameFmtErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate username
                userRepository.update(
                        new User.Builder(newUser).withUsername("jamal").build());
            });
            assertEquals("username", ex.getLabel());
            assertEquals(TestUtil.duplicateErrMsg("Username"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.update(
                        new User.Builder(newUser).withPassword("").build());
            });
            assertEquals("password", ex.getLabel());
            assertEquals(TestUtil.minLenErrMsg("Password", 1), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withEmail("").build());
            });
            assertEquals("email", ex.getLabel());
            assertEquals(TestUtil.requiredErrMsg("Email"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                userRepository.insert(
                        new User.Builder(newUser).withEmail("abcdef").build());
            });
            assertEquals("email", ex.getLabel());
            assertEquals(TestUtil.emailFmtErrMsg("Email"), ex.getMessage());
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
            final UserRepository userRepository = new UserRepository(filePath);

            User newUser = new User.Builder()
                    .withFullName("Barack Obama")
                    .withAddress("75 Peachtree Street, Lansdale, PA 19446")
                    .withEmail("barack@example.com")
                    .withIsAdministrator(false)
                    .withIsProductManager(true)
                    .withUsername("barack")
                    .withPassword("barack123")
                    .withIsActive(true)
                    .build();
            newUser = userRepository.insert(newUser);
            assertEquals(
                    String.join("\n", "2", newUser.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            fail(ex);
        }
    }

}
