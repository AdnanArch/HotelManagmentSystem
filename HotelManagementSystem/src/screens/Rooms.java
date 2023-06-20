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
import java.sql.Types;
import java.util.EventObject;

public class Rooms extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final RoundedTextField searchField;

    private final DatabaseConnection db =new DatabaseConnection();

    Rooms() {
        setTitle("Room Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
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
        addNewRoomButton.addActionListener(e -> {
            new AddNewRoom();
            searchRoom("");
        });

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

    public void fetchAndDisplayRoomDetails() {
        clearTableData();
        try {
            // Prepare the stored procedure call
            DatabaseConnection db = new DatabaseConnection();
            String storedProcedure = "{CALL sp_get_rooms_details()}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);

            renderRoomResultSet(statement);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Close the resources

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
                int roomNo = (int) table.getValueAt(selectedRow, 0);
                String roomType = table.getValueAt(selectedRow, 1).toString();
                int capacity = (int) table.getValueAt(selectedRow,2);
                double rent = (double) table.getValueAt(selectedRow,3);
                String status = table.getValueAt(selectedRow,4).toString();
                String description = table.getValueAt(selectedRow, 5).toString();

                // Get the action command of the button
                String actionCommand = button.getActionCommand();

                // Perform the desired action based on the button clicked
                if (actionCommand.equals("Edit")) {
                    AddNewRoom updateExistingRoom = new AddNewRoom();
                    boolean isUpdated = updateExistingRoom.updateRoom(roomNo, roomType, capacity, rent, status, description);
                    updateExistingRoom.setRoomComboBox(roomType);
                    updateExistingRoom.setRoomCapacityComboBox(capacity);
                    updateExistingRoom.setRentTextField(rent);
                    updateExistingRoom.setRoomStatusComboBox(status);
                    updateExistingRoom.setRoomDescriptionTextArea(description);
                    updateExistingRoom.setUpdateButtonVisibility();
                    if(isUpdated){
                        JOptionPane.showMessageDialog(null, "Room Updated Successfully");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error in Updating the room");
                    }

                } else if (actionCommand.equals("Delete")) {
                    boolean isDeleted = deleteRoom(String.valueOf(roomNo));
                    if (isDeleted) {
                        JOptionPane.showMessageDialog(null, "Room No " + roomNo + " deleted successfully");
                        fetchAndDisplayRoomDetails();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error!");
                    }
                }
            });
        }



        private boolean deleteRoom(String roomNo) {
            boolean isDeleted = false;
            try {
                DatabaseConnection db = new DatabaseConnection();
                CallableStatement statement = db.connection.prepareCall("{call sp_delete_room(?, ?)}");
                statement.setString(1, roomNo);
                statement.registerOutParameter(2, Types.BOOLEAN);

                statement.execute();
                isDeleted = statement.getBoolean(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return isDeleted;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            buttonText = value.toString(); // Store the button text
            button.setText(buttonText); // Set the button text
            button.setActionCommand(buttonText); // Set the action command
            return button;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true; // Enable single-click editing
        }

        @Override
        public Object getCellEditorValue() {
            return buttonText; // Return the button text when editing is finished
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Rooms::new);
    }
}
