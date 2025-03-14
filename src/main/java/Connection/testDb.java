package Connection;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class testDb {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Kết nối đến database thành công!");
            } else {
                System.out.println("Kết nối thất bại!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối đến database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
