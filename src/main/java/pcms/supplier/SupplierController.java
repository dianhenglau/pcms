package pcms.supplier;

import java.util.Locale;
import pcms.ContentView;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;

/** Supplier controller. */
public final class SupplierController {
    /** Session. */
    private final Session session; // NOPMD - temporary
    /** Supplier repository. */
    private final SupplierRepository supplierRepository;

    /** Supplier list view. */
    private final SupplierListView supplierListView;
    /** Supplier info view. */
    private final SupplierInfoView supplierInfoView;
    /** Add supplier view. */
    private final AddSupplierView addSupplierView;
    /** Edit supplier view. */
    private final EditSupplierView editSupplierView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public SupplierController(
            final Session session,
            final SupplierRepository supplierRepository,
            final SupplierListView supplierListView,
            final SupplierInfoView supplierInfoView,
            final AddSupplierView addSupplierView,
            final EditSupplierView editSupplierView,
            final RootView rootView) {

        this.session = session;
        this.supplierRepository = supplierRepository;
        this.supplierListView = supplierListView;
        this.supplierInfoView = supplierInfoView;
        this.addSupplierView = addSupplierView;
        this.editSupplierView = editSupplierView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        supplierListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
        supplierListView.addBtn.addActionListener(e -> create());
        supplierInfoView.editBtn.addActionListener(e -> edit(e.getActionCommand()));
        addSupplierView.saveBtn.addActionListener(e -> store());
        addSupplierView.cancelBtn.addActionListener(e -> index(""));
        editSupplierView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editSupplierView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
    }

    /** List suppliers. */
    public void index(final String search) {
        final String lowerCase = search.toLowerCase(Locale.US);

        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.SUPPLIER_LIST);

        if (search.isEmpty()) {
            supplierListView.render(supplierRepository.all(), e -> show(e.getActionCommand()));
        } else {
            supplierListView.render(
                    supplierRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getName().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getPhone().toLowerCase(Locale.US).contains(lowerCase)),
                    e -> show(e.getActionCommand()));
        }
    }

    /** Show create supplier form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_SUPPLIER);
        addSupplierView.render();
    }

    /** Store created supplier. */
    public void store() {
        try {
            final Supplier newSupplier = supplierRepository.insert(new Supplier.Builder()
                    .withName(addSupplierView.nameTf.getText())
                    .withEmail(addSupplierView.emailTf.getText())
                    .withPhone(addSupplierView.phoneTf.getText())
                    .withAddress(addSupplierView.addressTf.getText())
                    .withIsActive(addSupplierView.activeCb.isSelected())
                    .build());
            rootView.showSuccessDialog("Supplier added.");
            show(newSupplier.getId());
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Show supplier info. */
    public void show(final String id) {
        try {
            final Supplier supplier = ValidationUtil.recordExists(supplierRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.SUPPLIER_INFO);
            supplierInfoView.render(supplier);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Edit supplier info. */
    public void edit(final String id) {
        try {
            final Supplier supplier = ValidationUtil.recordExists(supplierRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.EDIT_SUPPLIER);
            editSupplierView.render(supplier);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update supplier info. */
    public void update(final String id) {
        try {
            final Supplier supplier = ValidationUtil.recordExists(supplierRepository, id);
            supplierRepository.update(new Supplier.Builder(supplier)
                    .withId(id)
                    .withName(editSupplierView.nameTf.getText())
                    .withEmail(editSupplierView.emailTf.getText())
                    .withPhone(editSupplierView.phoneTf.getText())
                    .withAddress(editSupplierView.addressTf.getText())
                    .withIsActive(editSupplierView.activeCb.isSelected())
                    .build());
            rootView.showSuccessDialog("Supplier updated.");
            show(id);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }
}
