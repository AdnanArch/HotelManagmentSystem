package screens;

import connection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchRoomDetails {
    private final DatabaseConnection db = new DatabaseConnection();
    private  DefaultTableModel tableModel;


    public void setTableModel(DefaultTableModel tm){
        tableModel = tm;
    }
//    FetchRoomDetails() {
//        tableModel = new Rooms().getTableModel();
//    }
    public void handleSQLException(SQLException e) {
        JOptionPane.showMessageDialog(null, "An error occurred while accessing the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    public void refreshTableData() {
        tableModel.fireTableDataChanged();
    }

    public void renderRoomResultSet(CallableStatement statement) {
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

}
