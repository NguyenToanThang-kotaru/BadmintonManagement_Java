package DAO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Form_ImportDAO {

    // Tạo mã nhập hàng mới (giữ nguyên vì đặc thù cho bảng nhap_hang)
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

    // Lưu thông tin nhập hàng và chi tiết nhập hàng (giữ nguyên vì đặc thù)
    public boolean saveImport(String importID, String employeeID, String supplierID, int totalAmount,
                              String receiptDate, Object[][] productData) {
        try {
            String importQuery = "INSERT INTO nhap_hang (ma_nhap_hang, ma_nhan_vien, ma_nha_cung_cap, tong_tien, ngay_nhap, is_deleted) " +
                    "VALUES (?, ?, ?, ?, ?, 0)";
            String detailQuery = "INSERT INTO chi_tiet_nhap_hang (ma_chi_tiet_nhap_hang, ma_nhap_hang, ma_san_pham, so_luong, gia, is_deleted) " +
                    "VALUES (?, ?, ?, ?, ?, 0)";
            String maxDetailIDQuery = "SELECT ma_chi_tiet_nhap_hang FROM chi_tiet_nhap_hang ORDER BY ma_chi_tiet_nhap_hang DESC LIMIT 1";

            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.out.println("Failed to connect to database");
                return false;
            }
            conn.setAutoCommit(false);

            try (PreparedStatement importStmt = conn.prepareStatement(importQuery);
                 PreparedStatement detailStmt = conn.prepareStatement(detailQuery);
                 PreparedStatement maxDetailStmt = conn.prepareStatement(maxDetailIDQuery)) {

                // Lưu thông tin nhập hàng
                importStmt.setString(1, importID);
                importStmt.setString(2, employeeID);
                importStmt.setString(3, supplierID);
                importStmt.setInt(4, totalAmount);
                importStmt.setString(5, receiptDate);
                importStmt.executeUpdate();

                // Tạo mã chi tiết nhập hàng
                ResultSet rs = maxDetailStmt.executeQuery();
                int nextDetailNumber = 1;
                if (rs.next()) {
                    String lastDetailID = rs.getString("ma_chi_tiet_nhap_hang");
                    nextDetailNumber = Integer.parseInt(lastDetailID.substring(5)) + 1;
                }

                // Lưu chi tiết nhập hàng
                for (int i = 0; i < productData.length; i++) {
                    String detailID = importID + String.format("%02d", nextDetailNumber + i);
                    String productID = (String) productData[i][0];
                    int quantity = (Integer) productData[i][2];
                    int price = Integer.parseInt(productData[i][3].toString().replaceAll("[^0-9]", ""));

                    detailStmt.setString(1, detailID);
                    detailStmt.setString(2, importID);
                    detailStmt.setString(3, productID);
                    detailStmt.setInt(4, quantity);
                    detailStmt.setInt(5, price);
                    detailStmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                System.out.println("Error during saveImport: " + e.getMessage());
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