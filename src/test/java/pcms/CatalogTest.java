package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// CHECKSTYLE:ON

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pcms.catalog.Catalog;
import pcms.catalog.ProductDiscount;
import pcms.user.User;
import pcms.user.UserRepository;

/** Test Catalog. */
@ExtendWith(MockitoExtension.class)
class CatalogTest {
    /** Test empty catalog. */
    @Test 
    public void testEmptyCatalog() {
        final Catalog catalog = new Catalog.Builder().build();
        assertEquals("", catalog.getId());
        assertEquals("", catalog.getTitle());
        assertEquals("", catalog.getBanner());
        assertEquals("", catalog.getDescription());
        assertEquals(LocalDate.ofEpochDay(0), catalog.getSeasonStartDate());
        assertEquals(LocalDate.ofEpochDay(0), catalog.getSeasonEndDate());
        assertTrue(catalog.getProductDiscounts().isEmpty());
        assertEquals(Instant.ofEpochSecond(0), catalog.getTimestamp());
        assertEquals("", catalog.getUserId());
    }

    /** Test complete catalog. */
    @Test 
    public void testCompleteCatalog() {
        final List<ProductDiscount> productDiscounts = new ArrayList<>(3);
        productDiscounts.add(new ProductDiscount("P003", 30.0));
        productDiscounts.add(new ProductDiscount("P004", 40.0));
        productDiscounts.add(new ProductDiscount("P005", 50.0));

        final Catalog catalog = new Catalog.Builder()
                .withId("G007") // NOPMD - Okay to repeat
                .withTitle("Deepavali") // NOPMD - Okay to repeat
                .withBanner("catalog_001.png") // NOPMD - Okay to repeat
                .withDescription("Happy Deepavali!") // NOPMD - Okay to repeat
                .withSeasonStartDate(LocalDate.of(2019, 12, 1))
                .withSeasonEndDate(LocalDate.of(2019, 12, 31))
                .withProductDiscounts(productDiscounts)
                .withTimestamp(Instant.ofEpochSecond(1))
                .withUserId("U007") // NOPMD - Okay to repeat
                .build();

        assertEquals("G007", catalog.getId());
        assertEquals("Deepavali", catalog.getTitle());
        assertEquals("catalog_001.png", catalog.getBanner());
        assertEquals("Happy Deepavali!", catalog.getDescription());
        assertEquals(LocalDate.of(2019, 12, 1), catalog.getSeasonStartDate());
        assertEquals(LocalDate.of(2019, 12, 31), catalog.getSeasonEndDate());
        assertSame(productDiscounts, catalog.getProductDiscounts());
        assertEquals(Instant.ofEpochSecond(1), catalog.getTimestamp());
        assertEquals("U007", catalog.getUserId());
    }

    /** Test incomplete catalog. */
    @Test 
    public void testIncompleteCatalog() {
        final List<ProductDiscount> productDiscounts = new ArrayList<>(3);
        productDiscounts.add(new ProductDiscount("P003", 30.0));
        productDiscounts.add(new ProductDiscount("P004", 40.0));
        productDiscounts.add(new ProductDiscount("P005", 50.0));

        final Catalog catalog = new Catalog.Builder()
                .withId("G007")
                .withTitle("Deepavali")
                .withBanner("catalog_001.png")
                .withDescription("Happy Deepavali!")
                .withSeasonEndDate(LocalDate.of(2019, 12, 31))
                .withProductDiscounts(productDiscounts)
                .build();

        assertEquals("G007", catalog.getId());
        assertEquals("Deepavali", catalog.getTitle());
        assertEquals("catalog_001.png", catalog.getBanner());
        assertEquals("Happy Deepavali!", catalog.getDescription());
        assertEquals(LocalDate.of(2019, 12, 31), catalog.getSeasonEndDate());
        assertSame(productDiscounts, catalog.getProductDiscounts());

        assertEquals(LocalDate.ofEpochDay(0), catalog.getSeasonStartDate());
        assertEquals(Instant.ofEpochSecond(0), catalog.getTimestamp());
        assertEquals("", catalog.getUserId());
    }

