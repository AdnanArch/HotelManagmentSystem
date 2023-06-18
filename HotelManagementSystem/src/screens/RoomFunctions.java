package screens;

import connection.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class RoomFunctions {
    public static boolean insertRoomData(String roomType, int capacity, double rent, String roomStatus, String roomDescription) {
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

    public static void fetchAndRefreshRoomsDataFromDatabase(){

    }
}
