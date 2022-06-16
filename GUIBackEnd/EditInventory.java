import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Caroline Mejia, Juliana Leano, Daniel Morrison, Anthony Bragg
 */
public class EditInventory extends JFrame {
    
    private CurrentInventory currentInventoryObject;

    // Initializing variables
    private final JFrame frame;

    /**
    * Class constructor that connects to a given database to edit and update the current market inventory
    * @param posDatabase
    */
    public EditInventory(DatabaseInterface posDatabase) {
        currentInventoryObject = new CurrentInventory(posDatabase);

        // Create the main frame
        frame = new JFrame("Edit Inventory");

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

        // Adding text fields
        JTextField prodID = new JTextField();
        prodID.setText("Product ID");

        JTextField amountInStock = new JTextField();
        amountInStock.setText("amountInStock");

        JTextField amountRestock = new JTextField();
        amountRestock.setText("amountRestock");

        // Declaring and adding buttons to the subpanels
        JButton editButton = new JButton("Edit");
        editButton.setBounds(100, 150, 20, 20);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(100, 150, 20, 20);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(150, 150, 20, 20);

        subPanel1.add(Box.createRigidArea(new Dimension(50, 50)));
        subPanel1.add(editButton);

        subPanel2.add(prodID);
        subPanel2.add(amountInStock);
        subPanel2.add(amountRestock);

        subPanel3.add(enterButton);
        subPanel3.add(Box.createRigidArea(new Dimension(50, 50)));
        subPanel3.add(homeButton);

        rightPanel.add(subPanel1);
        rightPanel.add(subPanel2);
        rightPanel.add(subPanel3);

        // Left Panel Table components

        // Column Names
        String[] columnNames = { "Product ID", "Product Name", "Amount in Stock", "Amount to Restock"};

        // Initializing the Table
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setBounds(30, 40, 200, 300);

        // Populate text boxes when a row is selected
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() != -1) {
                    prodID.setText((table.getValueAt(table.getSelectedRow(), 0).toString()));
                    amountInStock.setText((table.getValueAt(table.getSelectedRow(), 2).toString()));
                    amountRestock.setText((table.getValueAt(table.getSelectedRow(), 3).toString()));
                }
            }
        });

        // Form the table with all products
        CreateTable(posDatabase, model);

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
                currentInventoryObject.UpdateProductInventory(prodID.getText(), amountInStock.getText(), amountRestock.getText());
                UpdateRow(prodID.getText(), model, amountInStock.getText(), amountRestock.getText());
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
     * Populates the model of the table in the GUI with the information found in Current Inventory and also maps the productID to the product Name
     * @param posDatabase is a reference to the database connection, this gives the sale access to interacting with the databaseT
     * @param model the model that represents the table
     */
    public void CreateTable(DatabaseInterface posDatabase, DefaultTableModel model){
        String[][] currentInventoryMatrix = currentInventoryObject.ToStringMatrix();
        for(int i = 1; i < currentInventoryMatrix.length; i ++){
            model.addRow(new Object[]{
                String.valueOf(currentInventoryMatrix[i][0]),
                posDatabase.GetAttribute("product", "productName", String.valueOf(currentInventoryMatrix[i][0])),
                currentInventoryMatrix[i][1],
                currentInventoryMatrix[i][2]
            });
        }
    }

   /**
    * Given a PID, scan the table for the row to update with most current information
    * @param PID the PID of the item row to update
    * @param model the model that represents the table
    * @param stockQuantity the new specified stock quantity for the product
    * @param restockQuanitity the new specified restock quantity for the product
    */ 
    public void UpdateRow(String PID, DefaultTableModel model, String stockQuantity, String restockQuanitity){
        for(int i = 0; i < model.getRowCount(); i++){
            if (model.getValueAt(i, 0).equals(PID)) {
                model.setValueAt(stockQuantity, i, 2);
                model.setValueAt(restockQuanitity, i, 3);
                return;
            }
        }
    }

    /**
     * Function for testing the class
     * @param args Input from the terminal
     */
    public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();

        new EditInventory(posDatabase);
        CurrentInventory inventoryBackend = new CurrentInventory(posDatabase);
        
    }
}
