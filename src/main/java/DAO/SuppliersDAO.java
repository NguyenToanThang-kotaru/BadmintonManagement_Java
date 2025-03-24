package DAO;

import Connection.DatabaseConnection;
import DTO.SuppliersDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersDAO {

    public static SuppliersDTO getSuppliers(int maNCC) {
        String query = "SELECT * FROM nha_cung_cap WHERE ma_nha_cung_cap = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maNCC);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SuppliersDTO(
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("ten_nha_cung_cap"),
                            rs.getString("diaj_chi"),
                            rs.getString("so_dien_thoai")  
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<SuppliersDTO> getAllSuppliers() {
        ArrayList<SuppliersDTO> suppliers = new ArrayList<>();
        String query = "SELECT * FROM nha_cung_cap";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new SuppliersDTO(
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("ten_nha_cung_cap"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return suppliers;
    }

    public void updateSuppliers(SuppliersDTO suppliers) {
        String sql = "UPDATE suppliers SET ma_nha_cung_cap = ?, ten_nha_cung_cap = ?, dia_chi = ? WHERE so_dien_thoai = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, suppliers.getsuppliersID());
            stmt.setString(2, suppliers.getfullname());
            stmt.setString(3, suppliers.getaddress());
            stmt.setString(4, suppliers.getphone());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

}
