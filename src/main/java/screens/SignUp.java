package screens;

import components.*;
import connection.DatabaseConnection;
import email.EmailSender;
import email.OTPGenerator;
import security.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class SignUp extends JFrame implements ActionListener {
    private static RoundedTextField firstName;
    private static RoundedTextField lastName;
    private static RoundedTextField userName;
    private static RoundedPasswordField password;
    private static RoundedTextField email;
    private static RoundedTextField address;
    private static RoundedTextField phone;
    private static RoundedTextField emergencyContact;
    private static RoundedButton signUpButton;
    DatabaseConnection db;
//    private String generatedOTP; // Store the generated OTP

    SignUp() {
        super("Sign Up");
        db = new DatabaseConnection();
        JLabel label1 = new JLabel("Create new Account");
        label1.setBounds(320, 50, 500, 100);
        label1.setForeground(Color.WHITE.brighter());
        label1.setFont(new Font("Arial", Font.BOLD, 40));
        add(label1);

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setBounds(250, 170, 120, 30);
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        firstNameLabel.setForeground(Color.WHITE.brighter());
        add(firstNameLabel);

        firstName = new RoundedTextField();
        firstName.setBounds(250, 200, 280, 40);
        firstName.setForeground(Color.BLACK);
        firstName.setFont(new Font("Arial", Font.PLAIN, 17));
        add(firstName);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(250, 280, 120, 30);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setForeground(Color.WHITE.brighter());
        add(usernameLabel);

        userName = new RoundedTextField();
        userName.setBounds(250, 310, 280, 40);
        userName.setForeground(Color.BLACK);
        userName.setFont(new Font("Arial", Font.PLAIN, 17));
        add(userName);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(250, 390, 120, 30);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel.setForeground(Color.WHITE.brighter());
        add(emailLabel);

        email = new RoundedTextField();
        email.setBounds(250, 420, 280, 40);
        email.setForeground(Color.BLACK);
        email.setFont(new Font("Arial", Font.PLAIN, 17));
        add(email);

        JLabel phoneLabel = new JLabel("Phone No");
        phoneLabel.setBounds(250, 500, 120, 30);
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 18));
        phoneLabel.setForeground(Color.WHITE.brighter());
        add(phoneLabel);

        phone = new RoundedTextField();
        phone.setBounds(250, 530, 280, 40);
        phone.setForeground(Color.BLACK);
        phone.setFont(new Font("Arial", Font.PLAIN, 17));
        add(phone);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setBounds(600, 170, 120, 30);
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lastNameLabel.setForeground(Color.WHITE.brighter());
        add(lastNameLabel);

        lastName = new RoundedTextField();
        lastName.setBounds(600, 200, 280, 40);
        lastName.setForeground(Color.BLACK);
        lastName.setFont(new Font("Arial", Font.PLAIN, 17));
        add(lastName);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(600, 280, 120, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.WHITE.brighter());
        add(passwordLabel);

        password = new RoundedPasswordField();
        password.setBounds(600, 310, 280, 40);
        password.setForeground(Color.BLACK);
        password.setFont(new Font("Arial", Font.PLAIN, 17));
        add(password);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(600, 390, 120, 30);
        addressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        addressLabel.setForeground(Color.WHITE.brighter());
        add(addressLabel);

        address = new RoundedTextField();
        address.setBounds(600, 420, 280, 40);
        address.setForeground(Color.BLACK);
        address.setFont(new Font("Arial", Font.PLAIN, 17));
        add(address);

        JLabel emergencyContactLabel = new JLabel("Emergency Contact No");
        emergencyContactLabel.setBounds(600, 500, 300, 30);
        emergencyContactLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emergencyContactLabel.setForeground(Color.WHITE.brighter());
        add(emergencyContactLabel);

        emergencyContact = new RoundedTextField();
        emergencyContact.setBounds(600, 530, 280, 40);
        emergencyContact.setForeground(Color.BLACK);
        emergencyContact.setFont(new Font("Arial", Font.PLAIN, 17));
        add(emergencyContact);

        signUpButton = new RoundedButton("Sign up");
        signUpButton.setBackground(new Color(218, 215, 205));
        signUpButton.setBounds(510, 610, 110, 40);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.addActionListener(this);
        signUpButton.setFocusable(false);
        add(signUpButton);

        JLabel label2 = new JLabel("Already Registered? ");
        label2.setBounds(440, 650, 250, 100);
        label2.setForeground(Color.WHITE.brighter());
        label2.setFont(new Font("Arial", Font.BOLD, 18));
        add(label2);

        JLabel signInLabel = new JLabel("<html><i>Login</i></html>");
        signInLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        signInLabel.setBounds(650, 680, 110, 40);
        signInLabel.setForeground(new Color(218, 215, 205));
        add(signInLabel);

        // Add a mouse listener to the label
        signInLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                signInLabel.setText("<html><u><i>Login</i></u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signInLabel.setText("<html><i>Login</i></html>");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Login login = new Login();
                dispose();
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        add(signInLabel);

        setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1080, 820));
        setSize(1080, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(12, 55, 64));
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new SignUp();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == signUpButton) {
            try {
                String firstNameText = firstName.getText().trim();
                String lastNameText = lastName.getText().trim();
                String userNameText = userName.getText().trim();
                char[] passwordChars = password.getPassword();
                String passwordText = new String(passwordChars);
                String emailText = email.getText().trim();
                String phoneText = phone.getText().trim();
                String addressText = address.getText().trim();
                String emergencyContactText = emergencyContact.getText().trim();

                Validator validator = new Validator();
                String validationError = validator.validateCustomerData(firstNameText, lastNameText, userNameText, emailText, passwordText, addressText, phoneText, emergencyContactText);

                if (validationError != null) {
                    JOptionPane.showMessageDialog(this, validationError);
                } else {
                    boolean uniqueUsername = checkIfUsernameIsUnique(userNameText);
                    boolean uniqueEmail = checkIfEmailIsUnique(emailText);
                    if (!uniqueUsername) {
                        JOptionPane.showMessageDialog(this, "Account with this username already exists.");
                    } else if (!uniqueEmail) {
                        JOptionPane.showMessageDialog(this, "Account with this email already exists.");
                    } else {
                        // Create and customize the loading dialog
                        ProgressLoader loader = new ProgressLoader();
                        JDialog loadingDialog = loader.createLoadingDialog("Please wait while the OTP is being sent.");

                        // Perform time-consuming task in a separate thread
                        Thread otpThread = new Thread(() -> {
                            try {
                                OTPGenerator otpGenerator = new OTPGenerator();
                                String generatedOTP = otpGenerator.generateOTP();
                                boolean otpStatus = sendOTP(emailText, generatedOTP);
                                if (otpStatus) {
                                    SwingUtilities.invokeLater(() -> {
                                        loadingDialog.dispose(); // Close the loading dialog
                                        InputDialog otpInputDialog = new InputDialog();
                                        String enteredOTP = otpInputDialog.showInputDialog("Enter the OTP sent to your email:");
                                        if (!enteredOTP.isEmpty() && enteredOTP.equals(generatedOTP)) {
                                            registerCustomer(firstNameText, lastNameText, userNameText, passwordText, emailText, addressText, phoneText, emergencyContactText);
                                        } else if (!enteredOTP.isEmpty()) {
                                            JOptionPane.showMessageDialog(this, "Incorrect OTP. Please try again.");
                                        }
                                    });
                                } else {
                                    SwingUtilities.invokeLater(() -> {
                                        loadingDialog.dispose(); // Close the loading dialog
                                        JOptionPane.showMessageDialog(this, "Use stable internet connection. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                SwingUtilities.invokeLater(() -> {
                                    loadingDialog.dispose(); // Close the loading dialog
                                    JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                });
                            }
                        });

                        otpThread.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean sendOTP(String email, String otp) {
        EmailSender emailSender = new EmailSender();
        String from = "adnaninreallife@gmail.com"; // Replace with your Gmail email address
        String subject = "OTP for Sign Up";
        String text = "Your OTP is: " + otp;
        return emailSender.sendEmail(email, from, subject, text);
    }

    private boolean checkIfUsernameIsUnique(String userName) {
        try {
            CallableStatement statement = db.connection.prepareCall("{CALL sp_get_customer_username}");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                if (results.getString(1).equals(userName)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return true;
    }

    private boolean checkIfEmailIsUnique(String emailText) {
        try {
            CallableStatement statement = db.connection.prepareCall("{CALL sp_get_customer_email}");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                if (results.getString(1).equals(emailText)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return true;
    }


    private void registerCustomer(String firstName, String lastName, String userName, String password, String email,
                                  String address, String phone, String emergencyContact) {
        try {
            CallableStatement statement = db.connection.prepareCall("{CALL sp_sign_up(?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);
            statement.setString(4, password);
            statement.setString(5, email);
            statement.setString(6, address);
            statement.setString(7, phone);
            statement.setString(8, emergencyContact);

            statement.execute();
            JOptionPane.showMessageDialog(this, "Sign Up Successful");
            dispose();
            new Login();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
