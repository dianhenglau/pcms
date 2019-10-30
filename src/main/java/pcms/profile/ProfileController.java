package pcms.profile;

import pcms.ContentView;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.user.User;
import pcms.user.UserRepository;

/** Profile controller. */
public final class ProfileController {
    /** Session. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;

    /** Profile view. */
    private final ProfileView profileView;
    /** Edit profile view. */
    private final EditProfileView editProfileView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public ProfileController(
            final Session session,
            final UserRepository userRepository,
            final ProfileView profileView,
            final EditProfileView editProfileView,
            final RootView rootView) {

        this.session = session;
        this.userRepository = userRepository;
        this.profileView = profileView;
        this.editProfileView = editProfileView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        profileView.editBtn.addActionListener(e -> edit());
        editProfileView.saveBtn.addActionListener(e -> update());
        editProfileView.cancelBtn.addActionListener(e -> index());
    }

    /** Show profile. */
    public void index() {
        final User user = session.getUser().get();
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.PROFILE);
        profileView.render(user);
    }

    /** Edit user info. */
    public void edit() {
        final User user = session.getUser().get();
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.EDIT_PROFILE);
        editProfileView.render(user);
    }

    /** Update user info. */
    public void update() {
        try {
            final User user = userRepository.update(new User.Builder(session.getUser().get())
                    .withFullName(editProfileView.fullNameTf.getText())
                    .withAddress(editProfileView.addressTf.getText())
                    .withEmail(editProfileView.emailTf.getText())
                    .withUsername(editProfileView.usernameTf.getText())
                    .withPassword(editProfileView.passwordTf.getText())
                    .build());
            session.setUser(user);
            rootView.mainView.menuView.render(user);
            rootView.showSuccessDialog("Profile updated.");
            index();
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }
}
