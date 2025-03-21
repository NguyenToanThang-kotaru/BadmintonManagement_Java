package DAO;

import Connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Lớp này dùng để kết nối database và lấy dữ liệu sản phẩm
public class GuaranteeDAO {

    // Phương thức lấy danh sách sản phẩm từ database
    public static List<String[]> getGuaranteeData() {
        List<String[]> productList = new ArrayList<>();
        String query = "SELECT ma_bao_hanh, ten_san_pham, gia, so_luong, ma_thuong_hieu, thong_so_ki_thuat, ma_loai FROM san_pham";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] product = new String[]{
                   rs.getString("ma_san_pham"),
                    rs.getString("ten_san_pham"),
                    rs.getString("gia"),
                    rs.getString("so_luong"),
                    rs.getString("ma_thuong_hieu"),
                    rs.getString("thong_so_ki_thuat"),
                    rs.getString("ma_loai"),};
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public static String getProductImage(String productID) {
        String imagePath = null;
        String query = "SELECT hinh_anh_sp FROM san_pham WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imagePath = rs.getString("hinh_anh_sp"); // Lấy tên file ảnh
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
