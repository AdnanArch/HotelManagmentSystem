package screens;

import components.JTableButtonModel;
import components.JTableButtonRenderer;
import components.RoundedButton;
import connection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class NewRoom extends JFrame implements ActionListener {


// TODO: Add buttons Edit Rooms and Delete Room in the table.
// TODO: Baba g code ko kam se kam kr den. Shukria
    // Check kr boss
    private static JTableButtonModel tableButtonModel;
    private static RoundedButton searchButton;
    private static RoundedButton addButton;
    private static JComboBox<String> roomComboBox;
    private static JComboBox<Integer> roomCapacityComboBox;
    private static JTextField rentTextField;
    private static JComboBox<String> roomStatusComboBox;
    private static JTextArea roomDescriptionTextArea;
    private static JTextField searchField;

    private static TableCellRenderer tableRenderer;




    public NewRoom() {
        super("Room Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 170, 1500, 850);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(58, 109, 122));
        add(mainPanel);

        JLabel addRoomLabel = new JLabel("Add New Room");
        addRoomLabel.setBounds(640, 20, 190, 40);
        addRoomLabel.setFont(new Font("Arial", Font.BOLD, 22));
        addRoomLabel.setForeground(Color.WHITE.brighter());
        mainPanel.add(addRoomLabel);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeLabel.setBounds(310, 90, 200, 30);
        roomTypeLabel.setForeground(Color.WHITE.brighter());
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomTypeLabel);

        String[] roomTypes = {"Single", "Double", "Suite", "Family", "Deluxe", "Executive", "Penthouse", "Standard", "Economy"};
        roomComboBox = new JComboBox<>(roomTypes);
        roomComboBox.setBounds(440, 90, 260, 35);
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomComboBox);

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setBounds(740, 90, 200, 30);
        capacityLabel.setForeground(Color.WHITE.brighter());
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(capacityLabel);

        Integer[] capacityValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        roomCapacityComboBox = new JComboBox<>(capacityValues);
        roomCapacityComboBox.setBounds(850, 90, 260, 35);
        roomCapacityComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomCapacityComboBox);

        JLabel rentLabel = new JLabel("Rent");
        rentLabel.setBounds(310, 150, 200, 30);
        rentLabel.setForeground(Color.WHITE.brighter());
        rentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(rentLabel);

        rentTextField = new JTextField();
        rentTextField.setBounds(440, 150, 260, 35);
        rentTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(rentTextField);

        JLabel roomStatusLabel = new JLabel("Status");
        roomStatusLabel.setBounds(740, 150, 200, 30);
        roomStatusLabel.setForeground(Color.WHITE.brighter());
        roomStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomStatusLabel);

        String[] roomStatusOptions = {"Available", "Occupied", "Maintenance"};
        roomStatusComboBox = new JComboBox<>(roomStatusOptions);
        roomStatusComboBox.setBounds(850, 150, 260, 35);
        roomStatusComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomStatusComboBox);

        JLabel roomDescriptionLabel = new JLabel("Description");
        roomDescriptionLabel.setBounds(310, 210, 200, 30);
        roomDescriptionLabel.setForeground(Color.WHITE.brighter());
        roomDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomDescriptionLabel);

        roomDescriptionTextArea = new JTextArea();
        roomDescriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 17));
        JScrollPane descriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
        descriptionScrollPane.setBounds(440, 210, 670, 150);
        mainPanel.add(descriptionScrollPane);

        addButton = new RoundedButton("Add Room");
        addButton.setBounds(965, 380, 150, 40);
        addButton.setBackground(new Color(136, 208, 219));
        addButton.setFont(new Font("Arial", Font.BOLD, 17));
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        searchField = new JTextField();
        searchField.setBounds(60, 450, 250, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(searchField);

        searchButton = new RoundedButton("Search");
        searchButton.setBounds(320, 450, 130, 40);
        searchButton.setBackground(new Color(136, 208, 219));
        searchButton.setFont(new Font("Arial", Font.BOLD, 17));
        searchButton.setFocusable(false);
        searchButton.addActionListener(this);
        mainPanel.add(searchButton);

        RoundedButton clearButton = new RoundedButton("Clear");
        clearButton.setBounds(470, 450, 130, 40);
        clearButton.setBackground(new Color(136, 208, 219));
        clearButton.setFont(new Font("Arial", Font.BOLD, 17));
        clearButton.setFocusable(false);
        clearButton.addActionListener(this);
        mainPanel.add(clearButton);

        // Create a table model to hold the data
//        tableModel = new DefaultTableModel();
        tableButtonModel = new JTableButtonModel();
        fetchAndRefreshDataFromDatabase();


        JTable table2 = new JTable(tableButtonModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JComponent jComponent) {
                    jComponent.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the desired font size
//                    jComponent.setBackground(Color.BLACK.brighter());
                }
                return component;
            }
        };
        tableRenderer = table2.getDefaultRenderer(JButton.class);
        table2.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));


        table2.setRowHeight(25); // Set the desired row height
        table2.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        // Set table2 properties
        table2.setFillsViewportHeight(true);
