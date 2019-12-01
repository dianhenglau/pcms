package pcms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DatePicker {
    
    private int month = Calendar.getInstance().get(Calendar.MONTH);
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    
    String day = "";
    
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Calendar calendar = Calendar.getInstance();
    
    private final JButton[] gridBtn = new JButton[49];
    private final JLabel monthLbl = new JLabel("", JLabel.CENTER);
    
    /** Constructor. */
    public DatePicker(JFrame parent){
        final JDialog dialog = new JDialog();
        dialog.setModal(true);

        String[] dayHeader = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };

        /** Create date section in calendar. */
        JPanel dateSection = new JPanel(new GridLayout(7, 7));
        dateSection.setPreferredSize(new Dimension(430, 120));

        /** Set select date mechanism. */
        for (int x = 0; x < gridBtn.length; x++) 
        {		
                final int selection = x;
                gridBtn[x] = new JButton();
                gridBtn[x].setFocusPainted(false);
                gridBtn[x].setBackground(Color.white);

                if (x > 6){
                    gridBtn[x].addActionListener((ActionEvent ae) -> {
                        day = gridBtn[selection].getActionCommand();
                        dialog.dispose();
                    });
                }

                /** Set day header. */
                else if (x < 7){
                        gridBtn[x].setText(dayHeader[x]);
                        gridBtn[x].setForeground(Color.red);
                }

                dateSection.add(gridBtn[x]);
        }

        /** Create button section. */
        JPanel buttonSection = new JPanel(new GridLayout(1, 3));

        JButton previousBtn = new JButton("<< Previous");
        previousBtn.addActionListener((ActionEvent ae) -> {
            month--;
            displayDate();
        });

        JButton nextBtn = new JButton("Next >>");
        nextBtn.addActionListener((ActionEvent ae) -> {
            month++;
            displayDate();
        });

        buttonSection.add(previousBtn);
        buttonSection.add(monthLbl);
        buttonSection.add(nextBtn);

        /** Add both sections together and display date. */
        dialog.add(dateSection, BorderLayout.CENTER);
        dialog.add(buttonSection, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle("Date Picker");
        displayDate();
        dialog.setVisible(true);
    }

    public final void displayDate() 
    {
            for (int x = 7; x < gridBtn.length; x++){
                gridBtn[x].setText("");
            }
            
            			
            calendar.set(year, month, 1);
            int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            int daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            for (int x = 6 + dayOfWeek, days = 1; days <= daysInMonth; x++, days++){
                gridBtn[x].setText("" + days);
            }
            
            monthLbl.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public String setPickedDate() 
    {
            if (day.equals(""))
                    return day;
            calendar.set(year, month, Integer.parseInt(day));
            return simpleDateFormat.format(calendar.getTime());
    }
}
    

