package pcms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Date picker class. */
public final class DatePicker {
    /** Month. */
    private int month = Calendar.getInstance().get(Calendar.MONTH);
    /** Year. */
    private final int year = Calendar.getInstance().get(Calendar.YEAR);
    /** Day. */
    private String day = "";

    /** Date format. */
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    /** Calendar. */
    private final Calendar calendar = Calendar.getInstance();

    /** Grid of buttons. */
    private final JButton[] gridBtn = new JButton[49];
    /** Month label. */
    private final JLabel monthLbl = new JLabel("", JLabel.CENTER);

    /** Constructor. */
    public DatePicker(final JFrame parent) {
        final JDialog dialog = new JDialog();
        dialog.setModal(true);

        final String[] dayHeader = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };

        // Create date section in calendar
        final JPanel dateSection = new JPanel(new GridLayout(7, 7));
        dateSection.setPreferredSize(new Dimension(430, 120));

        // Set select date mechanism
        for (int x = 0; x < gridBtn.length; x++) {        
            final int selection = x;
            gridBtn[x] = new JButton();
            gridBtn[x].setFocusPainted(false);
            gridBtn[x].setBackground(Color.white);

            if (x > 6) {
                gridBtn[x].addActionListener(e -> {
                    day = gridBtn[selection].getActionCommand();
                    dialog.dispose();
                });
            }

            // Set day header
            if (x < 7) {
                gridBtn[x].setText(dayHeader[x]);
                gridBtn[x].setForeground(Color.red);
            }

            dateSection.add(gridBtn[x]);
        }

        // Create button section
        final JPanel buttonSection = new JPanel(new GridLayout(1, 3));

        final JButton previousBtn = new JButton("<< Previous");
        previousBtn.addActionListener(e -> {
            month--;
            displayDate();
        });

        final JButton nextBtn = new JButton("Next >>");
        nextBtn.addActionListener(e -> {
            month++;
            displayDate();
        });

        buttonSection.add(previousBtn);
        buttonSection.add(monthLbl);
        buttonSection.add(nextBtn);

        // Add both sections together and display date.
        dialog.add(dateSection, BorderLayout.CENTER);
        dialog.add(buttonSection, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle("Date Picker");
        displayDate();
        dialog.setVisible(true);
    }

    /** Display date. */
    public void displayDate() {
        for (int x = 7; x < gridBtn.length; x++) {
            gridBtn[x].setText("");
        }

        calendar.set(year, month, 1);
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
            gridBtn[x].setText(Integer.toString(day));
        }

        monthLbl.setText(simpleDateFormat.format(calendar.getTime()));
    }

    /** Set picked date. */
    public String setPickedDate() { // NOPMD - Let it return value
        if ("".equals(day)) {
            return day;
        }
        calendar.set(year, month, Integer.parseInt(day));
        return simpleDateFormat.format(calendar.getTime());
    }
}


