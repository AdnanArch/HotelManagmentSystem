import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:// localhost:3306/hotel_app","root","admin");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers");

            while(rs.next()) {

                System.out.println(rs.getString(2));
            }
            conn.close();
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}