package pcms;

// CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.*;
// CHECKSTYLE:ON

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Test Repository. */
class RepositoryTest {
    /** Row 1 of cars.csv. */
    private static final String ROW1 = "C00001,Toyota";
    /** Row 2 of cars.csv. */
    private static final String ROW2 = "C00002,Honda";
    /** Row 3 of cars.csv. */
    private static final String ROW3 = "C00003,BMW";
    /** Row 4 of cars.csv. */
    private static final String ROW4 = "C00004,Mercedes";
    /** Row 5 of cars.csv. */
    private static final String ROW5 = "C00005,Proton";
    /** cars.csv content. */
    private static final String CONTENT = String.join("\n", "6", ROW1, ROW2, ROW3, ROW4, ROW5, "");

    /** Dummy class. */
    private static final class Car implements Model {
        /** ID. */
        private final String id;
        /** Name. */
        private final String name;

        /** Construct with builder. */
        private Car(final Builder builder) {
            id = builder.id;
            name = builder.name;
        }

        /** Construct with row. */
        public Car(final String row) {
            final List<String> fields = CsvParsingUtil.splitIntoCols(row);
            if (fields.size() != 2) {
                throw new IllegalArgumentException("Fields count incorrect.");
            }

            id = CsvParsingUtil.decode(fields.get(0));
            name = CsvParsingUtil.decode(fields.get(1));
        }

        /** To row. */
        @Override
        public String toRow() {
            return String.join(",", 
                    CsvParsingUtil.encode(id), 
                    CsvParsingUtil.encode(name));
        }

        /** Get ID. */
        @Override
        public String getId() {
            return id;
        }

        /** Get name. */
        public String getName() {
            return name;
        }

        /** Builder. */
        public static class Builder implements Model.Builder {
            /** ID. */
            private String id;
            /** Name. */
            private String name;

            /** Default constructor. */
            public Builder() {
                id = "";
                name = "";
            }

            /** Construct with car. */
            public Builder(final Car car) {
                id = car.id;
                name = car.name;
            }

            /** Set ID. */
            @Override
            public Builder withId(final String id) {
                this.id = id;
                return this;
            }

            /** Set name. */
            public Builder withName(final String name) {
                this.name = name;
                return this;
            }

            /** Build. */
            @Override
            public Car build() {
                return new Car(this);
            }
        }
    }

    /** Dummy class. */
    private static final class Cars extends Repository<Car> {
        /** Construct. */
        public Cars(final Path dataPath) {
            super(dataPath, id -> new Car.Builder().withId(id).build(), Car::new);
        }

        /** Dummy insert. */
        @Override
        public Car insert(final Car car) {
            return car;
        }

        /** Dummy update. */
        @Override
        public Car update(final Car car) {
            return car;
        }
    }

