package pcms.catalog;

import java.nio.file.Path;
import pcms.Repository;
import pcms.ValidationUtil;
import pcms.product.ProductRepository;
import pcms.user.UserRepository;

/** Models catalog repository. */
public final class CatalogRepository extends Repository<Catalog> {
    /** Category repository. */
    private final UserRepository userRepository;
    /** Supplier repository. */
    private final ProductRepository productRepository;
    /** Image directory. */
    private final Path imageDir;
    
    /** Constructor. */
    public CatalogRepository(
            final Path dataPath, 
            final UserRepository userRepository, 
            final ProductRepository productRepository) {

        super(dataPath, id -> new Catalog.Builder().withId(id).build(), Catalog::new);
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.imageDir = getImageDir("catalog_images");
    }

    /** Insert catalog. */
    @Override
    public Catalog insert(final Catalog catalog) {
        readFromFile();

        doCommonValidation(catalog);
        
        // Copy image
        final String id = String.format("G%05d", newId);
        final String newFilename = copyImage(imageDir, catalog.getBanner(), id);

        // Add to cache
        final Catalog newCatalog = new Catalog.Builder(catalog)
                .withId(id)
                .withBanner(newFilename)
                .build();
        cache.add(newCatalog);
        newId += 1;

        writeToFile();
        return newCatalog;
    }

    /** Update catalog. */
    @Override
    public Catalog update(final Catalog catalog) {
        readFromFile();

        // Make sure catalog id exists.
        final int index = ValidationUtil.idExists(cache, catalog);

        doCommonValidation(catalog);

        // Copy image
        Catalog newCatalog = catalog;
        if (!cache.get(index).getBanner().equals(catalog.getBanner())) {
            final String newFilename = copyImage(imageDir, catalog.getBanner(), catalog.getId());
            deleteImage(cache.get(index).getBanner());
            newCatalog = new Catalog.Builder(catalog).withBanner(newFilename).build();
        }

        cache.set(index, newCatalog);
        writeToFile();
        return cache.get(index);
    }

    /** Delete record. */
    @Override
    public Catalog delete(final Catalog catalog) {
        deleteImage(catalog.getBanner());
        return super.delete(catalog);
    }

    /** Do common validation. */
    private void doCommonValidation(final Catalog catalog) {
        // Make sure title is valid.
        ValidationUtil.validMinLength("title", catalog.getTitle(), 1); // NOPMD
        ValidationUtil.notExists("title", catalog.getTitle(), cache, Catalog::getTitle,
                catalog.getId(), Catalog::getId);

        // Make sure banner is provided.
        ValidationUtil.notEmpty("banner", catalog.getBanner());
        
        // Make sure user ID is valid and exist.
        ValidationUtil.notEmpty("user ID", catalog.getUserId());
        ValidationUtil.recordExists(userRepository, catalog.getUserId());
        
        // Make sure product ID is valid and exist.
        for (final ProductDiscount p: catalog.getProductDiscounts()) {
            ValidationUtil.notEmpty("product ID", p.getProductId());
            ValidationUtil.recordExists(productRepository, p.getProductId());
        }

        // Make sure start date is less than or equals to end date.
        ValidationUtil.validDateRange("season date range", catalog.getSeasonStartDate(), 
                catalog.getSeasonEndDate());
    }
}
