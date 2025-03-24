package DAO;

import Connection.DatabaseConnection;
import DTO.DetailOrderDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailOrderDAO {

    public static DetailOrderDTO getDetailOrder(int maCTHD) {
        String query = "SELECT * FROM chi_tiet_hoa_don WHERE ma_chi_tiet_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maCTHD);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DetailOrderDTO(
                            rs.getString("ma_chi_tiet_hoa_don"),
                            rs.getString("ma_san_pham"),
                            rs.getString("ma_hoa_don"),
                            rs.getString("ma_serial"),
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
    public static ArrayList<DetailOrderDTO> getAllDetailOrder() {
        ArrayList<DetailOrderDTO> detailorder = new ArrayList<>();
        String query = "SELECT * FROM chi_tiet_hoa_don";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                detailorder.add(new DetailOrderDTO(
                        rs.getString("ma_chi_tiet_hoa_don"),
                        rs.getString("ma_san_pham"),
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_serial"),
                        rs.getString("so_luong"),
                        rs.getString("gia")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return detailorder;
    }

    public void updateCustomer(DetailOrderDTO detailorder) {
        String sql = "UPDATE detailorder SET ma_chi_tiet_hoa_don = ?, ma_san_pham = ?, ma_hoa_don = ?, ma_serial = ?, so_luong = ? WHERE gia = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, detailorder.getdetailorderID());
            stmt.setString(2, detailorder.getproductID());
            stmt.setString(3, detailorder.getorderID());
            stmt.setString(4, detailorder.getserialID());
            stmt.setString(5, detailorder.getamount());
            stmt.setString(6, detailorder.getprice());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    public void updateDetailOrder(DetailOrderDTO detailorder) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
