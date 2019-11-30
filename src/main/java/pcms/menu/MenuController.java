package pcms.menu;

import pcms.RootView;
import pcms.Session;
import pcms.catalog.CatalogController;
import pcms.category.CategoryController;
import pcms.dashboard.DashboardController;
import pcms.login.LoginController;
import pcms.loginrecord.LoginRecordController;
import pcms.product.ProductController;
import pcms.profile.ProfileController;
import pcms.supplier.SupplierController;
import pcms.user.UserController;

/** Menu controller. */
public final class MenuController {
    /** Session. */
    private final Session session; // NOPMD - temporary

    /** Menu view. */
    private final MenuView menuView;
    /** Root view. */
    private final RootView rootView; // NOPMD - temporary

    /** Construct. */
    public MenuController(
            final Session session,
            final MenuView menuView,
            final RootView rootView) {

        this.session = session;
        this.menuView = menuView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init(
            final LoginController loginController,
            final UserController userController,
            final ProfileController profileController,
            final LoginRecordController loginRecordController,
            final CategoryController categoryController,
            final SupplierController supplierController,
            final ProductController productController,
            final CatalogController catalogController,
            final DashboardController dashboardController) {

        menuView.userBtn.addActionListener(e -> userController.index(""));
        menuView.logoutBtn.addActionListener(e -> loginController.logout());
        menuView.profileBtn.addActionListener(e -> profileController.index());
        menuView.loginRecordBtn.addActionListener(e -> loginRecordController.index(""));
        menuView.categoryBtn.addActionListener(e -> categoryController.index(""));
        menuView.supplierBtn.addActionListener(e -> supplierController.index(""));
        menuView.productBtn.addActionListener(e -> productController.index(""));
        menuView.catalogBtn.addActionListener(e -> catalogController.index(""));
        menuView.dashboardBtn.addActionListener(e -> dashboardController.index());
    }
}

