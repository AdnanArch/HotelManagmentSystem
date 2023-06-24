package screens;

import components.RoundedButton;
import connection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class TestAdminDashboard extends JFrame implements ActionListener {
    private static RoundedButton roomButton;
    private static RoundedButton bookingButton;

    private final JLabel panel1NumberLabel;
    private final JLabel panel2NumberLabel;
    private final JLabel panel3NumberLabel;
    private final JLabel panel4NumberLabel;

    public TestAdminDashboard() {
        super("Admin Dashboard");

        // Creating the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(12, 55, 64));
        getContentPane().add(mainPanel);

        // Creating the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(12, 55, 64));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Creating the content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Creating and configuring the buttons
        roomButton = new RoundedButton("Rooms");
        roomButton.setFont(new Font("Arial", Font.BOLD, 18));
        roomButton.addActionListener(this);
        roomButton.setFocusable(false);
        buttonPanel.add(roomButton);

        bookingButton = new RoundedButton("Bookings");
        bookingButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookingButton.addActionListener(this);
        bookingButton.setFocusable(false);
        buttonPanel.add(bookingButton);

        RoundedButton revenueButton = new RoundedButton("Revenue");
        revenueButton.setFont(new Font("Arial", Font.BOLD, 18));
        revenueButton.addActionListener(this);
        revenueButton.setFocusable(false);
        buttonPanel.add(revenueButton);

        // Creating and configuring the labels for panel content
        // Labels for panel content
        JLabel panel1HeadingLabel = new JLabel("Revenue Earned");
        panel1HeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel1HeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1NumberLabel = new JLabel("0");
        panel1NumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel1NumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(createPanel(panel1HeadingLabel, panel1NumberLabel));

        JLabel panel2HeadingLabel = new JLabel("Available Rooms");
        panel2HeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel2HeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel2NumberLabel = new JLabel("0");
        panel2NumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel2NumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(createPanel(panel2HeadingLabel, panel2NumberLabel));

        JLabel panel3HeadingLabel = new JLabel("Current Bookings");
        panel3HeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel3HeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel3NumberLabel = new JLabel("0");
        panel3NumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel3NumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(createPanel(panel3HeadingLabel, panel3NumberLabel));

        JLabel panel4HeadingLabel = new JLabel("Total Bookings");
        panel4HeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel4HeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel4NumberLabel = new JLabel("0");
        panel4NumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel4NumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(createPanel(panel4HeadingLabel, panel4NumberLabel));

        updateLabels();

        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setSize(800, 600);


        // Creating the logout label
        JLabel logoutLabel = new JLabel("<html><i>Logout</i></html>");
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoutLabel.setBounds(670, 25, 100, 30);
        logoutLabel.setForeground(new Color(218, 215, 205));
        mainPanel.add(logoutLabel, BorderLayout.SOUTH);

        // Adding a mouse listener to the logout label
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
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


        setVisible(true);
    }

    JPanel createPanel(JLabel headingLabel, JLabel numberLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255));

        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(headingLabel, BorderLayout.NORTH);

        numberLabel.setFont(new Font("Arial", Font.BOLD, 24));
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(numberLabel, BorderLayout.CENTER);

        return panel;
    }
    private void updateLabels() {
        double totalRevenue = get_total_revenue();
        int availableRoomsCount = get_available_rooms_count();
        int currentBookings = get_current_bookings_count();
        int totalBookingsCount = get_total_bookings_count();

        panel1NumberLabel.setText(String.valueOf(totalRevenue));
        panel2NumberLabel.setText(String.valueOf(availableRoomsCount));
        panel3NumberLabel.setText(String.valueOf(currentBookings));
        panel4NumberLabel.setText(String.valueOf(totalBookingsCount));
    }

//     Database functions
    private double get_total_revenue() {
        double totalRevenue = 0;
        try{
            DatabaseConnection db = new DatabaseConnection();
            // Create a CallableStatement for calling the MySQL function
            CallableStatement statement = db.connection.prepareCall("{ ? = CALL get_total_revenue() }");

            // Register the output parameter
            statement.registerOutParameter(1, Types.DOUBLE);

            // Execute the CallableStatement
            statement.execute();

            // Get the result from the output parameter
            totalRevenue = statement.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return totalRevenue;
    }

    private int get_available_rooms_count() {
        int availableRoomsCount = 0;
        try {
            DatabaseConnection db = new DatabaseConnection();
            // Create a CallableStatement for calling the MySQL function
            CallableStatement statement = db.connection.prepareCall("{ ? = CALL get_available_rooms_count() }");

            // Register the output parameter
            statement.registerOutParameter(1, Types.INTEGER);

            // Execute the CallableStatement
            statement.execute();

            // Get the result from the output parameter
            availableRoomsCount = statement.getInt(1);
            System.out.println("Available rooms" + availableRoomsCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRoomsCount;
    }

    private int get_current_bookings_count() {
        int currentBookingsCount = 0;
        try {
            DatabaseConnection db = new DatabaseConnection();
            // Create a CallableStatement for calling the MySQL function
            CallableStatement statement = db.connection.prepareCall("{ ? = CALL get_current_bookings_count() }");

            // Register the output parameter
            statement.registerOutParameter(1, Types.INTEGER);

            // Execute the CallableStatement
            statement.execute();

            // Get the result from the output parameter
            currentBookingsCount = statement.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currentBookingsCount;
    }

    private int get_total_bookings_count() {
        int totalBookingsCount = 0;
        try {
            DatabaseConnection db = new DatabaseConnection();
            // Create a CallableStatement for calling the MySQL function
            CallableStatement statement = db.connection.prepareCall("{ ? = CALL get_total_bookings_count() }");

            // Register the output parameter
            statement.registerOutParameter(1, Types.INTEGER);

            // Execute the CallableStatement
            statement.execute();

            // Get the result from the output parameter
            totalBookingsCount = statement.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBookingsCount;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TestAdminDashboard::new);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == roomButton) {
            new Rooms();
        } else if (actionEvent.getSource() == bookingButton) {
            new Bookings();
        } else {
            new Revenue();
        }
    }
}
