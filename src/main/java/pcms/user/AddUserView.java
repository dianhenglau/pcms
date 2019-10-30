package pcms.user;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Add user view. */
public final class AddUserView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
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
    public AddUserView() {
        pane = ViewUtil.createContainerPane("Add User");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        fullNameTf = ViewUtil.createTextField(25);
        addressTf = ViewUtil.createTextField(25);
        emailTf = ViewUtil.createTextField(25);
        administratorCb = new JCheckBox();
        productManagerCb = new JCheckBox();
        usernameTf = ViewUtil.createTextField(25);
        passwordTf = ViewUtil.createTextField(25);
        activeCb = new JCheckBox();

        final String[] labels = {
                "Full name", "Email", "Address", "Administrator", "Product manager", 
                "Username", "Password", "Active"};
        final JComponent[] components = {
                fullNameTf, emailTf, addressTf, administratorCb, productManagerCb,
                usernameTf, passwordTf, activeCb};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render() {
        fullNameTf.setText("");
        addressTf.setText("");
        emailTf.setText("");
        administratorCb.setSelected(false);
        productManagerCb.setSelected(false);
        usernameTf.setText("");
        passwordTf.setText("");
        activeCb.setSelected(true);
    }
}
