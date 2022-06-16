import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Juliana Leano
 */
public class VendorOrders extends JFrame {
    // Initializing frame
    private final JFrame frame;

    String txIDStr;
    String dateStr;
    String pIDStr;

    public VendorOrders(DatabaseInterface posDatabase) {
        // Create the main frame
        frame = new JFrame("Vendor Orders");

        // Create the back-end class to draw from the database
        OrderHistory orderHistoryTool = new OrderHistory(posDatabase);

        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel mainPanel = (JPanel) getContentPane();
        String[] columnNames = { "Date", "Transaction ID", "Product ID", "Quantity", "Total" };
        DefaultTableModel model = new DefaultTableModel();

        // Adding 3 text fields to search through orders
        JTextField txIDTextField = new JTextField("Search by Transaction ID: ");
        txIDTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				txIDStr = txIDTextField.getText();
				updateTable(orderHistoryTool, model, columnNames);
            }
		});

         JTextField dateTextField = new JTextField("Search by Transaction Date: ");
         dateTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
               	dateStr = dateTextField.getText();
               	updateTable(orderHistoryTool, model, columnNames);
            }
		});

         JTextField pIDTextField = new JTextField("Search by Product ID: ");
         pIDTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	pIDStr = pIDTextField.getText();
               	updateTable(orderHistoryTool, model, columnNames);
            }
		});

        // Adding buttons to the top panel
        topPanel.setLayout(new GridLayout(2,3, 10, 10));
        topPanel.add(txIDTextField);
        topPanel.add(dateTextField);
        topPanel.add(pIDTextField);

        topPanel.add(Box.createRigidArea(new Dimension(50, 20)));
        topPanel.add(Box.createRigidArea(new Dimension(50, 20)));
        topPanel.add(Box.createRigidArea(new Dimension(50, 20)));

         // setting up the display table with column names for the mid panel
		JTable displayTable = new JTable();
		String[][] data = orderHistoryTool.FetchTableData(txIDStr, dateStr, pIDStr);
		matrixSort(data);
		model.setColumnIdentifiers(columnNames);
		displayTable.setEnabled(false);
		displayTable.setModel(model);
		updateTable(orderHistoryTool, model, columnNames);
		JScrollPane displayScrollPane = new JScrollPane(displayTable);

		midPanel.add(displayScrollPane);

         // adding the home button to the bottom panel
        JPanel bottomPanel = new JPanel();
        JButton homeButton = new JButton("Home");
        homeButton.setMaximumSize(new Dimension(100, 80));
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	new ManagerView(posDatabase); 
               	frame.dispose();
            }
        });

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(600, 10)));
        bottomPanel.add(homeButton);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(midPanel);
        mainPanel.add(bottomPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(600, 10)));


        // Adding panel and setting frame
        frame.add(mainPanel);

        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  

    }

    public void updateTable(OrderHistory orderHistoryTool, DefaultTableModel model, String[] columnNames){
		if(txIDStr != null && txIDStr.equals("")){
			txIDStr = null;
		} if(dateStr != null && dateStr.equals("")){
			dateStr = null;
		} if(pIDStr != null && pIDStr.equals("")){
			pIDStr = null;
		}
		String[][] data = orderHistoryTool.FetchTableData(txIDStr, dateStr, pIDStr);
		for(int i = 0; i < data.length; i++){
			data[i][4] = String.format("%.2f",Float.parseFloat(data[i][4]));
		}
		matrixSort(data);
		model.setDataVector(data, columnNames);
    }

    public void matrixSort(String[][] data){
      	for(int i = 0; i < data.length-1; i++){
        	 for(int j = i+1; j < data.length; j++){
            	if((data[i][0].compareTo(data[j][0]) > 0) || (data[i][0].compareTo(data[j][0]) == 0) && Integer.parseInt(data[j][2]) < Integer.parseInt(data[i][2])){
					String[] hold = data[i];
					data[i] = data[j];
					data[j] = hold;
				}
			}
		}
	}

    public static void main(String[] args) {
		DatabaseInterface db = new DatabaseInterface();  
		VendorOrders window = new VendorOrders(db);
    }
}
