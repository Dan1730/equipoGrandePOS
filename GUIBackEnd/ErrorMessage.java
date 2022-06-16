import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;

public class ErrorMessage{
    // variables outside of constructor scope
    
    public ErrorMessage() {
        // Initializing GUI error frame for incorrect input
        JFrame errorMsgFrame = new JFrame();
        JOptionPane.showMessageDialog(errorMsgFrame, "Incorrect Input. Please Try Again", "Error", JOptionPane.ERROR_MESSAGE);
    
    }


    public static void main(String[] args) {
        new ErrorMessage();
    }
}
