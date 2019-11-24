package pcms;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Utilities for views. */
public final class ViewUtil {
    /** Prevent object creation. */
    private ViewUtil() {
        // Empty
    }

    /** Create horizontal pane. */
    public static JPanel createHorizontalPane() {
        final JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
        pane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        return pane;
    }

    /** Create container pane. */
    public static JPanel createContainerPane(final String title) {
        final JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 40));
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        final JPanel titlePane = createHorizontalPane();

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        titleLbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLbl.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        titlePane.add(titleLbl);
        pane.add(titlePane);
        return pane;
    }

    /** Create control pane. */
    public static JPanel createControlPane() {
        final JPanel pane = createHorizontalPane();
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return pane;
    }

    /** Create list's control pane. */
    public static JPanel createListControlPane(final JButton addBtn, final JTextField searchTf) {
        final JPanel pane = createControlPane();
        pane.add(addBtn);
        pane.add(Box.createHorizontalGlue());
        pane.add(new JLabel("Search: "));
        pane.add(searchTf);
        return pane;
    }

    /** Create list's control pane without button. */
    public static JPanel createListControlPane(final JTextField searchTf) {
        final JPanel pane = createControlPane();
        pane.add(Box.createHorizontalGlue());
        pane.add(new JLabel("Search: "));
        pane.add(searchTf);
        return pane;
    }

    /** Create button control pane. */
    public static JPanel createButtonControlPane(final JButton... btns) {
        final JPanel pane = createControlPane();
        for (final JButton btn : btns) {
            pane.add(btn);
            pane.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        return pane;
    }

    /** Create content pane. */
    public static JPanel createContentPane() {
        final JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        pane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        return pane;
    }

    /** Create key-value pane. */
    public static JPanel createKeyValuePane(final String[] keys, final JComponent[] values) {
        assert keys.length == values.length;

        final JPanel pane = createContentPane();
        for (int i = 0; i < keys.length; i++) {
            pane.add(createKvPair(keys[i], values[i]));
            pane.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        return pane;
    }

    /** Create key-value pair. */
    public static JPanel createKvPair(final String key, final JComponent value) {
        final JPanel pane = createHorizontalPane();

        final JLabel keyLbl = new JLabel(key);
        final Font f = keyLbl.getFont();
        keyLbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        keyLbl.setMaximumSize(new Dimension(130, keyLbl.getPreferredSize().height));
        keyLbl.setAlignmentY(JLabel.TOP_ALIGNMENT);
        keyLbl.setHorizontalAlignment(JLabel.RIGHT);

        value.setAlignmentY(JLabel.TOP_ALIGNMENT);

        pane.add(keyLbl);
        pane.add(Box.createRigidArea(new Dimension(20, 0)));
        pane.add(value);
        return pane;
    }

    /** Create value label. */
    public static JLabel createValueLabel() {
        final JLabel label = new JLabel();
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        return label;
    }

    /** Create table body row. */
    public static JPanel createBodyRow(final JComponent[] components, final int[] widths) {
        assert components.length == widths.length;

        final JPanel row = createHorizontalPane();
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        for (int i = 0; i < components.length; i++) {
            final JComponent c = components[i];
            c.setMaximumSize(new Dimension(widths[i], c.getMaximumSize().height));
            c.setPreferredSize(new Dimension(widths[i], c.getPreferredSize().height));
            row.add(c);
            row.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        row.add(Box.createRigidArea(new Dimension(0, 30)));

        return row;
    }

    /** Create table header row. */
    public static JPanel createHeaderRow(final String[] columns, final int[] widths) {
        assert columns.length == widths.length;

        final JLabel[] lbls = new JLabel[columns.length];
        for (int i = 0; i < columns.length; i++) {
            lbls[i] = new JLabel(columns[i]);
        }

        final JPanel row = createBodyRow(lbls, widths);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        return row;
    }

    /** Create unbold label. */
    public static JLabel createUnboldLabel(final String s) {
        final JLabel lbl = new JLabel(s);
        final Font f = lbl.getFont();
        lbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        return lbl;
    }
    
    /** Create icon label. */ 
    public static JLabel createIconLabel(final String s){
        try {
            BufferedImage myPicture = ImageIO.read(new File(s));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            return picLabel;
        } catch (IOException ex){}
        return new JLabel();
    }
    
    /** Create full image label. */ 
    public static ImageIcon createFullImageIcon(final String s){
        try {
            BufferedImage myPicture = ImageIO.read(new File(s));
            return new ImageIcon(myPicture);
        } catch (IOException ex){}
        return new ImageIcon();
    }

    /** Create text field. */
    public static JTextField createTextField(final int columns) {
        final JTextField tf = new JTextField(columns);
        tf.setMaximumSize(new Dimension(tf.getPreferredSize().width, 25));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 25));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0.6f, 0.6f, 0.6f)),
                BorderFactory.createEmptyBorder(0, 7, 0, 7)));
        return tf;
    }

    /** Create text area. */
    public static JTextArea createTextArea() {
        final JTextArea ta = new JTextArea();
        ta.setMaximumSize(ta.getPreferredSize());
        ta.setBorder(BorderFactory.createEmptyBorder(3, 7, 3, 7));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    /** Create text area. */
    public static JTextArea createViewOnlyTextArea() {
        final JTextArea ta = new JTextArea() {
            /** Get maximum size. */
            @Override
            public Dimension getMaximumSize() {
                final Dimension d = super.getMaximumSize();
                d.height = getPreferredSize().height;
                return d;
            }
        };
        ta.setMaximumSize(new Dimension(450, 0));
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ta.setBackground(new Color(0f, 0f, 0f, 0f));
        return ta;
    }
    
    /** Create combo box. */
    public static JComboBox createComboBox() {
        final JComboBox cob = new JComboBox();
        cob.setMaximumSize(new Dimension(300, 25));
        cob.setPreferredSize(new Dimension(cob.getPreferredSize().width, 25));
        cob.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0.6f, 0.6f, 0.6f)),
                BorderFactory.createEmptyBorder(0, 7, 0, 7)));
        return cob;
    }

    /** Create centered input label. */
    public static JLabel createCenteredInputLabel(final String s) {
        final JLabel lbl = new JLabel(s);
        lbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(200, lbl.getMaximumSize().height));
        return lbl;
    }

    /** Create scroll pane. */
    public static JScrollPane createScrollPane(final JComponent component) {
        final JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return scrollPane;
    }

    /** Create scroll pane with width and height. */
    public static JScrollPane createScrollPane(
            final JComponent component, final int width, final int height) {
        final JScrollPane scrollPane = createScrollPane(component);
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.6f, 0.6f, 0.6f)));
        return scrollPane;
    }

    /** Style centered input. */
    private static void styleCenteredInput(final JComponent input) {
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0.6f, 0.6f, 0.6f)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        input.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        input.setMaximumSize(new Dimension(200, 30));
        input.setPreferredSize(new Dimension(200, 30));
    }

    /** Create centered input. */
    public static JTextField createCenteredInput() {
        final JTextField tf = new JTextField();
        styleCenteredInput(tf);
        return tf;
    }

    /** Create centered password input. */
    public static JPasswordField createCenteredPasswordInput() {
        final JPasswordField pf = new JPasswordField();
        styleCenteredInput(pf);
        return pf;
    }

    /** Create variable size card layout. */
    public static CardLayout createVariableSizeCardLayout() {
        return new CardLayout() {
            /** Preferred layout size. */
            @Override
            public Dimension preferredLayoutSize(final Container parent) {
                return Arrays.stream(parent.getComponents())
                        .filter(x -> x.isVisible())
                        .findFirst()
                        .map(x -> {
                            final Insets insets = parent.getInsets();
                            final Dimension pref = x.getPreferredSize();
                            pref.width += insets.left + insets.right;
                            pref.height += insets.top + insets.bottom;
                            return pref; })
                        .orElse(super.preferredLayoutSize(parent));
            }
        };
    }
}
