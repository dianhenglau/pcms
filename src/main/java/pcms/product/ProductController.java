package pcms.product;

import java.util.Locale;
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
    private final Session session;
    /** Product repository. */
    private final ProductRepository productRepository;
    /** Product repository. */
    private final CategoryRepository categoryRepository;
    /** Product repository. */
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
        addProductView.saveBtn.addActionListener(e -> store());
        addProductView.cancelBtn.addActionListener(e -> index(""));
        editProductView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editProductView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
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
            final Product newProduct = productRepository.insert(new Product.Builder()
                    .withName(addProductView.nameTf.getText())
                    .withImage(addProductView.imageFilePathLbl.getText())
                    .withBrand(addProductView.brandTf.getText())
                    .withCategoryId(((Category)addProductView.categoryCob.getSelectedItem()).getId())
                    .withQuantity(Integer.parseInt(addProductView.quantityTf.getText()))
                    .withDescription(addProductView.descriptionTa.getText())
                    .withRetailPrice(Double.parseDouble(addProductView.retailPriceTf.getText()))
                    .withSupplierId(((Supplier)addProductView.supplierCob.getSelectedItem()).getId())
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
            editProductView.render(product);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update product info. */
    public void update(final String id) {
        try {
            final Product product = ValidationUtil.recordExists(productRepository, id);
            final Product newProduct = productRepository.update(new Product.Builder(product)
                    .withId(id)
                    .withName(editProductView.nameTf.getText())
                    .withImage(editProductView.imageFilePathLbl.getText())
                    .withBrand(editProductView.brandTf.getText())
                    .withCategoryId(((Category)editProductView.categoryCob.getSelectedItem()).getId())
                    .withQuantity(Integer.parseInt(editProductView.quantityTf.getText()))
                    .withDescription(editProductView.descriptionTa.getText())
                    .withRetailPrice(Double.parseDouble(editProductView.retailPriceTf.getText()))
                    .withSupplierId(((Supplier)editProductView.supplierCob.getSelectedItem()).getId())
                    .build());
            
            productRepository.update(newProduct);
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
}
