package pcms.loginrecord;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** LoginRecord list view. */
public final class LoginRecordListView {
    /** Table columns. */
    private static final String[] COLUMNS = {"ID", "User ID", "Username", "Action", "Timestamp"};
    /** Table column width. */
    private static final int[] WIDTHS = {70, 70, 150, 100, 200};

    /** Pane. */
    public final JPanel pane;
    /** Table pane. */
    private final JPanel tablePane;
    /** Search text field. */
    public final JTextField searchTf;
    /** Table header row. */
    private final JPanel headerRow;

    /** Construct. */
    public LoginRecordListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("Login Record List");
        tablePane = ViewUtil.createContentPane();
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(searchTf));
        pane.add(tablePane);
    }

    /** Render loginRecords. */
    public void render(final List<LoginRecord> loginRecords) {
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final LoginRecord u : loginRecords) {
            tablePane.add(toTableRow(u));
        }
        tablePane.revalidate();
        tablePane.repaint();
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(final LoginRecord loginRecord) {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(loginRecord.getId()),
                ViewUtil.createUnboldLabel(loginRecord.getUserId()),
                ViewUtil.createUnboldLabel(loginRecord.getUser().getUsername()),
                ViewUtil.createUnboldLabel(loginRecord.getAction().toString()),
                ViewUtil.createUnboldLabel(formatter.format(loginRecord.getTimestamp()))};

        return ViewUtil.createBodyRow(components, WIDTHS);
    }

}
