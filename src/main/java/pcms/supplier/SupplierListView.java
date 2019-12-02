package pcms.supplier;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Supplier list view. */
public final class SupplierListView {
    /** Table columns. */
    private static final String[] COLUMNS = {
            "ID", "Name", "Email", "Phone", "Address", "Status", "Actions"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            50, 120, 150, 100, 200, 70, 150};

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
    public SupplierListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("Supplier List");
        tablePane = ViewUtil.createContentPane();
        addBtn = new JButton("New");
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(addBtn, searchTf));
        pane.add(tablePane);
    }

    /** Render suppliers. */
    public void render(
            final List<Supplier> suppliers, 
            final boolean isAdministrator,
            final String search, 
            final ActionListener goView) {

        searchTf.setText(search);
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final Supplier u : suppliers) {
            tablePane.add(toTableRow(u, goView));
        }
        tablePane.revalidate();
        tablePane.repaint();

        addBtn.setVisible(isAdministrator);
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(final Supplier supplier, final ActionListener goView) {
        final JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(goView);
        viewBtn.setActionCommand(supplier.getId());

        final JPanel actionPane = ViewUtil.createHorizontalPane();
        actionPane.add(viewBtn);

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(supplier.getId()),
                ViewUtil.createUnboldLabel(supplier.getName()),
                ViewUtil.createUnboldLabel(supplier.getEmail()),
                ViewUtil.createUnboldLabel(supplier.getPhone()),
                ViewUtil.createUnboldLabel(supplier.getAddress()),
                ViewUtil.createUnboldLabel(supplier.isActive() ? "Active" : "Inactive"),
                actionPane
        };

        return ViewUtil.createBodyRow(components, WIDTHS);
    }

}
