package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.catalog.ProductDiscount;
import pcms.product.Product;
import pcms.product.ProductRepository;

/** Test ProductDiscount. */
@ExtendWith(MockitoExtension.class)
class ProductDiscountTest {
    /** Test complete product discount. */
    @Test 
    public void testCompleteProductDiscount() {
        final ProductDiscount productDiscount = new ProductDiscount("P001", 10);
        assertEquals("P001", productDiscount.getProductId());
        assertEquals(10, productDiscount.getDiscount());
    }

    /** Test get product. */
    @Test
    public void testGetProduct() {
        // Mocking
        final Product product = mock(Product.class);
        final ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findWithId(any())).thenReturn(Optional.of(product));

        ProductDiscount.setProductRepository(productRepository);
        final ProductDiscount productDiscount = new ProductDiscount("P002", 20);
        assertSame(product, productDiscount.getProduct());
    }
}
