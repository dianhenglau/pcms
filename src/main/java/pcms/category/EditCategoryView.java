package pcms.category;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Edit category view. */
public final class EditCategoryView {
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
    /** Description text field. */
    public final JTextArea descriptionTa;

    /** Construct. */
    public EditCategoryView() {
        pane = ViewUtil.createContainerPane("Edit Category");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = new JLabel();
        nameTf = ViewUtil.createTextField(25);
        descriptionTa = ViewUtil.createTextArea();

        final String[] labels = {"ID", "Name", "Description"};
        final JComponent[] components = {
                idLbl, nameTf, ViewUtil.createScrollPane(descriptionTa, 450, 100)};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Category category) {
        saveBtn.setActionCommand(category.getId());
        cancelBtn.setActionCommand(category.getId());
        idLbl.setText(category.getId());
        nameTf.setText(category.getName());
        descriptionTa.setText(category.getDescription());
    }
}
