package pcms.category;

import java.nio.file.Path;
import pcms.Repository;
import pcms.ValidationUtil;

/** Models category repository. */
public final class CategoryRepository extends Repository<Category> {
    /** Constructor. */
    public CategoryRepository(final Path dataPath) {
        super(dataPath, id -> new Category.Builder().withId(id).build(), Category::new);
    }

    /** Insert category. */
    @Override
    public Category insert(final Category category) {
        readFromFile();

        // Make sure name is valid.
        ValidationUtil.validMinLength("name", category.getName(), 1); // NOPMD
        ValidationUtil.notExists("name", category.getName(), cache, Category::getName);

        // Add to cache
        final Category newCategory = new Category.Builder(category)
                .withId(String.format("C%05d", newId))
                .build();
        cache.add(newCategory);
        newId += 1;

        writeToFile();
        return newCategory;
    }

    /** Update category. */
    @Override
    public Category update(final Category category) {
        readFromFile();

        // Make sure category id exists.
        final int index = ValidationUtil.idExists(cache, category);

        // Make sure name is valid.
        ValidationUtil.validMinLength("name", category.getName(), 1);
        ValidationUtil.notExists("name", category.getName(), cache, Category::getName,
                category.getId(), Category::getId);
        
        cache.set(index, category);
        writeToFile();
        return cache.get(index);
    }
}
