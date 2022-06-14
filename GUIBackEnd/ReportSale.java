import javax.swing.*;
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
   private final JButton editButton;
   private final JButton removeButton;
   private final JButton nextItemButton;

   private static JLabel productLabel;
   private static JLabel amountLabel;
   private static JLabel totalPriceLabel;
	private static JLabel amountLabelDesc;

   private static JComboBox<String> productDropdown;

   private static JTextField amountText;

   private static JLabel totalPriceLabelText;

   private final JButton homeButton;

	private final List<String> currentSaleList = new ArrayList<String>();
	private static String currProductPrice;
	private static Double currentSaleSum = 0.0;

   public ReportSale() {
      
      splitPane = new JSplitPane();

		leftPanel = new JPanel();
		rightPanel = new JPanel();

		buttonLeftPanel = new JPanel();

      rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		buttonLeftPanel.setLayout(new BoxLayout(buttonLeftPanel, BoxLayout.X_AXIS));

      /* --------------------------------JTable function?-------------------------------- */
      String[][] data = {
         {"abc", "def"},
         {"123", "456"}
      };

      String[] columnNames = {"col1", "col2"};

      displayTable = new JTable(data, columnNames);
      displayTable.setBounds(30, 40, 200, 200);
		displayTable.setEnabled(false);

      JScrollPane displayScrollPane = new JScrollPane(displayTable);

      
      /* ------------------------------------------------------------------------------- */

      
      
      endSaleButton = new JButton("End Sale");
		// FIXME changes the total price display to 0.00 to finish order
		// should also clear out table
		endSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				totalPriceLabelText.setText("$ 0.00 ");
				currentSaleSum = 0.0;
			}
		});

      editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// CODE GOES HERE
			}
		});

      removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// CODE GOES HERE
			}
		});
      
		nextItemButton = new JButton("Next Item");
		nextItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				currentSaleSum += Double.parseDouble(currProductPrice);
				totalPriceLabelText.setText(Double.toString(currentSaleSum));
				// System.out.println(currentSaleSum);
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
				// System.out.println(currentSaleList.size());
			}
		}); 


		String[] options = { "top fruit 1", "top fruit 2", "top fruit 3" };
      productDropdown = new JComboBox<>(options);
		productDropdown.setMaximumSize(new Dimension(200, 20));
		productDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// CODE GOES HERE
			}
		});

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
      buttonLeftPanel.add(editButton);
      buttonLeftPanel.add(removeButton);
      buttonLeftPanel.add(nextItemButton);

		leftPanel.add(buttonLeftPanel);

      rightPanel.add(Box.createRigidArea(new Dimension(50, 20)));
		rightPanel.add(productLabel);
      rightPanel.add(productDropdown);
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
      public static void main(String args[])  {  
         new ReportSale(); 
   }  

}

