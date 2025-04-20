package DAO;

import DTO.DetailOrderDTO;
import DTO.ProductDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                            rs.getString("gia"),
                            rs.getString("loi_nhuan")
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
        String query = "SELECT * FROM chi_tiet_hoa_don WHERE is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                detailorder.add(new DetailOrderDTO(
                        rs.getString("ma_chi_tiet_hoa_don"),
                        rs.getString("ma_san_pham"),
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_serial"),
                        rs.getString("so_luong"),
                        rs.getString("gia"),
                        rs.getString("loi_nhuan")
                ));
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return detailorder;
    }

    public void updateCustomer(DetailOrderDTO detailorder) {
        String sql = "UPDATE detailorder SET ma_chi_tiet_hoa_don = ?, ma_san_pham = ?, ma_hoa_don = ?, ma_serial = ?, so_luong = ?, gia = ? WHERE loi_nhuan = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, detailorder.getdetailorderID());
            stmt.setString(2, detailorder.getproductID());
            stmt.setString(3, detailorder.getorderID());
            stmt.setString(4, detailorder.getserialID());
            stmt.setString(5, detailorder.getamount());
            stmt.setString(6, detailorder.getprice());
            stmt.setString(7, detailorder.getprofit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                            rs.getString("gia"),
                            rs.getString("loi_nhuan")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailorderList;
    }
    
    public static Boolean deleteDetailOrder(String orderID) {
        String queery = "UPDATE chi_tiet_hoa_don SET is_deleted = 1 WHERE ma_hoa_don = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(queery)) {
            stmt.setString(1, orderID);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertDetailOrder(DetailOrderDTO detail) {
        // Lấy thông tin sản phẩm để tính lợi nhuận
        ProductDTO product = ProductDAO.getProduct(detail.getproductID());
        if (product == null) {
            System.out.println("Không tìm thấy sản phẩm với ID: " + detail.getproductID());
            return;
        }

        // Tính toán giá bán sau khuyến mãi và lợi nhuận
        double giaBan = Double.parseDouble(product.getGia());
        double khuyenMai = Double.parseDouble(product.getkhuyenMai());
        double giaGoc = Double.parseDouble(product.getgiaGoc());

        // Tính giá sau khuyến mãi
        double giaSauKM = (giaBan - (giaBan * (khuyenMai / 100)));

        // Tính lợi nhuận
        double loiNhuan = giaSauKM - giaGoc;
        String sql = "INSERT INTO chi_tiet_hoa_don (ma_chi_tiet_hoa_don, ma_san_pham, ma_hoa_don, ma_serial, so_luong, gia, is_deleted, loi_nhuan) VALUES (?, ?, ?, ?, ?, ?, 0, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, detail.getdetailorderID());
            stmt.setString(2, detail.getproductID());
            stmt.setString(3, detail.getorderID());

            if (detail.getserialID() == null || detail.getserialID().trim().isEmpty()) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, detail.getserialID());
            }

            stmt.setString(5, detail.getamount());
            stmt.setString(6, detail.getprice());
            stmt.setDouble(7, loiNhuan);
            stmt.executeUpdate();
            // Cập nhật tổng lợi nhuận cho hóa đơn
            OrderDAO.updateTotalProfit(detail.getorderID());
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
    
    public static boolean deleteDetailOrderByID(String detailOrderID) {
        String sql = "DELETE FROM chi_tiet_hoa_don WHERE ma_chi_tiet_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, detailOrderID);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

 
    public void updateDetailOrder(DetailOrderDTO detail) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
