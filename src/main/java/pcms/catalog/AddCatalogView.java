package pcms.catalog;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Add catalog view. */
public final class AddCatalogView {
    /** Pane. */
    public final JPanel pane;
    /** Cancel button. */
    public final JButton cancelBtn;
    /** Save button. */
    public final JButton saveBtn;
    /** Banner button. */
    public final JButton bannerBtn;
    /** Banner label. */
    public final JLabel bannerLbl;
    /** Banner file path label. */
    public final JLabel filenameLbl;
    /** Title text field. */
    public final JTextField titleTf;
    /** Description text area. */
    public final JTextArea descriptionTa;
    /** Product label. */
    /** Season start date label. */
    public final JLabel seasonStartDateLbl;
    /** Display start date text field. */
    public final JLabel displayStartDateLbl;
    /** Select start date button */
    public final JButton selectStartDateBtn;
    /** Season end date label. */
    public final JLabel seasonEndDateLbl;
    /** Display end date label. */
    public final JLabel displayEndDateLbl;
    /** Select end date button */
    public final JButton selectEndDateBtn;
    
    /** Construct. */
    public AddCatalogView() {
        pane = ViewUtil.createContainerPane("Add Catalog");

        cancelBtn = new JButton("Cancel");
        saveBtn = new JButton("Save");
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

        final JPanel bannerPane = ViewUtil.createImagePane(bannerLbl, filenameLbl, bannerBtn);
        final JPanel seasonStartDatePane = ViewUtil.createSelectDatePane(
               seasonStartDateLbl, displayStartDateLbl, selectStartDateBtn);
        final JPanel seasonEndDatePane = ViewUtil.createSelectDatePane(
               seasonEndDateLbl, displayEndDateLbl, selectEndDateBtn);

        final String[] labels = {
                "Title", "Banner", "Description", "Season Start Date", "Season End Date"};
        final JComponent[] components = {
                titleTf, bannerPane, ViewUtil.createScrollPane(descriptionTa, 450, 100),
                seasonStartDatePane, seasonEndDatePane};

        pane.add(ViewUtil.createButtonControlPane(cancelBtn, saveBtn));
        pane.add(ViewUtil.createKeyValuePane(labels, components));
    }

    /** Render. */
    public void render() {
        titleTf.setText("");
        bannerLbl.setIcon(null);
        filenameLbl.setText("");
        descriptionTa.setText("");
        displayStartDateLbl.setText("");
        displayEndDateLbl.setText("");
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
