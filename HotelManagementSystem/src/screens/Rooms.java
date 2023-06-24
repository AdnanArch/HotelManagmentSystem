package screens;

import components.RoundedTextField;
import connection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rooms extends JFrame {
    private static Rooms instance;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final RoundedTextField searchField;

    private final DatabaseConnection db = new DatabaseConnection();
    private int roomNo;

    Rooms() {
        setTitle("Room Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();

        JLabel searchRoomLabel = new JLabel("Search Room:");
        searchRoomLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchRoomLabel.setPreferredSize(new Dimension(130, 30));

        searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Arial", Font.BOLD, 15));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setFont(new Font("Arial", Font.BOLD, 15));
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            searchRoom(searchTerm);
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setFont(new Font("Arial", Font.BOLD, 15));
        clearButton.addActionListener(e -> clearSearch());

        JButton addNewRoomButton = new JButton("Add New Room");
        addNewRoomButton.setPreferredSize(new Dimension(160, 30));
        addNewRoomButton.setFont(new Font("Arial", Font.BOLD, 15));
        addNewRoomButton.addActionListener(e -> new AddNewRoom());

        topPanel.add(searchRoomLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);
        topPanel.add(addNewRoomButton);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Room No");
        tableModel.addColumn("Room Type");
        tableModel.addColumn("Capacity");
        tableModel.addColumn("Rent");
        tableModel.addColumn("Status");
        tableModel.addColumn("Description");
        tableModel.addColumn("Edit");
        tableModel.addColumn("Delete");

        table = new JTable(tableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JComponent jComponent) {
                    jComponent.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the desired font size
//                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(25); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField()));
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        fetchAndDisplayRoomDetails();

        setVisible(true);
    }

    public static Rooms getInstance() {
        if (instance == null) {
            instance = new Rooms();
        }
        return instance;
    }


    public static void disposeObj() {
        // Dispose of any resources or perform cleanup operations here
        instance = null;

    }





    private void refreshTableData() {
        tableModel.fireTableDataChanged();
    }

    public void fetchAndDisplayRoomDetails() {
        clearTableData();
        try {
            // Prepare the stored procedure call
            String storedProcedure = "{CALL sp_get_rooms_details()}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);

            renderRoomResultSet(statement);
            statement.close();

            // Refresh the table data
            refreshTableData();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void searchRoom(String searchTerm) {
        // Clear existing table data
        clearTableData();

        try {
            String storedProcedure = "{CALL sp_search_room(?)}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);
            statement.setString(1, searchTerm);

            renderRoomResultSet(statement);

            statement.close();

            // Refresh the table data
            refreshTableData();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void clearSearch() {
        searchField.setText("");
        clearTableData();
        fetchAndDisplayRoomDetails();
    }

    private void renderRoomResultSet(CallableStatement statement) {
        ResultSet resultSet;
        try {
            // Execute the stored procedure
            resultSet = statement.executeQuery();

            // Populate the table with data
            while (resultSet.next()) {
                int roomID = resultSet.getInt("room_no");
                String roomType = resultSet.getString("room_type");
                int capacity = resultSet.getInt("capacity");
                double rent = resultSet.getDouble("rent");
                String status = resultSet.getString("room_status");
                String description = resultSet.getString("description");

                // Add the row data to the table model
                tableModel.addRow(new Object[]{roomID, roomType, capacity, rent, status, description, "Edit", "Delete"});
            }
            resultSet.close();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void clearTableData() {
        tableModel.setRowCount(0);
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String buttonText;

        public ButtonEditor(JTextField textField) {
            super(textField);

            button = new JButton();
            button.setOpaque(true);
            buttonText = "";

            // Add ActionListener to the button
            button.addActionListener(e -> {
                // Retrieve the room number from the table data
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    roomNo = (int) table.getValueAt(selectedRow, 0);
                    String roomType = table.getValueAt(selectedRow, 1).toString();
                    int capacity = (int) table.getValueAt(selectedRow, 2);
                    double rent = (double) table.getValueAt(selectedRow, 3);
                    String status = table.getValueAt(selectedRow, 4).toString();
                    String description = table.getValueAt(selectedRow, 5).toString();

                    // Get the action command of the button
                    String actionCommand = button.getActionCommand();

                    // Perform the desired action based on the button clicked
                    if (actionCommand.equals("Edit")) {
                        System.out.println(roomNo);
                        AddNewRoom updateExistingRoom = new AddNewRoom();
                        updateExistingRoom.setRoomNo(roomNo);
                        updateExistingRoom.setRoomComboBox(roomType);
                        updateExistingRoom.setRoomCapacityComboBox(capacity);
                        updateExistingRoom.setRentTextField(rent);
                        updateExistingRoom.setRoomStatusComboBox(status);
                        updateExistingRoom.setRoomDescriptionTextArea(description);
                        updateExistingRoom.setAddRoomButtonVisibility();
                        updateExistingRoom.setRoomNo(roomNo);
                    } else if (actionCommand.equals("Delete")) {
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the room?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            deleteRoom(roomNo, selectedRow);
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            buttonText = (value == null) ? "" : value.toString();
            button.setText(buttonText);
            return button;
        }

        public Object getCellEditorValue() {
            return buttonText;
        }
    }

    public void deleteRoom(int roomNo, int selectedRow) {
        try {
            String storedProcedure = "{CALL sp_delete_room_by_no(?)}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);
            statement.setInt(1, roomNo);
            statement.executeUpdate();
            statement.close();

            JOptionPane.showMessageDialog(null, "Room deleted successfully.");
//            tableModel.setValueAt("null", selectedRow, tableModel.findColumn("Delete"));

//            tableModel.removeRow(selectedRow);


//            refreshTableData();
//            fetchAndDisplayRoomDetails();
//            dispose();
//            new Rooms();
            Rooms.disposeObj();
            Rooms.getInstance();

//            Rooms.getInstance().dispose();
//            new Rooms();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }


    private void handleSQLException(SQLException e) {
        JOptionPane.showMessageDialog(null, "An error occurred while accessing the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        Rooms.getInstance();
    }
}
