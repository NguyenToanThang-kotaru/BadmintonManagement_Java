/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

/**
 *
 * @author ADMIN
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/badminton_store";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    // Singleton Pattern để chỉ tạo 1 kết nối duy nhất
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Succes connect database");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Lỗi kết nối database: " + e.getMessage());
            }
        }
        return connection;
    }

    // Đóng kết nối khi không cần nữa
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Đã đóng kết nối database.");
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
