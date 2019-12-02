package pcms.catalog;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;
import pcms.product.Product;

/** Edit catalog view. */
public final class EditCatalogView { // NOPMD - Okay to have many fields
    /** Table columns. */
    private static final String[] COLUMNS = {
            "", "ID", "Image", "Name", "Price", "Current Discount", "Special Discount"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            30, 70, 70, 120, 60, 120, 120};

    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** ID label. */
    public final JLabel idLbl;
    /** Banner button. */
    public final JButton bannerBtn;
    /** Banner label. */
    public final JLabel bannerLbl;
    /** Banner filename label. */
    public final JLabel filenameLbl;
    /** Title text field. */
    public final JTextField titleTf;
    /** Description text area. */
    public final JTextArea descriptionTa;
    /** Display start date text field. */
    public final JLabel displayStartDateLbl;
    /** Select start date button. */
    public final JButton selectStartDateBtn;
    /** Display end date text field. */
    public final JLabel displayEndDateLbl;
    /** Select end date button. */
    public final JButton selectEndDateBtn;
    /** Timestamp label. */
    public final JLabel timestampLbl;
    /** User label. */
    public final JLabel userLbl;
    /** Products pane. */
    private final JPanel productsPane;
    /** Selected product checkboxes. */
    public final List<JCheckBox> selectedProductCbs;
    /** Special discounts. */
    public final List<JFormattedTextField> specialDiscountTfs;

    /** Construct. */
    public EditCatalogView() {
        pane = ViewUtil.createContainerPane("Edit Catalog");
        
        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = ViewUtil.createValueLabel();
        titleTf = ViewUtil.createTextField(25);
        bannerLbl = ViewUtil.createFullImageLabel();
        filenameLbl = ViewUtil.createUnboldLabel("");
        bannerBtn = new JButton("Add Image");
        descriptionTa = ViewUtil.createTextArea();
        displayStartDateLbl = ViewUtil.createUnboldLabel("");
        selectStartDateBtn = new JButton("Select Date");
        displayEndDateLbl = ViewUtil.createUnboldLabel("");
        selectEndDateBtn = new JButton("Select Date");
        timestampLbl = ViewUtil.createValueLabel();
        userLbl = ViewUtil.createValueLabel();
        productsPane = ViewUtil.createContentPane();
        selectedProductCbs = new ArrayList<>();
        specialDiscountTfs = new ArrayList<>();

        final JPanel bannerPane = ViewUtil.createImagePane(bannerLbl, filenameLbl, bannerBtn);
        final JPanel seasonStartDatePane = ViewUtil.createSelectDatePane(
                displayStartDateLbl, selectStartDateBtn);
        final JPanel seasonEndDatePane = ViewUtil.createSelectDatePane(
                displayEndDateLbl, selectEndDateBtn);

        final String[] labels = {
                "ID", "Title", "Banner", "Description", "Season Start Date", "Season End Date",
                "Created On", "Created By", "Products"};
        final JComponent[] components = {
                idLbl, titleTf, bannerPane, ViewUtil.createScrollPane(descriptionTa, 450, 100),
                seasonStartDatePane, seasonEndDatePane, timestampLbl, userLbl, productsPane};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
        pane.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    /** Render. */
    public void render(final Catalog catalog, final List<Product> products) {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        
        saveBtn.setActionCommand(catalog.getId());
        cancelBtn.setActionCommand(catalog.getId());
        idLbl.setText(catalog.getId());
        titleTf.setText(catalog.getTitle());
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(catalog.getBanner()));
        filenameLbl.setText(catalog.getBanner());
        descriptionTa.setText(catalog.getDescription());
        displayStartDateLbl.setText(catalog.getSeasonStartDate().toString());
        displayEndDateLbl.setText(catalog.getSeasonEndDate().toString());
        timestampLbl.setText(formatter.format(catalog.getTimestamp()));
        userLbl.setText(catalog.getUser().getFullName());

        selectedProductCbs.clear();
        specialDiscountTfs.clear();
        productsPane.removeAll();
        productsPane.add(ViewUtil.createHeaderRow(COLUMNS, WIDTHS));
        for (final Product p: products) {
            productsPane.add(toTableRow(p, catalog));
        }
        productsPane.revalidate();
        productsPane.repaint();
    }

    /** Render image. */
    public void renderImage(final String imagePath) {
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(imagePath));
        filenameLbl.setText(imagePath);
    }
    
    /** Render season start date. */
    public void renderStartDate(final String date) {
        displayStartDateLbl.setText(date);
    }
    
    /** Render season end date. */
    public void renderEndDate(final String date) {
        displayEndDateLbl.setText(date);
    }

    /** Convert product discount to row. */
    private JPanel toTableRow(final Product product, final Catalog catalog) {
        final JCheckBox selectedCb = new JCheckBox();
        selectedCb.setActionCommand(product.getId());
        selectedProductCbs.add(selectedCb);

        final JFormattedTextField discountTf = ViewUtil.createFormattedTextField(
                NumberFormat.getPercentInstance());
        discountTf.setValue(Double.valueOf(0.0));
        specialDiscountTfs.add(discountTf);

        for (final ProductDiscount pd: catalog.getProductDiscounts()) {
            if (pd.getProduct().getId().equals(product.getId())) {
                selectedCb.setSelected(true);
                discountTf.setValue(Double.valueOf(pd.getDiscount()));
                break;
            }
        }

        final JComponent[] components = {
                selectedCb,
                ViewUtil.createUnboldLabel(product.getId()),
                ViewUtil.createThumbnailLabel(product.getImage()),
                ViewUtil.createUnboldLabel(product.getName()),
                ViewUtil.createUnboldLabel(String.format("%.2f", product.getRetailPrice())),
                ViewUtil.createUnboldLabel(String.format("%.0f%%", product.getDiscount() * 100)),
                discountTf};

        return ViewUtil.createBodyRow(components, WIDTHS);
    }
}
