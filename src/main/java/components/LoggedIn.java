package components;

import connection.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class LoggedIn {
    public static String userName;

    private static final DatabaseConnection db = new DatabaseConnection();

    public static void setUserName(String userName) {
        LoggedIn.userName = userName;
    }

    public static int getCustomerID() throws SQLException {
        int customerID;
        try (CallableStatement callableStatement = db.connection.prepareCall("{? = CALL get_customer_id(?)}")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerID = callableStatement.getInt(1);
        }

        return customerID;
    }

    public static String getCustomerEmail() throws SQLException {
        String customerEmail;
        try (CallableStatement callableStatement = db.connection.prepareCall("{? = CALL get_user_email(?)}")) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerEmail = callableStatement.getString(1);
        }

        return customerEmail;
    }

    public static String getCustomerName() throws SQLException {
        String customerName;
        try (CallableStatement callableStatement = db.connection.prepareCall("{? = CALL get_customer_name(?)}")) {
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerName = callableStatement.getString(1);
        }

        return customerName;
    }
}
