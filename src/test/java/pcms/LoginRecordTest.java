package pcms;

import static org.junit.jupiter.api.Assertions.*; // NOPMD

import org.junit.jupiter.api.Test;
import pcms.loginrecord.LoginRecord;

/** Test LoginRecord. */
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
                .withAction(LoginRecord.Action.LOGOUT);
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
                "L00013,U00007,LOGOUT,2019-10-29T23:10:55+08:00");
        assertEquals("L00013", loginRecord.getId());
        assertEquals("U00007", loginRecord.getUserId());
        assertEquals(LoginRecord.Action.LOGOUT, loginRecord.getAction());
        assertEquals(Instant.ofEpochSecond(1572361855), loginRecord.getTimestamp());
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
}
