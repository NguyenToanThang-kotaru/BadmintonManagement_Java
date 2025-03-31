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
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, sp.so_luong, sp.ma_nha_cung_cap, "
                + "sp.thong_so_ki_thuat, sp.ma_loai, lsp.ten_loai, sp.hinh_anh "
                + "FROM san_pham sp "
                + "JOIN loai lsp ON sp.ma_loai = lsp.ma_loai "
                + "WHERE sp.ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ProductID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProductDTO(
                            rs.getString("ma_san_pham"),
                            rs.getString("ten_san_pham"),
                            rs.getString("gia"),
                            rs.getString("so_luong"),
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("thong_so_ki_thuat"),
                            rs.getString("ma_loai"),
                            rs.getString("ten_loai"),
                            rs.getString("hinh_anh")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categoryList = new ArrayList<>();
        String query = "SELECT ten_loai FROM loai";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categoryList.add(rs.getString("ten_loai"));  // Lưu tên loại vào danh sách
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return categoryList;
    }

    // Lấy danh sách tất cả sản phẩm
    public static ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> products = new ArrayList<>();
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, sp.so_luong, sp.ma_nha_cung_cap, "
                + "sp.thong_so_ki_thuat, sp.ma_loai, lsp.ten_loai, sp.hinh_anh "
                + "FROM san_pham sp "
                + "JOIN loai lsp ON sp.ma_loai = lsp.ma_loai";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new ProductDTO(
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        rs.getString("gia"),
                        rs.getString("so_luong"),
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("thong_so_ki_thuat"),
                        rs.getString("ma_loai"),
                        rs.getString("ten_loai"),
                        rs.getString("hinh_anh")
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
    public static void updateProduct(ProductDTO product) {
        String findMaLoaiSQL = "SELECT ma_loai FROM loai WHERE ten_loai = ?";
        String updateProductSQL = "UPDATE san_pham SET ten_san_pham = ?, gia = ?, so_luong = ?, ma_nha_cung_cap = ?, thong_so_ki_thuat = ?, ma_loai = ?, hinh_anh = ? WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement findMaLoaiStmt = conn.prepareStatement(findMaLoaiSQL); PreparedStatement updateProductStmt = conn.prepareStatement(updateProductSQL)) {

            // 🔹 Tìm `ma_loai` từ `ten_loai`
            findMaLoaiStmt.setString(1, product.getTL());
            ResultSet rs = findMaLoaiStmt.executeQuery();
            String maLoai = null;

            if (rs.next()) {
                maLoai = rs.getString("ma_loai");  // Lấy `ma_loai` dưới dạng `String`
            } else {
                System.out.println("Không tìm thấy mã loại cho tên loại: " + product.getTL());
                return; // Không tiếp tục cập nhật nếu không tìm thấy
            }

            // 🔹 Cập nhật bảng `san_pham`
            updateProductStmt.setString(1, product.getProductName());
            updateProductStmt.setString(2, product.getGia());
            updateProductStmt.setString(3, product.getSoluong());
            updateProductStmt.setString(4, product.getMaNCC());
            updateProductStmt.setString(5, product.getTSKT());
            updateProductStmt.setString(6, maLoai); // Cập nhật `ma_loai` tìm được
            updateProductStmt.setString(7, product.getAnh());
            updateProductStmt.setString(8, product.getProductID());

            int rowsUpdated = updateProductStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật sản phẩm thành công.");
            } else {
                System.out.println("Không có sản phẩm nào được cập nhật. Kiểm tra lại ID!");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Lấy đường dẫn ảnh sản phẩm
    public static String getProductImage(String productID) {
        String imagePath = null;
        String query = "SELECT hinh_anh FROM san_pham WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imagePath = rs.getString("hinh_anh"); // Lấy tên file ảnh
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