    /** Test constructor. */
    @Test 
    public void testConstructor() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv")); // NOPMD
        assertNotNull(cars);
    }

    /** Test all with start and count. */
    @Test
    public void testAllWithStartAndCount() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        List<Car> someCars = cars.all(1, 2);
        assertEquals(2, someCars.size());
        assertEquals(ROW2, someCars.get(0).toRow());
        assertEquals(ROW3, someCars.get(1).toRow());

        someCars = cars.all(4, 100_000);
        assertEquals(1, someCars.size());
        assertEquals(ROW5, someCars.get(0).toRow());

        someCars = cars.all(5, 3);
        assertEquals(0, someCars.size());
    }

    /** Test all with count. */
    @Test
    public void testAllWithCount() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        List<Car> someCars = cars.all(3);
        assertEquals(3, someCars.size());
        assertEquals(ROW1, someCars.get(0).toRow());
        assertEquals(ROW2, someCars.get(1).toRow());
        assertEquals(ROW3, someCars.get(2).toRow());
        
        someCars = cars.all(100);
        assertEquals(5, someCars.size());
        assertEquals(ROW1, someCars.get(0).toRow());
        assertEquals(ROW2, someCars.get(1).toRow());
        assertEquals(ROW3, someCars.get(2).toRow());
        assertEquals(ROW4, someCars.get(3).toRow());
        assertEquals(ROW5, someCars.get(4).toRow());
    }

    /** Test all. */
    @Test
    public void testAll() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        final List<Car> someCars = cars.all();
        assertEquals(5, someCars.size());
        assertEquals(ROW1, someCars.get(0).toRow());
        assertEquals(ROW2, someCars.get(1).toRow());
        assertEquals(ROW3, someCars.get(2).toRow());
        assertEquals(ROW4, someCars.get(3).toRow());
        assertEquals(ROW5, someCars.get(4).toRow());
    }

    /** Test find with ID. */
    @Test
    public void testFindWithId() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        Optional<Car> user = cars.findWithId("C00003");
        assertTrue(user.isPresent());
        assertEquals(ROW3, user.get().toRow());

        user = cars.findWithId("C00001");
        assertTrue(user.isPresent());
        assertEquals(ROW1, user.get().toRow());

        user = cars.findWithId("C00005");
        assertTrue(user.isPresent());
        assertEquals(ROW5, user.get().toRow());

        user = cars.findWithId("C00006");
        assertTrue(user.isEmpty());
    }

    /** Test filter with start and count. */
    @Test
    public void testFilterWithStartAndCount() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        List<Car> someCars = cars.filter(x -> x.getName().contains("o"), 1, 2);
        assertEquals(2, someCars.size());
        assertEquals(ROW2, someCars.get(0).toRow());
        assertEquals(ROW5, someCars.get(1).toRow());

        someCars = cars.filter(x -> x.getName().contains("o"), 2, 100);
        assertEquals(1, someCars.size());
        assertEquals(ROW5, someCars.get(0).toRow());

        someCars = cars.filter(x -> x.getName().contains("a"), 50, 1);
        assertEquals(0, someCars.size());
    }

    /** Test filter with count. */
    @Test
    public void testFilterWithCount() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        List<Car> someCars = cars.filter(x -> x.getName().contains("M"), 1);
        assertEquals(1, someCars.size());
        assertEquals(ROW3, someCars.get(0).toRow());

        someCars = cars.filter(x -> x.getName().length() > 5, 321);
        assertEquals(3, someCars.size());
        assertEquals(ROW1, someCars.get(0).toRow());
        assertEquals(ROW4, someCars.get(1).toRow());
        assertEquals(ROW5, someCars.get(2).toRow());

        someCars = cars.filter(x -> x.getName().length() < 3, 123);
        assertEquals(0, someCars.size());
    }

    /** Test filter. */
    @Test
    public void testFilter() {
        final Cars cars = new Cars(TestUtil.getDataPath("cars.csv"));
        List<Car> someCars = cars.filter(x -> x.getName().length() > 5);
        assertEquals(3, someCars.size());
        assertEquals(ROW1, someCars.get(0).toRow());
        assertEquals(ROW4, someCars.get(1).toRow());
        assertEquals(ROW5, someCars.get(2).toRow());

        someCars = cars.filter(x -> x.getName().length() < 3);
        assertEquals(0, someCars.size());
    }

    /** Test delete. */
    @Test
    public void testDelete() {
        try {
            final Path filePath = TestUtil.getDataPath("cars_delete.csv");

            // Create original file.
            Files.writeString(filePath, CONTENT, StandardCharsets.UTF_8);

            // Start testing.
            final Cars cars = new Cars(filePath);
            cars.delete(cars.findWithId("C00002").get());
            assertEquals(
                    String.join("\n", "6", ROW1, ROW3, ROW4, ROW5, ""),
                    Files.readString(filePath, StandardCharsets.UTF_8));

            final InvalidFieldException ex = assertThrows(InvalidFieldException.class, () -> {
                cars.delete(new Car.Builder().withId("C00008").build());
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
            final Cars cars = new Cars(filePath);

            List<Car> someCars = cars.all(3, 2);
            assertEquals(0, someCars.size());

            final Optional<Car> user = cars.findWithId("C00001");
            assertTrue(user.isEmpty());

            someCars = cars.filter(x -> x.getName().contains("o"));
            assertEquals(0, someCars.size());
        } catch (IOException ex) {
            fail(ex);
        }
    }

}
