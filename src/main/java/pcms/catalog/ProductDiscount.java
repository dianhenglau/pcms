package pcms.catalog;

import java.util.Optional;
import pcms.product.Product;
import pcms.product.ProductRepository;

/** A Pair of product and its discount. */
public final class ProductDiscount {
    /** Product repository. */
    private static ProductRepository productRepository;

    /** Product Id. */
    private final String productId;
    /** Discount. */
    private final double discount;
    
    /** Construct. */
    public ProductDiscount(final String productId, final double discount) {
        this.productId = productId;
        this.discount = discount;
    }
    
    /** Get product ID. */
    public String getProductId() {
        return productId;
    }

    /** Get discount. */
    public double getDiscount() {
        return discount;
    }

    /** Get product. */
    public Optional<Product> getProduct() {
        return productRepository.findWithId(productId);
    }

    /** Set product repository. */
    public static void setProductRepository(final ProductRepository productRepository) {
        ProductDiscount.productRepository = productRepository;
    }
}
