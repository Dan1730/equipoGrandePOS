	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;

	import java.awt.*;
	import java.awt.event.*;
	import java.util.List;
	import java.util.ArrayList;

	/**
	* @author Juliana Leano
	*/
	public class ReportSale extends javax.swing.JFrame {

		// private final JButton homeButton;

		private final List<String> currentSaleList = new ArrayList<String>();
		private static String currProductPrice;
		private Sale currentSale;

		/**
		* Class constructor that connects to a given database to run a Point of Sale system and report a sale
		* @param posDatabase
		*/
		public ReportSale(DatabaseInterface posDatabase) {



		currentSale = new Sale(posDatabase);
		
		final JSplitPane splitPane = new JSplitPane();

		final JPanel leftPanel = new JPanel();
		final JPanel rightPanel = new JPanel();

		final JPanel buttonLeftPanel = new JPanel();

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		buttonLeftPanel.setLayout(new BoxLayout(buttonLeftPanel, BoxLayout.X_AXIS));

		/* ------------------------------------------------------------------------------- */
		final JTable displayTable = new JTable();
		String[] columns = {"PID","Product Name", "Amount", "Item Price"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		displayTable.setModel(model);

		displayTable.setBounds(30, 40, 200, 200);
		displayTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		// displayTable.setEnabled(false);

		JScrollPane displayScrollPane = new JScrollPane(displayTable);

		
		/* ------------------------------------------------------------------------------- */
		JLabel totalPriceLabelText = new JLabel(" € 0.00 ");
		
		final JButton endSaleButton = new JButton("End Sale");
		endSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currentSale.MakeSale();
				model.setNumRows(0);
				currentSale = new Sale(posDatabase);
				totalPriceLabelText.setText("€ 0.00");
			}
		});

		final JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (displayTable.getSelectedRow() != -1) {
                    // remove the selected row from the model
					System.out.println(displayTable.getModel().getValueAt(displayTable.getSelectedRow(), 0).toString());
					currentSale.RemoveItem(displayTable.getModel().getValueAt(displayTable.getSelectedRow(), 0).toString());
                    model.removeRow(displayTable.getSelectedRow());
					totalPriceLabelText.setText(String.format("€ %.2f",currentSale.TotalPrice()));
                    JOptionPane.showMessageDialog(null, "Row successfully removed");
                }
			}
		});
		
		
		JTextField idText = new JTextField(3);
		JTextField amountText = new JTextField(20);

		final JButton nextItemButton = new JButton("Add Item");
		nextItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currentSale.AddItem(idText.getText(),amountText.getText());

				String[] newLineItem = new String[4];

				newLineItem[0] = idText.getText();
				newLineItem[1] = currentSale.GetProductName(idText.getText());
				newLineItem[2] = amountText.getText();
				newLineItem[3] = String.format("€ %.2f",currentSale.GetItemPrice(Integer.parseInt(idText.getText()), Float.parseFloat(amountText.getText())));

				model.addRow(newLineItem);
				totalPriceLabelText.setText(String.format("€ %.2f",currentSale.TotalPrice()));
			}
		});


		JLabel productLabel = new JLabel("Product ID: ");
		JLabel productTitleLabel = new JLabel("Product chosen: ");
		JLabel productStockLabel = new JLabel("Amount left in stock: ");
		JLabel amountLabel = new JLabel("Amount: ");
		JLabel amountLabelDesc = new JLabel("After typing the amount, hit enter then Next Item.");

		JLabel totalPriceLabel = new JLabel("Total Price: ");

		amountText.setMaximumSize(new Dimension(200, 20));
		amountText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currProductPrice = amountText.getText();
				currentSaleList.add(currProductPrice);
				System.out.println(currentSaleList.size());
			}
			}
		);
		
			idText.setMaximumSize(new Dimension(200, 20));
				idText.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						productTitleLabel.setText("Product chosen: " + posDatabase.GetAttribute("product", "productName", idText.getText()));
						productStockLabel.setText("Amount left in stock: " + posDatabase.GetAttribute("currentInventory", "stockQuantity", idText.getText()));
					}
				}
			);
			

		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new StartPage();
				dispose();
			}
		});

		// Setting up the size of the frame and the adding the split pane to the frame 
		setPreferredSize(new Dimension(800, 700));
		getContentPane().setLayout(new GridLayout());
		getContentPane().add(splitPane);    

		// Configuring the splitPane that holds the left and right pane horizontally
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); 
		splitPane.setDividerLocation(400);   // will split the pane at 200 pixels horizontally
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);

		// bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically

		// add corresponsing components panels to left and right panels
		leftPanel.add(displayScrollPane);
		
		buttonLeftPanel.add(endSaleButton);
		buttonLeftPanel.add(removeButton);
		buttonLeftPanel.add(nextItemButton);

		leftPanel.add(buttonLeftPanel);

		rightPanel.add(Box.createRigidArea(new Dimension(50, 20)));
		rightPanel.add(productLabel);
		rightPanel.add(idText);
		rightPanel.add(productTitleLabel);
		rightPanel.add(productStockLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		rightPanel.add(amountLabel);
		rightPanel.add(amountLabelDesc);
		rightPanel.add(amountText);
		rightPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		rightPanel.add(totalPriceLabel);
		rightPanel.add(totalPriceLabelText);
		rightPanel.add(Box.createRigidArea(new Dimension(50, 300)));
		rightPanel.add(homeButton);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);


		pack();
	}

	/**
	 * Declares an instance of the Database Interface and of the Report Sale class using the database
	 * @param args
	 */
	public static void main(String args[])  {  
		DatabaseInterface db = new DatabaseInterface();
		ReportSale window = new ReportSale(db); 
	}  

	}