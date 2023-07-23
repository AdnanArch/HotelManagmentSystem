package screens;

import components.RoundedButton;
import connection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FeedbackSubmissionFrame extends JFrame {
    private final JComboBox<Integer> starsComboBox;
    private final JTextArea reviewTextArea;
    private final JTextField bookingTextField;

    FeedbackSubmissionFrame() {
        setTitle("Feedback");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 470);
        setLayout(new FlowLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel bookingLabel = new JLabel("Enter Booking Id:");
        bookingLabel.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        bookingLabel.setPreferredSize(new Dimension(295, 50));
        add(bookingLabel);

        bookingTextField = new JTextField();
        bookingTextField.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        bookingTextField.setPreferredSize(new Dimension(100, 25));
        add(bookingTextField);

        JLabel starsLabel = new JLabel("Select the number of stars:");
        starsLabel.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        starsLabel.setPreferredSize(new Dimension(360, 50));
        add(starsLabel);

        Integer[] starOptions = {1, 2, 3, 4, 5};
        starsComboBox = new JComboBox<>(starOptions);
        starsComboBox.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        add(starsComboBox);

        reviewTextArea = new JTextArea("Leave your comments here", 10, 30);
        reviewTextArea.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        reviewTextArea.setLineWrap(true); // Enable line wrapping
        reviewTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(reviewTextArea); // Add the text area to a scroll pane

        // Set preferred size for the scroll pane
        scrollPane.setPreferredSize(new Dimension(400, 250));

        add(scrollPane);

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.addActionListener(e -> {
            // Validate booking ID input
            String bookingIdText = bookingTextField.getText().trim();
            if (bookingIdText.isEmpty()) {
                // Show error dialog for empty booking ID
                showErrorDialog("Booking ID cannot be empty.");
                return;
            }

            int bookingId;
            try {
                bookingId = Integer.parseInt(bookingIdText);
            } catch (NumberFormatException ex) {
                // Show error dialog for invalid booking ID format
                showErrorDialog("Invalid Booking ID format. Please enter a valid integer.");
                return;
            }

            // Check if the booking ID is valid using the stored procedure
            boolean isValidBookingId = checkValidBookingId(bookingId);
            if (!isValidBookingId) {
                // Show error dialog for invalid booking ID
                showErrorDialog("Invalid Booking ID. Please enter a valid booking ID.");
                return;
            }

            // Check if feedback has already been given for the booking ID
            boolean isFeedbackGiven = checkFeedbackGiven(bookingId);
            if (isFeedbackGiven) {
                // Show error dialog for duplicate feedback
                showErrorDialog("Feedback has already been given for this booking ID.");
                return;
            }

            Integer selectedStars = (Integer) starsComboBox.getSelectedItem(); // Cast to Integer

            if (selectedStars != null) {
                int stars = selectedStars;
                String review = reviewTextArea.getText();

                // Insert the feedback data using the stored procedure
                insertFeedbackData(bookingId, stars, review);

                // Show success message
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close the feedback window after submitting
                dispose();
            } else {
                // Show warning dialog for no stars selected
                showWarningDialog("Please select the number of stars.");
            }
        });

        add(submitButton);

        setVisible(true);
    }

    // Method to show an error dialog
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Method to show a warning dialog
    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    // Method to check if the booking ID is valid using the stored procedure
    private boolean checkValidBookingId(int bookingId) {
        try {
            DatabaseConnection db = new DatabaseConnection();
             CallableStatement statement = db.connection.prepareCall("{CALL sp_check_valid_booking_id(?, ?)}");

            // Set the input parameter
            statement.setInt(1, bookingId);

            // Register the output parameter
            statement.registerOutParameter(2, Types.BOOLEAN);

            // Execute the stored procedure
            statement.execute();

            // Get the result from the output parameter
            return statement.getBoolean(2);

        } catch (SQLException ex) {
            // Handle any database errors
            ex.printStackTrace();
        }

        return false;
    }

    // Method to check if feedback has already been given for the booking ID using the stored procedure
    private boolean checkFeedbackGiven(int bookingId) {
        try {
            DatabaseConnection db = new DatabaseConnection();
             CallableStatement statement = db.connection.prepareCall("{CALL sp_check_if_feedback_given(?, ?)}");

            // Set the input parameter
            statement.setInt(1, bookingId);

            // Register the output parameter
            statement.registerOutParameter(2, Types.BOOLEAN);

            // Execute the stored procedure
            statement.execute();

            // Get the result from the output parameter
            return statement.getBoolean(2);

        } catch (SQLException ex) {
            // Handle any database errors
            ex.printStackTrace();
        }

        return false;
    }

    // Method to insert feedback data using the stored procedure
    private void insertFeedbackData(int bookingId, int stars, String review) {
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL sp_insert_feedback_data(?, ?, ?)}");

            // Set the input parameters
            statement.setInt(1, bookingId);
            statement.setInt(2, stars);
            statement.setString(3, review);

            // Execute the stored procedure
            statement.execute();

        } catch (SQLException ex) {
            // Handle any database errors
            ex.printStackTrace();
        }
    }
}
