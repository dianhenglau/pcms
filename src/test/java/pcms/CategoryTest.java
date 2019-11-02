package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import org.junit.jupiter.api.Test;
import pcms.category.Category;

/** Test Category. */
class CategoryTest {
    /** Test empty category. */
    @Test 
    public void testEmptyCategory() {
        final Category category = new Category.Builder().build();
        assertEquals("", category.getId());
        assertEquals("", category.getName());
        assertEquals("", category.getDescription());
    }

    /** Test complete category. */
    @Test 
    public void testCompleteCategory() {
        final String desc = "Clothing is a collective term for items worn on the body.";
        final Category category = new Category.Builder()
                .withId("C00007")
                .withName("Clothing")
                .withDescription(desc)
                .build();

        assertEquals("C00007", category.getId());
        assertEquals("Clothing", category.getName());
        assertEquals(desc, category.getDescription());
    }

    /** Test incomplete category. */
    @Test 
    public void testIncompleteCategory() {
        final Category category = new Category.Builder()
                .withId("C00008")
                .build();

        assertEquals("C00008", category.getId());
        assertEquals("", category.getName());
        assertEquals("", category.getDescription());
    }

    /** Test construct from row. */
    @Test
    public void testParse() {
        final String desc = "Furniture refers to movable "
                + "objects intended to support various human activities such as seating (e.g., "
                + "chairs, stools, and sofas), eating (tables), and sleeping (e.g., beds).";
        final Category category = new Category("C00013,Furniture,\"" + desc + "\"");
        assertEquals("C00013", category.getId());
        assertEquals("Furniture", category.getName());
        assertEquals(desc, category.getDescription());
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final String desc = "Home appliances, also known as domestic appliances, are electrical "
                + "machines that help in household functions such as cooking, cleaning and food "
                + "preservation.";
        final Category category = new Category.Builder()
                .withId("C00009")
                .withName("Home Appliances")
                .withDescription(desc)
                .build();
        assertEquals("C00009,Home Appliances,\"" + desc + "\"", category.toRow());
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        final String categoryRecord = "C00010,Toys,\"A toy is an item that is used in play, "
                + "especially one designed for such use.\"";
        assertEquals(categoryRecord, new Category(categoryRecord).toRow());
    }
}
