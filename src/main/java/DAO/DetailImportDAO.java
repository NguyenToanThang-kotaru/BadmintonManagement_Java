package DAO;

import Connection.DatabaseConnection;
import DTO.DetailImportDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailImportDAO {

    public static DetailImportDTO getDetailImport(int mactpn) {
        String query = "SELECT * FROM chi_tiet_nhap_hang WHERE ma_chi_tiet_phieu_nhap = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, mactpn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DetailImportDTO(
                            rs.getString("ma_chi_tiet_phieu_nhap"),
                            rs.getString("ma_nhap_hang"),
                            rs.getString("ma_san_pham"),
                            rs.getString("so_luong"),
                            rs.getString("gia")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<DetailImportDTO> getAllDetailImport() {
        ArrayList<DetailImportDTO> detailimport = new ArrayList<>();
        String query = "SELECT * FROM chi_tiet_phieu_nhap";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                detailimport.add(new DetailImportDTO(
                        rs.getString("ma_chi_tiet_phieu_nhap"),
                        rs.getString("ma_nhap_hang"),
                        rs.getString("ma_san_pham"),
                        rs.getString("so_luong"),
                        rs.getString("gia")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return detailimport;
    }

    public void updateDetailImport(DetailImportDTO detailimport) {
        String sql = "UPDATE detailimport SET ma_chi_tiet_phieu_nhap = ?, ma_nhap_hang = ?, ma_san_pham = ?, so_luong = ? WHERE gia = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, detailimport.getdetailimportID());
            stmt.setString(2, detailimport.getimportID());
            stmt.setString(3, detailimport.getproductID());
            stmt.setString(4, detailimport.getamount());
            stmt.setString(5, detailimport.getprice());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

}
