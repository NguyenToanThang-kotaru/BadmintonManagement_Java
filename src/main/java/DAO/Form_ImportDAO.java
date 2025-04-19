package DAO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Form_ImportDAO {

    // Tạo mã nhập hàng mới
    public String generateNextImportID() {
        String query = "SELECT ma_nhap_hang FROM nhap_hang ORDER BY ma_nhap_hang DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastID = rs.getString("ma_nhap_hang");
                int num = Integer.parseInt(lastID.substring(2)) + 1;
                return String.format("NH%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NH001";
    }

    public boolean saveImport(String importID, String employeeID, int totalAmount,
                             String receiptDate, Object[][] productData) {
        try {
            String importQuery = "INSERT INTO nhap_hang (ma_nhap_hang, ma_nhan_vien, tong_tien, ngay_nhap, is_deleted) " +
                                "VALUES (?, ?, ?, ?, 0)";
            String detailQuery = "INSERT INTO chi_tiet_nhap_hang (ma_chi_tiet_nhap_hang, ma_nhap_hang, ma_san_pham, so_luong, gia, ma_nha_cung_cap, is_deleted) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 0)";
            String maxDetailIDQuery = "SELECT ma_chi_tiet_nhap_hang FROM chi_tiet_nhap_hang ORDER BY ma_chi_tiet_nhap_hang DESC LIMIT 1";
            String supplierQuery = "SELECT ma_san_pham, ma_nha_cung_cap FROM san_pham WHERE ma_san_pham IN (" +
                                  String.join(",", new String[productData.length]).replaceAll("[^,]+", "?") +
                                  ") AND is_deleted = 0";

            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.out.println("Failed to connect to database");
                return false;
            }
            conn.setAutoCommit(false);

            try (PreparedStatement importStmt = conn.prepareStatement(importQuery);
                 PreparedStatement detailStmt = conn.prepareStatement(detailQuery);
                 PreparedStatement maxDetailStmt = conn.prepareStatement(maxDetailIDQuery);
                 PreparedStatement supplierStmt = conn.prepareStatement(supplierQuery)) {

                // Lấy tất cả ma_nha_cung_cap cho các sản phẩm
                Map<String, String> supplierMap = new HashMap<>();
                for (int i = 0; i < productData.length; i++) {
                    supplierStmt.setString(i + 1, (String) productData[i][0]);
                }
                ResultSet supplierRs = supplierStmt.executeQuery();
                while (supplierRs.next()) {
                    supplierMap.put(supplierRs.getString("ma_san_pham"), supplierRs.getString("ma_nha_cung_cap"));
                }

                // Lưu thông tin nhập hàng
                importStmt.setString(1, importID);
                importStmt.setString(2, employeeID);
                importStmt.setInt(3, totalAmount);
                importStmt.setString(4, receiptDate);
                importStmt.executeUpdate();

                // Tạo mã chi tiết nhập hàng
                ResultSet rs = maxDetailStmt.executeQuery();
                int nextDetailNumber = 1;
                if (rs.next()) {
                    String lastDetailID = rs.getString("ma_chi_tiet_nhap_hang");
                    nextDetailNumber = Integer.parseInt(lastDetailID.substring(5)) + 1;
                }

                // Lưu chi tiết nhập hàng và tạo serial
                ProductDAO productDAO = new ProductDAO();
                for (int i = 0; i < productData.length; i++) {
                    String detailID = importID + String.format("%02d", nextDetailNumber + i);
                    String productID = (String) productData[i][0];
                    int quantity = (Integer) productData[i][2];
                    int price = (Integer) productData[i][3];

                    // Lấy ma_nha_cung_cap từ map
                    String supplierID = supplierMap.get(productID);
                    if (supplierID == null) {
                        throw new SQLException("Không tìm thấy nhà cung cấp cho sản phẩm: " + productID);
                    }

                    detailStmt.setString(1, detailID);
                    detailStmt.setString(2, importID);
                    detailStmt.setString(3, productID);
                    detailStmt.setInt(4, quantity);
                    detailStmt.setInt(5, price);
                    detailStmt.setString(6, supplierID);
                    detailStmt.executeUpdate();

                    // Tạo serial cho sản phẩm
                    productDAO.generateSerials(productID, quantity);
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof SQLException) {
                    System.out.println("SQL Error during saveImport: " + e.getMessage() + ", SQLState: " + ((SQLException) e).getSQLState());
                } else {
                    System.out.println("Error during saveImport: " + e.getMessage());
                }
                e.printStackTrace();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Outer error in saveImport: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}