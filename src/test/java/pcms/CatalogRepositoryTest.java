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
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.catalog.Catalog;
import pcms.catalog.CatalogRepository;
import pcms.catalog.ProductDiscount;
import pcms.product.Product;
import pcms.product.ProductRepository;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test Catalog Repository. */
@ExtendWith(MockitoExtension.class)
class CatalogRepositoryTest {
    // CHECKSTYLE:OFF
    /** Row 1 of catalogs.csv. */
    private static final String ROW1 = "G00001,Chinese New Year,data/test/catalog_images/catalog_001.jpg,Happy Chinese New Year!,2019-01-01,2019-01-10,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U001";
    /** Row 2 of catalogs.csv. */
    private static final String ROW2 = "G00002,Good Friday,data/test/catalog_images/catalog_002.jpg,Happy Good Friday!,2019-02-01,2019-02-10,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U001";
    /** Row 3 of catalogs.csv. */
    private static final String ROW3 = "G00003,Wesak Day,data/test/catalog_images/catalog_003.jpg,Happy Wesak Day!,2019-03-01,2019-03-10,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U001";
    /** Row 4 of catalogs.csv. */
    private static final String ROW4 = "G00004,Hari Raya Aidilfitri,data/test/catalog_images/catalog_004.jpg,Happy Hari Raya Aidilfitri!,2019-04-01,2019-04-10,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U001";
    /** Row 5 of catalogs.csv. */
    private static final String ROW5 = "G00005,Deepavali,data/test/catalog_images/catalog_005.jpg,Happy Deepavali!,2019-05-01,2019-05-10,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U001";
    /** catalogs.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");
    // CHECKSTYLE:ON

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(CatalogRepository.class));

        final CatalogRepository catalogRepository = new CatalogRepository(
                TestUtil.getDataPath("catalogs.csv"),
                mock(UserRepository.class),
                mock(ProductRepository.class));
        assertNotNull(catalogRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("catalogs_insert.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.deleteIfExists(Path.of("data/test/catalog_images/G00006-image6.jpg"));

            // Mocking
            final Product product = mock(Product.class);
            final ProductRepository productRepository = mock(ProductRepository.class);

            final User user = mock(User.class);
            final UserRepository userRepository = mock(UserRepository.class);

            // Start testing.

            final CatalogRepository catalogRepository = new CatalogRepository(
                    filePath, userRepository, productRepository);

            final List<ProductDiscount> productDiscounts = new ArrayList<>(3);
            productDiscounts.add(new ProductDiscount("P003", 30.0)); // NOPMD - Okay to repeat
            productDiscounts.add(new ProductDiscount("P004", 40.0)); // NOPMD - Okay to repeat
            productDiscounts.add(new ProductDiscount("P005", 50.0)); // NOPMD - Okay to repeat

            final Catalog newCatalog = new Catalog.Builder()
                    .withId("G00006")
                    .withTitle("Merdeka Day")
                    .withBanner("data/test/image6.jpg")
                    .withDescription("Happy Merdeka Day!")
                    .withSeasonStartDate(LocalDate.of(2019, 6, 1))
                    .withSeasonEndDate(LocalDate.of(2019, 6, 10))
                    .withProductDiscounts(productDiscounts)
                    .withTimestamp(Instant.ofEpochSecond(1))
                    .withUserId("U001") // NOPMD - Okay to repeat
                    .build();

            when(productRepository.findWithId("P003")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P004")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P005")).thenReturn(Optional.of(product));
            when(userRepository.findWithId("U001")).thenReturn(Optional.of(user));

            final Catalog result = catalogRepository.insert(newCatalog);
            assertEquals(String.join("", "7\n", CONTENT.substring(2), result.toRow()
                    .replace("/image6.jpg", "/catalog_images/G00006-image6.jpg"), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertTrue(Files.exists(Path.of("data/test/catalog_images/G00006-image6.jpg")));

            verify(productRepository).findWithId("P003");
            verify(productRepository).findWithId("P004");
            verify(productRepository).findWithId("P005");
            verify(userRepository).findWithId("U001");

            doCommonValidations(catalogRepository, productRepository, userRepository, 
                    newCatalog);

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test update. */
    @Test
    public void testUpdate() {
        try {
            final Path filePath = TestUtil.getDataPath("catalogs_update.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.deleteIfExists(Path.of("data/test/catalog_images/G00006-image6.jpg"));
            Files.copy(Path.of("data/test/image4.jpg"), 
                    Path.of("data/test/catalog_images/catalog_004.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);

            // Mocking
            final Product product = mock(Product.class);
            final ProductRepository productRepository = mock(ProductRepository.class);

            final User user = mock(User.class);
            final UserRepository userRepository = mock(UserRepository.class);

            // Start testing.
            final CatalogRepository catalogRepository = new CatalogRepository(
                    filePath, userRepository, productRepository);
            final Catalog catalog = catalogRepository.findWithId("G00004").get();
            final Catalog newCatalog = new Catalog.Builder(catalog)
                    .withDescription("Fantastic")
                    .withBanner("data/test/image6.jpg")
                    .build();

            when(productRepository.findWithId("P003")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P004")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P005")).thenReturn(Optional.of(product));
            when(userRepository.findWithId("U001")).thenReturn(Optional.of(user));

            catalogRepository.update(newCatalog);
            assertEquals(
                    CONTENT.replace("Happy Hari Raya Aidilfitri!", "Fantastic")
                        .replace("catalog_004.jpg", "G00004-image6.jpg"),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertTrue(Files.exists(Path.of("data/test/catalog_images/G00004-image6.jpg")));
            assertFalse(Files.exists(Path.of("data/test/catalog_images/catalog_004.jpg")));

            verify(productRepository).findWithId("P003");
            verify(productRepository).findWithId("P004");
            verify(productRepository).findWithId("P005");
            verify(userRepository).findWithId("U001");

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                catalogRepository.update(new Catalog.Builder().withId("G00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

            doCommonValidations(catalogRepository, productRepository, userRepository, 
                    newCatalog);

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test delete. */
    @Test
    public void testDelete() {
        try {
            final Path filePath = TestUtil.getDataPath("catalogs_delete.csv");

            // Prepare files
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);
            Files.copy(Path.of("data/test/image4.jpg"), 
                    Path.of("data/test/catalog_images/catalog_003.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);

            // Mocking
            final ProductRepository productRepository = mock(ProductRepository.class);
            final UserRepository userRepository = mock(UserRepository.class);

            // Start testing.
            final CatalogRepository catalogRepository = new CatalogRepository(
                    filePath, userRepository, productRepository);
            final Catalog catalog = catalogRepository.findWithId("G00003").get();

            catalogRepository.delete(catalog);
            assertEquals(
                    CONTENT.replace(ROW3 + "\n", ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
            assertFalse(Files.exists(Path.of("data/test/catalog_images/catalog_003.jpg")));

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                catalogRepository.delete(new Catalog.Builder().withId("G00007").build());
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
            final Product product = mock(Product.class);
            final ProductRepository productRepository = mock(ProductRepository.class);

            final User user = mock(User.class);
            final UserRepository userRepository = mock(UserRepository.class);

            // Start testing.
            final CatalogRepository catalogRepository = new CatalogRepository(
                    filePath, userRepository, productRepository);

            final List<ProductDiscount> productDiscounts = new ArrayList<>(3);
            productDiscounts.add(new ProductDiscount("P003", 30.0));
            productDiscounts.add(new ProductDiscount("P004", 40.0));
            productDiscounts.add(new ProductDiscount("P005", 50.0));

            final Catalog newCatalog = new Catalog.Builder()
                    .withId("G00006")
                    .withTitle("Merdeka Day")
                    .withBanner("data/test/image6.jpg")
                    .withDescription("Happy Merdeka Day!")
                    .withSeasonStartDate(LocalDate.of(2019, 6, 1))
                    .withSeasonEndDate(LocalDate.of(2019, 6, 10))
                    .withProductDiscounts(productDiscounts)
                    .withTimestamp(Instant.ofEpochSecond(1))
                    .withUserId("U001")
                    .build();

            when(productRepository.findWithId("P003")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P004")).thenReturn(Optional.of(product));
            when(productRepository.findWithId("P005")).thenReturn(Optional.of(product));
            when(userRepository.findWithId("U001")).thenReturn(Optional.of(user));

            final Catalog result = catalogRepository.insert(newCatalog);
            assertEquals(
                    String.join("\n", "2", result.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            verify(productRepository).findWithId("P003");
            verify(productRepository).findWithId("P004");
            verify(productRepository).findWithId("P005");
            verify(userRepository).findWithId("U001");

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Do common validations. */
    private void doCommonValidations(
            final CatalogRepository catalogRepository,
            final ProductRepository productRepository,
            final UserRepository userRepository,
            final Catalog newCatalog) {

        InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
            catalogRepository.insert(new Catalog.Builder(newCatalog).withTitle("").build());
        });
        assertEquals("title", ex.getLabel());
        assertEquals(TestUtil.minLenErrMsg("Title", 1), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate title
            catalogRepository.insert(
                    new Catalog.Builder(newCatalog).withTitle("Deepavali").build());
        });
        assertEquals("title", ex.getLabel());
        assertEquals(TestUtil.duplicateErrMsg("Title"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            catalogRepository.insert(new Catalog.Builder(newCatalog)
                    .withTitle("abc") // NOPMD
                    .withBanner("")
                    .build());
        });
        assertEquals("banner", ex.getLabel());
        assertEquals(TestUtil.requiredErrMsg("Banner"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            catalogRepository.insert(new Catalog.Builder(newCatalog)
                    .withTitle("abc")
                    .withSeasonStartDate(LocalDate.ofEpochDay(7))
                    .withSeasonEndDate(LocalDate.ofEpochDay(6))
                    .build());
        });
        assertEquals("season date range", ex.getLabel());
        assertEquals(TestUtil.dateRangeErrMsg("Season date range"), ex.getMessage());

        final List<ProductDiscount> productDiscounts = new ArrayList<>(1);
        productDiscounts.add(new ProductDiscount("P005", 50.0));
        productDiscounts.add(new ProductDiscount("P006", 60.0)); // NOPMD - Okay to repeat

        when(productRepository.findWithId("P006")).thenReturn(Optional.empty());
        ex = assertThrows(InvalidFieldException.class, () -> {
            catalogRepository.insert(new Catalog.Builder(newCatalog)
                    .withTitle("abc").withProductDiscounts(productDiscounts).build());
        });
        assertEquals("id", ex.getLabel());
        assertEquals(TestUtil.recordNotFoundErrMsg("P006"), ex.getMessage());
        verify(productRepository).findWithId("P006");

        when(userRepository.findWithId("U002")).thenReturn(Optional.empty()); // NOPMD
        ex = assertThrows(InvalidFieldException.class, () -> {
            catalogRepository.insert(
                    new Catalog.Builder(newCatalog).withTitle("abc").withUserId("U002").build());
        });
        assertEquals("id", ex.getLabel());
        assertEquals(TestUtil.recordNotFoundErrMsg("U002"), ex.getMessage());
        verify(userRepository).findWithId("U002");
    }
}
