package pcms.catalog;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import pcms.ViewUtil;

/** Catalog info view. */
public final class CatalogInfoView {
    /** Pane. */
    public final JPanel pane;
    /** Edit button. */
    public final JButton editBtn;
    /** Back button. */
    public final JButton backBtn;
    /** ID label. */
    private final JLabel idLbl;
    /** Banner label. */
    private final JLabel bannerLbl;
    /** Title label. */
    private final JLabel titleLbl;
    /** Description text area. */
    private final JTextArea descriptionTa;
    /** Season start date label. */
    private final JLabel seasonStartDateLbl;
    /** Season end date label. */
    private final JLabel seasonEndDateLbl;
    /** Timestamp label. */
    private final JLabel timestampLbl;
    /** User label. */
    private final JLabel userLbl;

    /** Construct. */
    public CatalogInfoView() {
        pane = ViewUtil.createContainerPane("Catalog Info");

        editBtn = new JButton("Edit");
        backBtn = new JButton("Back");
        idLbl = ViewUtil.createValueLabel();
        titleLbl = ViewUtil.createValueLabel();
        bannerLbl = ViewUtil.createFullImageLabel();
        descriptionTa = ViewUtil.createViewOnlyTextArea();
        seasonStartDateLbl = ViewUtil.createValueLabel();
        seasonEndDateLbl = ViewUtil.createUnboldLabel("");
        timestampLbl = ViewUtil.createValueLabel();
        userLbl = ViewUtil.createValueLabel();

        final String[] labels = {
                "ID", "Title", "Banner", "Description", 
                "Season Start Date", "Season End Date", "Created On", "Created By"};
        final JComponent[] components = {
                idLbl, titleLbl, bannerLbl, descriptionTa,
                seasonStartDateLbl, seasonEndDateLbl, timestampLbl, userLbl};
        
        pane.add(ViewUtil.createButtonControlPane(backBtn, editBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }
    
    /** Render. */
    public void render(final Catalog catalog) {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        
        editBtn.setActionCommand(catalog.getId());
        idLbl.setText(catalog.getId());
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(catalog.getBanner()));
        titleLbl.setText(catalog.getTitle());
        descriptionTa.setText(catalog.getDescription());
        seasonStartDateLbl.setText(catalog.getSeasonStartDate());
        seasonEndDateLbl.setText(catalog.getSeasonEndDate());
        timestampLbl.setText(formatter.format(catalog.getTimestamp()));
        userLbl.setText(catalog.getUser().getFullName());
    }
}
