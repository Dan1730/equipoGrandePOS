import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.*;

/**
 * @author Caroline Mejia
 */

public class Trends extends JFrame {

    // Initializing frame
    DatabaseInterface posDatabase;
    SalesReport trendsReport;
    ProductPairs productPairClass;
    private final JFrame frame;
    

    public Trends(DatabaseInterface db) {
        // Create the main frame
        posDatabase = db;
        trendsReport = new SalesReport(posDatabase);
        productPairClass = new ProductPairs(posDatabase);

        frame = new JFrame("Trends & Analytics");

        // Creating panels
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setLayout(new GridLayout(2, 1));
        rightPanel.setLayout(new GridLayout(2, 1));

        // Creating a split pane
        JSplitPane splitPane = new JSplitPane(SwingConstants.VERTICAL, leftPanel, rightPanel);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(590); // will split the pane at 200 pixels horizontally
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Setting orientation for slider
        splitPane.setOrientation(SwingConstants.VERTICAL);
        setPreferredSize(new Dimension(600, 600));

        /// RIGHT PANEL///
        // Adding elements to the right panel
        JPanel subRightPanel1 = new JPanel();
        JPanel subRightPanel2 = new JPanel();
        subRightPanel1.setLayout(new BoxLayout(subRightPanel1, BoxLayout.Y_AXIS));
        subRightPanel2.setLayout(new BoxLayout(subRightPanel2, BoxLayout.X_AXIS));

        JTextField startDate = new JTextField();
        startDate.setMaximumSize(new Dimension(200, 20));
        startDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField endDate = new JTextField();
        endDate.setMaximumSize(new Dimension(200, 20));
        
        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(80, 20));

        JLabel startDateLabel = new JLabel("Start Date: ");
        JLabel endDateLabel = new JLabel("End Date: ");

        subRightPanel1.add(Box.createRigidArea(new Dimension(50, 200)));
        subRightPanel1.add(startDateLabel);
        subRightPanel1.add(startDate);
        subRightPanel1.add(endDateLabel);
        subRightPanel1.add(endDate);

        subRightPanel2.add(Box.createRigidArea(new Dimension(50, 50)));
        subRightPanel2.add(homeButton);

        rightPanel.add(subRightPanel1);
        rightPanel.add(subRightPanel2);

        /// LEFT PANEL///
        // Adding elements to the left panel
        DefaultTableModel model = new DefaultTableModel();

        JTable table = new JTable(model);

        // You can use the lines below to set the column names
        // String[] columnNames = { "Product ID", "Product Name", "Sell Price",
        // "Purchase Price", "Unit" };
        // model.setColumnIdentifiers(columnNames);

        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);

        JButton salesReport = new JButton("Sales Report");
        salesReport.setPreferredSize(new Dimension(200, 40));

        JButton excessReport = new JButton("Excess Report");
        excessReport.setPreferredSize(new Dimension(200, 40));

        JButton restockReport = new JButton("Restock Report");
        restockReport.setPreferredSize(new Dimension(200, 40));

        JButton endOfDayReport = new JButton("Product Pairs Report");
        endOfDayReport.setPreferredSize(new Dimension(200, 40));

        // Adding a new panel into left panel for the buttons
        JPanel subLeftPanel = new JPanel();

        
        leftPanel.add(scroll);
        leftPanel.add(subLeftPanel);

        subLeftPanel.add(salesReport);
        subLeftPanel.add(excessReport);
        subLeftPanel.add(restockReport);
        subLeftPanel.add(endOfDayReport);

   

        // Setting frame
        frame.add(splitPane);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManagerView(posDatabase);
                frame.dispose();
            }
        });

        // Adding action listeners for the reports, can add to them if needed
        salesReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] columnNames = {"Product", "Quantity Sold", "Revenue", "Cost", "Net Profit"};
                String startDateString = startDate.getText();
                String endDateString = endDate.getText();

                String[][] salesReportMatrix = trendsReport.generateReport(startDateString, endDateString);

                for(int i = 0; i < salesReportMatrix.length; i++){
                    salesReportMatrix[i][1] = String.format("%.2f",Float.parseFloat(salesReportMatrix[i][1]));
                    salesReportMatrix[i][2] = String.format("%.2f",Float.parseFloat(salesReportMatrix[i][2]));
                    salesReportMatrix[i][3] = String.format("%.2f",Float.parseFloat(salesReportMatrix[i][3]));
                    salesReportMatrix[i][4] = String.format("%.2f",Float.parseFloat(salesReportMatrix[i][4]));
                }

                model.setDataVector(salesReportMatrix, columnNames);
            }
        });

        excessReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If excess is selected, perform an action
            }
        });

        restockReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] columnNames = {"Product", "Quantity In-Stock", "Quanity Sold", "Revenue"};
                
                String startDateString = startDate.getText();
                String endDateString = endDate.getText();

                String[][] restockReportMatrix = trendsReport.generateRestockReport(startDateString, endDateString);

                for(int i = 0; i < restockReportMatrix.length; i++){
                    restockReportMatrix[i][1] = String.format("%.2f",Float.parseFloat(restockReportMatrix[i][1]));
                    restockReportMatrix[i][2] = String.format("%.2f",Float.parseFloat(restockReportMatrix[i][2]));
                    restockReportMatrix[i][3] = String.format("%.2f",Float.parseFloat(restockReportMatrix[i][3]));
                }

                model.setDataVector(restockReportMatrix, columnNames);
            }
        });

        endOfDayReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] columnNames = {"Product 1", "Product 2", "Pair Sales"};
                String startDateString = startDate.getText();
                String endDateString = endDate.getText();

                // Convert Product IDs to Product Names for display
                String[][] pairs = productPairClass.GetBestPairs(startDateString, endDateString);
                for(int i = 0; i < pairs.length; i++){
                    pairs[i][0] = posDatabase.GetAttribute("product", "productName", pairs[i][0]);
                    pairs[i][1] = posDatabase.GetAttribute("product", "productName", pairs[i][1]);
                }

                model.setDataVector(pairs, columnNames);
            }
        });

    }

    public static void main(String[] args) {
        new Trends(new DatabaseInterface());
    }
}
