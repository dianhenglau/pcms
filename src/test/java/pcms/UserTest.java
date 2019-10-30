package pcms;

import static org.junit.jupiter.api.Assertions.*; // NOPMD

import org.junit.jupiter.api.Test;
import pcms.user.User;

/** Test User. */
class UserTest {
    /** Test empty user. */
    @Test 
    public void testEmptyUser() {
        final User user = new User.Builder().build();
        assertEquals("", user.getId());
        assertEquals("", user.getFullName());
        assertEquals("", user.getAddress());
        assertEquals("", user.getEmail());
        assertFalse(user.isAdministrator());
        assertFalse(user.isProductManager());
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertFalse(user.isActive());
    }

    /** Test complete user. */
    @Test 
    public void testCompleteUser() {
        final User user = new User.Builder()
                .withId("U007")
                .withFullName("James Bond")
                .withAddress("1, Jalan Dua, Taman Tiga")
                .withEmail("james@example.com")
                .withIsAdministrator(true)
                .withIsProductManager(false)
                .withUsername("james")
                .withPassword("james123")
                .withIsActive(true)
                .build();

        assertEquals("U007", user.getId());
        assertEquals("James Bond", user.getFullName());
        assertEquals("1, Jalan Dua, Taman Tiga", user.getAddress());
        assertEquals("james@example.com", user.getEmail());
        assertTrue(user.isAdministrator());
        assertFalse(user.isProductManager());
        assertEquals("james", user.getUsername());
        assertEquals("james123", user.getPassword());
        assertTrue(user.isActive());
    }

    /** Test incomplete user. */
    @Test 
    public void testIncompleteUser() {
        final User user = new User.Builder()
                .withId("U008")
                .withIsAdministrator(true)
                .withUsername("jamie")
                .withPassword("jamie123")
                .build();

        assertEquals("U008", user.getId());
        assertTrue(user.isAdministrator());
        assertFalse(user.isProductManager());
        assertEquals("jamie", user.getUsername());
        assertEquals("jamie123", user.getPassword());

        assertEquals("", user.getFullName());
        assertEquals("", user.getAddress());
        assertEquals("", user.getEmail());
        assertFalse(user.isActive());
    }

    /** Test role. */
    @Test
    public void testRole() {
        User user = new User.Builder()
                .withIsAdministrator(true)
                .withIsProductManager(true)
                .build();
        assertTrue(user.isAdministrator());
        assertTrue(user.isProductManager());

        user = new User.Builder()
                .withIsAdministrator(false)
                .withIsProductManager(false)
                .build();
        assertFalse(user.isAdministrator());
        assertFalse(user.isProductManager());

        user = new User.Builder().withIsAdministrator(true).build();
        assertTrue(user.isAdministrator());
        assertFalse(user.isProductManager());

        user = new User.Builder().withIsProductManager(true).build();
        assertFalse(user.isAdministrator());
        assertTrue(user.isProductManager());

        user = new User.Builder().withIsAdministrator(false).build();
        assertFalse(user.isAdministrator());
        assertFalse(user.isProductManager());

        user = new User.Builder().withIsProductManager(false).build();
        assertFalse(user.isAdministrator());
        assertFalse(user.isProductManager());

        user = new User.Builder().build();
        assertFalse(user.isAdministrator());
        assertFalse(user.isProductManager());
    }

    /** Test construct from row. */
    @Test
    public void testParse() {
        final User user = new User("U00013,Samantha Bee,"
                + "\"1, Jalan Dua, Taman Tiga, 12345, KL, Malaysia.\",samantha@example.com,"
                + "[Administrator],sam,sam123,Active");
        assertEquals("U00013", user.getId());
        assertEquals("Samantha Bee", user.getFullName());
        assertEquals("1, Jalan Dua, Taman Tiga, 12345, KL, Malaysia.", user.getAddress());
        assertEquals("samantha@example.com", user.getEmail());
        assertTrue(user.isAdministrator());
        assertFalse(user.isProductManager());
        assertEquals("sam", user.getUsername());
        assertEquals("sam123", user.getPassword());
        assertTrue(user.isActive());
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final User user = new User.Builder()
                .withId("U008")
                .withFullName("Johnny English")
                .withAddress("4, Jalan Lima, Taman Enam")
                .withEmail("johnny@example.com")
                .withIsAdministrator(false)
                .withIsProductManager(true)
                .withUsername("johnny")
                .withPassword("johnny123")
                .withIsActive(false)
                .build();
        assertEquals("U008,Johnny English,\"4, Jalan Lima, Taman Enam\",johnny@example.com,"
                + "[Product Manager],johnny,johnny123,Inactive", user.toRow());
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        final String userRecord = "U009,Samurai Jack,No idea where,jack@example.com," 
                + "[Administrator][Product Manager],jack,jack123,Active";
        assertEquals(userRecord, new User(userRecord).toRow());
    }
}
