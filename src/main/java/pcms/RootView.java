package pcms;

import java.awt.CardLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pcms.login.LoginView;

/** Root view. */
public final class RootView {
    /** List of children views. */
    public enum Views {
        MAIN_VIEW, LOGIN_VIEW
    }

    /** Main view. */
    public final MainView mainView;
    /** Login view. */
    public final LoginView loginView;

    /** Frame. */
    public final JFrame frame;
    /** Pane. */
    private final JPanel pane;
    /** Card layout. */
    private final CardLayout cardLayout;
    /** Success icon. */
    private final ImageIcon successIcon;
    /** Error icon. */
    private final ImageIcon errorIcon;

    /** Construct. */
    public RootView(final MainView mainView, final LoginView loginView) {
        successIcon = new ImageIcon(getClass().getResource("/success.png"), "Success");
        errorIcon = new ImageIcon(getClass().getResource("/error.png"), "Error");

        this.mainView = mainView;
        this.loginView = loginView;

        frame = new JFrame("PCMS");
        cardLayout = new CardLayout();
        pane = new JPanel(cardLayout);

        pane.add(mainView.pane, Views.MAIN_VIEW.name());
        pane.add(loginView.pane, Views.LOGIN_VIEW.name());
        frame.getContentPane().add(ViewUtil.createScrollPane(pane));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200, 675);
        frame.setVisible(true);
    }

    /** Show a view with given key. */
    public void render(final Views key) {
        cardLayout.show(pane, key.name());
    }

    /** Show success message. */
    public void showSuccessDialog(final String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE, 
                successIcon);
    }

    /** Show error message. */
    public void showErrorDialog(final String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE, 
                errorIcon);
    }
}
