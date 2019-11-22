package pcms.product;

import java.nio.file.Path;
import pcms.Repository;
import pcms.ValidationUtil;
import pcms.category.CategoryRepository;
import pcms.supplier.SupplierRepository;

/** Models product repository. */
public final class ProductRepository extends Repository<Product> {
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    
    /** Constructor. */
    public ProductRepository(final Path dataPath, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        super(dataPath, id -> new Product.Builder().withId(id).build(), Product::new);
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    /** Insert product. */
    @Override
    public Product insert(final Product product) {
        readFromFile();

        // Make sure name is valid.
        ValidationUtil.validMinLength("name", product.getName(), 1); // NOPMD
        ValidationUtil.notExists("name", product.getName(), cache, Product::getName);

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
        
        // Add to cache
        final Product newProduct = new Product.Builder(product)
                .withId(String.format("P%05d", newId))
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

        // Make sure name is valid.
        ValidationUtil.validMinLength("name", product.getName(), 1); // NOPMD
        ValidationUtil.notExists("name", product.getName(), cache, Product::getName);

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
        
        cache.set(index, product);
        writeToFile();
        return cache.get(index);
    }
}
