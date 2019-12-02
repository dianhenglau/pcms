package pcms.catalog;

import java.awt.Dimension;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import pcms.ViewUtil;
import pcms.product.Product;

/** Catalog info view. */
public final class CatalogInfoView {
    /** Table columns. */
    private static final String[] COLUMNS = {
            "ID", "Image", "Name", "Price", "Current Discount", "Special Discount"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            70, 70, 120, 60, 120, 120};

    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** Back button. */
    public final JButton backBtn;
    /** PDF button. */
    public final JButton pdfBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Banner label. */
    private final JLabel bannerLbl;
    /** Title label. */
    private final JLabel titleLbl;
    /** Description text area. */
    private final JTextArea descriptionTa;
    /** Season start date label. */
    private final JLabel seasonStartDateLbl;
    /** Season end date label. */
    private final JLabel seasonEndDateLbl;
    /** Timestamp label. */
    private final JLabel timestampLbl;
    /** User label. */
    private final JLabel userLbl;
    /** Products pane. */
    private final JPanel productsPane;

    /** Construct. */
    public CatalogInfoView() {
        pane = ViewUtil.createContainerPane("Catalog Info");

        editBtn = new JButton("Edit");
        backBtn = new JButton("Back");
        pdfBtn = new JButton("Generate PDF");
        idLbl = ViewUtil.createValueLabel();
        titleLbl = ViewUtil.createValueLabel();
        bannerLbl = ViewUtil.createFullImageLabel();
        descriptionTa = ViewUtil.createViewOnlyTextArea();
        seasonStartDateLbl = ViewUtil.createValueLabel();
        seasonEndDateLbl = ViewUtil.createValueLabel();
        timestampLbl = ViewUtil.createValueLabel();
        userLbl = ViewUtil.createValueLabel();
        productsPane = ViewUtil.createContentPane();

        final String[] labels = {
                "ID", "Title", "Banner", "Description", "Season Start Date", "Season End Date",
                "Created On", "Created By", "Products"};
        final JComponent[] components = {
                idLbl, titleLbl, bannerLbl, descriptionTa, seasonStartDateLbl, seasonEndDateLbl, 
                timestampLbl, userLbl, productsPane};
        
        pane.add(ViewUtil.createButtonControlPane(backBtn, editBtn, pdfBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
        pane.add(Box.createRigidArea(new Dimension(0, 30)));
    }
    
    /** Render. */
    public void render(final Catalog catalog, final boolean isProductManager) {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        
        editBtn.setActionCommand(catalog.getId());
        pdfBtn.setActionCommand(catalog.getId());
        idLbl.setText(catalog.getId());
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(catalog.getBanner()));
        titleLbl.setText(catalog.getTitle());
        descriptionTa.setText(catalog.getDescription());
        seasonStartDateLbl.setText(catalog.getSeasonStartDate().toString());
        seasonEndDateLbl.setText(catalog.getSeasonEndDate().toString());
        timestampLbl.setText(formatter.format(catalog.getTimestamp()));
        userLbl.setText(catalog.getUser().getFullName());
        
        productsPane.removeAll();
        productsPane.add(ViewUtil.createHeaderRow(COLUMNS, WIDTHS));
        for (final ProductDiscount p: catalog.getProductDiscounts()) {
            productsPane.add(toTableRow(p));
        }
        productsPane.revalidate();
        productsPane.repaint();

        editBtn.setVisible(isProductManager);
        pdfBtn.setVisible(isProductManager);
    }

    /** Convert product discount to row. */
    private static JPanel toTableRow(final ProductDiscount productDiscount) {
        final Product product = productDiscount.getProduct();

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(product.getId()),
                ViewUtil.createThumbnailLabel(product.getImage()),
                ViewUtil.createUnboldLabel(product.getName()),
                ViewUtil.createUnboldLabel(String.format("%.2f", product.getRetailPrice())),
                ViewUtil.createUnboldLabel(String.format("%.0f%%", product.getDiscount() * 100)),
                ViewUtil.createUnboldLabel(String.format("%.0f%%", 
                        productDiscount.getDiscount() * 100))};

        return ViewUtil.createBodyRow(components, WIDTHS);
    }
}
