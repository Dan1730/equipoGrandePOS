import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class EditProductInfo extends JFrame {

    // Initializing variables
    private final JFrame frame;

    public EditProductInfo() {
        // Create the main frame
        frame = new JFrame("Edit Product Info");

        // Creating a panel
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new GridLayout(3, 1));
        // topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        // Creating a split plane
        JSplitPane pane = new JSplitPane(SwingConstants.VERTICAL, leftPanel, rightPanel);
        pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        pane.setDividerLocation(600); // will split the pane at 200 pixels horizontally
        pane.setLeftComponent(leftPanel);
        pane.setRightComponent(rightPanel);

        // Setting orientation for slider
        pane.setOrientation(SwingConstants.VERTICAL);
        setPreferredSize(new Dimension(600, 600));

        //// RIGHT PANEL////
        JPanel subPanel1 = new JPanel();
        JPanel subPanel2 = new JPanel();
        JPanel subPanel3 = new JPanel();

        subPanel1.setLayout(new BoxLayout(subPanel1, BoxLayout.X_AXIS));
        subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
        subPanel3.setLayout(new BoxLayout(subPanel3, BoxLayout.X_AXIS));

        // Drop down components
        String[] options = { "kg", "bulk", "single" };

        JComboBox<String> comboBox = new JComboBox<>(options);

        comboBox.setBounds(0, 0, 20, 20);
        comboBox.setMaximumSize(new Dimension(100, 100));

        // Adding text fields
        JTextField prodID = new JTextField();
        prodID.setText("Product ID");

        JTextField prodName = new JTextField();
        prodName.setText("Product Name");

        JTextField sell = new JTextField();
        sell.setText("Sell Price");

        JTextField purchase = new JTextField();
        purchase.setText("Purchase Price");

        // Adding drop down to right
        // Buttons

        JButton editButton = new JButton("Edit");
        editButton.setBounds(50, 150, 20, 20);
        subPanel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(100, 150, 20, 20);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(150, 150, 20, 20);

        subPanel1.add(editButton);

        subPanel2.add(prodID);
        subPanel2.add(prodName);
        subPanel2.add(sell);
        subPanel2.add(purchase);
        subPanel2.add(comboBox);

        subPanel3.add(enterButton);
        subPanel3.add(Box.createRigidArea(new Dimension(50, 50)));
        subPanel3.add(homeButton);

        rightPanel.add(subPanel1);
        rightPanel.add(subPanel2);
        rightPanel.add(subPanel3);

        //// LEFT PANEL ///
        // Table components
        String[][] data = {
                { "1", "23" },
                { "2", "14" }
        };

        // Column Names
        String[] columnNames = { "Product ID", "Product Name", "Sell Price", "Purchase Price", "Unit" };

        // Initializing the Table
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        table.setBounds(30, 40, 200, 300);
        table.setEnabled(false);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(table);

        leftPanel.add(sp);
        // Adding panel and seeting frame size
        frame.add(pane);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // When enterButton is pressed, entry is made in table
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // // table = new JTable(1, 1);
                // // leftPanel.add(table);
                // String selected = "You selected " +
                // comboBox.getItemAt(comboBox.getSelectedIndex());
                model.addRow(
                        new Object[] {
                                prodID.getText(),
                                prodName.getText(),
                                sell.getText(),
                                purchase.getText(),
                                comboBox.getItemAt(comboBox.getSelectedIndex())
                        });
            }
        });

    }

    public static void main(String[] args) {
        new EditProductInfo();
    }
}
