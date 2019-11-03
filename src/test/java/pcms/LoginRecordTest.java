package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.loginrecord.LoginRecord;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test LoginRecord. */
@ExtendWith(MockitoExtension.class)
class LoginRecordTest {
    /** Test empty login record. */
    @Test 
    public void testEmptyLoginRecord() {
        final LoginRecord loginRecord = new LoginRecord.Builder().build();
        assertEquals("", loginRecord.getId());
        assertEquals("", loginRecord.getUserId());
        assertEquals(LoginRecord.Action.LOGIN, loginRecord.getAction());
        assertEquals(Instant.ofEpochSecond(0), loginRecord.getTimestamp());
    }

    /** Test complete login record. */
    @Test 
    public void testCompleteLoginRecord() {
        final LoginRecord loginRecord = new LoginRecord.Builder()
                .withId("L007")
                .withUserId("U007")
                .withAction(LoginRecord.Action.LOGOUT)
                .withTimestamp(Instant.ofEpochSecond(10))
                .build();

        assertEquals("L007", loginRecord.getId());
        assertEquals("U007", loginRecord.getUserId());
        assertEquals(LoginRecord.Action.LOGOUT, loginRecord.getAction());
        assertEquals(Instant.ofEpochSecond(10), loginRecord.getTimestamp());
    }

    /** Test incomplete login record. */
    @Test 
    public void testIncompleteLoginRecord() {
        final LoginRecord loginRecord = new LoginRecord.Builder()
                .withId("L008")
                .withAction(LoginRecord.Action.LOGOUT)
                .build();

        assertEquals("L008", loginRecord.getId());
        assertEquals("", loginRecord.getUserId());
        assertEquals(LoginRecord.Action.LOGOUT, loginRecord.getAction());
        assertEquals(Instant.ofEpochSecond(0), loginRecord.getTimestamp());
    }

    /** Test construct from row. */
    @Test
    public void testConstructFromRow() {
        final LoginRecord loginRecord = new LoginRecord(
                "L00013,U00007,LOGOUT,2019-10-29T15:10:55Z");
        assertEquals("L00013", loginRecord.getId());
        assertEquals("U00007", loginRecord.getUserId());
        assertEquals(LoginRecord.Action.LOGOUT, loginRecord.getAction());
        assertEquals(Instant.ofEpochSecond(1_572_361_855), loginRecord.getTimestamp());

        assertEquals("Fields count incorrect.", assertThrows(IllegalArgumentException.class, () -> {
            new LoginRecord(",,"); }).getMessage());
        assertTrue(assertThrows(IllegalArgumentException.class, () -> {
            new LoginRecord(",,LOGUP,"); }).getMessage().startsWith("No enum constant"));
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final LoginRecord loginRecord = new LoginRecord.Builder()
                .withId("L00030")
                .withUserId("U00123")
                .withAction(LoginRecord.Action.LOGIN)
                .withTimestamp(Instant.ofEpochSecond(4))
                .build();
        assertEquals("L00030,U00123,LOGIN,1970-01-01T00:00:04Z", loginRecord.toRow());
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        final String s = "L54321,U12345,LOGOUT,2019-10-29T23:07:07Z";
        assertEquals(s, new LoginRecord(s).toRow());
    }

    /** Test get user. */
    @Test
    public void testGetUser() {
        // Mocking
        final User user = mock(User.class);
        final UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findWithId(any())).thenReturn(Optional.of(user));

        LoginRecord.setUserRepository(userRepository);
        final LoginRecord loginRecord = new LoginRecord.Builder().build();
        assertSame(user, loginRecord.getUser());
    }
}
