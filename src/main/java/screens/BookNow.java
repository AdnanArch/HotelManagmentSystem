package screens;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import components.LoggedIn;
import components.ProgressLoader;
import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;
import email.EmailSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;


public class BookNow extends JFrame implements ActionListener {
    private static RoundedButton confirmButton;
    private static RoundedTextField roomNoTextField;
    private static RoundedTextField roomStatusTextField;
    private final JDateChooser startDateChooser;
    private final JDateChooser endDateChooser;
    private double roomPrice;

    public BookNow(int roomNo, int roomCapacity, String roomType, String roomStatus, String roomDescription) {
        super("Book Room");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(58, 109, 122));
        add(mainPanel);

        JLabel addRoomLabel = new JLabel("Book Room");
        addRoomLabel.setBounds(400, 20, 190, 40);
        addRoomLabel.setFont(new Font("Arial", Font.BOLD, 22));
        addRoomLabel.setForeground(Color.WHITE.brighter());
        mainPanel.add(addRoomLabel);

        JLabel roomNoLabel = new JLabel("Room No");
        roomNoLabel.setBounds(90, 90, 200, 30);
        roomNoLabel.setForeground(Color.WHITE.brighter());
        roomNoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomNoLabel);

        roomNoTextField = new RoundedTextField();
        roomNoTextField.setBounds(220, 90, 260, 35);
        roomNoTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        roomNoTextField.setText(String.valueOf(roomNo));
        roomNoTextField.setEditable(false);
        mainPanel.add(roomNoTextField);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeLabel.setBounds(520, 90, 200, 30);
        roomTypeLabel.setForeground(Color.WHITE.brighter());
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomTypeLabel);

        RoundedTextField roomTypeTextField = new RoundedTextField();
        roomTypeTextField.setBounds(650, 90, 260, 35);
        roomTypeTextField.setText(roomType);
        roomTypeTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        roomTypeTextField.setEditable(false);
        mainPanel.add(roomTypeTextField);

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setBounds(90, 150, 200, 30);
        capacityLabel.setForeground(Color.WHITE.brighter());
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(capacityLabel);

        RoundedTextField roomCapacityTextField = new RoundedTextField();
        roomCapacityTextField.setBounds(220, 150, 260, 35);
        roomCapacityTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        roomCapacityTextField.setText(String.valueOf(roomCapacity));
        roomCapacityTextField.setEditable(false);
        mainPanel.add(roomCapacityTextField);

        JLabel roomStatusLabel = new JLabel("Status");
        roomStatusLabel.setBounds(520, 150, 200, 30);
        roomStatusLabel.setForeground(Color.WHITE.brighter());
        roomStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomStatusLabel);

        roomStatusTextField = new RoundedTextField();
        roomStatusTextField.setBounds(650, 150, 260, 35);
        roomStatusTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        roomStatusTextField.setText(roomStatus);
        roomStatusTextField.setEditable(false);
        mainPanel.add(roomStatusTextField);

        JLabel roomDescriptionLabel = new JLabel("Description");
        roomDescriptionLabel.setBounds(90, 210, 200, 30);
        roomDescriptionLabel.setForeground(Color.WHITE.brighter());
        roomDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomDescriptionLabel);

        JTextArea roomDescriptionTextArea = new JTextArea();
        roomDescriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 17));
        roomDescriptionTextArea.setText(roomDescription);
        roomDescriptionTextArea.setEditable(false);
        JScrollPane descriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
        descriptionScrollPane.setBounds(220, 210, 690, 100);
        mainPanel.add(descriptionScrollPane);

        JLabel checkInLabel = new JLabel("Check In");
        checkInLabel.setBounds(90, 335, 200, 30);
        checkInLabel.setForeground(Color.WHITE.brighter());
        checkInLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(checkInLabel);

        startDateChooser = new JDateChooser();
        startDateChooser.setBounds(220, 330, 260, 35);
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        JTextFieldDateEditor startDateEditor = (JTextFieldDateEditor) startDateChooser.getDateEditor();
        startDateEditor.setFont(new Font("Arial", Font.PLAIN, 13));
        startDateChooser.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(startDateChooser);

        JLabel checkOutLabel = new JLabel("Check Out");
        checkOutLabel.setBounds(520, 335, 200, 30);
        checkOutLabel.setForeground(Color.WHITE.brighter());
        checkOutLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(checkOutLabel);

        endDateChooser = new JDateChooser();
        endDateChooser.setBounds(650, 330, 260, 35);
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        JTextFieldDateEditor endDateEditor = (JTextFieldDateEditor) endDateChooser.getDateEditor();
        endDateEditor.setFont(new Font("Arial", Font.PLAIN, 13));
        endDateChooser.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(endDateChooser);

        confirmButton = new RoundedButton("Confirm");
        confirmButton.setBounds(760, 380, 150, 40);
        confirmButton.setBackground(new Color(136, 208, 219));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 17));
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(this);
        mainPanel.add(confirmButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(1000, 500);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - getWidth()) / 2;
        int centerY = (screenSize.height - getHeight()) / 2;

        // Set the location of the frame
        setLocation(centerX, centerY);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        int roomNo = Integer.parseInt(roomNoTextField.getText());
        String roomStatus = roomStatusTextField.getText();
        java.util.Date startDateUtil = startDateChooser.getDate();
        java.util.Date endDateUtil = endDateChooser.getDate();
        int customerID;
        try {
            customerID = LoggedIn.getCustomerID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (startDateUtil == null || endDateUtil == null) {
            JOptionPane.showMessageDialog(null, "Please select both check-in and check-out dates.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date startDate = new Date(startDateUtil.getTime());
        Date endDate = new Date(endDateUtil.getTime());


        int dateComparisonResult = startDate.compareTo(endDate);


        if (actionEvent.getSource() == confirmButton) {
            try {
                DatabaseConnection db = new DatabaseConnection();
                // Create a CallableStatement
                String sql = "{ ? = CALL get_booking_price(?, ?, ?) }";
                CallableStatement statement = db.connection.prepareCall(sql);

                statement.registerOutParameter(1, Types.FLOAT);
                statement.setInt(2, roomNo);
                statement.setDate(3, startDate);
                statement.setDate(4, endDate);

                // Execute the function call
                statement.execute();

                // Retrieve the result
                roomPrice = statement.getFloat(1);

                statement.close();
                db.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dateComparisonResult > 0) {
                JOptionPane.showMessageDialog(null, "Your check out date is earlier than check in date.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int userSelection = JOptionPane.showConfirmDialog(this, "Your Total bill for booking is " + roomPrice + "\nDo you want to proceed?");
                if (userSelection == JOptionPane.YES_OPTION) {
                    try {
                        sendBookingRequestEmailToAdminAndUser(roomNo, customerID, startDate, endDate, roomPrice);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Ok sir thanks");
                }
            }


        }
    }


    public void insertBookingData(int roomNo, int customerID, Date startDate, Date endDate, double rent) {
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            DatabaseConnection dbConnection = new connection.DatabaseConnection();

            // Create the SQL query
            CallableStatement statement = dbConnection.connection.prepareCall("{call sp_insert_booking_data(?, ?, ?, ?, ?, ?)}");
            statement.setString(1, String.valueOf(roomNo));
            statement.setString(2, String.valueOf(customerID));
            statement.setString(3, String.valueOf(startDate));
            statement.setString(4, String.valueOf(endDate));
            statement.setFloat(5, (float) rent);
            statement.registerOutParameter(6, Types.INTEGER);

            // Execute the statement
            statement.execute();

            int bookingID = statement.getInt(6);
            JOptionPane.showMessageDialog(this, "Your booking request has been sent against Booking ID " + bookingID + " .");
            this.dispose();
            statement.close();
            dbConnection.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendBookingRequestEmailToAdminAndUser(int roomNo, int customerID, Date startDate, Date endDate, double rent) throws SQLException {
        // set the email contents to send notification to admin
        String subject = "Room Booking Request";
        String messageForAdmin = "The customer with Name " + LoggedIn.getCustomerName() + " and having customer ID " + LoggedIn.getCustomerID() + " has requested for the booking of room no " + roomNo + ".";

        // TODO: set your own admin email from which you want to send email.
        String fromAdmin = "your_admin_emai@gmail.com";
        String adminTo = "your_admin_emai@gmail.com";

        // set the email contents to send notification to customer
        String messageForCustomer = "You have requested for the booking of room no " + roomNo + ".";
        String toCustomer = LoggedIn.getCustomerEmail();

        // open the progress loader
        ProgressLoader progressLoader = new ProgressLoader();
        progressLoader.showLoadingDialog("Please wait the booking request is being sent");

        // start a thread
        Thread bookingRequestThread = new Thread(() -> {
            try {
                EmailSender emailSender = new EmailSender();
                boolean adminEmailSentStatus = emailSender.sendEmail(adminTo, fromAdmin, subject, messageForAdmin);
                boolean customerEmailSentStatus = emailSender.sendEmail(toCustomer, fromAdmin, subject, messageForCustomer);
                if (adminEmailSentStatus && customerEmailSentStatus) {
                    SwingUtilities.invokeLater(() -> {
                        progressLoader.hideLoadingDialog();
                        insertBookingData(roomNo, customerID, startDate, endDate, rent);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        progressLoader.hideLoadingDialog();
                        showErrorMessage("Use stable internet connection. Try again.");
                    });
                }
            } catch (Exception e) {
                showErrorMessage(e.getMessage());
            }
        });

        bookingRequestThread.start();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
