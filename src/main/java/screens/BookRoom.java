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

public class BookRoom extends JFrame {
    private final JTable table;
    public static  DefaultTableModel tableModel;
    private final RoundedTextField searchField;

    private final DatabaseConnection db = new DatabaseConnection();
    private int roomNo;

    FetchRoomDetails fetchRoomDetails = new FetchRoomDetails();

    BookRoom() {

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

        topPanel.add(searchRoomLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Room No");
        tableModel.addColumn("Room Type");
        tableModel.addColumn("Capacity");
        tableModel.addColumn("Rent");
        tableModel.addColumn("Status");
        tableModel.addColumn("Description");
        tableModel.addColumn("Book Now");

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

        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField()));
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        fetchRoomDetails.setTableModel(tableModel);
        fetchAndDisplayRoomsDetails();
        setVisible(true);
    }

    private void fetchAndDisplayRoomsDetails() {
        fetchRoomDetails.clearTableData();
        try {
            // Prepare the stored procedure call
            String storedProcedure = "{CALL sp_get_rooms_details()}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);

            renderRoomResultSet(statement);
            statement.close();

            // Refresh the table data
            fetchRoomDetails.refreshTableData();
        } catch (SQLException e) {
            fetchRoomDetails.handleSQLException(e);
        }
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
                tableModel.addRow(new Object[]{roomID, roomType, capacity, rent, status, description, "Book Now"});
            }
            resultSet.close();
        } catch (SQLException e) {
            fetchRoomDetails.handleSQLException(e);
        }
    }

    public void searchRoom(String searchTerm) {
        // Clear existing table data
        fetchRoomDetails.clearTableData();
        try {
            String storedProcedure = "{CALL sp_search_room(?)}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);
            statement.setString(1, searchTerm);

            renderRoomResultSet(statement);

            statement.close();

            // Refresh the table data
            fetchRoomDetails.refreshTableData();
        } catch (SQLException e) {
            fetchRoomDetails.handleSQLException(e);
        }
    }

    private void clearSearch() {
        searchField.setText("");
        fetchRoomDetails.clearTableData();
        fetchAndDisplayRoomsDetails();
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
                    // Get the action command of the button
                    String actionCommand = button.getActionCommand();

                    // Perform the desired action based on the button clicked
                    if (actionCommand.equals("Book Now")) {
                        roomNo = (int) table.getValueAt(selectedRow, 0);
                        String roomType = table.getValueAt(selectedRow, 1).toString();
                        int capacity = (int) table.getValueAt(selectedRow, 2);
                        String roomStatus = table.getValueAt(selectedRow, 4).toString();
                        String description = table.getValueAt(selectedRow, 5).toString();

                        if (roomStatus.equals("Occupied")) {
                            JOptionPane.showMessageDialog(null, "Your can not book this room because it is occupied.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                        } else if (roomStatus.equals("Maintenance")) {
                            JOptionPane.showMessageDialog(null, "Your can not book this room because it is under Maintenance.", "Booking Error", JOptionPane.ERROR_MESSAGE);

                        }else{
                            new BookNow(roomNo, capacity, roomType, roomStatus, description);
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



    public static void main(String[] args) {new BookRoom();}
}
