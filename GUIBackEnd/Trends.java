import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.*;

public class Trends extends JFrame {

    // Initializing frame
    private final JFrame frame;

    public Trends() {
        // Create the main frame
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

        JTextField startDate = new JTextField("Start Date");
        startDate.setMaximumSize(new Dimension(200, 20));
        startDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField endDate = new JTextField("End Date");
        endDate.setMaximumSize(new Dimension(200, 20));

        JButton enterButton = new JButton("Enter");
        enterButton.setPreferredSize(new Dimension(80, 20));

        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(80, 20));

        subRightPanel1.add(Box.createRigidArea(new Dimension(50, 200)));
        subRightPanel1.add(startDate);
        subRightPanel1.add(endDate);
        subRightPanel1.add(enterButton);

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

        JButton endOfDayReport = new JButton("End of Day Report");
        endOfDayReport.setPreferredSize(new Dimension(200, 40));

        // Adding a new panel into left panel for the buttons
        JPanel subLeftPanel = new JPanel();

        subLeftPanel.add(salesReport);
        subLeftPanel.add(excessReport);
        subLeftPanel.add(restockReport);
        subLeftPanel.add(endOfDayReport);

        leftPanel.add(scroll);
        leftPanel.add(subLeftPanel);

        // Setting frame
        frame.add(splitPane);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding action listeners for the buttons
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If enter is selected, perform an action
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManagerView();
                frame.dispose();
            }
        });

        // Adding action listeners for the reports, can add to them if needed
        salesReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If sale is selected, perform an action
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
                // If restock is selected, perform an action
            }
        });

        endOfDayReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If eod is selected, perform an action
            }
        });

    }

    public static void main(String[] args) {
        new Trends();
    }
}
