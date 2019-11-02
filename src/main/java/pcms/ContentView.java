package pcms;

import java.awt.CardLayout;
import javax.swing.JPanel;
import pcms.category.AddCategoryView;
import pcms.category.CategoryInfoView;
import pcms.category.CategoryListView;
import pcms.category.EditCategoryView;
import pcms.loginrecord.LoginRecordListView;
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
        PROFILE, EDIT_PROFILE,
        LOGIN_RECORD_LIST,
        CATEGORY_LIST, CATEGORY_INFO, ADD_CATEGORY, EDIT_CATEGORY
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

    /** Login record list view. */
    public final LoginRecordListView loginRecordListView;

    /** Category list view. */
    public final CategoryListView categoryListView;
    /** Category info view. */
    public final CategoryInfoView categoryInfoView;
    /** Add category view. */
    public final AddCategoryView addCategoryView;
    /** Edit category view. */
    public final EditCategoryView editCategoryView;

    /** Pane. */
    public final JPanel pane;
    /** Card layout. */
    private final CardLayout cardLayout;

    /** Construct. */
    public ContentView(// NOPMD - Ok to have long parameter list
            final UserListView userListView,
            final UserInfoView userInfoView,
            final AddUserView addUserView,
            final EditUserView editUserView,
            final ProfileView profileView,
            final EditProfileView editProfileView,
            final LoginRecordListView loginRecordListView,
            final CategoryListView categoryListView,
            final CategoryInfoView categoryInfoView,
            final AddCategoryView addCategoryView,
            final EditCategoryView editCategoryView) {

        this.userListView = userListView;
        this.userInfoView = userInfoView;
        this.addUserView = addUserView;
        this.editUserView = editUserView;
        this.profileView = profileView;
        this.editProfileView = editProfileView;
        this.loginRecordListView = loginRecordListView;
        this.categoryListView = categoryListView;
        this.categoryInfoView = categoryInfoView;
        this.addCategoryView = addCategoryView;
        this.editCategoryView = editCategoryView;

        cardLayout = new CardLayout();
        pane = new JPanel(cardLayout);

        pane.add(userListView.pane, Views.USER_LIST.name());
        pane.add(userInfoView.pane, Views.USER_INFO.name());
        pane.add(addUserView.pane, Views.ADD_USER.name());
        pane.add(editUserView.pane, Views.EDIT_USER.name());
        pane.add(profileView.pane, Views.PROFILE.name());
        pane.add(editProfileView.pane, Views.EDIT_PROFILE.name());
        pane.add(loginRecordListView.pane, Views.LOGIN_RECORD_LIST.name());
        pane.add(categoryListView.pane, Views.CATEGORY_LIST.name());
        pane.add(categoryInfoView.pane, Views.CATEGORY_INFO.name());
        pane.add(addCategoryView.pane, Views.ADD_CATEGORY.name());
        pane.add(editCategoryView.pane, Views.EDIT_CATEGORY.name());
    }

    /** Show a view with given key. */
    public void render(final Views key) {
        cardLayout.show(pane, key.name());
    }
}
