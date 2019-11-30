package pcms.catalog;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Edit catalog view. */
public final class EditCatalogView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** ID label. */
    public final JLabel idLbl;
    /** Banner button. */
    public final JButton bannerBtn;
    /** Banner label. */
    public final JLabel bannerLbl;
    /** Banner filename label. */
    public final JLabel filenameLbl;
    /** Title text field. */
    public final JTextField titleTf;
    /** Description text area. */
    public final JTextArea descriptionTa;
    /** Season start date label. */
    public final JLabel seasonStartDateLbl;
    /** Display start date text field. */
    public final JLabel displayStartDateLbl;
    /** Select start date button. */
    public final JButton selectStartDateBtn;
    /** Season end date label. */
    public final JLabel seasonEndDateLbl;
    /** Display end date text field. */
    public final JLabel displayEndDateLbl;
    /** Select end date button. */
    public final JButton selectEndDateBtn;
    /** Timestamp label. */
    public final JLabel timestampLbl;
    /** User label. */
    public final JLabel userLbl;

    /** Construct. */
    public EditCatalogView() {
        pane = ViewUtil.createContainerPane("Edit Catalog");
        
        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
        idLbl = ViewUtil.createValueLabel();
        titleTf = ViewUtil.createTextField(25);
        bannerLbl = ViewUtil.createFullImageLabel();
        filenameLbl = ViewUtil.createUnboldLabel("");
        bannerBtn = new JButton("Add Image");
        descriptionTa = ViewUtil.createTextArea();
        seasonStartDateLbl = ViewUtil.createUnboldLabel("");
        displayStartDateLbl = ViewUtil.createUnboldLabel("");
        selectStartDateBtn = new JButton("Select Date");
        seasonEndDateLbl = ViewUtil.createUnboldLabel("");
        displayEndDateLbl = ViewUtil.createUnboldLabel("");
        selectEndDateBtn = new JButton("Select Date");
        timestampLbl = ViewUtil.createValueLabel();
        userLbl = ViewUtil.createValueLabel();

        final JPanel bannerPane = ViewUtil.createImagePane(bannerLbl, filenameLbl, bannerBtn);
        final JPanel seasonStartDatePane = ViewUtil.createSelectDatePane(
               seasonStartDateLbl, displayStartDateLbl, selectStartDateBtn);
        final JPanel seasonEndDatePane = ViewUtil.createSelectDatePane(
               seasonEndDateLbl, displayEndDateLbl, selectEndDateBtn);

        final String[] labels = {
                "ID", "Title", "Banner", "Description", 
                "Season Start Date", "Season End Date", "Created On", "Created By"};
        final JComponent[] components = {
                idLbl, titleTf, bannerPane, ViewUtil.createScrollPane(descriptionTa, 450, 100),
                seasonStartDatePane, seasonEndDatePane, timestampLbl, userLbl};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render(final Catalog catalog) {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        
        saveBtn.setActionCommand(catalog.getId());
        cancelBtn.setActionCommand(catalog.getId());
        idLbl.setText(catalog.getId());
        titleTf.setText(catalog.getTitle());
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(catalog.getBanner()));
        filenameLbl.setText(catalog.getBanner());
        descriptionTa.setText(catalog.getDescription());
        displayStartDateLbl.setText(catalog.getSeasonStartDate());
        displayEndDateLbl.setText(catalog.getSeasonEndDate());
        timestampLbl.setText(formatter.format(catalog.getTimestamp()));
        userLbl.setText(catalog.getUser().getFullName());
    }

    /** Render image. */
    public void renderImage(final String imagePath) {
        bannerLbl.setIcon(ViewUtil.createFullImageIcon(imagePath));
        filenameLbl.setText(imagePath);
    }
    
     /** Render season start date. */
    public void renderStartDate(final String date) {
        displayStartDateLbl.setText(date);
    }
    
    /** Render season end date. */
    public void renderEndDate(final String date) {
        displayEndDateLbl.setText(date);
    }
}
