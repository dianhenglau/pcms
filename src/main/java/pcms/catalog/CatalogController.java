package pcms.catalog;

import java.time.Instant;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import pcms.ContentView;
import pcms.DatePicker;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.product.ProductRepository;
import pcms.user.UserRepository;

/** Catalog controller. */
public final class CatalogController {
    /** Session. */
    private final Session session; // NOPMD - temporaray
    /** Catalog repository. */
    private final CatalogRepository catalogRepository;
//    /** User repository. */
//    private final UserRepository userRepository;
//    /** Product repository. */
//    private final ProductRepository productRepository;

    /** Catalog list view. */
    private final CatalogListView catalogListView;
    /** Catalog info view. */
    private final CatalogInfoView catalogInfoView;
    /** Add catalog view. */
    private final AddCatalogView addCatalogView;
    /** Edit catalog view. */
    private final EditCatalogView editCatalogView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public CatalogController(
            final Session session,
            final CatalogRepository catalogRepository,
            final UserRepository userRepository,
            final ProductRepository productRepository,
            final CatalogListView catalogListView,
            final CatalogInfoView catalogInfoView,
            final AddCatalogView addCatalogView,
            final EditCatalogView editCatalogView,
            final RootView rootView) {

        this.session = session;
        this.catalogRepository = catalogRepository;
//        this.userRepository = userRepository;
//        this.productRepository = productRepository;
        this.catalogListView = catalogListView;
        this.catalogInfoView = catalogInfoView;
        this.addCatalogView = addCatalogView;
        this.editCatalogView = editCatalogView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        catalogListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
        catalogListView.addBtn.addActionListener(e -> create());
        catalogInfoView.editBtn.addActionListener(e -> edit(e.getActionCommand()));
        catalogInfoView.backBtn.addActionListener(e -> index(catalogListView.searchTf.getText()));
        addCatalogView.saveBtn.addActionListener(e -> store());
        addCatalogView.cancelBtn.addActionListener(e -> index(""));
        addCatalogView.bannerBtn.addActionListener(e -> chooseImage(addCatalogView::renderImage));
        addCatalogView.selectStartDateBtn.addActionListener(e -> chooseDate(addCatalogView::renderStartDate));
        addCatalogView.selectEndDateBtn.addActionListener(e -> chooseDate(addCatalogView::renderEndDate));
        editCatalogView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editCatalogView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
        editCatalogView.bannerBtn.addActionListener(e -> chooseImage(editCatalogView::renderImage));
        editCatalogView.selectStartDateBtn.addActionListener(e -> chooseDate(editCatalogView::renderStartDate));
        editCatalogView.selectEndDateBtn.addActionListener(e -> chooseDate(editCatalogView::renderEndDate));   
    }

    /** List catalogs. */
    public void index(final String search) {
        final String lowerCase = search.toLowerCase(Locale.US);

        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.CATALOG_LIST);
        
        if (search.isEmpty()) {
            catalogListView.render(
                    catalogRepository.all(), 
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        } else {
            catalogListView.render(
                    catalogRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getTitle().toLowerCase(Locale.US).contains(lowerCase)),
                    search,
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        }
    }

    /** Show create catalog form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_CATALOG);
        addCatalogView.render();
    }

    /** Store created catalog. */
    public void store() {
        try {
            final Catalog newCatalog = catalogRepository.insert(new Catalog.Builder()
                    .withTitle(addCatalogView.titleTf.getText())
                    .withBanner(addCatalogView.filenameLbl.getText())
                    .withDescription(addCatalogView.descriptionTa.getText())
                    .withSeasonStartDate(addCatalogView.displayStartDateLbl.getText())
                    .withSeasonEndDate(addCatalogView.displayEndDateLbl.getText())
                    .withTimestamp(Instant.now())
                    .withUserId(session.getUser().get().getId())
                    .build());

            rootView.showSuccessDialog("Catalog added.");
            show(newCatalog.getId());

        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Show catalog info. */
    public void show(final String id) {
        try {
            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.CATALOG_INFO);
            catalogInfoView.render(catalog);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Edit catalog info. */
    public void edit(final String id) {
        try {
            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);
            rootView.render(RootView.Views.MAIN_VIEW);
            rootView.mainView.contentView.render(ContentView.Views.EDIT_CATALOG);
            editCatalogView.render(catalog);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update catalog info. */
    public void update(final String id) {
        try {

            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);
            final Catalog newCatalog = catalogRepository.update(new Catalog.Builder(catalog)
                    .withTitle(editCatalogView.titleTf.getText())
                    .withBanner(editCatalogView.filenameLbl.getText())
                    .withDescription(editCatalogView.descriptionTa.getText())
                    .withSeasonStartDate(editCatalogView.displayStartDateLbl.getText())
                    .withSeasonEndDate(editCatalogView.displayEndDateLbl.getText())
                    .withTimestamp(Instant.now())
                    .withUserId(session.getUser().get().getId())
                    .build());

            catalogRepository.update(newCatalog);
            rootView.showSuccessDialog("Catalog updated.");
            show(id);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }                                                       
    }
    
    /** Destroy (delete) category. */
    public void destroy(final String id, final String originalParameter) {
        try {
            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);
            catalogRepository.delete(catalog);
            rootView.showSuccessDialog("Catalog deleted.");
            index(originalParameter);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Choose image. */
    public void chooseImage(final Consumer<String> renderer) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg"));
        final int returnValue = fileChooser.showOpenDialog(rootView.frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            renderer.accept(fileChooser.getSelectedFile().toPath().toString());
        }
    }
    
    /** Date picker. */
    public void chooseDate(final Consumer<String> renderer){
        final JFrame dateChooser = new JFrame();
        renderer.accept((new DatePicker(dateChooser).setPickedDate()));
    }
}
