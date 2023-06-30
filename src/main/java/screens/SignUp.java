package screens;

import connection.DatabaseConnection;
import components.RoundedButton;
import components.RoundedPasswordField;
import components.RoundedTextField;
import security.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    SignUp(){
        super("Sign Up");
        db = new DatabaseConnection();
        JLabel label1 = new JLabel("Create new Account");
        label1.setBounds(320,50,500,100);
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
        label2.setBounds(440,650,250,100);
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == signUpButton){
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

                if (firstNameText.isEmpty() || lastNameText.isEmpty() || userNameText.isEmpty() || passwordText.isEmpty()
                        || emailText.isEmpty() || phoneText.isEmpty() || addressText.isEmpty()|| emergencyContactText.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Form is Incomplete");
                }else{
                    Validator validator = new Validator();
                    // level 1 validation
                    if(validator.isName(firstNameText) && validator.isName(lastNameText) && validator.isUserName(userNameText)
                    && validator.checkPassword(passwordText) && validator.isEmail(emailText) && validator.isAddress(addressText)
                    && validator.isPhoneNumber(phoneText) && validator.isPhoneNumber(emergencyContactText)) {
                        System.out.println("Entered info is correct");
                        // level 2 validation
                        // check if username is unique or not
                        boolean unique = true;
                        CallableStatement statement = db.connection.prepareCall("{CALL sp_get_customer_username}");
                        ResultSet results = statement.executeQuery();
                        while(results.next()) {
                            if(results.getString(1).equals(userNameText)) {
                                unique = false;
                            }
                        }
                        if(!unique) {
                            JOptionPane.showMessageDialog(this, "username already used, kindly use an unique username");

                        }else {
                            statement = db.connection.prepareCall("{CALL sp_sign_up(?, ?, ?, ?, ?, ?, ?, ?)}");
                            statement.setString(1, firstNameText);
                            statement.setString(2, lastNameText);
                            statement.setString(3, userNameText);
                            statement.setString(4, passwordText);
                            statement.setString(5, emailText);
                            statement.setString(6, addressText);
                            statement.setString(7, phoneText);
                            statement.setString(8, emergencyContactText);
                            try{
                                statement.execute();
                                JOptionPane.showMessageDialog(this, "Sign Up Successful");
                                dispose();
                                new Login();


                            }catch (Exception ex) {
//                            ex.printStackTrace();
                                System.out.println(ex.getMessage());
                            }
                        }

                    }else{
                        JOptionPane.showMessageDialog(this, validator.errorString());
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
