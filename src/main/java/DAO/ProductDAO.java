package DAO;

import Connection.DatabaseConnection;
import DTO.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Lớp này dùng để kết nối database và lấy dữ liệu sản phẩm
public class ProductDAO {

    // Lấy thông tin của một sản phẩm
    public static ProductDTO getProduct(String ProductID) {
        String query = "SELECT ma_san_pham, ten_san_pham, gia, so_luong, ma_thuong_hieu, thong_so_ki_thuat, ma_loai, hinh_anh_sp FROM san_pham WHERE ma_san_pham = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ProductID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProductDTO(
                            rs.getString("ma_san_pham"),
                            rs.getString("ten_san_pham"),
                            rs.getString("gia"),
                            rs.getString("so_luong"),
                            rs.getString("ma_thuong_hieu"),
                            rs.getString("thong_so_ki_thuat"),
                            rs.getString("ma_loai"),
                            rs.getString("hinh_anh_sp")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả sản phẩm
    public static ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> products = new ArrayList<>();
        String query = "SELECT * FROM san_pham";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new ProductDTO(
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        rs.getString("gia"),
                        rs.getString("so_luong"),
                        rs.getString("ma_thuong_hieu"),
                        rs.getString("thong_so_ki_thuat"),
                        rs.getString("ma_loai"),
                        rs.getString("hinh_anh_sp")
                ));
            }
            System.out.println("Lấy danh sách sản phẩm thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Cập nhật thông tin sản phẩm
    public void updateProduct(ProductDTO product) {
        String sql = "UPDATE san_pham SET ten_san_pham = ?, gia = ?, so_luong = ?, ma_thuong_hieu = ?, thong_so_ki_thuat = ?, ma_loai = ?, hinh_anh_sp = ? WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getGia());
            stmt.setString(3, product.getSoluong());
            stmt.setString(4, product.getMaThuongHieu());
            stmt.setString(5, product.getTSKT());
            stmt.setString(6, product.getML());
            stmt.setString(7, product.getAnh());
            stmt.setString(8, product.getProductID());

            stmt.executeUpdate();
            System.out.println("Cập nhật sản phẩm thành công.");
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lấy đường dẫn ảnh sản phẩm
    public static String getProductImage(String productID) {
        String imagePath = null;
        String query = "SELECT hinh_anh_sp FROM san_pham WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imagePath = rs.getString("hinh_anh_sp"); // Lấy tên file ảnh
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
