package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import pcms.category.Category;
import pcms.category.CategoryRepository;

/** Test Category Repository. */
class CategoryRepositoryTest {
    // CHECKSTYLE:OFF
    /** Row 1 of categories.csv. */
    private static final String ROW1 = "C00001,Clothing,Clothing is a collective term for items worn on the body.";
    /** Row 2 of categories.csv. */
    private static final String ROW2 = "C00002,Furniture,\"Furniture refers to movable objects intended to support various human activities such as seating (e.g., chairs, stools, and sofas), eating (tables), and sleeping (e.g., beds).\"";
    /** Row 3 of categories.csv. */
    private static final String ROW3 = "C00003,Home Appliances,\"Home appliances, also known as domestic appliances, are electrical machines that help in household functions such as cooking, cleaning and food preservation.\"";
    /** Row 4 of categories.csv. */
    private static final String ROW4 = "C00004,Toiletries,Personal care or toiletries are consumer products used in personal hygiene and for beautification.";
    /** Row 5 of categories.csv. */
    private static final String ROW5 = "C00005,Houseware,\"Small household items such as kitchen utensils, tableware, and decorative objects.\"";
    /** categories.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");

    /** InvalidFieldException message for validMinLength. */
    private static final String MSG1 = " length should be at least 1.";
    /** InvalidFieldException message for notExists. */
    private static final String MSG3 = " has been used. Choose another one.";
    /** InvalidFieldException message for primary key not found. */
    private static final String MSG4 = " does not exist in database.";
    // CHECKSTYLE:ON

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(CategoryRepository.class));

        final CategoryRepository categoryRepository = new CategoryRepository(
                TestUtil.getDataPath("categories.csv"));
        assertNotNull(categoryRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("categories_insert.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final CategoryRepository categoryRepository = new CategoryRepository(filePath);
            final Category.Builder builder = new Category.Builder()
                    .withName("Grocery")
                    .withDescription("Food and items that you buy in a food store or supermarket.");

            Category newCategory = builder.build();
            newCategory = categoryRepository.insert(newCategory);
            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), newCategory.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.insert(builder.withName("").build());
            });
            assertEquals("name", ex.getLabel()); // NOPMD
            assertEquals("Name" + MSG1, ex.getMessage()); // NOPMD

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate name
                categoryRepository.insert(builder.withName("Clothing").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals("Name" + MSG3, ex.getMessage());
        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test update. */
    @Test
    public void testUpdate() {
        try {
            final Path filePath = TestUtil.getDataPath("categories_update.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final CategoryRepository categoryRepository = new CategoryRepository(filePath);
            final Category category = categoryRepository.findWithId("C00004").get();
            final Category.Builder builder = new Category.Builder(category).withName("Stationery");
            final Category newCategory = builder.build();

            categoryRepository.update(newCategory);
            assertEquals(
                    CONTENT.replace("Toiletries", "Stationery"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.delete(new Category.Builder().withId("C00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals("ID" + MSG4, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.update(builder.withName("").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals("Name" + MSG1, ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate name
                categoryRepository.update(builder.withName("Houseware").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals("Name" + MSG3, ex.getMessage());
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
            final CategoryRepository categoryRepository = new CategoryRepository(filePath);

            Category newCategory = new Category.Builder()
                    .withName("Books")
                    .withDescription("A written or printed work consisting of pages glued or sewn "
                            + "together along one side and bound in covers.")
                    .build();
            newCategory = categoryRepository.insert(newCategory);
            assertEquals(
                    String.join("\n", "2", newCategory.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            fail(ex);
        }
    }

}
