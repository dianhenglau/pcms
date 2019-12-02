package pcms.dashboard;

import pcms.ContentView;
import pcms.RootView;
import pcms.Session;
import pcms.catalog.CatalogController;
import pcms.catalog.CatalogRepository;
import pcms.loginrecord.LoginRecordController;
import pcms.loginrecord.LoginRecordRepository;
import pcms.product.ProductController;
import pcms.product.ProductRepository;
import pcms.supplier.SupplierController;
import pcms.supplier.SupplierRepository;
import pcms.user.UserController;
import pcms.user.UserRepository;

/** Dashboard controller. */
public final class DashboardController {
    /** Session. */
    private final Session session; // NOPMD - temporary

    /** Supplier repository. */
    private final SupplierRepository supplierRepository;
    /** Product repository. */
    private final ProductRepository productRepository;
    /** Catalog repository. */
    private final CatalogRepository catalogRepository;
    /** Login record repository. */
    private final LoginRecordRepository loginRecordRepository;
    /** User repository. */
    private final UserRepository userRepository;

    /** Dashboard view. */
    private final DashboardView dashboardView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public DashboardController(
            final Session session,
            final SupplierRepository supplierRepository,
            final ProductRepository productRepository,
            final CatalogRepository catalogRepository,
            final LoginRecordRepository loginRecordRepository,
            final UserRepository userRepository,
            final DashboardView dashboardView,
            final RootView rootView) {

        this.session = session;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
        this.loginRecordRepository = loginRecordRepository;
        this.userRepository = userRepository;
        this.dashboardView = dashboardView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init(
            final SupplierController supplierController,
            final ProductController productController,
            final CatalogController catalogController,
            final LoginRecordController loginRecordController,
            final UserController userController) {

        dashboardView.supplierListBtn.addActionListener(e -> supplierController.index(""));
        dashboardView.productListBtn.addActionListener(e -> productController.index(""));
        dashboardView.catalogListBtn.addActionListener(e -> catalogController.index(""));
        dashboardView.loginRecordListBtn.addActionListener(e -> loginRecordController.index(""));
        dashboardView.userListBtn.addActionListener(e -> userController.index(""));
    }

    /** List users. */
    public void index() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.DASHBOARD);
        dashboardView.render(
                supplierRepository.allInReverse(5), 
                productRepository.allInReverse(5), 
                catalogRepository.allInReverse(5), 
                loginRecordRepository.allInReverse(5),
                userRepository.allInReverse(5));
    }
}
