# Architecture

Model-View-Controller


## Technical Details

1. When registering listener of UI component in view X, do it in controller X.

2. Do not call UI component's method method directly in controller, except `addActionListener`. Call view's `render` method instead.

3. Use `actionCommand` to pass ID.

4. All view's public method should be `render`.


# Directory Structure

1. Feature-based instead of type-based.

2. [Discussion needed] A class is placed under `common` package if and only if it does not depend on other class in this project.


# View Composition Hierarchy

    RootView
        LoginView
        MainView
            MenuView
            ContentView
                DashboardView
                UserListView
                UserInfoView
                AddUserView
                EditUserView
                LoginRecordView
                SupplierListView
                SupplierInfoView
                AddSupplierView
                EditSupplierView
                CategoryListView
                CategoryInfoView
                AddCategoryView
                EditCategoryView
                ProductListView
                ProductInfoView
                AddProductView
                EditProductView
                CatalogListView
                CatalogInfoView
                AddCatalogView
                EditCatalogView
                ProfileView
                EditProfileView

