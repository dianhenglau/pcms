package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.category.Category;
import pcms.category.CategoryRepository;
import pcms.product.Product;
import pcms.supplier.Supplier;
import pcms.supplier.SupplierRepository;

/** Test Product. */
@ExtendWith(MockitoExtension.class)
class ProductTest {
    /** Test empty product. */
    @Test 
    public void testEmptyProduct() {
        final Product product = new Product.Builder().build();
        assertEquals("", product.getId());
        assertEquals("", product.getImage());
        assertEquals("", product.getName());
        assertEquals("", product.getBrand());
        assertEquals("", product.getCategoryId());
        assertEquals(0, product.getQuantity());
        assertEquals("", product.getDescription());
        assertEquals(0.0, product.getRetailPrice());
        assertEquals(0.0, product.getDiscount());
        assertEquals("", product.getSupplierId());
    }

    /** Test complete product. */
    @Test 
    public void testCompleteProduct() {
        final Product product = new Product.Builder()
                .withId("P007")
                .withImage("product_001.png")
                .withName("Pillow")
                .withBrand("Coop Home Goods")
                .withCategoryId("C002")
                .withQuantity(5)
                .withDescription("Breathability")
                .withRetailPrice(69.99)
                .withDiscount(0.0)
                .withSupplierId("S007")
                .build();

        assertEquals("P007", product.getId());
        assertEquals("product_001.png", product.getImage());
        assertEquals("Pillow", product.getName());
        assertEquals("Coop Home Goods", product.getBrand());
        assertEquals("C002", product.getCategoryId());
        assertEquals(5, product.getQuantity());
        assertEquals("Breathability", product.getDescription());
        assertEquals(69.99, product.getRetailPrice());
        assertEquals(0.0, product.getDiscount());
        assertEquals("S007", product.getSupplierId());
    }

    /** Test incomplete product. */
    @Test 
    public void testIncompleteProduct() {
        final Product product = new Product.Builder()
                .withId("P008")
                .withImage("product_002.png")
                .withName("Long Throw Pillow")
                .withCategoryId("C003")
                .withQuantity(5)
                .withDescription("Comfort")
                .withDiscount(0.0)
                .build();

        assertEquals("P008", product.getId());
        assertEquals("product_002.png", product.getImage());
        assertEquals("Long Throw Pillow", product.getName());
        assertEquals("C003", product.getCategoryId());
        assertEquals(5, product.getQuantity());
        assertEquals("Comfort", product.getDescription());
        assertEquals(0.0, product.getDiscount());

        assertEquals("", product.getBrand());
        assertEquals(0.0, product.getRetailPrice());
        assertEquals("", product.getSupplierId());
    }

    /** Test construct from row. */
    @Test
    public void testParse() {
        final Product product = new Product("P00013,image001.png,Throw Pillow,IKEA,C00009,10,"
                + "Best,50.0,10,S00031");
        assertEquals("P00013", product.getId());
        assertEquals("image001.png", product.getImage());
        assertEquals("Throw Pillow", product.getName());
        assertEquals("IKEA", product.getBrand());
        assertEquals("C00009", product.getCategoryId());
        assertEquals(10, product.getQuantity());
        assertEquals("Best", product.getDescription());
        assertEquals(50.0, product.getRetailPrice());
        assertEquals(10.0, product.getDiscount());
        assertEquals("S00031", product.getSupplierId());

        assertEquals("Fields count incorrect.", assertThrows(IllegalArgumentException.class, () -> {
            new Product(",,,,,,,,"); }).getMessage());
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final Product product = new Product.Builder()
                .withId("P001")
                .withImage("product_008.png")
                .withName("Pillow Case")
                .withBrand("Bear Bear")
                .withCategoryId("C003")
                .withQuantity(7)
                .withDescription("Cute")
                .withRetailPrice(7.77)
                .withDiscount(20.0)
                .withSupplierId("S009")
                .build();
        assertEquals("P001,product_008.png,Pillow Case,Bear Bear,C003,7,Cute,7.77,20.0,S009",
                product.toRow());
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        final String productRecord 
                = "P001,product_008.png,Pillow Case,Bear Bear,C003,7,Cute,7.77,20.0,S009";
        assertEquals(productRecord, new Product(productRecord).toRow());
    }

    /** Test get category. */
    @Test
    public void testGetCategory() {
        // Mocking
        final Category category = mock(Category.class);
        final CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findWithId(any())).thenReturn(Optional.of(category));

        Product.setCategoryRepository(categoryRepository);
        final Product product = new Product.Builder().build();
        assertSame(category, product.getCategory());
    }

    /** Test get supplier. */
    @Test
    public void testGetSupplier() {
        // Mocking
        final Supplier supplier = mock(Supplier.class);
        final SupplierRepository supplierRepository = mock(SupplierRepository.class);
        when(supplierRepository.findWithId(any())).thenReturn(Optional.of(supplier));

        Product.setSupplierRepository(supplierRepository);
        final Product product = new Product.Builder().build();
        assertSame(supplier, product.getSupplier());
    }
}
