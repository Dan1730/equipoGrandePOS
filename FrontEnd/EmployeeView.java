import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class EmployeeView extends JFrame {
    // Initializing frame
    private final JFrame frame;

    public EmployeeView(DatabaseInterface posDatabase) {
        // Create the main frame
        frame = new JFrame("Employee View");

        JPanel topPanel = new JPanel();
        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(2, 1));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Adding report sale button
        JButton reportSaleButton = new JButton("Report Sale");
        reportSaleButton.setBounds(0, 0, 900, 200);
        reportSaleButton.setPreferredSize(new Dimension(300, 200));
        reportSaleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DatabaseInterface db = new DatabaseInterface();
		        ReportSale window = new ReportSale(db); 
                frame.dispose();
            }
        });

        // Adding buttons to top panel
        topPanel.add(Box.createRigidArea(new Dimension(100, 500)));
        topPanel.add(reportSaleButton);
        topPanel.add(Box.createRigidArea(new Dimension(50, 50)));

        // Add logout button to bottom panel
        JPanel bottomPanel = new JPanel();
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setMaximumSize(new Dimension(100, 50));

        // If logout button is pushed, then close out of POS
        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        JButton homeButton = new JButton("Start Page");
        homeButton.setMaximumSize(new Dimension(100, 50));
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new StartPage(posDatabase);
                frame.dispose();
            }
        });

        // Adjusting logout button position
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(600, 10)));
        bottomPanel.add(homeButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        bottomPanel.add(logOutButton);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(600, 10)));

        // Adding panel and setting frame
        frame.add(mainPanel);

        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        DatabaseInterface db = new DatabaseInterface();
		EmployeeView window = new EmployeeView(db); 
    }
}
