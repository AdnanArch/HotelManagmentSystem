package screens;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Rooms extends JFrame {
    private final DefaultTableModel tableModel;

    public Rooms() {
        super("Room Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 170, 1500, 850);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(58, 109, 122));
        add(mainPanel);

        JLabel addRoomLabel = new JLabel("Add New Room");
        addRoomLabel.setBounds(370, 20, 190, 40);
        addRoomLabel.setFont(new Font("Arial", Font.BOLD, 22));
        addRoomLabel.setForeground(Color.WHITE.brighter());
        mainPanel.add(addRoomLabel);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeLabel.setBounds(60, 90, 200, 30);
        roomTypeLabel.setForeground(Color.WHITE.brighter());
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomTypeLabel);

        String[] roomTypes = {"Single", "Double", "Suite", "Family", "Deluxe", "Executive", "Penthouse", "Standard", "Economy"};
        JComboBox<String> roomComboBox = new JComboBox<>(roomTypes);
        roomComboBox.setBounds(190, 90, 260, 35);
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomComboBox);

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setBounds(490, 90, 200, 30);
        capacityLabel.setForeground(Color.WHITE.brighter());
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(capacityLabel);

        Integer[] capacityValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> roomCapacityComboBox = new JComboBox<>(capacityValues);
        roomCapacityComboBox.setBounds(600, 90, 260, 35);
        roomCapacityComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomCapacityComboBox);

        JLabel rentLabel = new JLabel("Rent");
        rentLabel.setBounds(60, 150, 200, 30);
        rentLabel.setForeground(Color.WHITE.brighter());
        rentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(rentLabel);

        JTextField rentTextField = new JTextField();
        rentTextField.setBounds(190, 150, 260, 35);
        rentTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(rentTextField);

        JLabel roomStatusLabel = new JLabel("Status");
        roomStatusLabel.setBounds(490, 150, 200, 30);
        roomStatusLabel.setForeground(Color.WHITE.brighter());
        roomStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomStatusLabel);

        String[] roomStatusOptions = {"Available", "Occupied", "Maintenance"};
        JComboBox<String> roomStatusComboBox = new JComboBox<>(roomStatusOptions);
        roomStatusComboBox.setBounds(600, 150, 260, 35);
        roomStatusComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomStatusComboBox);

        JLabel roomDescriptionLabel = new JLabel("Description");
        roomDescriptionLabel.setBounds(60, 210, 200, 30);
        roomDescriptionLabel.setForeground(Color.WHITE.brighter());
        roomDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomDescriptionLabel);

        JTextArea roomDescriptionTextArea = new JTextArea();
        JScrollPane descriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
        descriptionScrollPane.setBounds(190, 210, 670, 200);
        mainPanel.add(descriptionScrollPane);

        JButton addButton = new JButton("Add Room");
        addButton.setBounds(60, 440, 150, 40);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomType = (String) roomComboBox.getSelectedItem();
                int capacity = (int) roomCapacityComboBox.getSelectedItem();
                double rent = Double.parseDouble(rentTextField.getText());
                String roomStatus = (String) roomStatusComboBox.getSelectedItem();
                String roomDescription = roomDescriptionTextArea.getText();

                insertRoomData(roomType, capacity, rent, roomStatus, roomDescription);

                // Refresh the table with updated data
                fetchAndRefreshDataFromDatabase();
            }
        });
        mainPanel.add(addButton);

        // Create a table model to hold the data
        tableModel = new DefaultTableModel();

        // Create the table with the table model
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JComponent) {
                    JComponent jComponent = (JComponent) component;
                    jComponent.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(25); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        // Set table properties
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 500, 1380, 300);
        mainPanel.add(scrollPane);

        // Fetch data from the database and populate the table
        fetchAndRefreshDataFromDatabase();

        setVisible(true);
    }

    private void fetchAndRefreshDataFromDatabase() {
        tableModel.setRowCount(0); // Clear the existing table data

        try {
            // Create an instance of DatabaseConnection to establish the database connection
            connection.DatabaseConnection dbConnection = new connection.DatabaseConnection();

            // Create the SQL query
            String sqlQuery = "SELECT R.room_no, R.room_type, R.room_status, RT.capacity, RT.rent, RT.description " +
                    "FROM rooms AS R JOIN room_types AS RT USING (room_type) ORDER BY room_no";

            // Execute the query
            java.sql.ResultSet resultSet = dbConnection.statement.executeQuery(sqlQuery);

            // Get the metadata of the result set
            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();

            // Get the number of columns in the result set
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Close the result set and database connection
            resultSet.close();
            dbConnection.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertRoomData(String roomType, int capacity, double rent, String roomStatus, String roomDescription) {
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            connection.DatabaseConnection dbConnection = new connection.DatabaseConnection();

            // Create the SQL query
            String sqlQuery = "INSERT INTO rooms (room_type, room_status) VALUES (?, ?)";

            // Prepare the statement
            java.sql.PreparedStatement preparedStatement = dbConnection.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, roomType);
            preparedStatement.setString(2, roomStatus);

            // Execute the statement
            preparedStatement.executeUpdate();

            // Close the statement and database connection
            preparedStatement.close();
            dbConnection.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Rooms::new);
    }
}
