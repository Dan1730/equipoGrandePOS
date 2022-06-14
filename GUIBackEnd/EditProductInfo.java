import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class EditProductInfo extends JFrame {

    // Initializing variables
    private final JFrame frame;
    private DatabaseInterface dbInterface;
    private CurrentProducts currentProducs;

    public EditProductInfo() {
        // Create the main frame
        frame = new JFrame("Edit Product Info");

        dbInterface = new DatabaseInterface();
        currentProducs = new CurrentProducts(dbInterface);

        // Creating a panel
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new GridLayout(3, 1));

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
        Unit[] options = {Unit.items, Unit.kgs};

        JComboBox<Unit> comboBox = new JComboBox<>(options);

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
        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(50, 150, 20, 20);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(100, 150, 20, 20);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(150, 150, 20, 20);

        subPanel1.add(Box.createRigidArea(new Dimension(50, 50)));
        subPanel1.add(removeButton);

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
        String[][] data = currentProducs.getProductMatrix();

        // Column Names
        String[] columnNames = { "Product ID", "Product Name", "Sell Price", "Purchase Price", "Unit" };

        // Initializing the Table
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        JTable table = new JTable(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setBounds(30, 40, 200, 300);
        // table.setEnabled(false);

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
                // model.addRow(
                //         new Object[] {
                //                 prodID.getText(),
                //                 prodName.getText(),
                //                 sell.getText(),
                //                 purchase.getText(),
                //                 comboBox.getItemAt(comboBox.getSelectedIndex())
                //         });

                currentProducs.AddProductToProducts(prodID.getText(), prodName.getText(), sell.getText(), purchase.getText(), comboBox.getItemAt(comboBox.getSelectedIndex()));
                String[][] updatedData = currentProducs.getProductMatrix();
                model.setDataVector(updatedData, columnNames);
            }
        });

    }
    
    // When enterButton is pressed, entry is made in table
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent r) {
                if (table.getSelectedRow() != -1) {
                    // remove the selected row from the model
                    model.removeRow(table.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Row successfully removed");
                }
            }
        });

    public static void main(String[] args) {
        new EditProductInfo();
    }
}
