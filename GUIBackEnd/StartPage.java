import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

/**
 * @author Juliana Leano
 */

public class StartPage extends JFrame {

    public StartPage(DatabaseInterface posDatabase) {
        // Create the main frame
        JFrame frame = new JFrame("Start Page");

        // Adding make a sale button
        JButton employeeButton = new JButton("Go to the Employee View");
        employeeButton.setPreferredSize(new Dimension(200, 200));
        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                CheckCashierLogin(frame, posDatabase);
            }
        });

        JButton managerButton = new JButton("Go to the Manager View");
        managerButton.setPreferredSize(new Dimension(200, 200));
        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                CheckManagerLogin(frame, posDatabase);
            }
        });

        // Adding buttons to panel
        JPanel topPanel = new JPanel();

        // manually centering the EmployeeButton and ManagerButton with space on above and below
        topPanel.setLayout(new GridLayout(4, 3, 10, 10));
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(employeeButton);
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(managerButton);
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        topPanel.add(Box.createRigidArea(new Dimension(10, 10)));


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

    // function to check the login and password of the manager
    public void CheckManagerLogin(JFrame frame, DatabaseInterface posDatabase) { // posDatabase as parameter?
        JPanel managerPanel = new JPanel();
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        usernameField.setPreferredSize(new Dimension(300, 20));

        managerPanel.add(new JLabel("Username: "));
        managerPanel.add(usernameField);
        managerPanel.add(new JLabel("Password: "));
        managerPanel.add(passwordField);
        managerPanel.setPreferredSize(new Dimension(300, 50));

        int passwordPopup = JOptionPane.showConfirmDialog(frame, managerPanel, "Enter Manager Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // after the OK option in the popup is selection, check the username and password from the database
        if (passwordPopup == JOptionPane.OK_OPTION) {
            String usernameStr = new String(usernameField.getText());    
            String passwordStr = new String(passwordField.getPassword());
            Boolean loginWorked = false;
            Boolean isCashier = false;
            
            // connect to database, get userinfo table
            String[][] userPasswordMatrix = posDatabase.getStringMatrix("userinformation", "username", "password", "role");
            // does username exist in database and does password match username?
            for(int row = 0; row < userPasswordMatrix.length; row++) {
                if (userPasswordMatrix[row][0].equals(usernameStr) && userPasswordMatrix[row][2].equals("Cashier")) {
                    isCashier = true;
                }
                if (userPasswordMatrix[row][0].equals(usernameStr) && userPasswordMatrix[row][1].equals(passwordStr) && userPasswordMatrix[row][2].equals("Manager")) {
                    loginWorked = true;
                    new ManagerView(posDatabase);
                    frame.dispose();
                }

            }
            // incorrect username/password, show incorrect popup and go back to start page
            if(!loginWorked) {
                if(isCashier) {
                    JOptionPane.showMessageDialog(frame, "Permission Denied", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Incorrect Login. Please Try Again", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // function to check the login and password of the cashier, managers can also log into the employee view
    public void CheckCashierLogin(JFrame frame, DatabaseInterface posDatabase) { // posDatabase as parameter?
        JPanel managerPanel = new JPanel();
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        usernameField.setPreferredSize(new Dimension(300, 20));

        managerPanel.add(new JLabel("Username: "));
        managerPanel.add(usernameField);
        managerPanel.add(new JLabel("Password: "));
        managerPanel.add(passwordField);
        managerPanel.setPreferredSize(new Dimension(300, 50));

        int passwordPopup = JOptionPane.showConfirmDialog(frame, managerPanel, "Enter Cashier Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // after the OK option in the popup is selection, check the username and password from the database
        if (passwordPopup == JOptionPane.OK_OPTION) {
            String usernameStr = new String(usernameField.getText());    
            String passwordStr = new String(passwordField.getPassword());
            Boolean loginWorked = false;
            
            // connect to database, get userinfo table
            String[][] userPasswordMatrix = posDatabase.getStringMatrix("userinformation", "username", "password", "role");
            //    - does username exist in database and does password match username?
            for(int row = 0; row < userPasswordMatrix.length; row++) {
                if (userPasswordMatrix[row][0].equals(usernameStr) && userPasswordMatrix[row][1].equals(passwordStr)) {
                    loginWorked = true;
                    new EmployeeView(posDatabase);
                    frame.dispose();
                }
            }
            // incorrect username/password, show incorrect popup and go back to start page
            if(!loginWorked) {
                JOptionPane.showMessageDialog(frame, "Incorrect Login. Please Try Again", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();
        new StartPage(posDatabase);

    }
}
