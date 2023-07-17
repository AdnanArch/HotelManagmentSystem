package screens;

import components.RoundedButton;
import connection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class AddNewRoom extends JFrame implements ActionListener {
    private static RoundedButton addButton;
    private static RoundedButton updateButton;
    private static JComboBox<String> roomComboBox;
    private static JComboBox<Integer> roomCapacityComboBox;
    private static JTextField rentTextField;
    private static JComboBox<String> roomStatusComboBox;
    private static JTextArea roomDescriptionTextArea;
    private int roomNo;

    public AddNewRoom() {
        super("New Room");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(58, 109, 122));
        add(mainPanel);

        JLabel addRoomLabel = new JLabel("Add New Room");
        addRoomLabel.setBounds(440, 20, 190, 40);
        addRoomLabel.setFont(new Font("Arial", Font.BOLD, 22));
        addRoomLabel.setForeground(Color.WHITE.brighter());
        mainPanel.add(addRoomLabel);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeLabel.setBounds(110, 90, 200, 30);
        roomTypeLabel.setForeground(Color.WHITE.brighter());
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomTypeLabel);

        String[] roomTypes = {"Single", "Double", "Suite", "Family", "Deluxe", "Executive", "Penthouse", "Standard", "Economy"};
        roomComboBox = new JComboBox<>(roomTypes);
        roomComboBox.setBounds(240, 90, 260, 35);
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomComboBox);

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setBounds(540, 90, 200, 30);
        capacityLabel.setForeground(Color.WHITE.brighter());
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(capacityLabel);

        Integer[] capacityValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        roomCapacityComboBox = new JComboBox<>(capacityValues);
        roomCapacityComboBox.setBounds(650, 90, 260, 35);
        roomCapacityComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomCapacityComboBox);

        JLabel rentLabel = new JLabel("Rent");
        rentLabel.setBounds(110, 150, 200, 30);
        rentLabel.setForeground(Color.WHITE.brighter());
        rentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(rentLabel);

        rentTextField = new JTextField();
        rentTextField.setBounds(240, 150, 260, 35);
        rentTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(rentTextField);

        JLabel roomStatusLabel = new JLabel("Status");
        roomStatusLabel.setBounds(540, 150, 200, 30);
        roomStatusLabel.setForeground(Color.WHITE.brighter());
        roomStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomStatusLabel);

        String[] roomStatusOptions = {"Available", "Occupied", "Maintenance"};
        roomStatusComboBox = new JComboBox<>(roomStatusOptions);
        roomStatusComboBox.setBounds(650, 150, 260, 35);
        roomStatusComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomStatusComboBox);

        JLabel roomDescriptionLabel = new JLabel("Description");
        roomDescriptionLabel.setBounds(110, 210, 200, 30);
        roomDescriptionLabel.setForeground(Color.WHITE.brighter());
        roomDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomDescriptionLabel);

        roomDescriptionTextArea = new JTextArea();
        roomDescriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 17));
        JScrollPane descriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
        descriptionScrollPane.setBounds(240, 210, 670, 150);
        mainPanel.add(descriptionScrollPane);

        addButton = new RoundedButton("Add Room");
        addButton.setBounds(760, 380, 150, 40);
        addButton.setBackground(new Color(136, 208, 219));
        addButton.setFont(new Font("Arial", Font.BOLD, 17));
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        updateButton = new RoundedButton("Update");
        updateButton.setBounds(760, 380, 150, 40);
        updateButton.setBackground(new Color(136, 208, 219));
        updateButton.setFont(new Font("Arial", Font.BOLD, 17));
        updateButton.setFocusable(false);
        updateButton.addActionListener(this);
        mainPanel.add(updateButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
        setSize(1000, 500);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - getWidth()) / 2;
        int centerY = (screenSize.height - getHeight()) / 2;

        // Set the location of the frame
        setLocation(centerX, centerY);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AddNewRoom();
    }

    public void setUpdateButtonVisibility() {
        addButton.setVisible(false);
    }

    public void setRoomComboBox(String roomType) {
        roomComboBox.setSelectedItem(roomType);
    }

    public void setRoomCapacityComboBox(int capacity) {
        roomCapacityComboBox.setSelectedItem(capacity);
    }

    public void setRentTextField(double rent) {
        rentTextField.setText(String.valueOf(rent));
    }

    public void setRoomStatusComboBox(String status) {
        roomCapacityComboBox.setSelectedItem(status);
    }

    public void setRoomDescriptionTextArea(String description) {
        roomDescriptionTextArea.setText(description);
    }

    public void setAddRoomButtonVisibility() {
        addButton.setVisible(false);
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        String roomType = (String) roomComboBox.getSelectedItem();
        int roomCapacity = (int) Objects.requireNonNull(roomCapacityComboBox.getSelectedItem());
        double roomRent = Double.parseDouble(rentTextField.getText());
        String roomStatus = Objects.requireNonNull(roomStatusComboBox.getSelectedItem()).toString();
        String roomDescription = roomDescriptionTextArea.getText();

        if (actionEvent.getSource() == addButton) {

            boolean result = insertRoomData(roomType, roomCapacity, roomRent, roomStatus, roomDescription);
            if (result) {
                JOptionPane.showMessageDialog(this, "Successfully Room Added");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Information");
            }
            // new line added
            // closing old instance first then opening new instance
            /*Rooms testRoom = Rooms.getInstance();
            testRoom.dispose();
            Rooms.disposeObj();
            // regular code
            testRoom = Rooms.getInstance();*/
            FetchRoomDetails testRoom = new FetchRoomDetails();
            testRoom.setTableModel(Rooms.tableModel);

            testRoom.fetchAndDisplayRoomDetails();
//            testRoom.setVisible(true);
            this.dispose();
        } else if (actionEvent.getSource() == updateButton) {
//            System.out.println(roomNo);
            boolean isUpdated = updateRoom(roomNo, roomType, roomCapacity, roomRent, roomStatus, roomDescription);
            if (isUpdated) {

                JOptionPane.showMessageDialog(null, "Room Updated Successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update room.", "Error", JOptionPane.ERROR_MESSAGE);
            }

//            Rooms room = Rooms.getInstance();
//            room.fetchAndDisplayRoomDetails();
//            room.setVisible(true);
            FetchRoomDetails room = new FetchRoomDetails();
            room.setTableModel(Rooms.tableModel);
            room.fetchAndDisplayRoomDetails();

            this.dispose();
        }
    }

    public boolean updateRoom(int roomNo, String roomType, int capacity, double rent, String status, String description) {
        boolean isUpdated = false;

        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL sp_update_room(?, ?, ?, ?, ?, ?, ?)}");

            statement.setString(1, String.valueOf(roomNo));
            statement.setString(2, status);
            statement.setString(3, roomType);
            statement.setString(4, String.valueOf(capacity));
            statement.setString(5, String.valueOf(rent));
            statement.setString(6, description);

            statement.registerOutParameter(7, Types.BOOLEAN);
            statement.executeQuery();
            isUpdated = statement.getBoolean(7);

            db.connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    public boolean insertRoomData(String roomType, int capacity, double rent, String roomStatus, String roomDescription) {
        boolean result = false;
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            DatabaseConnection dbConnection = new connection.DatabaseConnection();

            // Create the SQL query
            CallableStatement statement = dbConnection.connection.prepareCall("{call sp_insert_room_data(?, ?, ?, ?, ?, ?)}");
            statement.setString(1, roomType);
            statement.setString(2, roomStatus);
            statement.setString(3, String.valueOf(rent));
            statement.setString(4, String.valueOf(capacity));
            statement.setString(5, roomDescription);
            statement.registerOutParameter(6, Types.BOOLEAN);

            // Execute the statement
            statement.execute();
            result = statement.getBoolean(6);
            dbConnection.connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
