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
import pcms.supplier.Supplier;
import pcms.supplier.SupplierRepository;

/** Test Supplier Repository. */
class SupplierRepositoryTest {
    // CHECKSTYLE:OFF
    /** Row 1 of suppliers.csv. */
    private static final String ROW1 = "S00001,ABC Holdings Sdn Bhd,info@abc_holdings.example.com,+60123456789,1 Jalan Satu,Active";
    /** Row 2 of suppliers.csv. */
    private static final String ROW2 = "S00002,DEF Holdings Sdn Bhd,info@def_holdings.example.com,+60123456790,2 Jalan Satu,Active";
    /** Row 3 of suppliers.csv. */
    private static final String ROW3 = "S00003,GHI Holdings Sdn Bhd,info@ghi_holdings.example.com,+60123456791,3 Jalan Satu,Inactive";
    /** Row 4 of suppliers.csv. */
    private static final String ROW4 = "S00004,JKL Holdings Sdn Bhd,info@jkl_holdings.example.com,+60123456792,4 Jalan Satu,Inactive";
    /** Row 5 of suppliers.csv. */
    private static final String ROW5 = "S00005,MNO Holdings Sdn Bhd,info@mno_holdings.example.com,+60123456793,5 Jalan Satu,Active";
    /** suppliers.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");
    // CHECKSTYLE:ON

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        assertTrue(Repository.class.isAssignableFrom(SupplierRepository.class));

        final SupplierRepository supplierRepository = new SupplierRepository(
                TestUtil.getDataPath("suppliers.csv"));
        assertNotNull(supplierRepository);
    }

    /** Test insert. */
    @Test
    public void testInsert() {
        try {
            final Path filePath = TestUtil.getDataPath("suppliers_insert.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final SupplierRepository supplierRepository = new SupplierRepository(filePath);

            final Supplier newSupplier = new Supplier.Builder()
                    .withName("PQR Holdings Sdn Bhd")
                    .withEmail("info@pqr_holdings.example.com")
                    .withPhone("+60123456794")
                    .withAddress("17 Penn Street, Drexel Hill, PA 19026")
                    .withIsActive(true)
                    .build();
            final Supplier result = supplierRepository.insert(newSupplier);
            assertEquals(
                    String.join("", "7\n", CONTENT.substring(2), result.toRow(), "\n"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            doCommonValidations(supplierRepository, newSupplier);

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test update. */
    @Test
    public void testUpdate() {
        try {
            final Path filePath = TestUtil.getDataPath("suppliers_update.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final SupplierRepository supplierRepository = new SupplierRepository(filePath);
            final Supplier supplier = supplierRepository.findWithId("S00004").get();
            final Supplier newSupplier = new Supplier.Builder(supplier)
                    .withName("STU Holdings Sdn Bhd")
                    .build();

            supplierRepository.update(newSupplier);
            assertEquals(
                    CONTENT.replace("JKL", "STU"),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                supplierRepository.update(new Supplier.Builder().withId("S00007").build());
            });
            assertEquals("id", ex.getLabel());
            assertEquals(TestUtil.keyNotFoundErrMsg("ID"), ex.getMessage());

            doCommonValidations(supplierRepository, newSupplier);

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
            final SupplierRepository supplierRepository = new SupplierRepository(filePath);

            final Supplier newSupplier = new Supplier.Builder()
                    .withName("VWX Holdings Sdn Bhd")
                    .withEmail("info@vwx_holdings.example.com")
                    .withPhone("+60123456795")
                    .withAddress("75 Peachtree Street, Lansdale, PA 19446")
                    .withIsActive(true)
                    .build();
            final Supplier result = supplierRepository.insert(newSupplier);
            assertEquals(
                    String.join("\n", "2", result.toRow(), ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Test find with name. */
    @Test
    public void testFindWithName() {
        try {
            final Path filePath = TestUtil.getDataPath("suppliers_find_with_name.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final SupplierRepository supplierRepository = new SupplierRepository(filePath);
            assertEquals(5, supplierRepository.all().size());

            assertTrue(supplierRepository.findWithName("Blah Holdings Sdn Bhd").isEmpty());
            final Optional<Supplier> result = supplierRepository.findWithName(
                    "JKL Holdings Sdn Bhd");
            assertFalse(result.isEmpty());
            assertEquals("+60123456792", result.get().getPhone());

        } catch (IOException ex) {
            fail(ex);
        }
    }

    /** Do common validations. */
    private void doCommonValidations(
            final SupplierRepository supplierRepository,
            final Supplier newSupplier) {

        InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
            supplierRepository.insert(
                    new Supplier.Builder(newSupplier).withName("").build());
        });
        assertEquals("name", ex.getLabel()); // NOPMD
        assertEquals(TestUtil.minLenErrMsg("Name", 1), ex.getMessage()); // NOPMD

        ex = assertThrows(InvalidFieldException.class, () -> {
            supplierRepository.insert(
                    new Supplier.Builder(newSupplier).withName("ABC Holdings Sdn Bhd").build());
        });
        assertEquals("name", ex.getLabel()); // NOPMD
        assertEquals(TestUtil.duplicateErrMsg("Name"), ex.getMessage()); // NOPMD

        ex = assertThrows(InvalidFieldException.class, () -> {
            supplierRepository.insert(
                    new Supplier.Builder(newSupplier).withName("h").withEmail("").build());
        });
        assertEquals("email", ex.getLabel());
        assertEquals(TestUtil.requiredErrMsg("Email"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            supplierRepository.insert(new Supplier.Builder(newSupplier).withName("h")
                    .withEmail("hello@goodbye").build());
        });
        assertEquals("email", ex.getLabel());
        assertEquals(TestUtil.emailFmtErrMsg("Email"), ex.getMessage());

        ex = assertThrows(InvalidFieldException.class, () -> {
            supplierRepository.insert(
                    new Supplier.Builder(newSupplier).withName("h").withPhone("123").build());
        });
        assertEquals("phone", ex.getLabel());
        assertEquals(TestUtil.phoneFmtErrMsg("Phone"), ex.getMessage());
    }
}
