import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:// localhost:3306/hotel_app","root","admin");
            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from customers");
//
//            while(rs.next()) {
//
//                System.out.println(rs.getString(2));
//            }
            CallableStatement statement = conn.prepareCall("{CALL sp_admin_login(?,?,?)}");
            statement.setString(1, "admin1");
            statement.setString(2, "password1");
            statement.registerOutParameter(3, Types.BOOLEAN);
//            var hasResult = statement.execute();
            var hasResult = statement.execute();
            System.out.println(hasResult);

            System.out.println(statement.getBoolean(3));
//            if(hasResult) {
//                System.out.println(statement.getBoolean(3));
////                ResultSet resultSet = statement.getResultSet();
////                System.out.println(resultSet.getFetchSize());
////                while(resultSet.next()) {
////                    System.out.println(resultSet.getBoolean("result"));
////                }
//            }else {
//                System.out.println("Failed");
//            }

        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}