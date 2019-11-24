package pcms.product;

import java.nio.file.Path;
import pcms.Repository;
import pcms.ValidationUtil;
import pcms.category.CategoryRepository;
import pcms.supplier.SupplierRepository;

/** Models product repository. */
public final class ProductRepository extends Repository<Product> {
    /** Category repository. */
    private final CategoryRepository categoryRepository;
    /** Supplier repository. */
    private final SupplierRepository supplierRepository;
    /** Image directory. */
    private final Path imageDir;
    
    /** Constructor. */
    public ProductRepository(
            final Path dataPath, 
            final CategoryRepository categoryRepository, 
            final SupplierRepository supplierRepository) {

        super(dataPath, id -> new Product.Builder().withId(id).build(), Product::new);
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.imageDir = getImageDir();
    }

    /** Insert product. */
    @Override
    public Product insert(final Product product) {
        readFromFile();

        doCommonValidation(product);
        
        // Copy image
        final String id = String.format("P%05d", newId);
        final String newFilename = copyImage(imageDir, product.getImage(), id);

        // Add to cache
        final Product newProduct = new Product.Builder(product)
                .withId(id)
                .withImage(newFilename)
                .build();
        cache.add(newProduct);
        newId += 1;

        writeToFile();
        return newProduct;
    }

    /** Update product. */
    @Override
    public Product update(final Product product) {
        readFromFile();

        // Make sure product id exists.
        final int index = ValidationUtil.idExists(cache, product);

        doCommonValidation(product);

        // Copy image
        Product newProduct = product;
        if (!cache.get(index).getImage().equals(product.getImage())) {
            final String newFilename = copyImage(imageDir, product.getImage(), product.getId());
            deleteImage(cache.get(index).getImage());
            newProduct = new Product.Builder(product).withImage(newFilename).build();
        }

        cache.set(index, newProduct);
        writeToFile();
        return cache.get(index);
    }

    /** Delete record. */
    @Override
    public Product delete(final Product product) {
        deleteImage(product.getImage());
        return super.delete(product);
    }

    /** Do common validation. */
    private void doCommonValidation(final Product product) {
        // Make sure name is valid.
        ValidationUtil.validMinLength("name", product.getName(), 1); // NOPMD
        ValidationUtil.notExists("name", product.getName(), cache, Product::getName,
                product.getId(), Product::getId);

        // Make sure image is provided.
        ValidationUtil.notEmpty("image", product.getImage());
        
        // Make sure brand is provided.
        ValidationUtil.notEmpty("brand", product.getBrand());
        
        // Make sure category ID is valid and exist.
        ValidationUtil.notEmpty("category ID", product.getCategoryId());
        ValidationUtil.recordExists(categoryRepository, product.getCategoryId());
        
        // Make sure quantity, retail price and discount are not negative.
        ValidationUtil.notNegative("quantity", product.getQuantity());
        ValidationUtil.notNegative("retail price", product.getRetailPrice());
        ValidationUtil.notNegative("discount", product.getDiscount());
        
        // Make sure supplier ID is valid and exist.
        ValidationUtil.notEmpty("supplier ID", product.getSupplierId());
        ValidationUtil.recordExists(supplierRepository, product.getSupplierId());
    }
}
