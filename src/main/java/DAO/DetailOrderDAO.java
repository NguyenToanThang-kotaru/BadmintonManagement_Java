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
    
    public static ArrayList<DetailOrderDTO> getDetailOrderByOrderID(String orderID) {
        ArrayList<DetailOrderDTO> detailorderList = new ArrayList<>();
        String query = "SELECT * FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detailorderList.add(new DetailOrderDTO(
                            rs.getString("ma_chi_tiet_hoa_don"),
                            rs.getString("ma_san_pham"),
                            rs.getString("ma_hoa_don"),
                            rs.getString("ma_serial"),
                            rs.getString("so_luong"),
                            rs.getString("gia")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailorderList;
    }
    
    public static Boolean deleteDetailOrder(String detailorderID) {
        String queery = "UPDATE chi_tiet_hoa_don SET is_deleted = 1 WHERE ma_chi_tiet_hoa_don = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(queery)) {
            stmt.setString(1, detailorderID);
            stmt.executeUpdate();
            System.out.println("Xoa thanh cong");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertDetailOrder(DetailOrderDTO detail) {
        String sql = "INSERT INTO chi_tiet_hoa_don (ma_chi_tiet_hoa_don, ma_san_pham, ma_hoa_don, ma_serial, so_luong, gia, is_deleted) VALUES (?, ?, ?, ?, ?, ?,0)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, detail.getdetailorderID());
            stmt.setString(2, detail.getproductID());
            stmt.setString(3, detail.getorderID());
            stmt.setString(4, detail.getserialID());
            stmt.setString(5, detail.getamount());
            stmt.setString(6, detail.getprice());
            
            stmt.executeUpdate();
            System.out.println("cap nhat chi tiet thanh cong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int getMaxDetailOrderNumber() {
        String sql = "SELECT MAX(CAST(SUBSTRING(ma_chi_tiet_hoa_don, 5, 3) AS UNSIGNED)) AS max_number FROM chi_tiet_hoa_don";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("max_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
