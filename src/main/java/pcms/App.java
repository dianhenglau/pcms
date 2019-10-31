package pcms;

import java.nio.file.Path;
import javax.swing.SwingUtilities;
import pcms.login.LoginController;
import pcms.login.LoginView;
import pcms.loginrecord.LoginRecord;
import pcms.loginrecord.LoginRecordController;
import pcms.loginrecord.LoginRecordListView;
import pcms.loginrecord.LoginRecordRepository;
import pcms.menu.MenuController;
import pcms.menu.MenuView;
import pcms.profile.EditProfileView;
import pcms.profile.ProfileController;
import pcms.profile.ProfileView;
import pcms.user.AddUserView;
import pcms.user.EditUserView;
import pcms.user.UserController;
import pcms.user.UserInfoView;
import pcms.user.UserListView;
import pcms.user.UserRepository;

/** App. */
public final class App {
    /** Session that store anything related to current user. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;
    /** Login record repository. */
    private final LoginRecordRepository loginRecordRepository;

    /** Construct app. */
    public App() {
        // Enable anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        // Create session.
        session = new Session();
        // Create models.
        userRepository = new UserRepository(getDataPath("users.csv"));
        loginRecordRepository = new LoginRecordRepository(
                getDataPath("login_records.csv"), userRepository);
        LoginRecord.setUserRepository(userRepository);
    }

    /** Run app. */
    public void createAndShowGui() {
        // Create views.
        final UserListView userListView = new UserListView();
        final UserInfoView userInfoView = new UserInfoView();
        final AddUserView addUserView = new AddUserView();
        final EditUserView editUserView = new EditUserView();

        final ProfileView profileView = new ProfileView();
        final EditProfileView editProfileView = new EditProfileView();

        final LoginRecordListView loginRecordListView = new LoginRecordListView();

        final MenuView menuView = new MenuView();
        final ContentView contentView = new ContentView(
                userListView,
                userInfoView,
                addUserView,
                editUserView,
                profileView,
                editProfileView,
                loginRecordListView);

        final MainView mainView = new MainView(menuView, contentView);
        final LoginView loginView = new LoginView();

        final RootView rootView = new RootView(mainView, loginView);

        // Create controllers.
        final UserController userController = new UserController(
                session,
                userRepository,
                userListView,
                userInfoView,
                addUserView,
                editUserView,
                rootView);
        final ProfileController profileController = new ProfileController(
                session,
                userRepository,
                profileView,
                editProfileView,
                rootView);
        final LoginRecordController loginRecordController = new LoginRecordController(
                session,
                loginRecordRepository,
                loginRecordListView,
                rootView);

        final MenuController menuController = new MenuController(
                session,
                menuView,
                rootView);

        final LoginController loginController = new LoginController(
                session,
                userRepository,
                loginRecordRepository,
                loginView,
                rootView);

        userController.init();
        profileController.init();

        menuController.init(
                loginController,
                userController,
                profileController,
                loginRecordController);

        loginController.init(userController);

        // Load login page.
        loginController.index();
    }

    /** Program entry point. */
    public static void main(final String[] args) {
        final App app = new App();
        SwingUtilities.invokeLater(app::createAndShowGui);
    }

    /** Get data path. */
    private static Path getDataPath(final String filename) {
        return Path.of("data", "main", filename);
    }
}
