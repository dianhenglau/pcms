package pcms.catalog;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Catalog list view. */
public final class CatalogListView {
    /** Table columns. */
    private static final String[] COLUMNS = {
            "ID", "Banner", "Title", "Season Start Date", 
            "Season End Date", "Created On", "Created By", "Actions"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            70, 70, 120, 100, 
            100, 100, 100, 160};

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
    public CatalogListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("Catalog List");
        tablePane = ViewUtil.createContentPane();
        addBtn = new JButton("New");
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(addBtn, searchTf));
        pane.add(tablePane);
    }

    /** Render catalogs. */
    public void render(
            final List<Catalog> catalogs, 
            final String search,
            final boolean isProductManager,
            final ActionListener goView,
            final ActionListener goDelete) {

        searchTf.setText(search);
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final Catalog p : catalogs) {
            tablePane.add(toTableRow(p, goView, goDelete));
        }
        tablePane.revalidate();
        tablePane.repaint();

        addBtn.setVisible(isProductManager);
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(
            final Catalog catalog, 
            final ActionListener goView, 
            final ActionListener goDelete) {

        final JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(goView);
        viewBtn.setActionCommand(catalog.getId());
        
        final JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(goDelete);
        deleteBtn.setActionCommand(catalog.getId());

        final JPanel actionPane = ViewUtil.createHorizontalPane();
        actionPane.add(viewBtn);
        actionPane.add(Box.createRigidArea(new Dimension(5, 0)));
        actionPane.add(deleteBtn);
        
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(catalog.getId()),
                ViewUtil.createThumbnailLabel(catalog.getBanner()),
                ViewUtil.createUnboldLabel(catalog.getTitle()),
                ViewUtil.createUnboldLabel(catalog.getSeasonStartDate().toString()),
                ViewUtil.createUnboldLabel(catalog.getSeasonEndDate().toString()),
                ViewUtil.createUnboldLabel(formatter.format(catalog.getTimestamp())),
                ViewUtil.createUnboldLabel(catalog.getUser().getUsername()),
                actionPane};

        return ViewUtil.createBodyRow(components, WIDTHS);
    }
}
