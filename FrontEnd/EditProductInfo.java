import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

/**
 * @author Caroline Mejia
 */

public class EditProductInfo extends JFrame {

    // Initializing variables
    private final JFrame frame;
    private DatabaseInterface dbInterface;
    private CurrentProducts currentProducts;

    /**
     * Class constructor that connects to a given database to edit the information
     * of products
     */

    public EditProductInfo(DatabaseInterface posDatabase) {
        // Create the main frame
        frame = new JFrame("Edit Product Info");

        dbInterface = new DatabaseInterface();
        currentProducts = new CurrentProducts(dbInterface);

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
        Unit[] options = { Unit.items, Unit.kgs };

        JComboBox<Unit> comboBox = new JComboBox<>(options);

        comboBox.setBounds(0, 0, 20, 20);
        comboBox.setMaximumSize(new Dimension(100, 100));

        // Adding text fields
        JTextField prodID = new JTextField();
        JTextField prodName = new JTextField();
        JTextField sell = new JTextField();
        JTextField purchase = new JTextField();

        // Adding labels for text fields
        JLabel prodIDLabel = new JLabel("Product ID:");
        JLabel prodNameLabel = new JLabel("Product Name:");
        JLabel sellLabel = new JLabel("Sell Price:");
        JLabel purchaseLabel = new JLabel("Purchase Price:");

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

        subPanel2.add(prodIDLabel);
        subPanel2.add(prodID);
        subPanel2.add(prodNameLabel);
        subPanel2.add(prodName);
        subPanel2.add(sellLabel);
        subPanel2.add(sell);
        subPanel2.add(purchaseLabel);
        subPanel2.add(purchase);
        subPanel2.add(Box.createRigidArea(new Dimension(20, 20)));
        subPanel2.add(comboBox);

        subPanel3.add(enterButton);
        subPanel3.add(Box.createRigidArea(new Dimension(50, 50)));
        subPanel3.add(homeButton);

        rightPanel.add(subPanel1);
        rightPanel.add(subPanel2);
        rightPanel.add(subPanel3);

        //// LEFT PANEL ///
        // Table components
        String[][] data = currentProducts.getProductMatrix();
        for (String[] row : data) {
            row[4] = Unit.StringIntToString(row[4]);
        }

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
        // Adding panel and setting frame size
        frame.add(pane);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Populate text fields when row is selected
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() != -1) {
                    prodID.setText((table.getValueAt(table.getSelectedRow(), 0).toString()));
                    prodName.setText((table.getValueAt(table.getSelectedRow(), 1).toString()));
                    sell.setText((table.getValueAt(table.getSelectedRow(), 2).toString()));
                    purchase.setText((table.getValueAt(table.getSelectedRow(), 3).toString()));
                    comboBox.setSelectedItem(Unit.StringToUnit(table.getValueAt(table.getSelectedRow(), 4).toString()));
                }
            }
        });

        // When enterButton is pressed, entry is made in table
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                currentProducts.AddOrEditProduct(prodID.getText(), prodName.getText(), sell.getText(),
                        purchase.getText(), comboBox.getItemAt(comboBox.getSelectedIndex()));
                String[][] updatedData = currentProducts.getProductMatrix();
                for (String[] row : updatedData) {
                    row[4] = Unit.StringIntToString(row[4]);
                }
                model.setDataVector(updatedData, columnNames);
            }
        });

        // When removeButton is pressed, entry is removed from table
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent r) {
                if (table.getSelectedRow() != -1) {

                    String PIDToRemove = (String) table.getValueAt(table.getSelectedRow(), 0);

                    currentProducts.RemoveProductFromProducts(PIDToRemove);
                    String[][] updatedData = currentProducts.getProductMatrix();
                    model.setDataVector(updatedData, columnNames);
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManagerView(posDatabase);
                frame.dispose();
            }
        });
    }

    /**
     * Function for testing the class
     * 
     * @param args Input from the terminal
     */
    public static void main(String[] args) {
        DatabaseInterface db = new DatabaseInterface();
        EditProductInfo window = new EditProductInfo(db);
    }
}
