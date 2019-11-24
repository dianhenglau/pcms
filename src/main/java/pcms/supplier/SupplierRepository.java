package pcms.supplier;

import java.nio.file.Path;
import java.util.Optional;
import pcms.Repository;
import pcms.ValidationUtil;

/** Models supplier repository. */
public final class SupplierRepository extends Repository<Supplier> {
    /** Constructor. */
    public SupplierRepository(final Path dataPath) {
        super(dataPath, id -> new Supplier.Builder().withId(id).build(), Supplier::new);
    }

    /** Insert supplier. */
    @Override
    public Supplier insert(final Supplier supplier) {
        readFromFile();

        // Make sure name length is at least one.
        ValidationUtil.validMinLength("name", supplier.getName(), 1); // NOPMD
        ValidationUtil.notExists("name", supplier.getName(), cache, Supplier::getName);

        // Make sure email is valid.
        ValidationUtil.notEmpty("email", supplier.getEmail()); // NOPMD - Ok to duplicate literal
        ValidationUtil.validEmailFormat("email", supplier.getEmail());

        // Make sure phone is valid.
        ValidationUtil.validPhoneFormat("phone", supplier.getPhone());

        // Add to cache
        final Supplier newSupplier = new Supplier.Builder(supplier)
                .withId(String.format("S%05d", newId))
                .build();
        cache.add(newSupplier);
        newId += 1;

        writeToFile();
        return newSupplier;
    }

    /** Update supplier. */
    @Override
    public Supplier update(final Supplier supplier) {
        readFromFile();

        // Make sure supplier id exists.
        final int index = ValidationUtil.idExists(cache, supplier);

        // Make sure name length is at least one.
        ValidationUtil.validMinLength("name", supplier.getName(), 1);
        ValidationUtil.notExists("name", supplier.getName(), cache, Supplier::getName,
                supplier.getId(), Supplier::getId);

        // Make sure email is valid.
        ValidationUtil.notEmpty("email", supplier.getEmail());
        ValidationUtil.validEmailFormat("email", supplier.getEmail());

        // Make sure phone is valid.
        ValidationUtil.validPhoneFormat("phone", supplier.getPhone());

        cache.set(index, supplier);
        writeToFile();
        return cache.get(index);
    }

    /** Find with name. */
    public Optional<Supplier> findWithName(final String name) {
        return cache.stream().filter(x -> name.equals(x.getName())).findFirst();
    }
}
