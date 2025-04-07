package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import Connection.DatabaseConnection;

public class ImportBUS {
    private final ImportDAO importDAO;

    public ImportBUS() {
        this.importDAO = new ImportDAO();
    }

    // Lấy danh sách tất cả phiếu nhập
    public List<ImportDTO> getAllImport() {
        return importDAO.getAllImport();
    }

    // Xóa một phiếu nhập theo mã
    public boolean deleteImport(String importID) {
        return importDAO.deleteImport(importID);
    }

    // Cập nhật thông tin phiếu nhập
    public void updateImport(ImportDTO importDTO) {
        importDAO.updateImport(importDTO);
    }

    // Tính tổng tiền của một phiếu nhập dựa trên chi tiết nhập hàng
    public int calculateImportTotal(String importID) {
        String query = "SELECT so_luong, gia FROM chi_tiet_nhap_hang WHERE ma_nhap_hang = ?";
        int total = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quantity = rs.getInt("so_luong");
                    int price = rs.getInt("gia");
                    total += quantity * price;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}