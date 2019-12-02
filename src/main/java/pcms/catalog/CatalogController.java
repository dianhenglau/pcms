package pcms.catalog; // NOPMD - Okay to have many imports

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import pcms.ContentView;
import pcms.DatePicker;
import pcms.InvalidFieldException;
import pcms.RootView;
import pcms.Session;
import pcms.ValidationUtil;
import pcms.product.Product;
import pcms.product.ProductRepository;
import pcms.user.UserRepository;

/** Catalog controller. */
public final class CatalogController {
    /** Session. */
    private final Session session; // NOPMD - temporaray
    /** Catalog repository. */
    private final CatalogRepository catalogRepository;
    /** User repository. */
    private final UserRepository userRepository; // NOPMD - temporary
    /** Product repository. */
    private final ProductRepository productRepository; // NOPMD - temporary

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
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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
        catalogInfoView.pdfBtn.addActionListener(e -> createPdf(e.getActionCommand()));
        addCatalogView.saveBtn.addActionListener(e -> store());
        addCatalogView.cancelBtn.addActionListener(e -> index(""));
        addCatalogView.bannerBtn.addActionListener(e -> chooseImage(addCatalogView::renderImage));
        addCatalogView.selectStartDateBtn.addActionListener(
                e -> chooseDate(addCatalogView::renderStartDate));
        addCatalogView.selectEndDateBtn.addActionListener(
                e -> chooseDate(addCatalogView::renderEndDate));
        editCatalogView.saveBtn.addActionListener(e -> update(e.getActionCommand()));
        editCatalogView.cancelBtn.addActionListener(e -> show(e.getActionCommand()));
        editCatalogView.bannerBtn.addActionListener(e -> chooseImage(editCatalogView::renderImage));
        editCatalogView.selectStartDateBtn.addActionListener(
                e -> chooseDate(editCatalogView::renderStartDate));
        editCatalogView.selectEndDateBtn.addActionListener(
                e -> chooseDate(editCatalogView::renderEndDate));   
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
                    session.getUser().get().isProductManager(),
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        } else {
            catalogListView.render(
                    catalogRepository.filter(x ->
                        x.getId().toLowerCase(Locale.US).contains(lowerCase)
                        || x.getTitle().toLowerCase(Locale.US).contains(lowerCase)),
                    search,
                    session.getUser().get().isProductManager(),
                    e -> show(e.getActionCommand()),
                    e -> destroy(e.getActionCommand(), search));
        }
    }

    /** Show create catalog form. */
    public void create() {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.ADD_CATALOG);
        addCatalogView.render(productRepository.all());
    }

    /** Store created catalog. */
    public void store() {
        try {
            ValidationUtil.notEmpty("start date", addCatalogView.displayStartDateLbl.getText());
            ValidationUtil.notEmpty("end date", addCatalogView.displayEndDateLbl.getText());

            final List<ProductDiscount> productDiscounts = new ArrayList<>();
            for (int i = 0; i < addCatalogView.selectedProductCbs.size(); i++) {
                final JCheckBox cb = addCatalogView.selectedProductCbs.get(i);
                final JFormattedTextField tf = addCatalogView.specialDiscountTfs.get(i);
                if (cb.isSelected()) {
                    productDiscounts.add(new ProductDiscount(cb.getActionCommand(), 
                                ((Number) tf.getValue()).doubleValue()));
                }
            }

            final Catalog newCatalog = catalogRepository.insert(new Catalog.Builder()
                    .withTitle(addCatalogView.titleTf.getText())
                    .withBanner(addCatalogView.filenameLbl.getText())
                    .withDescription(addCatalogView.descriptionTa.getText())
                    .withSeasonStartDate(LocalDate.parse(
                            addCatalogView.displayStartDateLbl.getText(), 
                            DateTimeFormatter.ISO_LOCAL_DATE))
                    .withSeasonEndDate(LocalDate.parse(
                            addCatalogView.displayEndDateLbl.getText(),
                            DateTimeFormatter.ISO_LOCAL_DATE))
                    .withProductDiscounts(productDiscounts)
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
            catalogInfoView.render(catalog, session.getUser().get().isProductManager());
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
            editCatalogView.render(catalog, productRepository.all());
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        }
    }

    /** Update catalog info. */
    public void update(final String id) {
        try {
            ValidationUtil.notEmpty("start date", editCatalogView.displayStartDateLbl.getText());
            ValidationUtil.notEmpty("end date", editCatalogView.displayEndDateLbl.getText());
            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);

            final List<ProductDiscount> productDiscounts = new ArrayList<>();
            for (int i = 0; i < editCatalogView.selectedProductCbs.size(); i++) {
                final JCheckBox cb = editCatalogView.selectedProductCbs.get(i);
                final JFormattedTextField tf = editCatalogView.specialDiscountTfs.get(i);
                if (cb.isSelected()) {
                    productDiscounts.add(new ProductDiscount(cb.getActionCommand(), 
                                ((Number) tf.getValue()).doubleValue()));
                }
            }

            catalogRepository.update(new Catalog.Builder(catalog)
                    .withTitle(editCatalogView.titleTf.getText())
                    .withBanner(editCatalogView.filenameLbl.getText())
                    .withDescription(editCatalogView.descriptionTa.getText())
                    .withSeasonStartDate(LocalDate.parse(
                            editCatalogView.displayStartDateLbl.getText(),
                            DateTimeFormatter.ISO_LOCAL_DATE))
                    .withSeasonEndDate(LocalDate.parse(
                            editCatalogView.displayEndDateLbl.getText(),
                            DateTimeFormatter.ISO_LOCAL_DATE))
                    .withProductDiscounts(productDiscounts)
                    .withTimestamp(Instant.now())
                    .withUserId(session.getUser().get().getId())
                    .build());

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
    public void chooseDate(final Consumer<String> renderer) {
        final JFrame dateChooser = new JFrame();
        renderer.accept(new DatePicker(dateChooser).setPickedDate());
    }

    /** Generate catalog pdf. */
    public void createPdf(final String id) { // NOPMD - Okay to have long method
        Document doc = null;
        try {
            final Catalog catalog = ValidationUtil.recordExists(catalogRepository, id);

            // Set pdf file path.
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(String.join("", 
                    catalog.getTitle(),
                    "_",
                    catalog.getSeasonStartDate().toString().substring(0,4),
                    ".pdf")));
            final int returnValue = fileChooser.showOpenDialog(rootView.frame);
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                return;
            }

            // Initialize PDF document.
            doc = new Document(new PdfDocument(new PdfWriter(fileChooser.getSelectedFile())));
            // Set default font.
            final PdfFont norm = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            final PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

            // Display catalog info.
            final Table infoTable = new Table(UnitValue.createPercentArray(
                        new float[] {2, 5}), true);

            infoTable.addCell(new Cell()
                    .add(new Image(ImageDataFactory.create(catalog.getBanner()))
                        .setHeight(100)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER))
                    .setBorder(Border.NO_BORDER));

            infoTable.addCell(new Cell()
                    .add(new Paragraph("Price List of "
                            + catalog.getTitle()
                            + " Sales "
                            + catalog.getSeasonStartDate().toString().substring(0,4)
                            + "\n"
                            + "Effective on: "
                            + catalog.getSeasonStartDate().toString()
                            + "-"
                            + catalog.getSeasonEndDate().toString())
                        .setFont(norm))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
                    .setKeepTogether(true));

            doc.add(infoTable);       
            doc.add(new Paragraph(""));

            // Display product info
            final Table productTable = new Table(UnitValue.createPercentArray(
                        new float[] {1, 1, 4, 1, 1, 1}), true);

            // Add header to table. 
            final String[] header = {"Product ID", "Product Image", "Product\nName", 
                "Retail Price", "Special Discount", "Discount Price"};
            for (final String i : header) {
                productTable.addHeaderCell(new Cell()
                        .setKeepTogether(true)
                        .add(new Paragraph(new Text(i).setFont(bold)))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
            }

            doc.add(productTable);

            // Add product to table. 
            for (final ProductDiscount p: catalog.getProductDiscounts()) {
                final Product product = p.getProduct().get();

                productTable.addCell(new Cell()
                        .add(new Paragraph(product.getId()).setFont(norm))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setKeepTogether(true));
                productTable.addCell(new Image(ImageDataFactory.create(product.getImage()))
                        .setAutoScale(true));
                productTable.addCell(new Cell()
                        .add(new Paragraph(product.getName()).setFont(norm))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setKeepTogether(true));
                productTable.addCell(new Cell()
                        .add(new Paragraph(String.format("%.2f", product.getRetailPrice()))
                            .setFont(norm))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setKeepTogether(true));
                productTable.addCell(new Cell()
                        .add(new Paragraph(String.format("%.0f%%", p.getDiscount() * 100))
                            .setFont(norm))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setKeepTogether(true));
                final double discountPrice = (1 - p.getDiscount()) * product.getRetailPrice();
                productTable.addCell(new Cell()
                        .add(new Paragraph(String.format("%.2f", discountPrice)).setFont(norm))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setKeepTogether(true));
                productTable.flush();

            }
            productTable.complete();
            doc.close();
            rootView.showSuccessDialog("PDF created.");

        } catch (IOException ex) {
            Logger.getLogger(CatalogController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFieldException ex) {
            rootView.showErrorDialog(ex.getMessage());
        } finally {
            if (doc != null) {
                doc.close();
            }
        }

    }
}
