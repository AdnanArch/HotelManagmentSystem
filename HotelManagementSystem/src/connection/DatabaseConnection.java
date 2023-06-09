package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {
    public Connection connection;
    public Statement statement;
    public DatabaseConnection(){
        try {
//            Class.forName("com.mysql.jdbc.Driver"); // this line is depreciated
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_app?characterEncoding=latin1", "root", "admin");
            statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
