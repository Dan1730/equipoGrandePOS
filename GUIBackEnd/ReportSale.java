	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;

	import java.awt.*;
	import java.awt.event.*;
	import java.util.List;
	import java.util.ArrayList;

	public class ReportSale extends javax.swing.JFrame {

	private final JSplitPane splitPane;

	private final JPanel leftPanel;
	private final JPanel rightPanel;
	private final JPanel buttonLeftPanel;

	private final JTable displayTable;

	private final JButton endSaleButton;
	private final JButton removeButton;
	private final JButton nextItemButton;

	private static JLabel productLabel;
	private static JLabel amountLabel;
	private static JLabel totalPriceLabel;
	private static JLabel amountLabelDesc;

	private static JTextField idText;
	private static JTextField amountText;

	private static JLabel totalPriceLabelText;

	private final JButton homeButton;

	private final List<String> currentSaleList = new ArrayList<String>();
	private static String currProductPrice;
	private static Double currentSaleSum = 0.0;

	private Sale currentSale;

	public ReportSale(DatabaseInterface posDatabase) {
		
		currentSale = new Sale(posDatabase);
		
		splitPane = new JSplitPane();

		leftPanel = new JPanel();
		rightPanel = new JPanel();

		buttonLeftPanel = new JPanel();

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		buttonLeftPanel.setLayout(new BoxLayout(buttonLeftPanel, BoxLayout.X_AXIS));

		/* --------------------------------JTable function?-------------------------------- */
		String[] columnNames = {"col1", "col2"};

		displayTable = new JTable();
		String[] columns = {"PID","Product Name", "Amount"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		displayTable.setModel(model);

		displayTable.setBounds(30, 40, 200, 200);
		displayTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		// displayTable.setEnabled(false);

		JScrollPane displayScrollPane = new JScrollPane(displayTable);

		
		/* ------------------------------------------------------------------------------- */
		
		endSaleButton = new JButton("End Sale");
		// FIXME changes the total price display to 0.00 to finish order
		// should also clear out table
		endSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currentSale.MakeSale();
				model.setNumRows(0);
				currentSale = new Sale(posDatabase);
				totalPriceLabelText.setText("$ 0.00");
			}
		});

		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (displayTable.getSelectedRow() != -1) {
                    // remove the selected row from the model
					System.out.println(displayTable.getModel().getValueAt(displayTable.getSelectedRow(), 0).toString());
					currentSale.RemoveItem(displayTable.getModel().getValueAt(displayTable.getSelectedRow(), 0).toString());
                    model.removeRow(displayTable.getSelectedRow());
					totalPriceLabelText.setText(String.format("$ %.2f",currentSale.TotalPrice()));
                    JOptionPane.showMessageDialog(null, "Row successfully removed");
                }
			}
		});
		
		nextItemButton = new JButton("Add Item");
		nextItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currentSale.AddItem(idText.getText(),amountText.getText());

				String[] newLineItem = new String[3];

				newLineItem[0] = idText.getText();
				newLineItem[1] = currentSale.GetProductName(idText.getText());
				newLineItem[2] = amountText.getText();

				model.addRow(newLineItem);
				totalPriceLabelText.setText(String.format("$ %.2f",currentSale.TotalPrice()));
			}
		});


		productLabel = new JLabel("Product ID: ");
		amountLabel = new JLabel("Amount: ");
		amountLabelDesc = new JLabel("After typing the amount, hit enter then Next Item.");

		totalPriceLabel = new JLabel("Total Price: ");

		amountText = new JTextField(20);
		amountText.setMaximumSize(new Dimension(200, 20));
		// FIXME will save the amount typed to currentSaleList ...
		amountText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currProductPrice = amountText.getText();
				currentSaleList.add(currProductPrice);
				System.out.println(currentSaleList.size());
			}
			}
		);
		
		idText = new JTextField(3);
			idText.setMaximumSize(new Dimension(200, 20));
				idText.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
					// Code goes here
					}
				}
			);

		totalPriceLabelText = new JLabel(" $ 0.00 ");
			

		homeButton = new JButton("Home");
		// homeButton.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent ae) {
		// 		// CODE GOES HERE
		// 	}
		// });

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

		// public void actionPerformed(ActionEvent e) {
		// 	totalPriceLabelText.setText("$ 0.00 ");
		// }

		public static void main(String args[])  {  
		DatabaseInterface db = new DatabaseInterface();
		ReportSale window = new ReportSale(db); 
	}  

	}

