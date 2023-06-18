package screens;

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

public class RoomDetailsFetcher1 extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public RoomDetailsFetcher1() {
        setTitle("Room Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Room No");
        tableModel.addColumn("Room Type");
        tableModel.addColumn("Capacity");
        tableModel.addColumn("Rent");
        tableModel.addColumn("Status");
        tableModel.addColumn("Description");
        tableModel.addColumn("Edit");
        tableModel.addColumn("Delete");

        table = new JTable(tableModel);
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField()));
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        fetchAndDisplayRoomDetails();
    }

    public void fetchAndDisplayRoomDetails() {
        try {
            // Prepare the stored procedure call
            DatabaseConnection db = new DatabaseConnection();
            String storedProcedure = "{CALL sp_get_rooms_details()}";
            CallableStatement statement = db.connection.prepareCall(storedProcedure);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

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

            // Close the resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                String roomNo = table.getValueAt(selectedRow, 0).toString();

                // Get the action command of the button
                String actionCommand = button.getActionCommand();

                // Perform the desired action based on the button clicked
                if (actionCommand.equals("Edit")) {
                    System.out.println("Edit button is clicked for room number: " + roomNo);

                } else if (actionCommand.equals("Delete")) {
//                    System.out.println("Delete button is clicked for room number: " + roomNo);
                    boolean isDeleted = deleteRoom(roomNo);
                    if(isDeleted){
                        JOptionPane.showMessageDialog(null, "Room No " + roomNo +" deleted successfully");
                        fetchAndDisplayRoomDetails();
                    }else{
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
        SwingUtilities.invokeLater(() -> {
            RoomDetailsFetcher1 fetcher = new RoomDetailsFetcher1();
            fetcher.setVisible(true);
        });
    }
}
