package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import DTO.ChiTietNhapHangDTO;

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

    public List<ImportDTO> getAllImport() {
        return importDAO.getAllImport();
    }

    public boolean deleteImport(String importID) {
        return importDAO.deleteImportWithProductUpdate(importID);
    }

    public void updateImport(ImportDTO importDTO) {
        // Lấy thông tin cũ để so sánh và cập nhật số lượng sản phẩm nếu cần
        ImportDTO oldImport = importDAO.getImport(importDTO.getimportID());
        if (oldImport != null) {
            // Lấy danh sách chi tiết nhập hàng cũ và mới
            List<ChiTietNhapHangDTO> oldDetails = importDAO.getChiTietNhapHang(importDTO.getimportID());
            List<ChiTietNhapHangDTO> newDetails = importDTO.getChiTietNhapHang();

            // So sánh và cập nhật số lượng sản phẩm trong kho
            for (ChiTietNhapHangDTO newDetail : newDetails) {
                boolean found = false;
                for (ChiTietNhapHangDTO oldDetail : oldDetails) {
                    if (newDetail.getProductID().equals(oldDetail.getProductID())) {
                        found = true;
                        int quantityDifference = newDetail.getQuantity() - oldDetail.getQuantity();
                        if (quantityDifference != 0) {
                            // Cập nhật số lượng sản phẩm trong kho
                            importDAO.updateProductStock(newDetail.getProductID(), quantityDifference);
                        }
                        break;
                    }
                }
                if (!found) {
                    // Nếu sản phẩm mới không tồn tại trong chi tiết cũ, thêm số lượng mới vào kho
                    importDAO.updateProductStock(newDetail.getProductID(), newDetail.getQuantity());
                }
            }

            // Xóa các sản phẩm không còn trong chi tiết mới
            for (ChiTietNhapHangDTO oldDetail : oldDetails) {
                boolean found = false;
                for (ChiTietNhapHangDTO newDetail : newDetails) {
                    if (oldDetail.getProductID().equals(newDetail.getProductID())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Trừ số lượng sản phẩm đã bị xóa khỏi chi tiết nhập hàng
                    importDAO.updateProductStock(oldDetail.getProductID(), -oldDetail.getQuantity());
                }
            }

            // Cập nhật thông tin nhập hàng
            importDAO.updateImport(importDTO);
        } else {
            throw new IllegalArgumentException("Không tìm thấy thông tin nhập hàng với ID: " + importDTO.getimportID());
        }
    }

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