package pcms;

import java.awt.CardLayout;
import javax.swing.JPanel;
import pcms.catalog.AddCatalogView;
import pcms.catalog.CatalogInfoView;
import pcms.catalog.CatalogListView;
import pcms.catalog.EditCatalogView;
import pcms.category.AddCategoryView;
import pcms.category.CategoryInfoView;
import pcms.category.CategoryListView;
import pcms.category.EditCategoryView;
import pcms.dashboard.DashboardView;
import pcms.loginrecord.LoginRecordListView;
import pcms.product.AddProductView;
import pcms.product.EditProductView;
import pcms.product.ProductInfoView;
import pcms.product.ProductListView;
import pcms.profile.EditProfileView;
import pcms.profile.ProfileView;
import pcms.supplier.AddSupplierView;
import pcms.supplier.EditSupplierView;
import pcms.supplier.SupplierInfoView;
import pcms.supplier.SupplierListView;
import pcms.user.AddUserView;
import pcms.user.EditUserView;
import pcms.user.UserInfoView;
import pcms.user.UserListView;

/** Content view. */
public final class ContentView { // NOPMD - Ok to have many fields
    /** List of children views. */
    public enum Views {
        USER_LIST, USER_INFO, ADD_USER, EDIT_USER, 
        PROFILE, EDIT_PROFILE,
        LOGIN_RECORD_LIST,
        CATEGORY_LIST, CATEGORY_INFO, ADD_CATEGORY, EDIT_CATEGORY,
        SUPPLIER_LIST, SUPPLIER_INFO, ADD_SUPPLIER, EDIT_SUPPLIER,
        PRODUCT_LIST, PRODUCT_INFO, ADD_PRODUCT, EDIT_PRODUCT,
        CATALOG_LIST, CATALOG_INFO, ADD_CATALOG, EDIT_CATALOG,
        DASHBOARD
    }

    /** User list view. */
    public final UserListView userListView;
    /** User info view. */
    public final UserInfoView userInfoView;
    /** Add user view. */
    public final AddUserView addUserView;
    /** Edit user view. */
    public final EditUserView editUserView;

    /** Profile view. */
    public final ProfileView profileView;
    /** Edit profile view. */
    public final EditProfileView editProfileView;

    /** Login record list view. */
    public final LoginRecordListView loginRecordListView;

    /** Category list view. */
    public final CategoryListView categoryListView;
    /** Category info view. */
    public final CategoryInfoView categoryInfoView;
    /** Add category view. */
    public final AddCategoryView addCategoryView;
    /** Edit category view. */
    public final EditCategoryView editCategoryView;

    /** Supplier list view. */
    public final SupplierListView supplierListView;
    /** Supplier info view. */
    public final SupplierInfoView supplierInfoView;
    /** Add supplier view. */
    public final AddSupplierView addSupplierView;
    /** Edit supplier view. */
    public final EditSupplierView editSupplierView;
    
    /** Supplier list view. */
    public final ProductListView productListView;
    /** Supplier info view. */
    public final ProductInfoView productInfoView;
    /** Add supplier view. */
    public final AddProductView addProductView;
    /** Edit supplier view. */
    public final EditProductView editProductView;
    
    /** Supplier list view. */
    public final CatalogListView catalogListView;
    /** Supplier info view. */
    public final CatalogInfoView catalogInfoView;
    /** Add supplier view. */
    public final AddCatalogView addCatalogView;
    /** Edit supplier view. */
    public final EditCatalogView editCatalogView;

    /** Dashboard view. */
    public final DashboardView dashboardView;

    /** Pane. */
    public final JPanel pane;
    /** Card layout. */
    private final CardLayout cardLayout;

