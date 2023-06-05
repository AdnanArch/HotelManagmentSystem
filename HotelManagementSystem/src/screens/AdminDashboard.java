package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class AdminDashboard extends JFrame implements ActionListener {
    private static RoundedButton roomButton;
    private static RoundedButton bookingButton;
    private static RoundedButton revenueButton;

    public AdminDashboard() {
        super("Admin Dashboard");

        JLabel label1 = new JLabel("Hotel Management System");
        label1.setBounds(650,10,800,100);
        label1.setForeground(Color.WHITE.brighter());
        label1.setFont(new Font("Arial", Font.BOLD, 40));
        add(label1);

        JLabel logoutLabel = new JLabel("<html><i>Logout</i></html>");
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoutLabel.setBounds(1760, 25, 170, 60);
        logoutLabel.setForeground(new Color(218, 215, 205));
        add(logoutLabel);

        // Add a mouse listener to the label
        logoutLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutLabel.setText("<html><u><i>Logout</i></u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutLabel.setText("<html><i>Logout</i></html>");
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                Login login = new Login();
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        ImageIcon logoutImageIcon = new ImageIcon(ClassLoader.getSystemResource("assets/logout.png"));
        Image logoutImage = logoutImageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        logoutLabel.setIcon(new ImageIcon(logoutImage));
        add(logoutLabel);

        ImageIcon roomImageIcon = new ImageIcon(ClassLoader.getSystemResource("assets/bedroom.png"));
        Image image = roomImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon roomImage = new ImageIcon(image);
        JLabel roomImageLabel = new JLabel(roomImage);
        roomImageLabel.setBounds(60, 200, 150, 150);
        add(roomImageLabel);

        roomButton = new RoundedButton("Rooms");
        roomButton.setBackground(new Color(218, 215, 205));
        roomButton.setBounds(240, 255, 150, 45);
        roomButton.setFont(new Font("Arial", Font.BOLD, 18));
        roomButton.addActionListener(this);
        roomButton.setFocusable(false);
        add(roomButton);

        ImageIcon bookingImageIcon = new ImageIcon(ClassLoader.getSystemResource("assets/booking.png"));
        Image bookingImage = bookingImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon bookingImageIcon2 = new ImageIcon(bookingImage);
        JLabel bookingLabel = new JLabel(bookingImageIcon2);
        bookingLabel.setBounds(60, 450, 150, 150);
        add(bookingLabel);

        bookingButton = new RoundedButton("Bookings");
        bookingButton.setBackground(new Color(218, 215, 205));
        bookingButton.setBounds(240, 500, 150, 45);
        bookingButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookingButton.addActionListener(this);
        bookingButton.setFocusable(false);
        add(bookingButton);

        ImageIcon revenueImageIcon = new ImageIcon(ClassLoader.getSystemResource("assets/revenue.png"));
        Image revenueImage = revenueImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon revenueImageIcon2 = new ImageIcon(revenueImage);
        JLabel revenueLabel = new JLabel(revenueImageIcon2);
        revenueLabel.setBounds(60, 700, 150, 150);
        add(revenueLabel);

        revenueButton = new RoundedButton("Revenue");
        revenueButton.setBackground(new Color(218, 215, 205));
        revenueButton.setBounds(240, 750, 150, 45);
        revenueButton.setFont(new Font("Arial", Font.BOLD, 18));
        revenueButton.addActionListener(this);
        revenueButton.setFocusable(false);
        add(revenueButton);


        // TODO: make a Panel to Show the Rooms

        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        getContentPane().setBackground(new Color(12, 55, 64));
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == roomButton){
            new Rooms();
        } else if (actionEvent.getSource() == bookingButton) {
            new Bookings();
        }else {
            new  Revenue();
        }
    }
}
