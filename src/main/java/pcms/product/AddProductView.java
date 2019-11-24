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

/** Add product view. */
public final class AddProductView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** Image button. */
    public final JButton imageBtn;
    /** Image label. */
    public final JLabel imageLbl;
    /** Image file path label. */
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
    public AddProductView() {
        pane = ViewUtil.createContainerPane("Add Product");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
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
                "Name", "Image", "Brand", "Catergory", "Quantity", 
                "Description", "Retail Price", "Discount", "Supplier"};
        final JComponent[] components = {
                nameTf, imagePane, brandTf, categoryCob, quantityTf, 
                ViewUtil.createScrollPane(descriptionTa, 450, 100),
                retailPriceTf, discountTf, supplierCob};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final List<Category> categories, final List<Supplier> suppliers) {
        nameTf.setText("");
        imageLbl.setIcon(null);
        filenameLbl.setText("");
        brandTf.setText("");
        
        categoryCob.removeAllItems();
        for (final Category c : categories) {
            categoryCob.addItem(c.getName());
        }
        categoryCob.setSelectedItem(null); 
        
        quantityTf.setValue(Integer.valueOf(0));
        descriptionTa.setText("");
        retailPriceTf.setValue(Double.valueOf(0.0));
        discountTf.setValue(Double.valueOf(0.0));
        
        supplierCob.removeAllItems();
        for (final Supplier s : suppliers) {
            supplierCob.addItem(s.getName());
        }
        supplierCob.setSelectedItem(null);
    }

    /** Render image. */
    public void renderImage(final String imagePath) {
        imageLbl.setIcon(ViewUtil.createFullImageIcon(imagePath));
        filenameLbl.setText(imagePath);
    }
}
