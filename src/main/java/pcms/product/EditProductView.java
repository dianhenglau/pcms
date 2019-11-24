package pcms.product;

import java.text.NumberFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;
import pcms.category.Category;
import pcms.supplier.Supplier;

/** Edit product view. */
public final class EditProductView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** ID label. */
    public final JLabel idLbl;
    /** Image button. */
    public final JButton imageBtn;
    /** Image label. */
    public final JLabel imageLbl;
    /** Filename label. */
    public final JLabel filenameLbl;
    /** Name text field. */
    public final JTextField nameTf;
    /** Brand text field. */
    public final JTextField brandTf;
    /** Category combo box. */
    public final JComboBox<String> categoryCob;
    /** Quantity text field. */
    public final JFormattedTextField quantityTf;
    /** Description text area. */
    public final JTextArea descriptionTa;
    /** Retail Price text field. */
    public final JFormattedTextField retailPriceTf;
    /** Discount text field. */
    public final JFormattedTextField discountTf;
    /** Supplier combo box. */
    public final JComboBox<String> supplierCob;

    /** Construct. */
    public EditProductView() {
        pane = ViewUtil.createContainerPane("Edit Product");
        
        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = ViewUtil.createValueLabel();
        nameTf = ViewUtil.createTextField(25);
        imageLbl = ViewUtil.createFullImageLabel();
        filenameLbl = ViewUtil.createUnboldLabel("");
        imageBtn = new JButton("Add Image");
        brandTf = ViewUtil.createTextField(25);
        categoryCob = ViewUtil.createComboBox();
        quantityTf = ViewUtil.createFormattedTextField(NumberFormat.getIntegerInstance());
        descriptionTa = ViewUtil.createTextArea();
        retailPriceTf = ViewUtil.createFormattedTextField(NumberFormat.getCurrencyInstance());
        discountTf = ViewUtil.createFormattedTextField(NumberFormat.getPercentInstance());
        supplierCob = ViewUtil.createComboBox();

        final JPanel imagePane = ViewUtil.createImagePane(imageLbl, filenameLbl, imageBtn);

        final String[] labels = {
                "ID", "Name", "Image", "Brand", "Catergory", 
                "Quantity", "Description", "Retail Price", "Discount", "Supplier"};
        final JComponent[] components = {
                idLbl, nameTf, imagePane, brandTf, categoryCob, 
                quantityTf, ViewUtil.createScrollPane(descriptionTa, 450, 100),
                retailPriceTf, discountTf, supplierCob};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(
            final Product product, 
            final List<Category> categories, 
            final List<Supplier> suppliers) {

        saveBtn.setActionCommand(product.getId());
        cancelBtn.setActionCommand(product.getId());

        idLbl.setText(product.getId());
        nameTf.setText(product.getName());
        imageLbl.setIcon(ViewUtil.createFullImageIcon(product.getImage()));
        filenameLbl.setText(product.getImage());
        brandTf.setText(product.getBrand());

        categoryCob.removeAllItems();
        for (final Category c : categories) {
            categoryCob.addItem(c.getName());
        }
        categoryCob.setSelectedItem(product.getCategory().getName());

        quantityTf.setValue(Integer.valueOf(product.getQuantity()));
        descriptionTa.setText(product.getDescription());
        retailPriceTf.setValue(Double.valueOf(product.getRetailPrice()));
        discountTf.setValue(Double.valueOf(product.getDiscount()));

        supplierCob.removeAllItems();
        for (final Supplier s : suppliers) {
            supplierCob.addItem(s.getName());
        }
        supplierCob.setSelectedItem(product.getSupplier().getName());
    }

    /** Render image. */
    public void renderImage(final String imagePath) {
        imageLbl.setIcon(ViewUtil.createFullImageIcon(imagePath));
        filenameLbl.setText(imagePath);
    }
}
