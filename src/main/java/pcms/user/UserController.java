package pcms.user;

import java.util.Locale;
import pcms.ContentView;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;

/** User controller. */
public final class UserController {
    /** Session. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;

    /** User list view. */
    private final UserListView userListView;
    /** User info view. */
    private final UserInfoView userInfoView;
    /** Add user view. */
    private final AddUserView addUserView;
    /** Edit user view. */
    private final EditUserView editUserView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public UserController(
            final Session session,
            final UserRepository userRepository,
            final UserListView userListView,
            final UserInfoView userInfoView,
            final AddUserView addUserView,
            final EditUserView editUserView,
            final RootView rootView) {

        this.session = session;
        this.userRepository = userRepository;
        this.userListView = userListView;
        this.userInfoView = userInfoView;
        this.addUserView = addUserView;
        this.editUserView = editUserView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        userListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
        userListView.addBtn.addActionListener(e -> create());
        userInfoView.editBtn.addActionListener(e -> edit(e.getActionCommand()));
        addUserView.saveBtn.addActionListener(e -> store());
        addUserView.cancelBtn.addActionListener(e -> index(""));
        editUserView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editUserView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
    }

    /** List users. */
    public void index(final String search) {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.USER_LIST);

        if (search.isEmpty()) {
            userListView.render(userRepository.all(), e -> show(e.getActionCommand()));
        } else {
            userListView.render(
                    userRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US)
                            .contains(search.toLowerCase(Locale.US))
                        || x.getUsername().toLowerCase(Locale.US)
                            .contains(search.toLowerCase(Locale.US))
                        || x.getFullName().toLowerCase(Locale.US)
                            .contains(search.toLowerCase(Locale.US))),
                    e -> show(e.getActionCommand()));
        }
    }

    /** Show create user form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_USER);
        addUserView.render();
    }

    /** Store created user. */
    public void store() {
        try {
            final User newUser = userRepository.insert(new User.Builder()
                    .withFullName(addUserView.fullNameTf.getText())
                    .withAddress(addUserView.addressTf.getText())
                    .withEmail(addUserView.emailTf.getText())
                    .withIsAdministrator(addUserView.administratorCb.isSelected())
                    .withIsProductManager(addUserView.productManagerCb.isSelected())
                    .withUsername(addUserView.usernameTf.getText())
                    .withPassword(addUserView.passwordTf.getText())
                    .withIsActive(addUserView.activeCb.isSelected())
                    .build());
            rootView.showSuccessDialog("User added.");
            show(newUser.getId());
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Show user info. */
    public void show(final String id) {
        try {
            final User user = ValidationUtil.recordExists(userRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.USER_INFO);
            userInfoView.render(user);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Edit user info. */
    public void edit(final String id) {
        try {
            final User user = ValidationUtil.recordExists(userRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.EDIT_USER);
            editUserView.render(user);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update user info. */
    public void update(final String id) {
        try {
            final User user = ValidationUtil.recordExists(userRepository, id);
            final User newUser = userRepository.update(new User.Builder(user)
                    .withId(id)
                    .withFullName(editUserView.fullNameTf.getText())
                    .withAddress(editUserView.addressTf.getText())
                    .withEmail(editUserView.emailTf.getText())
                    .withIsAdministrator(editUserView.administratorCb.isSelected())
                    .withIsProductManager(editUserView.productManagerCb.isSelected())
                    .withUsername(editUserView.usernameTf.getText())
                    .withPassword(editUserView.passwordTf.getText())
                    .withIsActive(editUserView.activeCb.isSelected())
                    .build());
            if (newUser.getId().equals(session.getUser().get().getId())) {
                session.setUser(newUser);
                rootView.mainView.menuView.render(newUser);
            }
            rootView.showSuccessDialog("User updated.");
            show(id);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }
}
