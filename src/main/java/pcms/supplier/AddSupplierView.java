package pcms.supplier;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Add supplier view. */
public final class AddSupplierView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
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
    public AddSupplierView() {
        pane = ViewUtil.createContainerPane("Add Supplier");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        nameTf = ViewUtil.createTextField(25);
        emailTf = ViewUtil.createTextField(25);
        phoneTf = ViewUtil.createTextField(25);
        addressTf = ViewUtil.createTextField(25);
        activeCb = new JCheckBox();

        final String[] labels = {"Name", "Email", "Phone", "Address", "Active"};
        final JComponent[] components = {nameTf, emailTf, phoneTf, addressTf, activeCb};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render() {
        nameTf.setText("");
        emailTf.setText("");
        phoneTf.setText("");
        addressTf.setText("");
        activeCb.setSelected(true);
    }
}
