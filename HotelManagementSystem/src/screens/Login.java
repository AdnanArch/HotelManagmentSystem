package screens;

import connection.DatabaseConnection;
import components.RoundedButton;
import components.RoundedPasswordField;
import components.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Types;

public class Login extends JFrame implements ActionListener {

    private static RoundedTextField userNameTextField;
    private static RoundedPasswordField passwordTextField;
    private static RoundedButton loginButton;
    DatabaseConnection db = new DatabaseConnection();
    Login() {
        super("Login");

        JLabel label1 = new JLabel("Login");
        label1.setBounds(480, 100, 200, 100);
        label1.setForeground(Color.WHITE.brighter());
        label1.setFont(new Font("Arial", Font.BOLD, 40));
        add(label1);

        JLabel label2 = new JLabel("Sign in to continue");
        label2.setBounds(460, 150, 200, 100);
        label2.setForeground(Color.WHITE.brighter());
        label2.setFont(new Font("Arial", Font.BOLD, 16));
        add(label2);

        JLabel userNameLabel = new JLabel("Username");
        userNameLabel.setBounds(405, 250, 120, 30);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userNameLabel.setForeground(Color.WHITE.brighter());
        add(userNameLabel);

        userNameTextField = new RoundedTextField();
        userNameTextField.setBounds(405, 280, 280, 40);
        userNameTextField.setForeground(Color.BLACK);
        userNameTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        add(userNameTextField);

        JLabel userPasswordLabel = new JLabel("Password");
        userPasswordLabel.setBounds(405, 345, 120, 30);
        userPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userPasswordLabel.setForeground(Color.WHITE.brighter());
        add(userPasswordLabel);

        passwordTextField = new RoundedPasswordField();
        passwordTextField.setBounds(405, 375, 280, 40);
        passwordTextField.setForeground(Color.BLACK);
        passwordTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        add(passwordTextField);

        loginButton = new RoundedButton("Login");
        loginButton.setBackground(new Color(218, 215, 205));
        loginButton.setBounds(495, 465, 110, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);
        add(loginButton);

        JLabel label = new JLabel("New to RoomRover? ");
        label.setBounds(405, 555, 300, 30);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE.brighter());
        add(label);

        JLabel signUpLabel = new JLabel("<html><i>Sign Up</i></html>");
        signUpLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        signUpLabel.setBounds(610, 550, 110, 40);
        signUpLabel.setForeground(new Color(218, 215, 205));
        add(signUpLabel);

        // Add a mouse listener to the label
        signUpLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                signUpLabel.setText("<html><u><i>Sign Up</i></u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUpLabel.setText("<html><i>Sign Up</i></html>");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                SignUp signUp = new SignUp();
                dispose();
                signUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        add(signUpLabel);

        RoundedButton exitButton = new RoundedButton("Exit");
        exitButton.setBackground(new Color(218, 215, 205));
        exitButton.setBounds(940, 725, 110, 40);
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        exitButton.addActionListener(this);
        exitButton.setFocusable(false);
        add(exitButton);

        getContentPane().setBackground(new Color(12, 55, 64));
        setLayout(null);
        setResizable(false);
        setBounds(450, 100, 1080, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == loginButton) {
            String userName = userNameTextField.getText();
            char[] passwordChars = passwordTextField.getPassword();
            String password = new String(passwordChars);

            boolean isAdmin = checkAdminLogin(userName, password);
            if (isAdmin) {
                dispose();
                new AdminDashBoard();
            } else {
                boolean isCustomer = checkCustomerLogin(userName, password);
                if (isCustomer) {
                    dispose();
                    new CustomerDashBoard();
                } else {
                    if (userName.isEmpty()) {
                        showErrorMessage("Enter your username");
                    } else if (password.isEmpty()) {
                        showErrorMessage("Enter your password");
                    } else showErrorMessage("Invalid username or password");
                }
            }


        } else {
            System.exit(111);
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean checkCustomerLogin(String userName, String password) {
        boolean result = false;
        try {
            CallableStatement statement = db.connection.prepareCall("{call sp_customer_login(?, ?, ?)}");
            statement.setString(1, userName);
            statement.setString(2, password);
            statement.registerOutParameter(3, Types.BOOLEAN);

            statement.execute();
            result = statement.getBoolean(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean checkAdminLogin(String userName, String password) {
        boolean result = false;
        try {
            CallableStatement statement = db.connection.prepareCall("{call sp_admin_login(?, ?, ?)}");
            statement.setString(1, userName);
            statement.setString(2, password);
            statement.registerOutParameter(3, Types.BOOLEAN);

            statement.execute();
            result = statement.getBoolean(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
