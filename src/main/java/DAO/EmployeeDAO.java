package DAO;

import Connection.DatabaseConnection;
import DTO.EmployeeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO {

    public static EmployeeDTO getEmployee(int maNhanVien) {
        String query = "SELECT * FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                            rs.getInt("ma_nhan_vien"),
                            rs.getString("ten_nhan_vien"),
                            rs.getString("dia_chi"),
                            rs.getString("so_dien_thoai"),
                       
                            rs.getInt("ma_tai_khoan")
                        
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return null;
    }

    public static ArrayList<EmployeeDTO> getAllEmployees() {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String query = "SELECT * FROM nhan_vien";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new EmployeeDTO(
                    rs.getInt("ma_nhan_vien"),
                    rs.getString("ten_nhan_vien"),
                    rs.getString("dia_chi"),
                    rs.getString("so_dien_thoai"),
                
                    rs.getInt("ma_tai_khoan")
                   
                ));
            }
            System.out.println("Lấy danh sách nhân viên thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public void updateEmployee(EmployeeDTO employee) {
      String sql = "UPDATE nhan_vien SET ten_nhan_vien = ?, dia_chi = ?, so_dien_thoai = ?, ma_tai_khoan = ? WHERE ma_nhan_vien = ?";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {

    stmt.setString(1, employee.getFullName());
    stmt.setString(2, employee.getAddress());
    stmt.setString(3, employee.getPhone());
    stmt.setInt(4, employee.getAccountID());
    stmt.setInt(5, employee.getEmployeeID()); // Chuyển về vị trí đúng

    stmt.executeUpdate();
    System.out.println("Cập nhật nhân viên thành công.");
} catch (SQLException e) {
    System.out.println("Lỗi cập nhật nhân viên: " + e.getMessage());
    e.printStackTrace();
}

    }
}