    /** Test construct from row. */
    @Test
    public void testParse() {
        // CHECKSTYLE:OFF
        final Catalog catalog = new Catalog("G007,Deepavali,catalog_001.png,Happy Deepavali!,2019-12-01,2019-12-31,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U007");
        // CHECKSTYLE:ON
        assertEquals("G007", catalog.getId());
        assertEquals("Deepavali", catalog.getTitle());
        assertEquals("catalog_001.png", catalog.getBanner());
        assertEquals("Happy Deepavali!", catalog.getDescription());
        assertEquals(LocalDate.of(2019, 12, 1), catalog.getSeasonStartDate());
        assertEquals(LocalDate.of(2019, 12, 31), catalog.getSeasonEndDate());
        assertEquals(3, catalog.getProductDiscounts().size());
        assertEquals(Instant.ofEpochSecond(1), catalog.getTimestamp());
        assertEquals("U007", catalog.getUserId());

        assertEquals("Fields count incorrect.", assertThrows(IllegalArgumentException.class, () -> {
            new Catalog(",,,,,,,,,"); }).getMessage());
    }

    /** Test construct from row with no product. */
    @Test
    public void testParseWithNoProduct() {
        // CHECKSTYLE:OFF
        final Catalog catalog = new Catalog("G007,Deepavali,catalog_001.png,Happy Deepavali!,2019-12-01,2019-12-31,,1970-01-01T00:00:01Z,U007");
        // CHECKSTYLE:ON
        assertEquals("G007", catalog.getId());
        assertEquals("Deepavali", catalog.getTitle());
        assertEquals("catalog_001.png", catalog.getBanner());
        assertEquals("Happy Deepavali!", catalog.getDescription());
        assertEquals(LocalDate.of(2019, 12, 1), catalog.getSeasonStartDate());
        assertEquals(LocalDate.of(2019, 12, 31), catalog.getSeasonEndDate());
        assertEquals(0, catalog.getProductDiscounts().size());
        assertEquals(Instant.ofEpochSecond(1), catalog.getTimestamp());
        assertEquals("U007", catalog.getUserId());
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final List<ProductDiscount> productDiscounts = new ArrayList<>(3);
        productDiscounts.add(new ProductDiscount("P003", 30.0));
        productDiscounts.add(new ProductDiscount("P004", 40.0));
        productDiscounts.add(new ProductDiscount("P005", 50.0));

        final Catalog catalog = new Catalog.Builder()
                .withId("G007")
                .withTitle("Deepavali")
                .withBanner("catalog_001.png")
                .withDescription("Happy Deepavali!")
                .withSeasonStartDate(LocalDate.of(2019, 12, 1))
                .withSeasonEndDate(LocalDate.of(2019, 12, 31))
                .withProductDiscounts(productDiscounts)
                .withTimestamp(Instant.ofEpochSecond(1))
                .withUserId("U007")
                .build();
        // CHECKSTYLE:OFF
        assertEquals("G007,Deepavali,catalog_001.png,Happy Deepavali!,2019-12-01,2019-12-31,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U007", catalog.toRow());
        // CHECKSTYLE:ON
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        // CHECKSTYLE:OFF
        final String catalogRecord = "G007,Deepavali,catalog_001.png,Happy Deepavali!,2019-12-01,2019-12-31,\"P003:30.0,P004:40.0,P005:50.0\",1970-01-01T00:00:01Z,U007";
        // CHECKSTYLE:ON
        assertEquals(catalogRecord, new Catalog(catalogRecord).toRow());
    }

    /** Test get user. */
    @Test
    public void testGetUser() {
        // Mocking
        final User user = mock(User.class);
        final UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findWithId(any())).thenReturn(Optional.of(user));

        Catalog.setUserRepository(userRepository);
        final Catalog catalog = new Catalog.Builder().build();
        assertSame(user, catalog.getUser());
    }
}
