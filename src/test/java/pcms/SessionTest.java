package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import org.junit.jupiter.api.Test;
import pcms.user.User;

/** Test Session. */
class SessionTest {
    /** Test constructor, getter, setter. */
    @Test
    public void testConstructorGetterSetter() {
        final Session session = new Session();
        assertTrue(session.getUser().isEmpty());
        
        final User user = new User.Builder().build();
        session.setUser(user);
        assertFalse(session.getUser().isEmpty());
        assertSame(user, session.getUser().get());
    }

    /** Test clear. */
    @Test
    public void testClear() {
        final Session session = new Session();
        final User user = new User.Builder().build();
        session.setUser(user);
        session.clear();
        assertTrue(session.getUser().isEmpty());
    }
}
