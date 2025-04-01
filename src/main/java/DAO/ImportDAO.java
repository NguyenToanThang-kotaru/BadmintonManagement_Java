package DAO;

import Connection.DatabaseConnection;
import DTO.ImportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImportDAO {

    public static ImportDTO getImport(int maphieunhap) {
        String query = "SELECT * FROM nhap_hang WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maphieunhap);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ImportDTO(
                            rs.getString("ma_nhap_hang"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("tong_tien"),
                            rs.getString("ngay_nhap")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<ImportDTO> getAllImport() {
        ArrayList<ImportDTO> imports = new ArrayList<>();
        String query = "SELECT * FROM nhap_hang";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                imports.add(new ImportDTO(
                        rs.getString("ma_nhap_hang"),
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("tong_tien"),
                        rs.getString("ngay_nhap")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imports;
    }
    public boolean deleteImport(String importID) {
        String query = "DELETE FROM nhap_hang WHERE ma_nhap_hang = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, importID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if rows were deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        }
    }

    public void updateImport(ImportDTO importDTO) {
        String sql = "UPDATE nhap_hang SET ma_nhan_vien = ?, ma_nha_cung_cap = ?, tong_tien = ?, ngay_nhap = ? WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, importDTO.getemployeeID());
            stmt.setString(2, importDTO.getsupplierID());
            stmt.setString(3, importDTO.gettotalmoney());
            stmt.setString(4, importDTO.getreceiptdate());
            stmt.setString(5, importDTO.getimportID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
