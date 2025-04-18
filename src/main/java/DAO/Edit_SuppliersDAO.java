package DAO;

import DTO.SuppliersDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Connection.DatabaseConnection;

public class Edit_SuppliersDAO {

    public void updateSupplier(SuppliersDTO supplier) {
        String sql = "UPDATE nha_cung_cap SET ten_nha_cung_cap = ?, dia_chi = ?, so_dien_thoai = ? WHERE ma_nha_cung_cap = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getfullname());
            stmt.setString(2, supplier.getaddress());
            stmt.setString(3, supplier.getphone());
            stmt.setString(4, supplier.getsuppliersID()); // WHERE dựa trên mã NCC gốc
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật nhà cung cấp: " + e.getMessage());
        }
    }
}