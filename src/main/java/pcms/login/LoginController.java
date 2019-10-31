package pcms.login;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.loginrecord.LoginRecord;
import pcms.loginrecord.LoginRecordRepository;
import pcms.user.User;
import pcms.user.UserController;
import pcms.user.UserRepository;

/** Login controller. */
public final class LoginController {
    /** Session. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;
    /** Login record repository. */
    private final LoginRecordRepository loginRecordRepository;
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
            final LoginRecordRepository loginRecordRepository,
            final LoginView loginView,
            final RootView rootView) {

        this.session = session;
        this.userRepository = userRepository;
        this.loginRecordRepository = loginRecordRepository;
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
        rootView.frame.addWindowListener(new WindowAdapter() {
            /** Logout when window is closing. */
            @Override
            public void windowClosing(final WindowEvent e) {
                if (!session.getUser().isEmpty()) {
                    logout();
                }
            }
        });
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

            loginRecordRepository.insert(new LoginRecord.Builder()
                    .withUserId(user.getId())
                    .withAction(LoginRecord.Action.LOGIN)
                    .build());

            session.setUser(user);
            rootView.mainView.menuView.render(user);
            userController.get().index("");

        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Logout. */
    public void logout() {
        loginRecordRepository.insert(new LoginRecord.Builder()
                .withUserId(session.getUser().get().getId())
                .withAction(LoginRecord.Action.LOGOUT)
                .build());

        session.clear();
        index();
    }
}
