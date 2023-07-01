package components;

import connection.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;


public class LoggedIn {
    public static String userName;
    public static int customerID;
    public static String customerEmail;

    public static String customerName;

    public static void setUserName(String userName) {
        LoggedIn.userName = userName;
    }

    public static int getCustomerID() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        CallableStatement callableStatement = null;
        customerID = 0;

        try {
            String query = "{? = CALL get_customer_id(?)}";
            callableStatement = db.connection.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerID = callableStatement.getInt(1);
        } finally {
            // Close the callableStatement in the appropriate finally block
            // to release the resources
            if (callableStatement != null) {
                callableStatement.close();
            }
        }

        return customerID;
    }

    public static String getCustomerEmail() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        CallableStatement callableStatement = null;
        customerID = 0;

        try {
            String query = "{? = CALL get_user_email(?)}";
            callableStatement = db.connection.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerEmail = callableStatement.getString(1);
        } finally {
            // Close the callableStatement in the appropriate finally block
            // to release the resources
            if (callableStatement != null) {
                callableStatement.close();
            }
        }

        return customerEmail;
    }

    public static String getCustomerName() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        CallableStatement callableStatement = null;
        customerName = null;

        try {
            String query = "{? = CALL get_customer_name(?)}";
            callableStatement = db.connection.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, userName);
            callableStatement.execute();

            customerName = callableStatement.getString(1);
        } finally {
            // Close the callableStatement in the appropriate finally block
            // to release the resources
            if (callableStatement != null) {
                callableStatement.close();
            }
        }

        return customerName;
    }

}
