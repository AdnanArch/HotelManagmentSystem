package components;

import javax.swing.*;
import java.awt.*;

public class OTPInputDialog extends Component {
    public String showOTPInputDialog() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the OTP sent to your email:");
        JTextField otpField = new RoundedTextField();
        otpField.setFont(new Font("Arial", Font.PLAIN, 17)); // Set the font for the text field
        otpField.setPreferredSize(new Dimension(200, 30)); // Set the preferred size for the text field
        panel.add(label);
        panel.add(otpField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Enter OTP",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.OK_OPTION) {
            return otpField.getText();
        }

        return ""; // Return empty string instead of null when cancel or close is clicked
    }
}
