import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.*;

public class ManagerView extends JFrame {
    // Initializing frame
    private final JFrame frame;

    public ManagerView() {
        // Create the main frame
        frame = new JFrame("Manager View");

        JPanel topPanel = new JPanel();
        JPanel mainPanel = (JPanel) getContentPane();

        // Adding 4 main buttons
        JButton editProductButton = new JButton("Edit Product Info");
        editProductButton.setPreferredSize(new Dimension(200, 200));
        editProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new EditProductInfo();
                frame.dispose();
            }
        });

        JButton trendsButton = new JButton("View Trends");
        trendsButton.setPreferredSize(new Dimension(200, 200));
        trendsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new Trends();
                frame.dispose();
            }
        });

        JButton vendorButton = new JButton("Vendor Orders");
        vendorButton.setPreferredSize(new Dimension(200, 200));
        vendorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // vendor class instantiated
            }
        });

        JButton editInventoryButton = new JButton("Edit Inventory");
        editInventoryButton.setPreferredSize(new Dimension(200, 200));
        editInventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DatabaseInterface posDatabase = new DatabaseInterface();
                new EditInventory(posDatabase);
                CurrentInventory inventoryBackend = new CurrentInventory(posDatabase);
                frame.dispose();
            }
        });

        JButton reportSaleButton = new JButton("Make a Sale");
        reportSaleButton.setPreferredSize(new Dimension(200, 200));
        reportSaleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DatabaseInterface db = new DatabaseInterface();
		        ReportSale window = new ReportSale(db); 
                frame.dispose();
            }
        });

        // Adding buttons to panel
        topPanel.setLayout(new GridLayout(3,2, 10, 10));
        topPanel.add(editProductButton);
        topPanel.add(trendsButton);
        topPanel.add(vendorButton);
        topPanel.add(editInventoryButton);
        topPanel.add(reportSaleButton);
        topPanel.add(Box.createRigidArea(new Dimension(50, 20)));
        topPanel.add(Box.createRigidArea(new Dimension(50, 20)));


        JPanel bottomPanel = new JPanel();
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setMaximumSize(new Dimension(100, 80));
        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(600, 10)));
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
        new ManagerView();
    }
}
