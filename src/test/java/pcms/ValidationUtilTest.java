package pcms;

import static org.junit.jupiter.api.Assertions.*; // NOPMD

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Test ValidationUtil. */
class ValidationUtilTest {
    /** Random class used for testing purpose. */
    private static final class Foo implements Model {
        /** ID. */
        private String id;
        /** A random field. */
        private String bar;
        /** A random field. */
        private String baz;

        /** Default constructor. */
        public Foo() {
            id = "";
            bar = "";
            baz = "";
        }

        /** Construct with string. */
        public Foo(final String s) {
            this.id = s;
            this.bar = s;
            this.baz = "";
        }

        /** Construct with two strings. */
        public Foo(final String bar, final String baz) {
            this.id = "";
            this.bar = bar;
            this.baz = baz;
        }

        /** Construct with three strings. */
        public Foo(final String id, final String bar, final String baz) {
            this.id = id;
            this.bar = bar;
            this.baz = baz;
        }

        /** Construct from columns. */
        public Foo(final List<String> cols) {
            id = cols.get(0);
            bar = cols.get(1);
            baz = cols.get(2);
        }

        /** Parse from string. */
        public static Foo parse(final String row) {
            return new Foo(CsvParsingUtil.splitIntoCols(row));
        }

        /** To row. */
        @Override
        public String toRow() {
            return String.join(",", 
                    CsvParsingUtil.encode(id), 
                    CsvParsingUtil.encode(bar),
                    CsvParsingUtil.encode(baz));
        }

        /** Get id. */
        @Override
        public String getId() {
            return id;
        }

        /** Set id. */
        public void setId(final String id) {
            this.id = id;
        }

        /** Get bar. */
        public String getBar() {
            return bar;
        }

        /** Set bar. */
        public void setBar(final String bar) {
            this.bar = bar;
        }

        /** Get baz. */
        public String getBaz() {
            return baz;
        }

        /** Set baz. */
        public void setBaz(final String baz) {
            this.baz = baz;
        }
    }

    /** Random class used for testing purpose. */
    private static final class Foos extends Repository<Foo> {
        /** Construct. */
        public Foos(final Path dataPath) {
            super(dataPath, Foo::new, Foo::parse);
        }

        /** Dummy insert. */
        @Override
        public Foo insert(final Foo foo) {
            return foo;
        }

        /** Dummy update. */
        @Override
        public Foo update(final Foo foo) {
            return foo;
        }
    }

    /** Test idExists. */
    @Test
    public void testIdExists() {
        final List<Foo> cache = new ArrayList<>();
        cache.add(new Foo("F001", "Toyota", "丰田"));
        cache.add(new Foo("F002", "Honda", "本田"));
        cache.add(new Foo("F003", "Mitsubishi", "三菱"));
        cache.add(new Foo("F004", "Nissan", "日产"));
        cache.add(new Foo("F005", "Suzuki", "铃木"));
        final int index = ValidationUtil.idExists(cache, new Foo("F005"));
        assertTrue(index >= 0);
        assertTrue(index < cache.size());
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.idExists(cache, new Foo("F010"));
        });
    }

    /** Test recordExists. */
    @Test
    public void testRecordExists() {
        final Foos foos = new Foos(TestUtil.getDataPath("foos.csv"));
        final Foo foo = ValidationUtil.recordExists(foos, "F005");
        assertEquals("F005,Suzuki,铃木", foo.toRow());
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.recordExists(foos, "F123");
        });
    }

    /** Test validMinLength. */
    @Test
    public void testHasMinLength() {
        ValidationUtil.validMinLength("nah", "world", 3);
        ValidationUtil.validMinLength("nor", "fight", 5);
        ValidationUtil.validMinLength("nay", "", 0);
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validMinLength("hay", "", 1);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validMinLength("hoh", "z", 8);
        });
    }

    /** Test validUsernameFormat. */
    @Test
    public void testHasUsernameFormat() {
        ValidationUtil.validUsernameFormat("yay", "jolin_tsai");
        ValidationUtil.validUsernameFormat("yah", "jay79chou");
        ValidationUtil.validUsernameFormat("yao", "janice1988");
        ValidationUtil.validUsernameFormat("yat", "s_o_u_p_");
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yiy", "_wish_");
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yip", "123");
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yih", "ho ho");
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yit", "h@lo");
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yir", "bl*nk");
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.validUsernameFormat("yiq", "^_^");
        });
    }

    /** Test notExists. */
    @Test
    public void testNotExists() {
        final List<Foo> cache = new ArrayList<>();
        cache.add(new Foo("who"));
        cache.add(new Foo("what"));
        cache.add(new Foo("when"));
        cache.add(new Foo("why"));
        cache.add(new Foo("which"));
        cache.add(new Foo("how"));

        ValidationUtil.notExists("pa", "wish", cache, Foo::getBar);
        ValidationUtil.notExists("pe", "well", cache, Foo::getBar);
        ValidationUtil.notExists("pi", "woop", cache, Foo::getBar);
        ValidationUtil.notExists("po", "", cache, Foo::getBar);
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("uip", "who", cache, Foo::getBar);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("uth", "what", cache, Foo::getBar);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("upp", "when", cache, Foo::getBar);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("uvw", "which", cache, Foo::getBar);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("utc", "why", cache, Foo::getBar);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("uob", "how", cache, Foo::getBar);
        });
    }

    /** Test notExists with ignore. */
    @Test
    public void testNotExistsWithIgnore() {
        final List<Foo> cache = new ArrayList<>();
        cache.add(new Foo("Spring", "春天"));
        cache.add(new Foo("Summer", "夏天"));
        cache.add(new Foo("Autumn", "秋天")); // NOPMD
        cache.add(new Foo("Winter", "冬天"));

        ValidationUtil.notExists("za", "Autumn", cache, Foo::getBar, "秋天", Foo::getBaz);
        ValidationUtil.notExists("zb", "Autumn", cache, Foo::getBar, "秋天", Foo::getBaz);
        ValidationUtil.notExists("zc", "Sunday", cache, Foo::getBar, "星期天", Foo::getBaz);
        ValidationUtil.notExists("zd", "Sunday", cache, Foo::getBar, "", Foo::getBaz);
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("ze", "Autumn", cache, Foo::getBar, "夏天", Foo::getBaz);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("zf", "Autumn", cache, Foo::getBar, "夏天", Foo::getBaz);
        });
        assertThrows(InvalidFieldException.class, () -> {
            ValidationUtil.notExists("zg", "Autumn", cache, Foo::getBar, "", Foo::getBaz);
        });
    }
}
