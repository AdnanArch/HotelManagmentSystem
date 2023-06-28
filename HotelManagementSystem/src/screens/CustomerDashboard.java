package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerDashboard extends JFrame implements ActionListener {
    private final RoundedButton bookingButton;
    private final RoundedButton checkoutButton;

    public CustomerDashboard() {
        super("Customer Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(12, 55, 64));
        contentPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(12, 55, 64));

        JLabel label1 = new JLabel("Welcome To RoomRover");
        label1.setForeground(Color.WHITE.brighter());
        label1.setFont(new Font("Arial", Font.BOLD, 40));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setBorder(BorderFactory.createEmptyBorder(0, 130, 0, 0)); // Move label 50 pixels to the right
        topPanel.add(label1, BorderLayout.CENTER);

        JLabel logoutLabel = new JLabel("<html><i>Logout</i></html>");
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoutLabel.setForeground(new Color(218, 215, 205));
        logoutLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 30)); // Move logout button 30 pixels to the left

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
        topPanel.add(logoutLabel, BorderLayout.EAST);

        contentPanel.add(topPanel, BorderLayout.NORTH);

        ImageIcon roomsIcon = new ImageIcon(ClassLoader.getSystemResource("assets/room_booking.png"));
        ImageIcon bookingsIcon = new ImageIcon(ClassLoader.getSystemResource("assets/checkout.png"));
        ImageIcon revenueIcon = new ImageIcon(ClassLoader.getSystemResource("assets/feedback.png"));

        // Scale the images to 150x150 pixels
        Image scaledRoomsImage = roomsIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        Image scaledBookingsImage = bookingsIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        Image scaledRevenueImage = revenueIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        // Create ImageIcon instances with the scaled images
        ImageIcon scaledRoomsIcon = new ImageIcon(scaledRoomsImage);
        ImageIcon scaledBookingsIcon = new ImageIcon(scaledBookingsImage);
        ImageIcon scaledRevenueIcon = new ImageIcon(scaledRevenueImage);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(12, 55, 64));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 50, 50, 50); // Add spacing around components

        JLabel roomsLabel = new JLabel(scaledRoomsIcon);
        centerPanel.add(roomsLabel, gbc);

        gbc.gridx = 0; // Move to the next column
        gbc.gridy = 1; // Move to the next row

        bookingButton = new RoundedButton("Book Room");
        bookingButton.setPreferredSize(new Dimension(150, 50));
        bookingButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookingButton.setBackground(new Color(65, 131, 215));
        bookingButton.addActionListener(this);
        bookingButton.setFocusable(false);
        centerPanel.add(bookingButton, gbc);

        gbc.gridx = 1; // Move to the next column
        gbc.gridy = 0; // Reset to the first row

        JLabel bookingsLabel = new JLabel(scaledBookingsIcon);
        centerPanel.add(bookingsLabel, gbc);

        gbc.gridx = 1; // Move to the next column
        gbc.gridy = 1; // Move to the next row

        checkoutButton = new RoundedButton("Checkout");
        checkoutButton.setPreferredSize(new Dimension(150, 50));
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        checkoutButton.setBackground(new Color(65, 131, 215));
        checkoutButton.addActionListener(this);
        checkoutButton.setFocusable(false);
        centerPanel.add(checkoutButton, gbc);

        gbc.gridx = 2; // Move to the next column
        gbc.gridy = 0; // Reset to the first row

        JLabel revenueLabel = new JLabel(scaledRevenueIcon);
        centerPanel.add(revenueLabel, gbc);

        gbc.gridx = 2; // Move to the next column
        gbc.gridy = 1; // Move to the next row

        RoundedButton feedbackButton = new RoundedButton("Feedback");
        feedbackButton.setPreferredSize(new Dimension(150, 50));
        feedbackButton.setFont(new Font("Arial", Font.BOLD, 18));
        feedbackButton.setBackground(new Color(65, 131, 215));
        feedbackButton.addActionListener(this);
        feedbackButton.setFocusable(false);
        centerPanel.add(feedbackButton, gbc);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(contentPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerDashboard::new);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == bookingButton) {
            new BookRoom();
        } else if (actionEvent.getSource() == checkoutButton) {
            new Bookings();
        } else {
            new Revenue();
        }
    }
}
