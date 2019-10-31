package pcms.menu;

import pcms.RootView;
import pcms.Session;
import pcms.login.LoginController;
import pcms.loginrecord.LoginRecordController;
import pcms.profile.ProfileController;
import pcms.user.UserController;

/** Menu controller. */
public final class MenuController {
    /** Session. */
    private final Session session; // NOPMD - temporary

    /** Menu view. */
    private final MenuView menuView;
    /** Root view. */
    private final RootView rootView; // NOPMD - temporary

    /** Construct. */
    public MenuController(
            final Session session,
            final MenuView menuView,
            final RootView rootView) {

        this.session = session;
        this.menuView = menuView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init(
            final LoginController loginController,
            final UserController userController,
            final ProfileController profileController,
            final LoginRecordController loginRecordController) {

        menuView.userBtn.addActionListener(e -> userController.index(""));
        menuView.logoutBtn.addActionListener(e -> loginController.logout());
        menuView.profileBtn.addActionListener(e -> profileController.index());
        menuView.loginRecordBtn.addActionListener(e -> loginRecordController.index(""));
    }
}

