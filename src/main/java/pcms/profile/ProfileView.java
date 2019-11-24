package pcms.profile;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pcms.ViewUtil;
import pcms.user.User;

/** Profile view. */
public final class ProfileView {
    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Full name label. */
    private final JLabel fullNameLbl;
    /** Address label. */
    private final JLabel addressLbl;
    /** Email label. */
    private final JLabel emailLbl;
    /** Administrator label. */
    private final JLabel administratorLbl;
    /** Product manager label. */
    private final JLabel productManagerLbl;
    /** Username label. */
    private final JLabel usernameLbl;
    /** Password label. */
    private final JLabel passwordLbl;
    /** Status label. */
    private final JLabel statusLbl;

    /** Construct. */
    public ProfileView() {
        pane = ViewUtil.createContainerPane("Profile");

        editBtn = new JButton("Edit");
        idLbl = ViewUtil.createValueLabel();
        fullNameLbl = ViewUtil.createValueLabel();
        addressLbl = ViewUtil.createValueLabel();
        emailLbl = ViewUtil.createValueLabel();
        administratorLbl = ViewUtil.createValueLabel();
        productManagerLbl = ViewUtil.createValueLabel();
        usernameLbl = ViewUtil.createValueLabel();
        passwordLbl = ViewUtil.createValueLabel();
        statusLbl = ViewUtil.createValueLabel();

        final String[] labels = {
                "ID", "Full name", "Email", "Address", "Administrator", 
                "Product manager", "Username", "Password", "Status"};
        final JComponent[] components = {
                idLbl, fullNameLbl, emailLbl, addressLbl, administratorLbl,
                productManagerLbl, usernameLbl, passwordLbl, statusLbl};

        pane.add(ViewUtil.createButtonControlPane(editBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final User user) {
        idLbl.setText(user.getId());
        fullNameLbl.setText(user.getFullName());
        addressLbl.setText(user.getAddress());
        emailLbl.setText(user.getEmail());
        administratorLbl.setText(user.isAdministrator() ? "Yes" : "No");
        productManagerLbl.setText(user.isProductManager() ? "Yes" : "No");
        usernameLbl.setText(user.getUsername());
        passwordLbl.setText(user.getPassword());
        statusLbl.setText(user.isActive() ? "Active" : "Inactive");
    }
}
