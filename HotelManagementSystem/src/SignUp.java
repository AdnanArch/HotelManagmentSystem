import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private static JLabel firstNameValidator;
    SignUp(){
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
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        add(signInLabel);

        getContentPane().setBackground(new Color(12, 55, 64));
        setLayout(null);
        setResizable(false);
        setBounds(450, 100,1080,820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Helper method to validate the text field
    private void validateTextField() {
        // Check if the text field is empty
        if (firstNameValidator.getText().isEmpty()) {
            firstNameValidator.setText("First name is required.");
        } else {
            firstNameValidator.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == signUpButton){
            try {
                String firstNameText = firstName.getText();
                String lastNameText = lastName.getText();
                String userNameText = userName.getText();
                char[] passwordChars = password.getPassword();
                String passwordText = new String(passwordChars);
                String emailText = email.getText();
                String phoneText = phone.getText();
                String addressText = address.getText();
                String emergencyContactText = emergencyContact.getText();

                if (firstNameText.isEmpty() || lastNameText.isEmpty() || userNameText.isEmpty() || passwordText.isEmpty()
                        || emailText.isEmpty() || phoneText.isEmpty() || addressText.isEmpty()|| emergencyContactText.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Form is Incomplete");
                }else System.out.println("First Name" + firstNameText);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
