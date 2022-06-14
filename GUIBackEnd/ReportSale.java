import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

public class ReportSale extends javax.swing.JFrame {
	
   private final JSplitPane splitPane;

	private final JPanel leftPanel;
   private final JPanel rightPanel;

   private final JTable displayTable;

   private final JButton endSaleButton;
   private final JButton editButton;
   private final JButton removeButton;
   private final JButton nextItemButton;

   private static JLabel productLabel;
   private static JLabel amountLabel;
   private static JLabel totalPriceLabel;

   private static JTextField productText;
   private static JTextField amountText;

   private static JLabel totalPriceLabelText;

   private final JButton homeButton;

   public ReportSale() {
      
      splitPane = new JSplitPane();

		leftPanel = new JPanel();
		rightPanel = new JPanel();

      rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

      /* --------------------------------JTable function?-------------------------------- */
      DatabaseInterface testInterface = new DatabaseInterface();
      String[][] data = {
         {"1", testInterface.GetAttribute("product","productName","1")},
         {"2", testInterface.GetAttribute("product","productName","2")}
      };
   

      String[] columnNames = {"col1", "col2"};

      displayTable = new JTable(data, columnNames);
      displayTable.setBounds(30, 40, 200, 300);

      JScrollPane displayScrollPane = new JScrollPane(displayTable);

      
      // FIXME: column name not showing and table is editable?

      /* ------------------------------------------------------------------------------- */

      
      
      endSaleButton = new JButton("End Sale");
      editButton = new JButton("Edit");
      removeButton = new JButton("Remove");
      nextItemButton = new JButton("Next Item");


      productLabel = new JLabel("Product ID: ");
      amountLabel = new JLabel("Amount: ");
      totalPriceLabel = new JLabel("Total Price: ");

      productText = new JTextField(20);
      amountText = new JTextField(20);

      productText.setMaximumSize(new Dimension(200, 20));
      amountText.setMaximumSize(new Dimension(200, 20));

      totalPriceLabelText = new JLabel(" [ TOTAL PRICE DISPLAY ]");      

      homeButton = new JButton("Home");

      setPreferredSize(new Dimension(600, 600));
      getContentPane().setLayout(new GridLayout());
      getContentPane().add(splitPane);    

      // Configuring the splitPane that holds the left and right pane horizontally
      splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); 
      splitPane.setDividerLocation(400);   // will split the pane at 200 pixels horizontally
      splitPane.setLeftComponent(leftPanel);
      splitPane.setRightComponent(rightPanel);

      // bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically

      leftPanel.add(displayScrollPane);
      
      leftPanel.add(endSaleButton);
      leftPanel.add(editButton);
      leftPanel.add(removeButton);
      leftPanel.add(nextItemButton);

      rightPanel.add(productLabel);
      rightPanel.add(productText);
      rightPanel.add(amountLabel);
      rightPanel.add(amountText);
      rightPanel.add(totalPriceLabel);
      rightPanel.add(totalPriceLabelText);
      rightPanel.add(homeButton);

      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);


      pack();
	}

      public static void main(String args[])  {  
         new ReportSale(); 
   }  

}

