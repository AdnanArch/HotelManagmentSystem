package components;

import javax.swing.*;
import java.awt.*;

public class InputDialog {
    private static final int FONT_SIZE = 18;
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(200, 30);

    public String showInputDialog(String inputMessage) {
        JLabel label = new JLabel(inputMessage);
        JTextField textField = new RoundedTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
        textField.setPreferredSize(TEXT_FIELD_SIZE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        int option = JOptionPane.showOptionDialog(
                null,
                panel,
                "Enter Input",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        return (option == JOptionPane.OK_OPTION) ? textField.getText() : null;
    }
}
