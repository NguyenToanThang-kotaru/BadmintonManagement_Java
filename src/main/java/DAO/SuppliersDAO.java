package DAO;

import DTO.SuppliersDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersDAO {

    public static SuppliersDTO getSuppliers(int maNCC) {
        String query = "SELECT * FROM nha_cung_cap WHERE ma_nha_cung_cap = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, maNCC);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SuppliersDTO(
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("ten_nha_cung_cap"),
                            rs.getString("dia_chi"),
                            rs.getString("so_dien_thoai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateSupplierID() {
        String newID = "NCC001";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT MAX(ma_nha_cung_cap) AS maxID FROM nha_cung_cap")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maxID = rs.getString("maxID");
                if (maxID != null) {
                    int number = Integer.parseInt(maxID.replaceAll("[^0-9]", "")) + 1;
                    newID = String.format("NCC%03d", number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newID;
    }

    public static ArrayList<SuppliersDTO> getAllSuppliers() {
        ArrayList<SuppliersDTO> suppliers = new ArrayList<>();
        String query = "SELECT * FROM nha_cung_cap WHERE is_deleted = 0"; // Chỉ lấy nhà cung cấp chưa bị xóa
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new SuppliersDTO(
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("ten_nha_cung_cap"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public static ArrayList<String> getAllNCCNames() {
        ArrayList<String> NCCList = new ArrayList<>();
        String query = "SELECT ten_nha_cung_cap FROM nha_cung_cap";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                NCCList.add(rs.getString("ten_nha_cung_cap"));  // Lưu tên loại vào danh sách
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
        return NCCList;
    }

    public void updateSuppliers(SuppliersDTO suppliers) {
        String sql = "UPDATE nha_cung_cap SET ten_nha_cung_cap = ?, dia_chi = ?, so_dien_thoai = ? WHERE ma_nha_cung_cap = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, suppliers.getfullname());
            stmt.setString(2, suppliers.getaddress());
            stmt.setString(3, suppliers.getphone());
            stmt.setString(4, suppliers.getsuppliersID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getSupplierIDByProduct(String productId) {
        String query = "SELECT ma_nha_cung_cap FROM san_pham WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ma_nha_cung_cap");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public SuppliersDTO getSupplierByID(String supplierID) {
        String query = "SELECT * FROM nha_cung_cap WHERE ma_nha_cung_cap = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supplierID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SuppliersDTO(
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("ten_nha_cung_cap"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"),
                        rs.getInt("is_deleted")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}