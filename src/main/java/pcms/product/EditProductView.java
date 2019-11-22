package pcms.product;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;

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
    /** Image file path label. */
    public final JLabel imageFilePathLbl;
    /** Name text field. */
    public final JTextField nameTf;
    /** Brand text field. */
    public final JTextField brandTf;
    /** Category combo box. */
    public final JComboBox categoryCob;
    /** Quantity text field. */
    public final JTextField quantityTf;
    /** Description text area. */
    public final JTextArea descriptionTa;
    /** Retail Price text field. */
    public final JTextField retailPriceTf;
    /** Discount text field. */
    public final JTextField discountTf;
    /** Supplier combo box. */
    public final JComboBox supplierCob;

    /** Construct. */
    public EditProductView() {
        pane = ViewUtil.createContainerPane("Edit Product");
        
        String[] categoryStrings = {};
        String[] supplierStrings = {};

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = new JLabel();
        nameTf = ViewUtil.createTextField(25);
        imageLbl = new JLabel();
        imageFilePathLbl = new JLabel();
        imageBtn = new JButton("Add File");
        brandTf = ViewUtil.createTextField(25);
        categoryCob = new JComboBox(categoryStrings);
        quantityTf = ViewUtil.createTextField(25);
        descriptionTa = ViewUtil.createTextArea();
        retailPriceTf = ViewUtil.createTextField(25);
        discountTf = ViewUtil.createTextField(25);
        supplierCob = new JComboBox(supplierStrings);

        final String[] labels = {
                "ID", "Name", "Image", "Image File Path", "Brand", "Catergory", 
                "Quantity", "Description", "Retail Price", "Discount", "Supplier"};
        final JComponent[] components = {
                idLbl, nameTf, imageLbl, imageFilePathLbl, brandTf, categoryCob, 
                quantityTf, descriptionTa, retailPriceTf, discountTf, supplierCob};

        pane.add(ViewUtil.createButtonControlPane(imageBtn, cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Product product) {
        saveBtn.setActionCommand(product.getId());
        cancelBtn.setActionCommand(product.getId());
        idLbl.setText(product.getId());
        nameTf.setText(product.getName());
        imageLbl.setIcon(ViewUtil.createFullImageIcon(product.getImage()));
        imageFilePathLbl.setText(product.getImage());
        brandTf.setText(product.getBrand());
        categoryCob.setSelectedItem(product.getCategory());
        quantityTf.setText(Integer.toString(product.getQuantity()));
        descriptionTa.setText(product.getDescription());
        retailPriceTf.setText(Double.toString(product.getRetailPrice()));
        discountTf.setText(Double.toString(product.getDiscount()));
        supplierCob.setSelectedItem(product.getSupplier());
    }
}
