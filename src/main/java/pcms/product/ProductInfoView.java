package pcms.product;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import pcms.ViewUtil;

/** Product info view. */
public final class ProductInfoView {
    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Image label. */
    private final JLabel imageLbl;
    /** Name label. */
    private final JLabel nameLbl;
    /** Brand label. */
    private final JLabel brandLbl;
    /** Category label. */
    private final JLabel categoryLbl;
    /** Quantity label. */
    private final JLabel quantityLbl;
    /** Description label. */
    private final JTextArea descriptionLbl;
    /** Retail Price label. */
    private final JLabel retailPriceLbl;
    /** Discount label. */
    private final JLabel discountLbl;
    /** Supplier label. */
    private final JLabel supplierLbl;

    /** Construct. */
    public ProductInfoView() {
        pane = ViewUtil.createContainerPane("Product Info");

        editBtn = new JButton("Edit");
        idLbl = new JLabel();
        imageLbl = new JLabel();
        nameLbl = new JLabel();
        brandLbl = new JLabel();
        categoryLbl = new JLabel();
        quantityLbl = new JLabel();
        descriptionLbl = ViewUtil.createViewOnlyTextArea();
        retailPriceLbl = new JLabel();
        discountLbl = new JLabel();
        supplierLbl = new JLabel();

        final String[] labels = {
                "ID", "Image", "Name", "Brand", "Category",
                "Quantity", "Description", "Retail Price", "Discount", "Supplier"};
        final JComponent[] components = {
                idLbl, imageLbl, nameLbl, brandLbl, categoryLbl,
                quantityLbl, descriptionLbl, retailPriceLbl, discountLbl, supplierLbl};

        pane.add(ViewUtil.createButtonControlPane(editBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }
    
    /** Render. */
    public void render(final Product product) {
        editBtn.setActionCommand(product.getId());
        idLbl.setText(product.getId());
        imageLbl.setIcon(ViewUtil.createFullImageIcon(product.getImage()));
        nameLbl.setText(product.getName());
        brandLbl.setText(product.getBrand());
        categoryLbl.setText(product.getCategory().getName());
        quantityLbl.setText(Integer.toString(product.getQuantity()));
        descriptionLbl.setText(product.getDescription());
        retailPriceLbl.setText(String.format("%.2f", product.getRetailPrice()));
        discountLbl.setText(String.format("%.0f%%", product.getDiscount()));
        supplierLbl.setText(product.getSupplier().getName());
    }
}
