package pcms.category;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Add category view. */
public final class AddCategoryView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** Name text field. */
    public final JTextField nameTf;
    /** Description text field. */
    public final JTextArea descriptionTa;

    /** Construct. */
    public AddCategoryView() {
        pane = ViewUtil.createContainerPane("Add Category");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        nameTf = ViewUtil.createTextField(25);
        descriptionTa = ViewUtil.createTextArea();

        final String[] labels = {"Name", "Description"};
        final JComponent[] components = {
                nameTf, ViewUtil.createScrollPane(descriptionTa, 450, 100)};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render() {
        nameTf.setText("");
        descriptionTa.setText("");
    }
}
