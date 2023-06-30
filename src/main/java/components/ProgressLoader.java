package components;

import javax.swing.*;
import java.awt.*;

public class ProgressLoader extends Component {
    public JDialog createLoadingDialog(String messageText){
        // Create and customize the loading dialog
        JDialog loadingDialog = new JDialog();
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);
        loadingDialog.setSize(350, 150);
        loadingDialog.setLocationRelativeTo(this);

        // Create the panel for the loading dialog
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the title label
        JLabel titleLabel = new JLabel("Processing...");
        titleLabel.setFont(new Font("Sans-serif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create the message label
        JLabel messageLabel = new JLabel(messageText);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create the progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.SOUTH);

        loadingDialog.add(panel);
        loadingDialog.setVisible(true);

        return loadingDialog;
    }
}
