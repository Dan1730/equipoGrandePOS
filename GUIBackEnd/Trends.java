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
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

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
        JTextField startDate = new JTextField("Start Date");
        startDate.setMaximumSize(new Dimension(200, 20));

        JTextField endDate = new JTextField("End Date");
        endDate.setMaximumSize(new Dimension(200, 20));

        JButton enterButton = new JButton("Enter");
        enterButton.setMaximumSize(new Dimension(80, 20));

        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(80, 20));

        rightPanel.add(startDate);
        rightPanel.add(endDate);
        rightPanel.add(enterButton);
        rightPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        rightPanel.add(homeButton);
        rightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        /// LEFT PANEL///
        // Adding elements to the left panel
        JTable table = new JTable();
        table.setEnabled(false);

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

        leftPanel.add(table);
        leftPanel.add(subLeftPanel);

        // Setting frame
        frame.add(splitPane);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Trends();
    }
}
