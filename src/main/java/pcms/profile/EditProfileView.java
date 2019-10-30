package pcms.profile;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;
import pcms.user.User;

/** Edit profile view. */
public final class EditProfileView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** ID label. */
    public final JLabel idLbl;
    /** Full name text field. */
    public final JTextField fullNameTf;
    /** Address text field. */
    public final JTextField addressTf;
    /** Email text field. */
    public final JTextField emailTf;
    /** Administrator label. */
    public final JLabel administratorLbl;
    /** Product manager label. */
    public final JLabel productManagerLbl;
    /** Username text field. */
    public final JTextField usernameTf;
    /** Password text field. */
    public final JTextField passwordTf;
    /** Status label. */
    public final JLabel statusLbl;

    /** Construct. */
    public EditProfileView() {
        pane = ViewUtil.createContainerPane("Edit Profile");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = new JLabel();
        fullNameTf = ViewUtil.createTextField(25);
        addressTf = ViewUtil.createTextField(25);
        emailTf = ViewUtil.createTextField(25);
        administratorLbl = new JLabel();
        productManagerLbl = new JLabel();
        usernameTf = ViewUtil.createTextField(25);
        passwordTf = ViewUtil.createTextField(25);
        statusLbl = new JLabel();

        final String[] labels = {
                "ID", "Full name", "Email", "Address", "Administrator", 
                "Product manager", "Username", "Password", "Active"};
        final JComponent[] components = {
                idLbl, fullNameTf, emailTf, addressTf, administratorLbl,
                productManagerLbl, usernameTf, passwordTf, statusLbl};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final User user) {
        idLbl.setText(user.getId());
        fullNameTf.setText(user.getFullName());
        addressTf.setText(user.getAddress());
        emailTf.setText(user.getEmail());
        administratorLbl.setText(user.isAdministrator() ? "Yes" : "No");
        productManagerLbl.setText(user.isProductManager() ? "Yes" : "No");
        usernameTf.setText(user.getUsername());
        passwordTf.setText(user.getPassword());
        statusLbl.setText(user.isActive() ? "Active" : "Inactive");
    }
}
