/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.EmployeeDTO;
import DTO.PermissionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class PermissionDAO {
    public static PermissionDTO getPermission(String maquyen){
        String query = "SELECT * FROM quyen where ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maquyen);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PermissionDTO(
                            rs.getString("ma_quyen"),
                            rs.getString("ten_quyen")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ArrayList<PermissionDTO> getAllPermissions(){
        ArrayList<PermissionDTO> permission = new ArrayList<>();
        String query = "SELECT * FROM quyen WHERE is_deleted = 0;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                permission.add(new PermissionDTO(
                        rs.getString("ma_quyen"),
                        rs.getString("ten_quyen")                        
                ));
            }
            System.out.println("Lấy danh sách nhân viên thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return permission;
    }
    
    
    
    
//    public static PermissionDAO getCustomer(int maKhachHang) {
//        String query = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, maKhachHang);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return new CustomerDTO(
//                            rs.getString("ma_khach_hang"),
//                            rs.getString("ten_khach_hang"),
//                            rs.getString("so_dien_thoai"),
//                            rs.getString("email")  
//                    );
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
}
