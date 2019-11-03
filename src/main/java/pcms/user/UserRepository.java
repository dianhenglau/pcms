package pcms.user;

import java.nio.file.Path;
import pcms.Repository;
import pcms.ValidationUtil;

/** Models user repository. */
public final class UserRepository extends Repository<User> {
    /** Constructor. */
    public UserRepository(final Path dataPath) {
        super(dataPath, id -> new User.Builder().withId(id).build(), User::new);
    }

    /** Insert user. */
    @Override
    public User insert(final User user) {
        readFromFile();

        // Make sure email is valid.
        ValidationUtil.notEmpty("email", user.getEmail()); // NOPMD - Ok to duplicate literal
        ValidationUtil.validEmailFormat("email", user.getEmail());

        // Make sure username is valid.
        ValidationUtil.validMinLength("username", user.getUsername(), 1); // NOPMD
        ValidationUtil.validUsernameFormat("username", user.getUsername());
        ValidationUtil.notExists("username", user.getUsername(), cache, User::getUsername);

        // Make sure password length is at least one.
        ValidationUtil.validMinLength("password", user.getPassword(), 1);

        // Add to cache
        final User newUser = new User.Builder(user)
                .withId(String.format("U%05d", newId))
                .build();
        cache.add(newUser);
        newId += 1;

        writeToFile();
        return newUser;
    }

    /** Update user. */
    @Override
    public User update(final User user) {
        readFromFile();

        // Make sure user id exists.
        final int index = ValidationUtil.idExists(cache, user);

        // Make sure email is valid.
        ValidationUtil.notEmpty("email", user.getEmail());
        ValidationUtil.validEmailFormat("email", user.getEmail());

        // Make sure username is valid.
        ValidationUtil.validMinLength("username", user.getUsername(), 1);
        ValidationUtil.validUsernameFormat("username", user.getUsername());
        ValidationUtil.notExists("username", user.getUsername(), cache, User::getUsername,
                user.getId(), User::getId);
        
        // Make sure password length is at least one.
        ValidationUtil.validMinLength("password", user.getPassword(), 1);

        cache.set(index, user);
        writeToFile();
        return cache.get(index);
    }
}
