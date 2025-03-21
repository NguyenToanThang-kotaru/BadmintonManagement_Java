package DAO;

import Connection.DatabaseConnection;
import DTO.ImportDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImportDAO {
    
    public ImportDTO getImport(String importID) {
        String query = "SELECT * FROM nhap_hang WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ImportDTO(
                            rs.getString("ma_nhap_hang"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ma_nha_cung_cap"),
                            rs.getDouble("tong_tien"),
                            rs.getDate("ngay_nhap")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<ImportDTO> getAllImport() {
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
                        rs.getDouble("tong_tien"),
                        rs.getDate("ngay_nhap")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imports;
    }

    public void updateImport(ImportDTO importDTO) {
        String sql = "UPDATE nhap_hang SET ma_nhan_vien = ?, ma_nha_cung_cap = ?, tong_tien = ?, ngay_nhap = ? WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, importDTO.getEmployeeID());
            stmt.setString(2, importDTO.getSupplierID());
            stmt.setDouble(3, importDTO.getTotalMoney());
            stmt.setDate(4, importDTO.getImportDate());
            stmt.setString(5, importDTO.getImportID());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
