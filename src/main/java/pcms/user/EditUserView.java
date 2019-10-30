package pcms.user;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Edit user view. */
public final class EditUserView {
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
    /** Administrator check box. */
    public final JCheckBox administratorCb;
    /** Product manager check box. */
    public final JCheckBox productManagerCb;
    /** Username text field. */
    public final JTextField usernameTf;
    /** Password text field. */
    public final JTextField passwordTf;
    /** Active check box. */
    public final JCheckBox activeCb;

    /** Construct. */
    public EditUserView() {
        pane = ViewUtil.createContainerPane("Edit User");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = new JLabel();
        fullNameTf = ViewUtil.createTextField(25);
        addressTf = ViewUtil.createTextField(25);
        emailTf = ViewUtil.createTextField(25);
        administratorCb = new JCheckBox();
        productManagerCb = new JCheckBox();
        usernameTf = ViewUtil.createTextField(25);
        passwordTf = ViewUtil.createTextField(25);
        activeCb = new JCheckBox();

        final String[] labels = {
                "ID", "Full name", "Email", "Address", "Administrator", 
                "Product manager", "Username", "Password", "Active"};
        final JComponent[] components = {
                idLbl, fullNameTf, emailTf, addressTf, administratorCb,
                productManagerCb, usernameTf, passwordTf, activeCb};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final User user) {
        saveBtn.setActionCommand(user.getId());
        cancelBtn.setActionCommand(user.getId());
        idLbl.setText(user.getId());
        fullNameTf.setText(user.getFullName());
        addressTf.setText(user.getAddress());
        emailTf.setText(user.getEmail());
        administratorCb.setSelected(user.isAdministrator());
        productManagerCb.setSelected(user.isProductManager());
        usernameTf.setText(user.getUsername());
        passwordTf.setText(user.getPassword());
        activeCb.setSelected(user.isActive());
    }
}
