package pcms.category;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import pcms.ViewUtil;

/** Category info view. */
public final class CategoryInfoView {
    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Name label. */
    private final JLabel nameLbl;
    /** Description text area. */
    private final JTextArea descriptionTa;

    /** Construct. */
    public CategoryInfoView() {
        pane = ViewUtil.createContainerPane("Category Info");

        editBtn = new JButton("Edit");
        idLbl = new JLabel();
        nameLbl = new JLabel();
        descriptionTa = ViewUtil.createViewOnlyTextArea();

        final String[] labels = {"ID", "Name", "Description"};
        final JComponent[] components = {idLbl, nameLbl, descriptionTa};

        pane.add(ViewUtil.createButtonControlPane(editBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Category category) {
        editBtn.setActionCommand(category.getId());
        idLbl.setText(category.getId());
        nameLbl.setText(category.getName());
        descriptionTa.setText(category.getDescription());
    }
}
