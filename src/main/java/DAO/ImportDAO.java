package DAO;

import DTO.ImportDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportDAO {
    private final ProductDAO productDAO; // Thêm instance của ProductDAO

    public ImportDAO() {
        this.productDAO = new ProductDAO(); // Khởi tạo trong constructor
    }

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

    public boolean deleteImportWithProductUpdate(String importID) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Lấy chi tiết phiếu nhập để trừ số lượng sản phẩm
            String query = "SELECT ma_san_pham, so_luong FROM chi_tiet_nhap_hang WHERE ma_nhap_hang = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, importID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String productID = rs.getString("ma_san_pham");
                        int quantity = rs.getInt("so_luong");
                        productDAO.updateProductQuantity(productID, -quantity); // Sử dụng instance
                    }
                }
            }

            // Đánh dấu phiếu nhập là đã xóa
            String deleteQuery = "UPDATE nhap_hang SET is_deleted = 1 WHERE ma_nhap_hang = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setString(1, importID);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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