package pcms.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pcms.ViewUtil;
import pcms.catalog.Catalog;
import pcms.loginrecord.LoginRecord;
import pcms.product.Product;
import pcms.supplier.Supplier;
import pcms.user.User;

/** Dashboard view. */
public final class DashboardView {
    /** Pane. */
    public final JPanel pane;
    /** Supplier list button. */
    public final JButton supplierListBtn;
    /** Supplier table body button. */
    private final JPanel supplierTablePane;
    /** Product list button. */
    public final JButton productListBtn;
    /** Product table body button. */
    private final JPanel productTablePane;
    /** Catalog list button. */
    public final JButton catalogListBtn;
    /** Catalog table body button. */
    private final JPanel catalogTablePane;
    /** Login record list button. */
    public final JButton loginRecordListBtn;
    /** Login record table body button. */
    private final JPanel loginRecordTablePane;
    /** User list button. */
    public final JButton userListBtn;
    /** User table body button. */
    private final JPanel userTablePane;

    /** Construct. */
    public DashboardView() {
        final String buttonStr = "List";
        pane = ViewUtil.createContainerPane("Dashboard Info");
        supplierListBtn = new JButton(buttonStr);
        supplierTablePane = ViewUtil.createContentPane();
        productListBtn = new JButton(buttonStr);
        productTablePane = ViewUtil.createContentPane();
        catalogListBtn = new JButton(buttonStr);
        catalogTablePane = ViewUtil.createContentPane();
        loginRecordListBtn = new JButton(buttonStr);
        loginRecordTablePane = ViewUtil.createContentPane();
        userListBtn = new JButton(buttonStr);
        userTablePane = ViewUtil.createContentPane();

        final JPanel container = new JPanel(new GridBagLayout());
        container.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        container.add(createListPane("Suppliers", supplierListBtn, supplierTablePane),
                createConstraints(0, 0));
        container.add(createListPane("Products", productListBtn, productTablePane),
                createConstraints(0, 1));
        container.add(createListPane("Catalogs", catalogListBtn, catalogTablePane),
                createConstraints(1, 1));
        container.add(createListPane("Login Records", loginRecordListBtn, loginRecordTablePane),
                createConstraints(0, 2));
        container.add(createListPane("Users", userListBtn, userTablePane),
                createConstraints(1, 2));

        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        container.add(new JPanel(), c);

        pane.add(container);
    }

    /** Render. */
    public void render(
            final List<Supplier> suppliers,
            final List<Product> products,
            final List<Catalog> catalogs,
            final List<LoginRecord> loginRecords,
            final List<User> users,
            final boolean isAdministrator) {

        updateTable(
                new String[] {"ID", "Name", "Status"}, 
                new int[] {50, 150, 70},
                suppliers, 
                supplierTablePane, 
                x -> new JComponent[] {
                    ViewUtil.createUnboldLabel(x.getId()),
                    ViewUtil.createUnboldLabel(x.getName()),
                    ViewUtil.createUnboldLabel(x.isActive() ? "Active" : "Inactive")});

        updateTable(
                new String[] {"ID", "Name", "Quantity", "Price", "Discount"}, 
                new int[] {50, 150, 50, 50, 70},
                products, 
                productTablePane, 
                x -> new JComponent[] {
                    ViewUtil.createUnboldLabel(x.getId()),
                    ViewUtil.createUnboldLabel(x.getName()),
                    ViewUtil.createUnboldLabel(Integer.toString(x.getQuantity())),
                    ViewUtil.createUnboldLabel(String.format("%.2f", x.getRetailPrice())),
                    ViewUtil.createUnboldLabel(String.format("%.2f", x.getDiscount() * 100))});

        updateTable(
                new String[] {"ID", "Title", "Start", "End"}, 
                new int[] {50, 120, 90, 90},
                catalogs, 
                catalogTablePane, 
                x -> new JComponent[] {
                    ViewUtil.createUnboldLabel(x.getId()),
                    ViewUtil.createUnboldLabel(x.getTitle()),
                    ViewUtil.createUnboldLabel(x.getSeasonStartDate().toString()),
                    ViewUtil.createUnboldLabel(x.getSeasonEndDate().toString())});
                    

        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM HH:mm")
                .withZone(ZoneId.systemDefault());

        updateTable(
                new String[] {"ID", "User ID", "Username", "Action", "Timestamp"}, 
                new int[] {50, 50, 80, 70, 100},
                loginRecords, 
                loginRecordTablePane, 
                x -> new JComponent[] {
                    ViewUtil.createUnboldLabel(x.getId()),
                    ViewUtil.createUnboldLabel(x.getUserId()),
                    ViewUtil.createUnboldLabel(x.getUser().getUsername()),
                    ViewUtil.createUnboldLabel(x.getAction().toString()),
                    ViewUtil.createUnboldLabel(formatter.format(x.getTimestamp()))});

        updateTable(
                new String[] {"ID", "Username", "Status"}, 
                new int[] {50, 80, 70},
                users, 
                userTablePane, 
                x -> new JComponent[] {
                    ViewUtil.createUnboldLabel(x.getId()),
                    ViewUtil.createUnboldLabel(x.getUsername()),
                    ViewUtil.createUnboldLabel(x.isActive() ? "Active" : "Inactive")});

        loginRecordTablePane.getParent().setVisible(isAdministrator);
        userTablePane.getParent().setVisible(isAdministrator);
    }

    /** Update table. */
    private static <T> void updateTable(
            final String[] tableHeads, 
            final int[] columnWidths, 
            final List<T> records,
            final JPanel tablePane,
            final Function<T, JComponent[]> toComponents) {

        tablePane.removeAll();
        tablePane.add(ViewUtil.createHeaderRow(tableHeads, columnWidths));
        for (final T x : records) {
            tablePane.add(ViewUtil.createBodyRow(toComponents.apply(x), columnWidths));
        }
        tablePane.revalidate();
        tablePane.repaint();
    }

    /** Create list pane. */
    private static JPanel createListPane(
            final String title, 
            final JButton linkBtn, 
            final JPanel tablePane) {

        final JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(BorderFactory.createLineBorder(new Color(0.4f, 0.4f, 0.4f), 2));

        final JPanel titlePane = ViewUtil.createHorizontalPane();
        titlePane.setBackground(new Color(0.4f, 0.4f, 0.4f));

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);

        tablePane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        titlePane.add(Box.createRigidArea(new Dimension(0, 40)));
        titlePane.add(Box.createRigidArea(new Dimension(10, 0)));
        titlePane.add(titleLbl);
        titlePane.add(Box.createHorizontalGlue());
        titlePane.add(linkBtn);
        titlePane.add(Box.createRigidArea(new Dimension(10, 0)));
        pane.add(titlePane, BorderLayout.PAGE_START);
        pane.add(tablePane, BorderLayout.CENTER);

        return pane;
    }

    /** Create constraint. */
    private static GridBagConstraints createConstraints(final int x, final int y) {
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 30, 30);
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }
}
