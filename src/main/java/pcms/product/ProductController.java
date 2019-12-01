package pcms.product;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import pcms.ContentView;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.category.Category;
import pcms.category.CategoryRepository;
import pcms.supplier.Supplier;
import pcms.supplier.SupplierRepository;

/** Product controller. */
public final class ProductController {
    /** Session. */
    private final Session session; // NOPMD - temporaray
    /** Product repository. */
    private final ProductRepository productRepository;
    /** Category repository. */
    private final CategoryRepository categoryRepository;
    /** Supplier repository. */
    private final SupplierRepository supplierRepository;

    /** Product list view. */
    private final ProductListView productListView;
    /** Product info view. */
    private final ProductInfoView productInfoView;
    /** Add product view. */
    private final AddProductView addProductView;
    /** Edit product view. */
    private final EditProductView editProductView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public ProductController(
            final Session session,
            final ProductRepository productRepository,
            final CategoryRepository categoryRepository,
            final SupplierRepository supplierRepository,
            final ProductListView productListView,
            final ProductInfoView productInfoView,
            final AddProductView addProductView,
            final EditProductView editProductView,
            final RootView rootView) {

        this.session = session;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productListView = productListView;
        this.productInfoView = productInfoView;
        this.addProductView = addProductView;
        this.editProductView = editProductView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        productListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
        productListView.addBtn.addActionListener(e -> create());
        productInfoView.editBtn.addActionListener(e -> edit(e.getActionCommand()));
        productInfoView.backBtn.addActionListener(e -> index(productListView.searchTf.getText()));
        addProductView.saveBtn.addActionListener(e -> store());
        addProductView.cancelBtn.addActionListener(e -> index(""));
        addProductView.imageBtn.addActionListener(e -> chooseImage(addProductView::renderImage));
        editProductView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editProductView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
        editProductView.imageBtn.addActionListener(e -> chooseImage(editProductView::renderImage));
    }

    /** List products. */
    public void index(final String search) {
        final String lowerCase = search.toLowerCase(Locale.US);

        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.PRODUCT_LIST);
        
        if (search.isEmpty()) {
            productListView.render(
                    productRepository.all(), 
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        } else {
            productListView.render(
                    productRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getName().toLowerCase(Locale.US).contains(lowerCase)),
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        }
    }

    /** Show create product form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_PRODUCT);
        addProductView.render(categoryRepository.all(), supplierRepository.all());
    }

    /** Store created product. */
    public void store() {
        try {
            final String categoryName = (String) addProductView.categoryCob.getSelectedItem();
            if (categoryName == null) {
                throw new InvalidFieldException("category", "Category is required."); // NOPMD
            }

            final Optional<Category> category = categoryRepository.findWithName(categoryName);
            if (category.isEmpty()) {
                throw new InvalidFieldException("category ID", "Category ID not found.");
            }

            final String supplierName = (String) addProductView.supplierCob.getSelectedItem();
            if (supplierName == null) {
                throw new InvalidFieldException("supplier", "Supplier is required.");
            }

            final Optional<Supplier> supplier = supplierRepository.findWithName(supplierName);
            if (supplier.isEmpty()) {
                throw new InvalidFieldException("supplier ID", "Supplier ID not found.");
            }

            final Product newProduct = productRepository.insert(new Product.Builder()
                    .withName(addProductView.nameTf.getText())
                    .withImage(addProductView.filenameLbl.getText())
                    .withBrand(addProductView.brandTf.getText())
                    .withCategoryId(category.get().getId())
                    .withQuantity(((Number) addProductView.quantityTf.getValue()).intValue())
                    .withDescription(addProductView.descriptionTa.getText())
                    .withRetailPrice(((Number) addProductView.retailPriceTf.getValue())
                        .doubleValue())
                    .withDiscount(((Number) addProductView.discountTf.getValue()).doubleValue())
                    .withSupplierId(supplier.get().getId())
                    .build());

            rootView.showSuccessDialog("Product added.");
            show(newProduct.getId());

        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Show product info. */
    public void show(final String id) {
        try {
            final Product product = ValidationUtil.recordExists(productRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.PRODUCT_INFO);
            productInfoView.render(product);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Edit product info. */
    public void edit(final String id) {
        try {
            final Product product = ValidationUtil.recordExists(productRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.EDIT_PRODUCT);
            editProductView.render(product, categoryRepository.all(), supplierRepository.all());
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update product info. */
    public void update(final String id) {
        try {
            final String categoryName = (String) editProductView.categoryCob.getSelectedItem();
            if (categoryName == null) {
                throw new InvalidFieldException("category", "Category is required.");
            }

            final Optional<Category> category = categoryRepository.findWithName(categoryName);
            if (category.isEmpty()) {
                throw new InvalidFieldException("category ID", "Category ID not found.");
            }

            final String supplierName = (String) editProductView.supplierCob.getSelectedItem();
            if (supplierName == null) {
                throw new InvalidFieldException("category", "Category is required.");
            }

            final Optional<Supplier> supplier = supplierRepository.findWithName(supplierName);
            if (supplier.isEmpty()) {
                throw new InvalidFieldException("supplier ID", "Supplier ID not found.");
            }

            final Product product = ValidationUtil.recordExists(productRepository, id);
            productRepository.update(new Product.Builder(product)
                    .withName(editProductView.nameTf.getText())
                    .withImage(editProductView.filenameLbl.getText())
                    .withBrand(editProductView.brandTf.getText())
                    .withCategoryId(category.get().getId())
                    .withQuantity(((Number) editProductView.quantityTf.getValue()).intValue())
                    .withDescription(editProductView.descriptionTa.getText())
                    .withRetailPrice(((Number) editProductView.retailPriceTf.getValue())
                        .doubleValue())
                    .withDiscount(((Number) editProductView.discountTf.getValue()).doubleValue())
                    .withSupplierId(supplier.get().getId())
                    .build());

            rootView.showSuccessDialog("Product updated.");
            show(id);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }
    
    /** Destroy (delete) category. */
    public void destroy(final String id, final String originalParameter) {
        try {
            final Product product = ValidationUtil.recordExists(productRepository, id);
            productRepository.delete(product);
            rootView.showSuccessDialog("Product deleted.");
            index(originalParameter);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Choose image. */
    public void chooseImage(final Consumer<String> renderer) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg"));
        final int returnValue = fileChooser.showOpenDialog(rootView.frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            renderer.accept(fileChooser.getSelectedFile().toPath().toString());
        }
    }
}
