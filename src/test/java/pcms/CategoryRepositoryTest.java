package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
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
            final Category newCategory = new Category.Builder()
                    .withName("Grocery")
                    .withDescription("Food and items that you buy in a food store or supermarket.")
                    .build();

            final Category result = categoryRepository.insert(newCategory);
            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), result.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.insert(
                        new Category.Builder(newCategory).withName("").build());
            });
            assertEquals("name", ex.getLabel()); // NOPMD
            assertEquals(TestUtil.minLenErrMsg("Name", 1), ex.getMessage()); // NOPMD

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate name
                categoryRepository.insert(
                        new Category.Builder(newCategory).withName("Clothing").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals(TestUtil.duplicateErrMsg("Name"), ex.getMessage());
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
            final Category newCategory = new Category.Builder(category)
                    .withName("Stationery")
                    .build();

            categoryRepository.update(newCategory);
            assertEquals(
                    CONTENT.replace("Toiletries", "Stationery"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.delete(
                        new Category.Builder().withId("C00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> {
                categoryRepository.update(
                        new Category.Builder(newCategory).withName("").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals(TestUtil.minLenErrMsg("Name", 1), ex.getMessage());

            ex = assertThrows(InvalidFieldException.class, () -> { // Duplicate name
                categoryRepository.update(
                        new Category.Builder(newCategory).withName("Houseware").build());
            });
            assertEquals("name", ex.getLabel());
            assertEquals(TestUtil.duplicateErrMsg("Name"), ex.getMessage());
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

    /** Test find with name. */
    @Test
    public void testWithFindWithName() {
        try {
            final Path filePath = TestUtil.getDataPath("categories_find_with_name.csv");
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            final CategoryRepository categoryRepository = new CategoryRepository(filePath);
            assertEquals(5, categoryRepository.all().size());

            final Optional<Category> category = categoryRepository.findWithName("Home Appliances");
            assertFalse(category.isEmpty());
            assertEquals("Home Appliances", category.get().getName());

            assertTrue(categoryRepository.findWithName("Toys").isEmpty());
        } catch (IOException ex) {
            fail(ex);
        }
    }
}
