package pcms.product;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
    public AddProductView() {
        pane = ViewUtil.createContainerPane("Add Product");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        nameTf = ViewUtil.createTextField(25);
        imageLbl = new JLabel();
        imageFilePathLbl = new JLabel();
        imageBtn = new JButton("Add File");
        brandTf = ViewUtil.createTextField(25);
        categoryCob = ViewUtil.createComboBox();
        quantityTf = ViewUtil.createTextField(25);
        descriptionTa = ViewUtil.createTextArea();
        retailPriceTf = ViewUtil.createTextField(25);
        discountTf = ViewUtil.createTextField(25);
        supplierCob = ViewUtil.createComboBox();       

        final String[] labels = {
                "Name", "Image","Image File Path", "Brand", "Catergory", "Quantity", "Description", 
                "Retail Price", "Discount", "Supplier"};
        final JComponent[] components = {
                nameTf, imageLbl, imageFilePathLbl, brandTf, categoryCob, quantityTf, descriptionTa,
                retailPriceTf, discountTf, supplierCob};

        pane.add(ViewUtil.createButtonControlPane(imageBtn, cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final List<Category> categories, final List<Supplier> suppliers) {
        nameTf.setText("");
        imageLbl.setIcon(null);
        imageFilePathLbl.setText("");
        
        brandTf.setText("");
        
        for (Category c : categories){
            categoryCob.addItem(c.getName());
        }
        categoryCob.setSelectedItem(null); 
        
        quantityTf.setText("");
        descriptionTa.setText("");
        retailPriceTf.setText("");
        discountTf.setText("");
        
        for (Supplier s : suppliers){
            supplierCob.addItem(s.getName());
        }
        supplierCob.setSelectedItem(null);
    }
}
