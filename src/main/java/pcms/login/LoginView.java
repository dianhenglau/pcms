package pcms.login;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import pcms.ViewUtil;

/** Login view. */
public final class LoginView {
    /** Pane. */
    public final JPanel pane;
    /** Username text field. */
    public final JTextField usernameTf;
    /** Password text field. */
    public final JPasswordField passwordPf;
    /** Login button. */
    public final JButton loginBtn;

    /** Construct. */
    public LoginView() {
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        
        final JLabel title = new JLabel("𝓟𝓒𝓜𝓢");
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 64));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        final JLabel subtitle = new JLabel("Product Catalog Management System");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        final JLabel usernameLbl = ViewUtil.createCenteredInputLabel("Username");
        usernameTf = ViewUtil.createCenteredInput();
        final JLabel passwordLbl = ViewUtil.createCenteredInputLabel("Password");
        passwordPf = ViewUtil.createCenteredPasswordInput();

        loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);

        pane.add(Box.createVerticalGlue());
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0, 20)));
        pane.add(subtitle);
        pane.add(Box.createRigidArea(new Dimension(0, 30)));
        pane.add(usernameLbl);
        pane.add(usernameTf);
        pane.add(Box.createRigidArea(new Dimension(0, 10)));
        pane.add(passwordLbl);
        pane.add(passwordPf);
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(loginBtn);
        pane.add(Box.createVerticalGlue());
    }

    /** Render. */
    public void render() {
        usernameTf.setText("");
        passwordPf.setText("");
    }
}
