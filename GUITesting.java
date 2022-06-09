// This code create a button that is able to count and display the amount
// of times a button is clicked

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButtonn;
import javax.swing.JLabel;


public class GUI implements ActionListener{

    int count  0;
    private JLabel label;
    priavte JFrame frame;
    private JPanel panel;


    public GUI(){
        frame = new JFrame();
        button = new JButton("Click me"); 
        button.addActioListener(this);
        label =  new JLabel("Number of clicks: 0");
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our GUI");
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String [] args){
        new GUI();
    }

    @Override
    public void actionPerfromed(ActionEvent e){
        count++;
        label.setText("Number of clicks: " + count);

    }

}
