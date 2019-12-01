package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.category.Category;
import pcms.category.CategoryRepository;
import pcms.product.Product;
import pcms.product.ProductRepository;
import pcms.supplier.Supplier;
import pcms.supplier.SupplierRepository;

/** Test Product Repository. */
@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {
    // CHECKSTYLE:OFF
    /** Row 1 of products.csv. */
    private static final String ROW1 = "P00001,data/test/product_images/P00001-image1.jpg,Pillow,Coop Home Goods,C00001,15,Good,12.34,0.0,S00001";
    /** Row 2 of products.csv. */
    private static final String ROW2 = "P00002,data/test/product_images/P00002-image2.jpg,Long Throw Pillow,Sleep Better,C00001,16,Better,56.78,10.0,S00002";
    /** Row 3 of products.csv. */
    private static final String ROW3 = "P00003,data/test/product_images/P00003-image3.jpg,Bed,Malouf,C00001,17,Best,90.12,15.0,S00003";
    /** Row 4 of products.csv. */
    private static final String ROW4 = "P00004,data/test/product_images/P00004-image4.jpg,Bed Rack,Snuggle-Pedic,C00002,18,Awesome,34.56,20.0,S00002";
    /** Row 5 of products.csv. */
    private static final String ROW5 = "P00005,data/test/product_images/P00005-image5.jpg,Blanket,Mediflow,C00003,19,Holly,78.9,30.0,S00004";
    /** products.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");
    // CHECKSTYLE:ON

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(ProductRepository.class));

        final ProductRepository productRepository = new ProductRepository(
                TestUtil.getDataPath("products.csv"),
                mock(CategoryRepository.class),
                mock(SupplierRepository.class));
        assertNotNull(productRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("products_insert.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.deleteIfExists(Path.of("data/test/product_images/P00006-image6.jpg"));

            // Mocking
            final Category category = mock(Category.class);
            final CategoryRepository categoryRepository = mock(CategoryRepository.class);

            final Supplier supplier = mock(Supplier.class);
            final SupplierRepository supplierRepository = mock(SupplierRepository.class);

            // Start testing.
            final ProductRepository productRepository = new ProductRepository(
                    filePath, categoryRepository, supplierRepository);
            final Product newProduct = new Product.Builder()
                    .withId("P00006")
                    .withImage("data/test/image6.jpg")
                    .withName("Pillow2")
                    .withBrand("Coop Home Goods")
                    .withCategoryId("C00001") // NOPMD
                    .withQuantity(20)
                    .withDescription("Wow")
                    .withRetailPrice(69.99)
                    .withDiscount(0.0)
                    .withSupplierId("S00003") // NOPMD
                    .build();

            when(categoryRepository.findWithId("C00001")).thenReturn(Optional.of(category));
            when(supplierRepository.findWithId("S00003")).thenReturn(Optional.of(supplier));
            final Product result = productRepository.insert(newProduct);
            assertEquals(String.join("", "7\n", CONTENT.substring(2), result.toRow()
                    .replace("/image6.jpg", "/product_images/P00006-image6.jpg"), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertTrue(Files.exists(Path.of("data/test/product_images/P00006-image6.jpg")));
            verify(categoryRepository).findWithId("C00001");
            verify(supplierRepository).findWithId("S00003");

            doCommonValidations(productRepository, categoryRepository, supplierRepository, 
                    newProduct);

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test update. */
    @Test
    public void testUpdate() {
        try {
            final Path filePath = TestUtil.getDataPath("products_update.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.deleteIfExists(Path.of("data/test/product_images/P00006-image6.jpg"));
            Files.copy(Path.of("data/test/image4.jpg"), Path.of("data/test/P00004-image4.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);

            // Mocking
            final Category category = mock(Category.class);
            final CategoryRepository categoryRepository = mock(CategoryRepository.class);

            final Supplier supplier = mock(Supplier.class);
            final SupplierRepository supplierRepository = mock(SupplierRepository.class);

            // Start testing.
            final ProductRepository productRepository = new ProductRepository(
                    filePath, categoryRepository, supplierRepository);
            final Product product = productRepository.findWithId("P00004").get();
            final Product newProduct = new Product.Builder(product)
                    .withDescription("Fantastic")
                    .withImage("data/test/image6.jpg")
                    .build();

            when(categoryRepository.findWithId("C00002")).thenReturn(Optional.of(category));
            when(supplierRepository.findWithId("S00002")).thenReturn(Optional.of(supplier));
            productRepository.update(newProduct);
            assertEquals(
                    CONTENT.replace("Awesome", "Fantastic").replace("image4.jpg", "image6.jpg"),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertTrue(Files.exists(Path.of("data/test/product_images/P00004-image6.jpg")));
            assertFalse(Files.exists(Path.of("data/test/product_images/P00004-image4.jpg")));
            verify(categoryRepository).findWithId("C00002");
            verify(supplierRepository).findWithId("S00002");

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                productRepository.update(new Product.Builder().withId("P00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

            doCommonValidations(productRepository, categoryRepository, supplierRepository, 
                    newProduct);

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test delete. */
    @Test
    public void testDelete() {
        try {
            final Path filePath = TestUtil.getDataPath("products_delete.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.copy(Path.of("data/test/image4.jpg"), Path.of("data/test/P00003-image4.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);

            // Mocking
            final CategoryRepository categoryRepository = mock(CategoryRepository.class);
            final SupplierRepository supplierRepository = mock(SupplierRepository.class);

            // Start testing.
            final ProductRepository productRepository = new ProductRepository(
                    filePath, categoryRepository, supplierRepository);
            final Product product = productRepository.findWithId("P00003").get();

            productRepository.delete(product);
            assertEquals(
                    CONTENT.replace(ROW3 + "\n", ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertFalse(Files.exists(Path.of("data/test/product_images/P00003-image4.jpg")));

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                productRepository.delete(new Product.Builder().withId("P00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test with non existent file. */
    @Test
    public void testWithNonExistentFile() {
        try {
            final Path filePath = TestUtil.getDataPath("non_existent_file.csv");
            Files.deleteIfExists(filePath);

            // Mocking
            final Category category = mock(Category.class);
            final CategoryRepository categoryRepository = mock(CategoryRepository.class);

            final Supplier supplier = mock(Supplier.class);
            final SupplierRepository supplierRepository = mock(SupplierRepository.class);

            // Start testing.
            final ProductRepository productRepository = new ProductRepository(
                    filePath, categoryRepository, supplierRepository);

            final Product newProduct = new Product.Builder()
                    .withId("P00006")
                    .withImage("image6.jpg")
                    .withName("Pillow2")
                    .withBrand("Coop Home Goods")
                    .withCategoryId("C00001")
                    .withQuantity(20)
                    .withDescription("Wow")
                    .withRetailPrice(69.99)
                    .withDiscount(0.0)
                    .withSupplierId("S00003")
                    .build();

            when(categoryRepository.findWithId("C00001")).thenReturn(Optional.of(category));
            when(supplierRepository.findWithId("S00003")).thenReturn(Optional.of(supplier));
            final Product result = productRepository.insert(newProduct);
            assertEquals(
                    String.join("\n", "2", result.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            verify(categoryRepository).findWithId("C00001");
            verify(supplierRepository).findWithId("S00003");

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Do common validations. */
    private void doCommonValidations(
            final ProductRepository productRepository,
            final CategoryRepository categoryRepository,
            final SupplierRepository supplierRepository,
            final Product newProduct) {

        InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(new Product.Builder(newProduct).withName("").build());
        });
        assertEquals("name", ex.getLabel());
        assertEquals(TestUtil.minLenErrMsg("Name", 1), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate name
            productRepository.insert(
                    new Product.Builder(newProduct).withName("Pillow").build());
        });
        assertEquals("name", ex.getLabel());
        assertEquals(TestUtil.duplicateErrMsg("Name"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withImage("").build()); // NOPMD
        });
        assertEquals("image", ex.getLabel());
        assertEquals(TestUtil.requiredErrMsg("Image"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withCategoryId("").build());
        });
        assertEquals("category ID", ex.getLabel());
        assertEquals(TestUtil.requiredErrMsg("Category ID"), ex.getMessage());

        when(categoryRepository.findWithId("C00004")).thenReturn(Optional.empty()); // NOPMD
        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(new Product.Builder(newProduct)
                    .withName("abc").withCategoryId("C00004").build());
        });
        assertEquals("id", ex.getLabel());
        assertEquals(TestUtil.recordNotFoundErrMsg("C00004"), ex.getMessage());
        verify(categoryRepository).findWithId("C00004");

        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withQuantity(-1).build());
        });
        assertEquals("quantity", ex.getLabel());
        assertEquals(TestUtil.negativeErrMsg("Quantity"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withRetailPrice(-0.1).build());
        });
        assertEquals("retail price", ex.getLabel());
        assertEquals(TestUtil.negativeErrMsg("Retail price"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withDiscount(-1).build());
        });
        assertEquals("discount", ex.getLabel());
        assertEquals(TestUtil.negativeErrMsg("Discount"), ex.getMessage());

        when(supplierRepository.findWithId("S00004")).thenReturn(Optional.empty()); // NOPMD
        ex = assertThrows(InvalidFieldException.class, () -> {
            productRepository.insert(
                    new Product.Builder(newProduct).withName("abc").withSupplierId("S00004")
                        .build());
        });
        assertEquals("id", ex.getLabel());
        assertEquals(TestUtil.recordNotFoundErrMsg("S00004"), ex.getMessage());
        verify(supplierRepository).findWithId("S00004");
    }
}
