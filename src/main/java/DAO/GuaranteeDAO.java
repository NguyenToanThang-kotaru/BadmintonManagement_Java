package DAO;

import DTO.GuaranteeDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Lớp này dùng để kết nối database và lấy dữ liệu sản phẩm
public class GuaranteeDAO {

    // Lấy thông tin của một sản phẩm
    public static GuaranteeDTO getGuarantee(String BaohanhID) {
        String query = """
    SELECT bh.*
    FROM bao_hanh bh
    JOIN danh_sach_san_pham ds ON bh.ma_serial = ds.ma_serial
    JOIN san_pham sp ON ds.ma_san_pham = sp.ma_san_pham
    WHERE bh.ma_bao_hanh = ? AND bh.thoi_gian_bao_hanh < sp.thoi_gian_bao_hanh
""";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, BaohanhID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GuaranteeDTO(
                            rs.getString("ma_bao_hanh"),
                            rs.getString("ma_serial"),
                            rs.getString("ly_do_bao_hanh"),
                            String.valueOf(rs.getInt("thoi_gian_bao_hanh")),
                            rs.getString("trang_thai")
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

        String query = """
    SELECT bh.*
    FROM bao_hanh bh
    JOIN danh_sach_san_pham ds ON bh.ma_serial = ds.ma_serial
    JOIN san_pham sp ON ds.ma_san_pham = sp.ma_san_pham
    WHERE bh.thoi_gian_bao_hanh < sp.thoi_gian_bao_hanh
""";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String trangThai = rs.getString("trang_thai");
                if (trangThai == null || trangThai.trim().isEmpty()) {
                    trangThai = "Không";
                }

                products.add(new GuaranteeDTO(
                        rs.getString("ma_bao_hanh"),
                        rs.getString("ma_serial"),
                        rs.getString("ly_do_bao_hanh"),
                        String.valueOf(rs.getInt("thoi_gian_bao_hanh")),
                        trangThai
                ));
            }

//            System.out.println("Lấy danh sách sản phẩm bảo hành thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Cập nhật thông tin sản phẩm
    public static void updateGuarantee(GuaranteeDTO guarantee) {
        String updateSql = "UPDATE bao_hanh SET ma_serial = ?, ly_do_bao_hanh = ?, thoi_gian_bao_hanh = ?, trang_thai = ? WHERE ma_bao_hanh = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, guarantee.getSerialID());
            stmt.setString(2, guarantee.getLydo());
            stmt.setString(3, guarantee.getTGBH());
            stmt.setString(4, guarantee.gettrangthai());
            stmt.setString(5, guarantee.getBaohanhID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật bảo hành thành công.");
            } else {
                System.out.println("Không tìm thấy bảo hành để cập nhật.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật bảo hành: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String generateNewGuaranteeID() {
        String query = "SELECT ma_bao_hanh FROM bao_hanh ORDER BY ma_bao_hanh DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("ma_bao_hanh"); // Ví dụ: "NV005"

                // Cắt bỏ "TK", chỉ lấy số
                int number = Integer.parseInt(lastID.substring(2));

                // Tạo ID mới với định dạng NVXXX
                return String.format("BH%03d", number + 1);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã nhân viên mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "BH001"; // Nếu không có nhân viên nào, bắt đầu từ "NV001"
    }

    public static boolean addGuarantee(String ma_serial) {
        String sql = "INSERT INTO `bao_hanh`(`ma_bao_hanh`, `ma_serial`, `ly_do_bao_hanh`, `trang_thai`, `is_deleted`) VALUES (?, ?, '', 'Không', 0) ";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String newID = generateNewGuaranteeID(); // Tạo ID mới

            stmt.setString(1, newID);
            stmt.setString(2, ma_serial);
            stmt.executeUpdate();
            System.out.println("Thêm sản phẩm thành công với ID: " + newID);
            return true;

        } catch (SQLException e) {
            System.out.println("Lỗi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
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
