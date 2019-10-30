package pcms.login;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.user.User;
import pcms.user.UserController;
import pcms.user.UserRepository;

/** Login controller. */
public final class LoginController {
    /** Session. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;
    /** User controller. */
    private Optional<UserController> userController;

    /** Login view. */
    private final LoginView loginView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public LoginController(
            final Session session,
            final UserRepository userRepository,
            final LoginView loginView,
            final RootView rootView) {

        this.session = session;
        this.userRepository = userRepository;
        this.loginView = loginView;
        this.rootView = rootView;
        userController = Optional.empty();
    }

    /** Initialize. */
    // TODO - Change userController to dashboardController
    public void init(final UserController userController) {
        this.userController = Optional.of(userController);
        loginView.usernameTf.addActionListener(e -> login());
        loginView.passwordPf.addActionListener(e -> login());
        loginView.loginBtn.addActionListener(e -> login());
    }

    /** Show login page. */
    public void index() {
        rootView.render(RootView.Views.LOGIN_VIEW);
        loginView.render();
    }

    /** Login. */
    public void login() {
        try {
            ValidationUtil.notEmpty("username", loginView.usernameTf.getText());
            ValidationUtil.notEmpty("password", loginView.passwordPf.getPassword());

            final List<User> results = userRepository.filter(
                    x -> x.getUsername().equals(loginView.usernameTf.getText()));

            if (results.isEmpty()) {
                throw new InvalidFieldException("username", "Username not found.");
            }

            assert results.size() == 1;
            final User user = results.get(0);

            if (!Arrays.equals(user.getPassword().toCharArray(),
                    loginView.passwordPf.getPassword())) {
                throw new InvalidFieldException("password", "Password incorrect.");
            }

            if (!user.isActive()) {
                throw new InvalidFieldException("username", "You account is inactive. Kindly"
                        + " contact administrator.");
            }

            session.setUser(user);
            rootView.mainView.menuView.render(user);
            userController.get().index("");

        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Logout. */
    public void logout() {
        session.clear();
        index();
    }
}
