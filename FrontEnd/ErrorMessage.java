import javax.swing.*;

public class ErrorMessage{
    
    public ErrorMessage() {
        // Initializing GUI error frame for incorrect input
        JFrame errorMsgFrame = new JFrame();
        JOptionPane.showMessageDialog(errorMsgFrame, "Incorrect Input. Please Try Again", "Error", JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args) {
        new ErrorMessage();
    }
}
