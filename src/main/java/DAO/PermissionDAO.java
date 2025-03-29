/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.PermissionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ADMIN
 */
public class PermissionDAO {
    public static PermissionDAO getCustomer(int maKhachHang) {
        String query = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDTO(
                            rs.getString("ma_khach_hang"),
                            rs.getString("ten_khach_hang"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("email")  
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
}
