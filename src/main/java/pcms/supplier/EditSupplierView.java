package pcms.supplier;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Edit supplier view. */
public final class EditSupplierView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** ID label. */
    public final JLabel idLbl;
    /** Name text field. */
    public final JTextField nameTf;
    /** Email text field. */
    public final JTextField emailTf;
    /** Phone text field. */
    public final JTextField phoneTf;
    /** Address text field. */
    public final JTextField addressTf;
    /** Active check box. */
    public final JCheckBox activeCb;

    /** Construct. */
    public EditSupplierView() {
        pane = ViewUtil.createContainerPane("Edit Supplier");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = new JLabel();
        nameTf = ViewUtil.createTextField(25);
        emailTf = ViewUtil.createTextField(25);
        phoneTf = ViewUtil.createTextField(25);
        addressTf = ViewUtil.createTextField(25);
        activeCb = new JCheckBox();

        final String[] labels = {"ID", "Name", "Email", "Phone", "Address", "Active"};
        final JComponent[] components = {idLbl, nameTf, emailTf, phoneTf, addressTf, activeCb};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Supplier supplier) {
        saveBtn.setActionCommand(supplier.getId());
        cancelBtn.setActionCommand(supplier.getId());
        idLbl.setText(supplier.getId());
        nameTf.setText(supplier.getName());
        emailTf.setText(supplier.getEmail());
        phoneTf.setText(supplier.getPhone());
        addressTf.setText(supplier.getAddress());
        activeCb.setSelected(supplier.isActive());
    }
}
