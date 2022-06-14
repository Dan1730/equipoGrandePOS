import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class EditInventory extends JFrame {
    
    private CurrentInventory currentInventoryObject;

    // Initializing variables
    private final JFrame frame;

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

        // Buttons

         //   JButton removeButton = new JButton("Remove");
         //   removeButton.setBounds(50, 150, 20, 20);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(100, 150, 20, 20);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(100, 150, 20, 20);

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(150, 150, 20, 20);

        subPanel1.add(Box.createRigidArea(new Dimension(50, 50)));
         //   subPanel1.add(removeButton);
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

        //// LEFT PANEL ///
        // Table components

        // Column Names
        String[] columnNames = { "Product ID", "Product Name", "Amount in Stock", "Amount to Restock"};

        // Initializing the Table
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setBounds(30, 40, 200, 300);
        // table.setEnabled(false);

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
                // prodID, amountInStock, amountRestock
                currentInventoryObject.UpdateProductInventory(prodID.getText(), amountInStock.getText(), amountRestock.getText());
                UpdateRow(prodID.getText(), model, amountInStock.getText(), amountRestock.getText());
            }
        });

    }

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

    public void UpdateRow(String PID, DefaultTableModel model, String stockQuantity, String restockQuanitity){
        for(int i = 0; i < model.getRowCount(); i++){
            if (model.getValueAt(i, 0).equals(PID)) {
                model.setValueAt(stockQuantity, i, 2);
                model.setValueAt(restockQuanitity, i, 3);
                return;
            }
        }
    }

    public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();

        new EditInventory(posDatabase);
        CurrentInventory inventoryBackend = new CurrentInventory(posDatabase);
        
    }
}