//        table2.setAutoResizeMode(Jtable2.AUTO_RESIZE_ALL_COLUMNS);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table2);
        scrollPane.setBounds(60, 500, 1380, 300);
        mainPanel.add(scrollPane);

        // Fetch data from the database and populate the table
//      TODO : new code
//        tableRenderer = table2.getDefaultRenderer(JButton.class);
//        table2.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NewRoom::new);
    }

    private void fetchAndRefreshDataFromDatabase() {
        clearTableData(); // Clear the existing table data
        clearTableColumns();
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            DatabaseConnection dbConnection = new DatabaseConnection();

            CallableStatement statement = dbConnection.connection.prepareCall("{call sp_get_rooms_details()}");

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Get the metadata of the result set
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Get the number of columns in the result set
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableButtonModel.addColumn(metaData.getColumnName(i));
            }

            tableButtonModel.addColumn("Edit");
            tableButtonModel.addColumn("Delete");


            // Add rows to the table model
            while (resultSet.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
//                    rowData[i - 1] = resultSet.getObject(i);
                    rowData.add(resultSet.getObject(i));
                }
                rowData.add( new RoundedButton("Edit"));
                rowData.add( new RoundedButton("Del"));
//                rowData[columnCount+1] = new JButton("Delete");
                tableButtonModel.addRow(rowData);
            }

            // Close the result set and database connection
            resultSet.close();
            dbConnection.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == addButton) {
            String roomType = (String) roomComboBox.getSelectedItem();
            int roomCapacity = (int) Objects.requireNonNull(roomCapacityComboBox.getSelectedItem());
            double roomRent = Double.parseDouble(rentTextField.getText());
            String roomStatus = Objects.requireNonNull(roomStatusComboBox.getSelectedItem()).toString();
            String roomDescription = roomDescriptionTextArea.getText();

            boolean result = insertRoomData(roomType, roomCapacity, roomRent, roomStatus, roomDescription);
            if (result) {
                JOptionPane.showMessageDialog(this, "Successfully Room Added");
            }else {
                JOptionPane.showMessageDialog(this, "Invalid Information");
            }

            // Refresh the table with updated data
            fetchAndRefreshDataFromDatabase();
        } else if (actionEvent.getSource() == searchButton) {
            String searchTerm = searchField.getText();
            clearTableColumns();
            clearTableData(); // Clear the existing table data

            try {
                // Create an instance of DatabaseConnection to establish the database connection
                DatabaseConnection dbConnection = new DatabaseConnection();

                // Create the SQL query with a WHERE clause to search for matching rooms
                String sqlQuery = "SELECT R.room_no, RT.room_type, R.room_status, RT.capacity, RT.rent, RT.description " + "FROM rooms AS R JOIN room_types AS RT USING (type_id) " + "WHERE R.room_no LIKE ? OR RT.room_type LIKE ?";

                // Prepare the statement
                PreparedStatement preparedStatement = dbConnection.connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, "%" + searchTerm + "%"); // Use wildcard '%' to match any part of the room number
                preparedStatement.setString(2, "%" + searchTerm + "%"); // Use wildcard '%' to match any part of the room type

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Get the metadata of the result set
                ResultSetMetaData metaData = resultSet.getMetaData();

                // Get the number of columns in the result set
                int columnCount = metaData.getColumnCount();

                // Add column names to the table model
                for (int i = 1; i <= columnCount; i++) {
                    tableButtonModel.addColumn(metaData.getColumnName(i));
                }

                // Add rows to the table model
                while (resultSet.next()) {
                    Vector<Object> rowData = new Vector<Object>();
                    for (int i = 1; i <= columnCount; i++) {
//                    rowData[i - 1] = resultSet.getObject(i);
                        rowData.add(resultSet.getObject(i));
                    }
                    tableButtonModel.addRow(rowData);
                }

                resultSet.close();// Close the result set, statement, and database connection
                preparedStatement.close();
                dbConnection.connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            clearTableColumns();
            searchField.setText("");
            tableButtonModel.setRowCount(0);
            fetchAndRefreshDataFromDatabase();
        }
    }
    private void clearTableColumns() {
        while (tableButtonModel.getColumnCount() > 0) {
            tableButtonModel.setColumnCount(0);
        }
    }
    private void clearTableData() {
        tableButtonModel.setRowCount(0);
    }

    private boolean insertRoomData(String roomType, int capacity, double rent, String roomStatus, String roomDescription) {
        boolean result = false;
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            DatabaseConnection dbConnection = new connection.DatabaseConnection();

            // Create the SQL query
            // String sqlQuery = "INSERT INTO rooms (room_type, room_status) VALUES (?, ?)";
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

