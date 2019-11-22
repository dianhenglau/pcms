package pcms.product;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Product list view. */
public final class ProductListView {
    /** Table columns. */
    private static final String[] COLUMNS = {
            "ID", "Image", "Name", "Brand", "Category",
            "Quantity", "Retail Price", "Discount", "Supplier", "Actions"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            70, 120, 120, 100, 100, 
            60, 60, 60, 100, 120};

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
    public ProductListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("Product List");
        tablePane = ViewUtil.createContentPane();
        addBtn = new JButton("New");
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(addBtn, searchTf));
        pane.add(tablePane);
    }

    /** Render products. */
    public void render(
            final List<Product> products, 
            final String search,
            final ActionListener goView,
            final ActionListener goDelete) {

        searchTf.setText(search);
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final Product u : products) {
            tablePane.add(toTableRow(u, goView, goDelete));
        }
        tablePane.revalidate();
        tablePane.repaint();
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(
            final Product product, 
            final ActionListener goView, 
            final ActionListener goDelete) {

        final JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(goView);
        viewBtn.setActionCommand(product.getId());
        
        final JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(goDelete);
        deleteBtn.setActionCommand(product.getId());

        final JPanel actionPane = ViewUtil.createHorizontalPane();
        actionPane.add(viewBtn);
        actionPane.add(Box.createRigidArea(new Dimension(5, 0)));
        actionPane.add(deleteBtn);
        
        

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(product.getId()),
                ViewUtil.createIconLabel(product.getImage()),
                ViewUtil.createUnboldLabel(product.getName()),
                ViewUtil.createUnboldLabel(product.getCategory().getName()),
                ViewUtil.createUnboldLabel(Integer.toString(product.getQuantity())),
                ViewUtil.createUnboldLabel(String.format("%.2f", product.getRetailPrice())),
                ViewUtil.createUnboldLabel(String.format("%.0f%%", product.getDiscount())),
                ViewUtil.createUnboldLabel(product.getSupplier().getName()),
                actionPane
        };

        return ViewUtil.createBodyRow(components, WIDTHS);
    }

}
