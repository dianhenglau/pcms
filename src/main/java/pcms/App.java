package pcms; // NOPMD - Ok to have high number of imports

import java.nio.file.Path;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import pcms.category.AddCategoryView;
import pcms.category.CategoryController;
import pcms.category.CategoryInfoView;
import pcms.category.CategoryListView;
import pcms.category.CategoryRepository;
import pcms.category.EditCategoryView;
import pcms.dashboard.DashboardController;
import pcms.dashboard.DashboardView;
import pcms.login.LoginController;
import pcms.login.LoginView;
import pcms.loginrecord.LoginRecord;
import pcms.loginrecord.LoginRecordController;
import pcms.loginrecord.LoginRecordListView;
import pcms.loginrecord.LoginRecordRepository;
import pcms.menu.MenuController;
import pcms.menu.MenuView;
import pcms.product.AddProductView;
import pcms.product.EditProductView;
import pcms.product.Product;
import pcms.product.ProductController;
import pcms.product.ProductInfoView;
import pcms.product.ProductListView;
import pcms.product.ProductRepository;
import pcms.profile.EditProfileView;
import pcms.profile.ProfileController;
import pcms.profile.ProfileView;
import pcms.supplier.AddSupplierView;
import pcms.supplier.EditSupplierView;
import pcms.supplier.SupplierController;
import pcms.supplier.SupplierInfoView;
import pcms.supplier.SupplierListView;
import pcms.supplier.SupplierRepository;
import pcms.user.AddUserView;
import pcms.user.EditUserView;
import pcms.user.UserController;
import pcms.user.UserInfoView;
import pcms.user.UserListView;
import pcms.user.UserRepository;

/** App. */
public final class App {
    /** Session that store anything related to current user. */
    private final Session session;
    /** User repository. */
    private final UserRepository userRepository;
    /** Login record repository. */
    private final LoginRecordRepository loginRecordRepository;
    /** Category repository. */
    private final CategoryRepository categoryRepository;
    /** Supplier repository. */
    private final SupplierRepository supplierRepository;
    /** Product repository. */
    private final ProductRepository productRepository;

    /** Construct app. */
    public App() {
        // Enable anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        // Set cross platform look and feel
        final String errMsg = "Failed to set look and feel: ";
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException 
                | UnsupportedLookAndFeelException ex) {
            System.err.println(errMsg + ex.getMessage());
        }

        // Create session.
        session = new Session();

        // Create models.
        userRepository = new UserRepository(getDataPath("users.csv"));

        loginRecordRepository = new LoginRecordRepository(
                getDataPath("login_records.csv"), userRepository);
        LoginRecord.setUserRepository(userRepository);

        categoryRepository = new CategoryRepository(getDataPath("categories.csv"));
        
        supplierRepository = new SupplierRepository(getDataPath("suppliers.csv"));
        
        productRepository = new ProductRepository(
                getDataPath("products.csv"), categoryRepository, supplierRepository);
        Product.setCategoryRepository(categoryRepository);
        Product.setSupplierRepository(supplierRepository);
    }

    /** Run app. */
    public void createAndShowGui() { // NOPMD - Ok to have long methods
        // Create views.
        final UserListView userListView = new UserListView();
        final UserInfoView userInfoView = new UserInfoView();
        final AddUserView addUserView = new AddUserView();
        final EditUserView editUserView = new EditUserView();

        final ProfileView profileView = new ProfileView();
        final EditProfileView editProfileView = new EditProfileView();

        final LoginRecordListView loginRecordListView = new LoginRecordListView();

        final CategoryListView categoryListView = new CategoryListView();
        final CategoryInfoView categoryInfoView = new CategoryInfoView();
        final AddCategoryView addCategoryView = new AddCategoryView();
        final EditCategoryView editCategoryView = new EditCategoryView();

        final SupplierListView supplierListView = new SupplierListView();
        final SupplierInfoView supplierInfoView = new SupplierInfoView();
        final AddSupplierView addSupplierView = new AddSupplierView();
        final EditSupplierView editSupplierView = new EditSupplierView();
        final ProductListView productListView = new ProductListView();
        final ProductInfoView productInfoView = new ProductInfoView();
        final AddProductView addProductView = new AddProductView();
        final EditProductView editProductView = new EditProductView();
        final DashboardView dashboardView = new DashboardView();
        final MenuView menuView = new MenuView();
        final ContentView contentView = new ContentView(
                userListView,
                userInfoView,
                addUserView,
                editUserView,
                profileView,
                editProfileView,
                loginRecordListView,
                categoryListView,
                categoryInfoView,
                addCategoryView,
                editCategoryView,
                supplierListView,
                supplierInfoView,
                addSupplierView,
                editSupplierView,
                productListView,
                productInfoView,
                addProductView,
                editProductView,
                dashboardView);

        final MainView mainView = new MainView(menuView, contentView);
        final LoginView loginView = new LoginView();

        final RootView rootView = new RootView(mainView, loginView);

        // Create controllers.
        final UserController userController = new UserController(
                session,
                userRepository,
                userListView,
                userInfoView,
                addUserView,
                editUserView,
                rootView);
        final ProfileController profileController = new ProfileController(
                session,
                userRepository,
                profileView,
                editProfileView,
                rootView);
        final LoginRecordController loginRecordController = new LoginRecordController(
                session,
                loginRecordRepository,
                loginRecordListView,
                rootView);
        final CategoryController categoryController = new CategoryController(
                session,
                categoryRepository,
                categoryListView,
                categoryInfoView,
                addCategoryView,
                editCategoryView,
                rootView);
        final SupplierController supplierController = new SupplierController(
                session,
                supplierRepository,
                supplierListView,
                supplierInfoView,
                addSupplierView,
                editSupplierView,
                rootView);
        final ProductController productController = new ProductController(
                session,
                productRepository,
                categoryRepository,
                supplierRepository,
                productListView,
                productInfoView,
                addProductView,
                editProductView,
                rootView);
        final DashboardController dashboardController = new DashboardController(
                session,
                supplierRepository,
                loginRecordRepository,
                userRepository,
                dashboardView,
                rootView);

        final MenuController menuController = new MenuController(
                session,
                menuView,
                rootView);

        final LoginController loginController = new LoginController(
                session,
                userRepository,
                loginRecordRepository,
                loginView,
                rootView);

        userController.init();
        profileController.init();
        loginRecordController.init();
        categoryController.init();
        supplierController.init();
        productController.init();
        dashboardController.init(
                supplierController, 
                loginRecordController,
                userController);

        menuController.init(
                loginController,
                userController,
                profileController,
                loginRecordController,
                categoryController,
                supplierController,
                productController,
                dashboardController);

        loginController.init(dashboardController);

        // Load login page.
        loginController.index();
    }

    /** Program entry point. */
    public static void main(final String[] args) {
        final App app = new App();
        SwingUtilities.invokeLater(app::createAndShowGui);
    }

    /** Get data path. */
    private static Path getDataPath(final String filename) {
        return Path.of("data", "main", filename);
    }
}
