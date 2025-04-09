package DAO;

import Connection.DatabaseConnection;
import DTO.ImportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportDAO {

    // Lấy một phiếu nhập theo mã
    public ImportDTO getImport(String importID) {
        String query = "SELECT * FROM nhap_hang WHERE ma_nhap_hang = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả phiếu nhập chưa bị xóa
    public List<ImportDTO> getAllImport() {
        List<ImportDTO> imports = new ArrayList<>();
        String query = "SELECT * FROM nhap_hang WHERE is_deleted = 0";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imports;
    }

    // Đánh dấu phiếu nhập là đã xóa (is_deleted = 1) thay vì xóa thật
    public boolean deleteImport(String importID) {
        String query = "UPDATE nhap_hang SET is_deleted = 1 WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin phiếu nhập
    public void updateImport(ImportDTO importDTO) {
        String query = "UPDATE nhap_hang SET ma_nhan_vien = ?, ma_nha_cung_cap = ?, tong_tien = ?, ngay_nhap = ? WHERE ma_nhap_hang = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
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