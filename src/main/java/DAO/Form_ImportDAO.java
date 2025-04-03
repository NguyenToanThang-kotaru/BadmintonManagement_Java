package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connection.DatabaseConnection;
import DTO.ProductDTO;

public class Form_ImportDAO {
    public String generateNextImportID() {
        String query = "SELECT ma_nhap_hang FROM nhap_hang ORDER BY ma_nhap_hang DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastID = rs.getString("ma_nhap_hang");
                int num = Integer.parseInt(lastID.substring(4)) + 1;
                return String.format("NH%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NH001";
    }

    public String getEmployeeName(String employeeID) {
        String query = "SELECT ten_nhan_vien FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ten_nhan_vien");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String[] getSupplierNames() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT ma_nha_cung_cap, ten_nha_cung_cap FROM nha_cung_cap";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                suppliers.add(rs.getString("ma_nha_cung_cap") + " - " + rs.getString("ten_nha_cung_cap"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers.toArray(new String[0]);
    }

    public Map<String, List<ProductDTO>> loadSupplierProducts() {
        Map<String, List<ProductDTO>> map = new HashMap<>();
        String query = "SELECT ma_san_pham, ten_san_pham, gia, ma_nha_cung_cap FROM san_pham ORDER BY ma_nha_cung_cap, ma_san_pham";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String supplierID = rs.getString("ma_nha_cung_cap");
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("ma_san_pham"));
                product.setProductName(rs.getString("ten_san_pham"));
                product.setGia(rs.getString("gia"));
                product.setMaNCC(supplierID);
                
                map.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean saveImport(String importID, String employeeID, String supplierID, int totalAmount, 
                            String receiptDate, Object[][] productData) {
        try {
            // 1. Save main import record
            String importQuery = "INSERT INTO nhap_hang (ma_nhap_hang, ma_nhan_vien, ma_nha_cung_cap, tong_tien, ngay_nhap) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            // 2. Save import details
            String detailQuery = "INSERT INTO chi_tiet_nhap_hang (ma_chi_tiet_nhap_hang, ma_nhap_hang, ma_san_pham, so_luong, gia) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement importStmt = conn.prepareStatement(importQuery);
                 PreparedStatement detailStmt = conn.prepareStatement(detailQuery)) {
                
                // Insert main import record
                importStmt.setString(1, importID);
                importStmt.setString(2, employeeID);
                importStmt.setString(3, supplierID);
                importStmt.setInt(4, totalAmount);
                importStmt.setString(5, receiptDate);
                importStmt.executeUpdate();
                
                // Insert each product detail
                for (int i = 0; i < productData.length; i++) {
                    String detailID = importID + String.format("%02d", i+1);
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
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}