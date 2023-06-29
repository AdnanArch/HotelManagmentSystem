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

public class Bookings extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final RoundedTextField searchField;

    private final DatabaseConnection db = new DatabaseConnection();

    Bookings() {
        setTitle("Room Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1750, 900);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();

        JLabel searchRoomLabel = new JLabel("Search Booking:");
        searchRoomLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchRoomLabel.setPreferredSize(new Dimension(150, 30));

        searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Arial", Font.BOLD, 15));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setFont(new Font("Arial", Font.BOLD, 15));
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            searchBooking(searchTerm);
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.setFont(new Font("Arial", Font.BOLD, 15));
        clearButton.addActionListener(e -> clearSearch());

        topPanel.add(searchRoomLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Room No");
        tableModel.addColumn("Room Type");
        tableModel.addColumn("Room Status");
        tableModel.addColumn("Check In");
        tableModel.addColumn("Check Out");
        tableModel.addColumn("Booking Date");
        tableModel.addColumn("Booking Status");
        tableModel.addColumn("Price");
        tableModel.addColumn("Action");

        table = new JTable(tableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JComponent jComponent) {
                    jComponent.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the desired font size
                }
                return component;
            }
        };

        table.setRowHeight(25); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(11).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(11).setCellEditor(new ButtonEditor(new JTextField()));
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        fetchAndDisplayBookingsDetails();

        setVisible(true);
    }

    public static void main(String[] args) {
        new Bookings();
    }

    private void refreshTableData() {
        tableModel.fireTableDataChanged();
    }

    public void fetchAndDisplayBookingsDetails() {
        clearTableData();
        try {
            // Prepare the stored procedure call
            String storedProcedure = "{CALL sp_get_booking_details()}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);

            renderBookingResultSet(statement);
            statement.close();

            // Refresh the table data
            refreshTableData();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void searchBooking(String searchTerm) {
        // Clear existing table data
        clearTableData();

        try {
            String storedProcedure = "{CALL sp_search_booking(?)}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);
            statement.setString(1, searchTerm);

            renderBookingResultSet(statement);

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
        fetchAndDisplayBookingsDetails();
    }

    private void renderBookingResultSet(CallableStatement statement) {
        ResultSet resultSet;
        try {
            // Execute the stored procedure
            resultSet = statement.executeQuery();

            // Populate the table with data
            while (resultSet.next()) {
                int bookingID = resultSet.getInt("booking_id");
                String name = resultSet.getString("Name");
                String phone = resultSet.getString("phone");
                int roomNo = resultSet.getInt("room_no");
                String roomType = resultSet.getString("room_type");
                String roomStatus = resultSet.getString("room_status");
                String checkInDate = resultSet.getString("start_date");
                String checkOutDate = resultSet.getString("end_date");
                String bookingDate = resultSet.getString("booking_date");
                String bookingStatus = resultSet.getString("booking_status");
                double bookingPrice = resultSet.getDouble("price");

                // Add the row data to the table model
                tableModel.addRow(new Object[]{bookingID, name, phone, roomNo, roomType, roomStatus, checkInDate, checkOutDate, bookingDate, bookingStatus, bookingPrice, "Set Status"});
            }
            resultSet.close();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void clearTableData() {
        tableModel.setRowCount(0);
    }

    private void handleSQLException(SQLException e) {
        JOptionPane.showMessageDialog(null, "An error occurred while accessing the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
                // Retrieve the row number from the table data
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the action command of the button
                    String actionCommand = button.getActionCommand();
                    int bookingID = (int) table.getValueAt(selectedRow, 0);
                    int roomNo = (int) table.getValueAt(selectedRow, 3);

                    // Perform the desired action based on the button clicked
                    if (actionCommand.equals("Set Status")) {
                        // Show a dialog box to choose the booking status
                        String[] options = {"Accept", "Cancel"};
                        JComboBox<String> statusComboBox = new JComboBox<>(options);

                        JOptionPane.showOptionDialog(Bookings.this, statusComboBox, "Select Booking Status", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, options[0]);

                        // Retrieve the selected option value
                        String selectedOption = (String) statusComboBox.getSelectedItem();

                        // Perform the desired action based on the selected option
                        assert selectedOption != null;
                        if (selectedOption.equals("Accept")) {
                            // Accepted status selected
                            String bookingStatus = "Booked";
                            String roomStatus = "Occupied";
                            // TODO: Update the booking status and room status in the database for the selected row
                            // using the row value (row variable) and the values (bookingStatus and roomStatus) obtained here
                            updateRoomAndBookingStatus(roomNo, bookingStatus, roomStatus, bookingID);
                        } else if (selectedOption.equals("Cancel")) {
                            // Cancel status selected
                            String bookingStatus = "Cancelled";
                            String roomStatus = "Available";
                            // TODO: Update the booking status and room status in the database for the selected row
                            // using the row value (row variable) and the values (bookingStatus and roomStatus) obtained here
                            updateRoomAndBookingStatus(roomNo, bookingStatus, roomStatus, bookingID);
                        }

                    }
                }
            });
        }

        private void updateRoomAndBookingStatus(int roomNo, String bookingStatus, String roomStatus, int bookingID) {
            try {
                CallableStatement statement = db.connection.prepareCall("{CALL sp_update_booking_status(?, ?)}");
                statement.setInt(1, bookingID);
                statement.setString(2, bookingStatus);
                statement.executeUpdate();
                statement.close();

                statement = db.connection.prepareCall("{CALL sp_update_room_status(?, ?)}");
                statement.setInt(1, roomNo);
                statement.setString(2, roomStatus);
                statement.executeUpdate();
                statement.close();

                // Update the table data
                fetchAndDisplayBookingsDetails();
            } catch (SQLException e) {
                handleSQLException(e);
            }
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
}
