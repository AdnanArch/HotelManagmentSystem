package screens;

import components.OTPInputDialog;
import components.ProgressLoader;
import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;
import email.EmailSender;
import email.OTPGenerator;
import security.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Types;

public class ForgotPassword extends JFrame implements ActionListener {

    private final RoundedTextField emailTextField;
    private final RoundedButton resetButton;
    private final DatabaseConnection db;
    private final OTPGenerator otpGenerator;
    private final EmailSender emailSender;
    private String userEmail;

    public ForgotPassword() {
        super("Forgot Password");

        JLabel titleLabel = new JLabel("Forgot Password");
        titleLabel.setBounds(340, 100, 400, 100);
        titleLabel.setForeground(Color.WHITE.brighter());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        add(titleLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(370, 250, 120, 30);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel.setForeground(Color.WHITE.brighter());
        add(emailLabel);

        emailTextField = new RoundedTextField();
        emailTextField.setBounds(370, 280, 280, 40);
        emailTextField.setForeground(Color.BLACK);
        emailTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        add(emailTextField);

        resetButton = new RoundedButton("Reset Password");
        resetButton.setBackground(new Color(218, 215, 205));
        resetButton.setBounds(470, 335, 180, 40);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);
        add(resetButton);

        db = new DatabaseConnection();
        otpGenerator = new OTPGenerator();
        emailSender = new EmailSender();

        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1024, 600);
        getContentPane().setBackground(new Color(12, 55, 64));
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            String userEmail = emailTextField.getText().trim();

            if (userEmail.isEmpty()) {
                showErrorMessage("Enter your email");
            } else {
                boolean isUserExists = checkUserExists(userEmail);
                if (isUserExists) {
                    this.userEmail = userEmail;
                    sendOTP();
                } else {
                    showErrorMessage("Invalid email");
                }
            }
        }
    }

    private void sendOTP() {
        String otp = otpGenerator.generateOTP();
        String subject = "Password Reset OTP";
        String message = "Your OTP for password reset is: " + otp;
        String from = "adnaninreallife@gmail.com";

        ProgressLoader progressLoader = new ProgressLoader();
        JDialog loadingDialog = progressLoader.createLoadingDialog("Sending OTP...");
        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Perform the email sending task
                boolean emailSent = emailSender.sendEmail(userEmail, from, subject, message);

                if (emailSent) {
                    OTPInputDialog otpInputDialog = new OTPInputDialog();
                    String enteredOTP = otpInputDialog.showOTPInputDialog();

                    if (!enteredOTP.isEmpty()) {
                        verifyOTP(enteredOTP);
                    } else {
                        showErrorMessage("Invalid OTP. Please try again.");
                    }
                } else {
                    showErrorMessage("Failed to send OTP. Please try again.");
                }

                return null;
            }

            @Override
            protected void done() {
                // Close the loading dialog
                loadingDialog.dispose();
            }
        };

        worker.execute();
    }


    private void verifyOTP(String enteredOTP) {
        String generatedOTP = otpGenerator.getGeneratedOTP();

        if (enteredOTP.equals(generatedOTP)) {
            ProgressLoader progressLoader = new ProgressLoader();
            progressLoader.createLoadingDialog("");
            resetPassword(userEmail);
        } else {
            showErrorMessage("Invalid OTP. Please try again.");
        }
    }

    private void resetPassword(String userEmail) {
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password");
        try {
            if (newPassword != null && !newPassword.isEmpty()) {
                CallableStatement statement = db.connection.prepareCall("{call sp_reset_password(?, ?)}");

                statement.setString(1, userEmail);
                statement.setString(2, newPassword);
                statement.execute();

                JOptionPane.showMessageDialog(this, "Password reset successful.");
                dispose();
            } else {
                showErrorMessage("Invalid password. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("An error occurred. Please try again.");
        }
    }

    private boolean checkUserExists(String userEmail) {
        boolean result = false;
        try {
            CallableStatement statement = db.connection.prepareCall("{call sp_check_user_exists(?, ?)}");
            statement.setString(1, userEmail);
            statement.registerOutParameter(2, Types.BOOLEAN);

            statement.execute();
            result = statement.getBoolean(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new ForgotPassword();
    }
}
