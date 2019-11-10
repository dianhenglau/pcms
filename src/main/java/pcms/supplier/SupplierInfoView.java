package pcms.supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pcms.ViewUtil;

/** Supplier info view. */
public final class SupplierInfoView {
    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** Back button. */
    public final JButton backBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Full name label. */
    private final JLabel nameLbl;
    /** Email label. */
    private final JLabel emailLbl;
    /** Phone label. */
    private final JLabel phoneLbl;
    /** Address label. */
    private final JLabel addressLbl;
    /** Status label. */
    private final JLabel statusLbl;

    /** Construct. */
    public SupplierInfoView() {
        pane = ViewUtil.createContainerPane("Supplier Info");

        editBtn = new JButton("Edit");
        backBtn = new JButton("Back");
        idLbl = new JLabel();
        nameLbl = new JLabel();
        emailLbl = new JLabel();
        phoneLbl = new JLabel();
        addressLbl = new JLabel();
        statusLbl = new JLabel();

        final String[] labels = {
                "ID", "Name", "Email", "Phone", "Address", "Status"};
        final JComponent[] components = {
                idLbl, nameLbl, emailLbl, phoneLbl, addressLbl, statusLbl};

        pane.add(ViewUtil.createButtonControlPane(backBtn, editBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Supplier supplier) {
        editBtn.setActionCommand(supplier.getId());
        idLbl.setText(supplier.getId());
        nameLbl.setText(supplier.getName());
        emailLbl.setText(supplier.getEmail());
        phoneLbl.setText(supplier.getPhone());
        addressLbl.setText(supplier.getAddress());
        statusLbl.setText(supplier.isActive() ? "Active" : "Inactive");
    }
}
