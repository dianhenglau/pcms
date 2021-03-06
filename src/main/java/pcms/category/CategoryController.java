package pcms.category;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import pcms.ContentView;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.product.Product;
import pcms.product.ProductRepository;

/** Category controller. */
public final class CategoryController {
    /** Session. */
    private final Session session; // NOPMD - temporary
    /** Category repository. */
    private final CategoryRepository categoryRepository;
    /** Product repository. */
    private final ProductRepository productRepository;

    /** Category list view. */
    private final CategoryListView categoryListView;
    /** Category info view. */
    private final CategoryInfoView categoryInfoView;
    /** Add category view. */
    private final AddCategoryView addCategoryView;
    /** Edit category view. */
    private final EditCategoryView editCategoryView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public CategoryController(
            final Session session,
            final CategoryRepository categoryRepository,
            final ProductRepository productRepository,
            final CategoryListView categoryListView,
            final CategoryInfoView categoryInfoView,
            final AddCategoryView addCategoryView,
            final EditCategoryView editCategoryView,
            final RootView rootView) {

        this.session = session;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryListView = categoryListView;
        this.categoryInfoView = categoryInfoView;
        this.addCategoryView = addCategoryView;
        this.editCategoryView = editCategoryView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        categoryListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
        categoryListView.addBtn.addActionListener(e -> create());
        categoryInfoView.editBtn.addActionListener(e -> edit(e.getActionCommand()));
        categoryInfoView.backBtn.addActionListener(e -> index(categoryListView.searchTf.getText()));
        addCategoryView.saveBtn.addActionListener(e -> store());
        addCategoryView.cancelBtn.addActionListener(e -> index(""));
        editCategoryView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editCategoryView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
    }

    /** List categories. */
    public void index(final String search) {
        final String lowerCase = search.toLowerCase(Locale.US);

        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.CATEGORY_LIST);

        if (search.isEmpty()) {
            categoryListView.render(
                    categoryRepository.all(), 
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        } else {
            categoryListView.render(
                    categoryRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getName().toLowerCase(Locale.US).contains(lowerCase)),
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        }
    }

    /** Show create category form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_CATEGORY);
        addCategoryView.render();
    }

    /** Store created category. */
    public void store() {
        try {
            final Category newCategory = categoryRepository.insert(new Category.Builder()
                    .withName(addCategoryView.nameTf.getText())
                    .withDescription(addCategoryView.descriptionTa.getText())
                    .build());
            rootView.showSuccessDialog("Category added.");
            show(newCategory.getId());
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Show category info. */
    public void show(final String id) {
        try {
            final Category category = ValidationUtil.recordExists(categoryRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.CATEGORY_INFO);
            categoryInfoView.render(category);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Edit category info. */
    public void edit(final String id) {
        try {
            final Category category = ValidationUtil.recordExists(categoryRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.EDIT_CATEGORY);
            editCategoryView.render(category);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update category info. */
    public void update(final String id) {
        try {
            final Category category = ValidationUtil.recordExists(categoryRepository, id);
            categoryRepository.update(new Category.Builder(category)
                    .withId(id)
                    .withName(editCategoryView.nameTf.getText())
                    .withDescription(editCategoryView.descriptionTa.getText())
                    .build());
            rootView.showSuccessDialog("Category updated.");
            show(id);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Destroy (delete) category. */
    public void destroy(final String id, final String originalParameter) {
        try {
            final Category category = ValidationUtil.recordExists(categoryRepository, id);
            final List<Product> products = productRepository.filter(x ->
                    x.getCategoryId().equals(category.getId()));
            if (!products.isEmpty()) {
                // CHECKSTYLE:OFF
                throw new InvalidFieldException("product", String.format(
                        "Cannot delete category, because it was used in the following products: %s",
                        String.join(", ", products.stream().map(Product::getId).collect(Collectors.toList()))));
                // CHECKSTYLE:ON
            }

            categoryRepository.delete(category);
            rootView.showSuccessDialog("Category deleted.");
            index(originalParameter);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }
}