    /** Construct. */
    public ContentView(// NOPMD - Ok to have long parameter list
            final UserListView userListView,
            final UserInfoView userInfoView,
            final AddUserView addUserView,
            final EditUserView editUserView,
            final ProfileView profileView,
            final EditProfileView editProfileView,
            final LoginRecordListView loginRecordListView,
            final CategoryListView categoryListView,
            final CategoryInfoView categoryInfoView,
            final AddCategoryView addCategoryView,
            final EditCategoryView editCategoryView,
            final SupplierListView supplierListView,
            final SupplierInfoView supplierInfoView,
            final AddSupplierView addSupplierView,
            final EditSupplierView editSupplierView,
            final ProductListView productListView,
            final ProductInfoView productInfoView,
            final AddProductView addProductView,
            final EditProductView editProductView,
            final CatalogListView catalogListView,
            final CatalogInfoView catalogInfoView,
            final AddCatalogView addCatalogView,
            final EditCatalogView editCatalogView,
            final DashboardView dashboardView) {

        this.userListView = userListView;
        this.userInfoView = userInfoView;
        this.addUserView = addUserView;
        this.editUserView = editUserView;
        this.profileView = profileView;
        this.editProfileView = editProfileView;
        this.loginRecordListView = loginRecordListView;
        this.categoryListView = categoryListView;
        this.categoryInfoView = categoryInfoView;
        this.addCategoryView = addCategoryView;
        this.editCategoryView = editCategoryView;
        this.supplierListView = supplierListView;
        this.supplierInfoView = supplierInfoView;
        this.addSupplierView = addSupplierView;
        this.editSupplierView = editSupplierView;
        this.productListView = productListView;
        this.productInfoView = productInfoView;
        this.addProductView = addProductView;
        this.editProductView = editProductView;
        this.catalogListView = catalogListView;
        this.catalogInfoView = catalogInfoView;
        this.addCatalogView = addCatalogView;
        this.editCatalogView = editCatalogView;
        this.dashboardView = dashboardView;

        cardLayout = ViewUtil.createVariableSizeCardLayout();
        pane = new JPanel(cardLayout);

        pane.add(userListView.pane, Views.USER_LIST.name());
        pane.add(userInfoView.pane, Views.USER_INFO.name());
        pane.add(addUserView.pane, Views.ADD_USER.name());
        pane.add(editUserView.pane, Views.EDIT_USER.name());
        pane.add(profileView.pane, Views.PROFILE.name());
        pane.add(editProfileView.pane, Views.EDIT_PROFILE.name());
        pane.add(loginRecordListView.pane, Views.LOGIN_RECORD_LIST.name());
        pane.add(categoryListView.pane, Views.CATEGORY_LIST.name());
        pane.add(categoryInfoView.pane, Views.CATEGORY_INFO.name());
        pane.add(addCategoryView.pane, Views.ADD_CATEGORY.name());
        pane.add(editCategoryView.pane, Views.EDIT_CATEGORY.name());
        pane.add(supplierListView.pane, Views.SUPPLIER_LIST.name());
        pane.add(supplierInfoView.pane, Views.SUPPLIER_INFO.name());
        pane.add(addSupplierView.pane, Views.ADD_SUPPLIER.name());
        pane.add(editSupplierView.pane, Views.EDIT_SUPPLIER.name());
        pane.add(productListView.pane, Views.PRODUCT_LIST.name());
        pane.add(productInfoView.pane, Views.PRODUCT_INFO.name());
        pane.add(addProductView.pane, Views.ADD_PRODUCT.name());
        pane.add(editProductView.pane, Views.EDIT_PRODUCT.name());
        pane.add(catalogListView.pane, Views.CATALOG_LIST.name());
        pane.add(catalogInfoView.pane, Views.CATALOG_INFO.name());
        pane.add(addCatalogView.pane, Views.ADD_CATALOG.name());
        pane.add(editCatalogView.pane, Views.EDIT_CATALOG.name());
        pane.add(dashboardView.pane, Views.DASHBOARD.name());
    }

    /** Show a view with given key. */
    public void render(final Views key) {
        cardLayout.show(pane, key.name());
    }
}
