package pcms;

import java.awt.CardLayout;
import javax.swing.JPanel;
import pcms.profile.EditProfileView;
import pcms.profile.ProfileView;
import pcms.user.AddUserView;
import pcms.user.EditUserView;
import pcms.user.UserInfoView;
import pcms.user.UserListView;

/** Content view. */
public final class ContentView {
    /** List of children views. */
    public enum Views {
        USER_LIST, USER_INFO, ADD_USER, EDIT_USER, 
        PROFILE, EDIT_PROFILE
    }

    /** User list view. */
    public final UserListView userListView;
    /** User info view. */
    public final UserInfoView userInfoView;
    /** Add user view. */
    public final AddUserView addUserView;
    /** Edit user view. */
    public final EditUserView editUserView;

    /** Profile view. */
    public final ProfileView profileView;
    /** Edit profile view. */
    public final EditProfileView editProfileView;

    /** Pane. */
    public final JPanel pane;
    /** Card layout. */
    private final CardLayout cardLayout;

    /** Construct. */
    public ContentView(
            final UserListView userListView,
            final UserInfoView userInfoView,
            final AddUserView addUserView,
            final EditUserView editUserView,
            final ProfileView profileView,
            final EditProfileView editProfileView) {

        this.userListView = userListView;
        this.userInfoView = userInfoView;
        this.addUserView = addUserView;
        this.editUserView = editUserView;
        this.profileView = profileView;
        this.editProfileView = editProfileView;

        cardLayout = new CardLayout();
        pane = new JPanel(cardLayout);

        pane.add(userListView.pane, Views.USER_LIST.name());
        pane.add(userInfoView.pane, Views.USER_INFO.name());
        pane.add(addUserView.pane, Views.ADD_USER.name());
        pane.add(editUserView.pane, Views.EDIT_USER.name());
        pane.add(profileView.pane, Views.PROFILE.name());
        pane.add(editProfileView.pane, Views.EDIT_PROFILE.name());
    }

    /** Show a view with given key. */
    public void render(final Views key) {
        cardLayout.show(pane, key.name());
    }
}
