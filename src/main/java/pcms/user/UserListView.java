package pcms.user;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** User list view. */
public final class UserListView {
    /** Table columns. */
    private static final String[] COLUMNS = {
            "ID", "Full name", "Email", "Admin", "PM",
            "Username", "Status", "Actions"};
    /** Table column width. */
    private static final int[] WIDTHS = {
            70, 120, 210, 60, 60,
            100, 100, 100};

    /** Pane. */
    public final JPanel pane;
    /** Table pane. */
    private final JPanel tablePane;
    /** Add button. */
    public final JButton addBtn;
    /** Search text field. */
    public final JTextField searchTf;
    /** Table header row. */
    private final JPanel headerRow;

    /** Construct. */
    public UserListView() {
        assert COLUMNS.length == WIDTHS.length;

        pane = ViewUtil.createContainerPane("User List");
        tablePane = ViewUtil.createContentPane();
        addBtn = new JButton("New");
        searchTf = ViewUtil.createTextField(20);
        headerRow = ViewUtil.createHeaderRow(COLUMNS, WIDTHS);

        pane.add(ViewUtil.createListControlPane(addBtn, searchTf));
        pane.add(tablePane);
    }

    /** Render users. */
    public void render(final List<User> users, final String search, final ActionListener goView) {
        searchTf.setText(search);
        tablePane.removeAll();
        tablePane.add(headerRow);
        for (final User u : users) {
            tablePane.add(toTableRow(u, goView));
        }
        tablePane.revalidate();
        tablePane.repaint();
    }

    /** Convert record to table row. */
    private static JPanel toTableRow(final User user, final ActionListener goView) {
        final JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(goView);
        viewBtn.setActionCommand(user.getId());

        final JPanel actionPane = ViewUtil.createHorizontalPane();
        actionPane.add(viewBtn);

        final JComponent[] components = {
                ViewUtil.createUnboldLabel(user.getId()),
                ViewUtil.createUnboldLabel(user.getFullName()),
                ViewUtil.createUnboldLabel(user.getEmail()),
                ViewUtil.createUnboldLabel(user.isAdministrator() ? "Yes" : ""),
                ViewUtil.createUnboldLabel(user.isProductManager() ? "Yes" : ""),
                ViewUtil.createUnboldLabel(user.getUsername()),
                ViewUtil.createUnboldLabel(user.isActive() ? "Active" : "Inactive"),
                actionPane
        };

        return ViewUtil.createBodyRow(components, WIDTHS);
    }

}
