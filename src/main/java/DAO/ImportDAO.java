package DAO;

import Connection.DatabaseConnection;
import DTO.ImportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImportDAO {

    public static ImportDTO getImport(int maphieunhap) {
        String query = "SELECT * FROM nhap_hang WHERE ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maphieunhap);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<ImportDTO> getAllImport() {
        ArrayList<ImportDTO> Import = new ArrayList<>();
        String query = "SELECT * FROM nhap_hang";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Import.add(new ImportDTO(
                        rs.getString("ma_nhap_hang"),
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("ngay_nhap")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return Import;
    }

    public void updateImport(ImportDTO customer) {
        String sql = "UPDATE import SET ma_nhap_hang = ?, ma_nhan_vien = ?, ma_nha_cung_cap = ?, ma_nha_cung_cap = ? WHERE ngay_nhap = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getimportID());
            stmt.setString(2, customer.getemployeeID());
            stmt.setString(3, customer.getsupplierID());
            stmt.setString(4, customer.gettotalmoney());
            stmt.setString(5, customer.getreceiptdate());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

}
