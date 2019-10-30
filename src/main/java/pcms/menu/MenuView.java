package pcms.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pcms.user.User;

/** Menu view. */
public final class MenuView {
    /** Pane. */
    public final JPanel pane;
    /** Username label. */
    private final JLabel usernameLbl;
    /** Profile button. */
    public final JButton profileBtn;
    /** Logout button. */
    public final JButton logoutBtn;
    /** Dashboard button. */
    public final JButton dashboardBtn;
    /** User button. */
    public final JButton userBtn;
    /** Login record button. */
    public final JButton loginRecordBtn;
    /** Supplier button. */
    public final JButton supplierBtn;
    /** Category button. */
    public final JButton categoryBtn;
    /** Product button. */
    public final JButton productBtn;
    /** Catalog button. */
    public final JButton catalogBtn;

    /** Construct. */
    public MenuView() {
        pane = new JPanel();
        usernameLbl = new JLabel("james_bond");
        profileBtn = new JButton("My Profile");
        logoutBtn = new JButton("Logout");
        dashboardBtn = new JButton("Dashboard");
        userBtn = new JButton("User");
        loginRecordBtn = new JButton("Login Record");
        supplierBtn = new JButton("Supplier");
        categoryBtn = new JButton("Category");
        productBtn = new JButton("Product");
        catalogBtn = new JButton("Catalog");

        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.setMaximumSize(new Dimension(pane.getMaximumSize().width, Integer.MAX_VALUE));
        pane.setBackground(new Color(0.2f, 0.2f, 0.2f));

        pane.add(styleUsername(usernameLbl));
        pane.add(styleBtn(profileBtn));
        pane.add(styleBtn(logoutBtn));
        pane.add(Box.createRigidArea(new Dimension(0, 36)));
        pane.add(styleBtn(dashboardBtn));
        pane.add(styleBtn(userBtn));
        pane.add(styleBtn(loginRecordBtn));
        pane.add(styleBtn(supplierBtn));
        pane.add(styleBtn(categoryBtn));
        pane.add(styleBtn(productBtn));
        pane.add(styleBtn(catalogBtn));
    }

    /** Render user. */
    public void render(final User user) {
        usernameLbl.setText(user.getUsername());
        profileBtn.setActionCommand(user.getId());
    }

    /** Style username. */
    private static JLabel styleUsername(final JLabel c) {
        c.setPreferredSize(new Dimension(160, c.getPreferredSize().height));
        c.setMaximumSize(new Dimension(160, 60));
        c.setHorizontalAlignment(JLabel.CENTER);
        c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        c.setForeground(new Color(0.9f, 0.9f, 0.9f));
        return c;
    }

    /** Style button. */
    private static JButton styleBtn(final JButton c) {
        c.setPreferredSize(new Dimension(160, c.getPreferredSize().height));
        c.setMaximumSize(new Dimension(160, 36));
        c.setHorizontalAlignment(JButton.LEFT);
        c.setBackground(new Color(0.3f, 0.3f, 0.3f));
        c.setForeground(new Color(0.9f, 0.9f, 0.9f));
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0.2f, 0.2f, 0.2f)),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)));
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                c.setBackground(new Color(0.4f, 0.4f, 0.4f));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                c.setBackground(new Color(0.3f, 0.3f, 0.3f));
            }
        });
        return c;
    }
}
