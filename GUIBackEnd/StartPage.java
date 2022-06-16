import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class StartPage extends JFrame {
    // Initializing frame
    private final JFrame frame;

    public StartPage() {
        // Create the main frame
        frame = new JFrame("Start Page");

        // Adding make a sale button
        JButton employeeButton = new JButton("Go to the Employee View");
        employeeButton.setPreferredSize(new Dimension(200, 200));
        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new EmployeeView();
                frame.dispose();
            }
        });

        JButton managerButton = new JButton("Go to the Manager View");
        managerButton.setPreferredSize(new Dimension(200, 200));
        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
               new ManagerView();
               frame.dispose();
            }
        });

        // Adding buttons to panel
        JPanel topPanel = new JPanel();

        // manually centering the EmployeeButton and ManagerButton with space on above and below
        topPanel.setLayout(new GridLayout(4, 3, 10, 10));
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));

        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(employeeButton);
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));

        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(managerButton);
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));

        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        topPanel.add(Box.createRigidArea(new Dimension(100, 100)));


        JPanel bottomPanel = new JPanel();
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setMaximumSize(new Dimension(100, 50));
        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(600, 10)));
        bottomPanel.add(logOutButton);

        JPanel mainPanel = (JPanel) getContentPane();

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
        new StartPage();
    }
}
