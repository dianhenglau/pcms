package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import org.junit.jupiter.api.Test;
import pcms.supplier.Supplier;

/** Test Supplier. */
class SupplierTest {
    /** Test empty supplier. */
    @Test 
    public void testEmptySupplier() {
        final Supplier supplier = new Supplier.Builder().build();
        assertEquals("", supplier.getId());
        assertEquals("", supplier.getName());
        assertEquals("", supplier.getEmail());
        assertEquals("", supplier.getPhone());
        assertEquals("", supplier.getAddress());
        assertFalse(supplier.isActive());
    }

    /** Test complete supplier. */
    @Test 
    public void testCompleteSupplier() {
        final Supplier supplier = new Supplier.Builder()
                .withId("S00007")
                .withName("ABC Holdings Sdn Bhd")
                .withEmail("info@abc_holding.example.com")
                .withPhone("+60123456789")
                .withAddress("1, Jalan Dua, Taman Tiga")
                .withIsActive(true)
                .build();

        assertEquals("S00007", supplier.getId());
        assertEquals("ABC Holdings Sdn Bhd", supplier.getName());
        assertEquals("info@abc_holding.example.com", supplier.getEmail());
        assertEquals("+60123456789", supplier.getPhone());
        assertEquals("1, Jalan Dua, Taman Tiga", supplier.getAddress());
        assertTrue(supplier.isActive());
    }

    /** Test incomplete supplier. */
    @Test 
    public void testIncompleteSupplier() {
        final Supplier supplier = new Supplier.Builder()
                .withId("S00008")
                .withName("DEF Holdings Sdn Bhd")
                .withEmail("info@def_holding.example.com")
                .build();

        assertEquals("S00008", supplier.getId());
        assertEquals("DEF Holdings Sdn Bhd", supplier.getName());
        assertEquals("info@def_holding.example.com", supplier.getEmail());
        assertEquals("", supplier.getPhone());
        assertEquals("", supplier.getAddress());
        assertFalse(supplier.isActive());
    }

    /** Test construct from row. */
    @Test
    public void testParse() {
        final Supplier supplier = new Supplier("S00013,GHI Holdings Sdn Bhd,"
                + "info@ghi_holdings.example.com,+60123456791,"
                + "\"3, Jalan Dua, Taman Tiga, 12345, KL, Malaysia.\",Active");
        assertEquals("S00013", supplier.getId());
        assertEquals("GHI Holdings Sdn Bhd", supplier.getName());
        assertEquals("info@ghi_holdings.example.com", supplier.getEmail());
        assertEquals("+60123456791", supplier.getPhone());
        assertEquals("3, Jalan Dua, Taman Tiga, 12345, KL, Malaysia.", supplier.getAddress());
        assertTrue(supplier.isActive());

        assertEquals("Fields count incorrect.", assertThrows(IllegalArgumentException.class, () -> {
            new Supplier(",,,,"); }).getMessage());
        assertEquals("Status invalid.", assertThrows(IllegalArgumentException.class, () -> {
            new Supplier(",,,,,Bahaha"); }).getMessage());
    }

    /** Test to row. */
    @Test
    public void testToRow() {
        final Supplier supplier = new Supplier.Builder()
                .withId("S00014")
                .withName("JKL Holdings Sdn Bhd")
                .withEmail("info@jkl_holdings.example.com")
                .withPhone("+60123456792")
                .withAddress("4, Jalan Lima, Taman Enam")
                .withIsActive(false)
                .build();
        assertEquals("S00014,JKL Holdings Sdn Bhd,info@jkl_holdings.example.com,+60123456792,"
                + "\"4, Jalan Lima, Taman Enam\",Inactive", supplier.toRow());
    }

    /** Test from and to row. */
    @Test
    public void testFromAndToRow() {
        final String supplierRecord = "S00014,JKL Holdings Sdn Bhd,info@jkl_holdings.example.com,"
                +  "+60123456792,\"4, Jalan Lima, Taman Enam\",Inactive";
        assertEquals(supplierRecord, new Supplier(supplierRecord).toRow());
    }
}
