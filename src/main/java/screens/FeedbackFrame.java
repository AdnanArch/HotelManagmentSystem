package screens;

import components.RoundedButton;
import connection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class FeedbackFrame extends JFrame {
    private final boolean isCustomerLoggedIn; // Flag to indicate whether a customer has logged in

    public FeedbackFrame(boolean isCustomerLoggedIn) {
        super("User Feedbacks");
        this.isCustomerLoggedIn = isCustomerLoggedIn; // Set the flag
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Set a new color scheme for the frame
        Color backgroundColor = new Color(240, 245, 255); // Light Blue
        Color headerColor = new Color(52, 152, 219); // Dark Blue
        Color borderColor = new Color(189, 195, 199); // Light Gray

        // Create a panel to hold the feedback panels
        JPanel feedbacksPanel = new JPanel();
        feedbacksPanel.setLayout(new BoxLayout(feedbacksPanel, BoxLayout.Y_AXIS));
        feedbacksPanel.setBackground(backgroundColor);
        feedbacksPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fetch feedback data from the database
        ArrayList<FeedbackData> feedbackDataList = fetchDataFromDatabase();

        // Create custom panels for each feedback and add them to the feedbacksPanel
        for (FeedbackData feedbackData : feedbackDataList) {
            JPanel feedbackPanel = createFeedbackPanel(feedbackData);
            feedbacksPanel.add(feedbackPanel);
            feedbacksPanel.add(Box.createVerticalStrut(20));
        }

        // Add the feedbacksPanel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(feedbacksPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));

        // Add the scroll pane to the frame
        add(scrollPane);

        // Set the header panel with the heading
        JPanel header = new JPanel();
        header.setBackground(headerColor);
        JLabel headingLabel = new JLabel("Customer's Feedbacks");
        headingLabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);
        header.add(headingLabel);
        add(header, BorderLayout.NORTH);

        // If a customer has logged in, add the "Give Feedback" panel
        if (isCustomerLoggedIn) {
            JPanel giveFeedbackPanel = createGiveFeedbackPanel();
            add(giveFeedbackPanel, BorderLayout.SOUTH);
        }

        // Display the frame
        setVisible(true);
    }

    // Method to create the panel containing the label and "Give Feedback" button
    private JPanel createGiveFeedbackPanel() {
        JPanel giveFeedbackPanel = new JPanel();
        giveFeedbackPanel.setBackground(Color.WHITE);

        JLabel promptLabel = new JLabel("Do you want to give Feedback?");
        promptLabel.setFont(new Font("sans-serif", Font.PLAIN, 18));
        giveFeedbackPanel.add(promptLabel);

        RoundedButton giveFeedbackButton = new RoundedButton("Give Feedback");
        giveFeedbackButton.setPreferredSize(new Dimension(200, 40));
        giveFeedbackButton.addActionListener(e -> {
            // Open the feedback submission window
            openFeedbackSubmission();
        });
        giveFeedbackPanel.add(giveFeedbackButton);

        // Set the layout to center the components
        giveFeedbackPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        return giveFeedbackPanel;
    }

    // Method to open the feedback submission window
    private void openFeedbackSubmission() {
        FeedbackSubmissionFrame submissionFrame = new FeedbackSubmissionFrame();
        submissionFrame.setVisible(true);
    }

    // Method to fetch feedback data from the database using the stored procedure
    private ArrayList<FeedbackData> fetchDataFromDatabase() {
        ArrayList<FeedbackData> feedbackDataList = new ArrayList<>();

        // JDBC objects for database connection and result set
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL sp_get_feedback_data()}");

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and create FeedbackData objects
            while (resultSet.next()) {
                String customerName = resultSet.getString("Name");
                int bookingId = resultSet.getInt("booking_id");
                String date = resultSet.getString("feedback_date");
                int stars = resultSet.getInt("stars");
                String comments = resultSet.getString("review");

                feedbackDataList.add(new FeedbackData(bookingId, customerName, stars, date, comments));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return feedbackDataList;
    }

    // Method to create a custom panel for each feedback
    private JPanel createFeedbackPanel(FeedbackData feedbackData) {
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setBackground(Color.WHITE);
        feedbackPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        feedbackPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Create labels for feedback details
        JLabel bookingIdLabel = new JLabel("<html><b>Booking ID:</b> " + feedbackData.bookingId() + "</html>");
        JLabel nameLabel = new JLabel("<html><b>Customer Name:</b> " + feedbackData.customerName() + "</html>");
        JLabel starsLabel = new JLabel("<html><b>Feedback Stars:</b> " + feedbackData.stars() + "</html>");
        JLabel dateLabel = new JLabel("<html><b>Feedback Date:</b> " + feedbackData.date() + "</html>");
        JLabel commentsLabel = new JLabel("<html><b>Description:</b> " + feedbackData.comments() + "</html>");

        // Set fonts and alignment
        Font boldFont = new Font("Helvetica Neue", Font.BOLD, 16);
        Font regularFont = new Font("Helvetica Neue", Font.PLAIN, 16);
        bookingIdLabel.setFont(boldFont);
        nameLabel.setFont(boldFont);
        starsLabel.setFont(boldFont);
        dateLabel.setFont(boldFont);
        commentsLabel.setFont(regularFont);

        // Set colors
        Color starColor = new Color(255, 215, 0); // Gold
        starsLabel.setForeground(starColor);

        // Add the labels to the feedback panel
        feedbackPanel.add(bookingIdLabel);
        feedbackPanel.add(nameLabel);
        feedbackPanel.add(dateLabel);
        feedbackPanel.add(starsLabel);
        feedbackPanel.add(commentsLabel);

        return feedbackPanel;
    }

    // Custom class to hold feedback data
    private record FeedbackData(int bookingId, String customerName, int stars, String date, String comments) {
    }

    public static void main(String[] args) {
        // Assume a customer has logged in for demonstration purposes
        SwingUtilities.invokeLater(() -> new FeedbackFrame(true));
    }
}
