package screens;

import components.InputDialog;
import components.ProgressLoader;
import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;
import email.EmailSender;
import email.OTPGenerator;

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
        titleLabel.setBounds(145, 30, 400, 100);
        titleLabel.setForeground(Color.WHITE.brighter());
        titleLabel.setFont(new Font("Sans-serif", Font.BOLD, 35));
        add(titleLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(170, 150, 130, 30);
        emailLabel.setFont(new Font("Sans-serif", Font.BOLD, 18));
        emailLabel.setForeground(Color.WHITE.brighter());
        add(emailLabel);

        emailTextField = new RoundedTextField();
        emailTextField.setBounds(170, 180, 280, 45);
        emailTextField.setForeground(Color.BLACK);
        emailTextField.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        add(emailTextField);

        resetButton = new RoundedButton("Reset");
        resetButton.setBackground(new Color(218, 215, 205));
        resetButton.setBounds(350, 235, 100, 40);
        resetButton.setFont(new Font("Sans-serif", Font.BOLD, 16));
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);
        add(resetButton);

        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setBackground(new Color(218, 215, 205));
        loginButton.setBounds(25, 310, 100, 40);
        loginButton.setFont(new Font("Sans-serif", Font.BOLD, 16));
        loginButton.addActionListener(actionEvent -> {
            new Login();
            dispose();
        });
        loginButton.setFocusable(false);
        add(loginButton);

        db = new DatabaseConnection();
        otpGenerator = new OTPGenerator();
        emailSender = new EmailSender();

        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
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
        JDialog loadingDialog = progressLoader.createLoadingDialog("Please wait while the OTP is being sent.");

        Thread otpThread = new Thread(() ->{
            try{
                // Perform the email sending task
                boolean otpStatus = emailSender.sendEmail(userEmail, from, subject, message);
                if (otpStatus){
                    SwingUtilities.invokeLater(()->{
                        loadingDialog.dispose(); // Close the loading dialog
                        InputDialog otpInputDialog = new InputDialog();
                        String enteredOTP = otpInputDialog.showInputDialog("Enter the OTP sent to your email:");
                        if (!enteredOTP.isEmpty() && enteredOTP.equals(otp)) {
                            resetPassword(userEmail);
                        } else if (!enteredOTP.isEmpty()) {
                            showErrorMessage("Incorrect OTP. Please try again.");
                        }
                    });
                }else{
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        showErrorMessage("Use stable internet connection. Try again.");
                    });
                }
            }catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        });

        otpThread.start();
    }


    private void resetPassword(String userEmail) {
        try {
            String newPassword = new InputDialog().showInputDialog("Enter new password:");
            if (newPassword != null && !newPassword.isEmpty()) {
                CallableStatement statement = db.connection.prepareCall("{call sp_reset_password(?, ?)}");

                statement.setString(1, userEmail);
                statement.setString(2, newPassword);
                statement.execute();

                JOptionPane.showMessageDialog(this, "Password reset successful.");
                dispose();
                new Login();
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
