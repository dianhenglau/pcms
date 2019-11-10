package pcms.category;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Category list view. */
public final class CategoryListView {
    /** Table columns. */
    private static final String[] COLUMNS = {"ID", "Name", "Description", "Actions"};
    /** Table column width. */
    private static final int[] WIDTHS = {70, 120, 210, 200};

    /** Pane. */
    public final JPanel pane;
    /** Table pane. */
    private final JPanel tablePane;
    /** Add button. */
    public final JButton addBtn;
    /** Search text field. */
    public final JTextField searchTf;
    /** Table header row. */
    private final JPanel headerRow;

    /** Construct. */
    public CategoryListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("Category List");
        tablePane = ViewUtil.createContentPane();
        addBtn = new JButton("New");
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(addBtn, searchTf));
        pane.add(tablePane);
    }

    /** Render categorys. */
    public void render(
            final List<Category> categorys, 
            final String search,
            final ActionListener goView,
            final ActionListener goDelete) {

        searchTf.setText(search);
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final Category u : categorys) {
            tablePane.add(toTableRow(u, goView, goDelete));
        }
        tablePane.revalidate();
        tablePane.repaint();
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(
            final Category category, 
            final ActionListener goView, 
            final ActionListener goDelete) {

        final JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(goView);
        viewBtn.setActionCommand(category.getId());

        final JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(goDelete);
        deleteBtn.setActionCommand(category.getId());

        final JPanel actionPane = ViewUtil.createHorizontalPane();
        actionPane.add(viewBtn);
        actionPane.add(Box.createRigidArea(new Dimension(5, 0)));
        actionPane.add(deleteBtn);

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(category.getId()),
                ViewUtil.createUnboldLabel(category.getName()),
                ViewUtil.createUnboldLabel(category.getDescription()),
                actionPane};

        return ViewUtil.createBodyRow(components, WIDTHS);
    }

}
