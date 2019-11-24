package pcms.dashboard;

import pcms.ContentView;
import pcms.RootView;
import pcms.Session;
import pcms.loginrecord.LoginRecordController;
import pcms.loginrecord.LoginRecordRepository;
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
            final LoginRecordRepository loginRecordRepository,
            final UserRepository userRepository,
            final DashboardView dashboardView,
            final RootView rootView) {

        this.session = session;
        this.supplierRepository = supplierRepository;
        this.loginRecordRepository = loginRecordRepository;
        this.userRepository = userRepository;
        this.dashboardView = dashboardView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init(
            final SupplierController supplierController,
            final LoginRecordController loginRecordController,
            final UserController userController) {

        dashboardView.supplierListBtn.addActionListener(e -> supplierController.index(""));
        dashboardView.loginRecordListBtn.addActionListener(e -> loginRecordController.index(""));
        dashboardView.userListBtn.addActionListener(e -> userController.index(""));
    }

    /** List users. */
    public void index() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.DASHBOARD);
        dashboardView.render(
                supplierRepository.allInReverse(5), 
                loginRecordRepository.allInReverse(5),
                userRepository.allInReverse(5));
    }
}