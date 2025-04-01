package DAO;

import Connection.DatabaseConnection;
import DTO.GuaranteeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Lớp này dùng để kết nối database và lấy dữ liệu sản phẩm
public class GuaranteeDAO {

    // Lấy thông tin của một sản phẩm
    public static GuaranteeDTO getGuarantee(String BaohanhID) {
        String query = "SELECT ma_bao_hanh, ma_serial, ly_do_bao_hanh, thoi_gian_bao_hanh FROM bao_hanh WHERE ma_bao_hanh = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, BaohanhID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GuaranteeDTO(
                            rs.getString("ma_bao_hanh"),
                            rs.getString("ma_serial"),
                            //rs.getString("trang_thai"),
                            rs.getString("ly_do_bao_hanh"),
                            rs.getString("thoi_gian_bao_hanh")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả sản phẩm
    public static ArrayList<GuaranteeDTO> getAllGuarantee() {
        ArrayList<GuaranteeDTO> products = new ArrayList<>();
        String query = "SELECT * FROM bao_hanh";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new GuaranteeDTO(
                        rs.getString("ma_bao_hanh"),
                        rs.getString("ma_serial"),
                        //rs.getString("trang_thai"),
                        rs.getString("ly_do_bao_hanh"),
                        rs.getString("thoi_gian_bao_hanh")
                ));
            }
            System.out.println("Lấy danh sách sản phẩm bảo hành thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Cập nhật thông tin sản phẩm
    public void updateGuarantee(GuaranteeDTO product) {
        String sql = "UPDATE bao_hanh SET ma_serial = ?, ly_do_bao_hanh = ?, thoi_gian_bao_hanh WHERE ma_bao_hanh = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getBaohanhID());
            stmt.setString(2, product.getSerialID());
            stmt.setString(3, product.getTGBH());
//            stmt.setString(3, product.gettrangthai());
            stmt.setString(4, product.getLydo());
            stmt.executeUpdate();
            System.out.println("Cập nhật sản phẩm thành công.");
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }
//
//    // Lấy đường dẫn ảnh sản phẩm
//    public static String getProductImage(String productID) {
//        String imagePath = null;
//        String query = "SELECT hinh_anh_sp FROM san_pham WHERE ma_bao_hanh = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, productID);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    imagePath = rs.getString("hinh_anh_sp"); // Lấy tên file ảnh
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return imagePath;
//    }
}
