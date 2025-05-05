package DAO;

import DTO.OrderDTO;

import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDAO {

    public long getTotalSpentByCustomer(CustomerDTO cus) {
        long total = 0;
        String query = "SELECT SUM(tong_tien) FROM hoa_don WHERE ma_khach_hang = ? AND is_deleted = 0";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cus.getcustomerID());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getLong(1); // Lấy tổng tiền
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getQuantityOrderForCus(CustomerDTO cus) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM hoa_don WHERE ma_khach_hang = ? AND is_deleted = 0";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập tham số cho câu truy vấn
            stmt.setString(1, cus.getcustomerID()); // Sử dụng mã khách hàng từ DTO

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1); // Lấy giá trị COUNT
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<OrderDTO> getAllOrderVerify() {
        ArrayList<OrderDTO> order = new ArrayList<>();
        String query = "SELECT * FROM hoa_don where is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                order.add(new OrderDTO(
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ma_khach_hang"),
                        rs.getString("tong_tien"),
                        rs.getString("ngay_xuat"),
                        rs.getString("tong_loi_nhuan"),
                        rs.getBoolean("is_deleted")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public static OrderDTO getOrder(String orderID) {
        String query = "SELECT * FROM hoa_don WHERE ma_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderDTO(
                            rs.getString("ma_hoa_don"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ma_khach_hang"),
                            rs.getString("tong_tien"),
                            rs.getString("ngay_xuat"),
                            rs.getString("tong_loi_nhuan"),
                            rs.getBoolean("is_deleted")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static ArrayList<OrderDTO> getAllOrder() {
        ArrayList<OrderDTO> order = new ArrayList<>();
        String query = "SELECT * FROM hoa_don";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                order.add(new OrderDTO(
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ma_khach_hang"),
                        rs.getString("tong_tien"),
                        rs.getString("ngay_xuat"),
                        rs.getString("tong_loi_nhuan"),
                        rs.getBoolean("is_deleted")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public void updateOrder(OrderDTO order) {
        // Tính tổng lợi nhuận từ các chi tiết hóa đơn
        String sumProfitQuery = "SELECT SUM(loi_nhuan) AS tong_loi_nhuan FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";
        double totalProfit = 0;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement sumStmt = conn.prepareStatement(sumProfitQuery)) {
            sumStmt.setString(1, order.getorderID());
            try (ResultSet rs = sumStmt.executeQuery()) {
                if (rs.next()) {
                    totalProfit = rs.getDouble("tong_loi_nhuan");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "UPDATE hoa_don SET ma_nhan_vien = ?, ma_khach_hang = ?, tong_tien = ?, tong_loi_nhuan = ? WHERE ma_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getemployeeID());
            stmt.setString(2, order.getcustomerID());
            stmt.setString(3, order.gettotalmoney());
            stmt.setDouble(4, totalProfit);
            stmt.setString(5, order.getorderID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteOrder(String orderID) {
        String queery = "UPDATE hoa_don SET is_deleted = 1 WHERE ma_hoa_don = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(queery)) {
            stmt.setString(1, orderID);
            stmt.executeUpdate();
            System.out.println("Xoa thanh cong");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertOrder(OrderDTO order) {
        String sql = "INSERT INTO hoa_don (ma_hoa_don, ma_nhan_vien, ma_khach_hang, tong_tien, is_deleted, tong_loi_nhuan) VALUES (?, ?, ?, ?, 0, 0)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getorderID());
            stmt.setString(2, order.getemployeeID());
            stmt.setString(3, order.getcustomerID());
            stmt.setString(4, order.gettotalmoney());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTotalProfit(String orderID) {
        String sumProfitQuery = "SELECT SUM(loi_nhuan) AS tong_loi_nhuan FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";
        String updateQuery = "UPDATE hoa_don SET tong_loi_nhuan = ? WHERE ma_hoa_don = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement sumStmt = conn.prepareStatement(sumProfitQuery); PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            sumStmt.setString(1, orderID);
            try (ResultSet rs = sumStmt.executeQuery()) {
                if (rs.next()) {
                    double totalProfit = rs.getDouble("tong_loi_nhuan");
                    updateStmt.setDouble(1, totalProfit);
                    updateStmt.setString(2, orderID);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNextOrderID() {
        String query = "SELECT ma_hoa_don FROM hoa_don ORDER BY ma_hoa_don DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("ma_hoa_don"); // Ví dụ: "HD005"

                // Cắt bỏ "HD", chỉ lấy số
                int number = Integer.parseInt(lastID.substring(2));

                // Tạo ID mới với định dạng HDXXX
                return String.format("HD%03d", number + 1); // Ví dụ: "HD006"
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return "HD001"; // Nếu không có nhân viên nào, bắt đầu từ "NV001"
    }

    public static ArrayList<OrderDTO> searchOrder(String keyword) {
        ArrayList<OrderDTO> orders = new ArrayList<>();
        String query = "SELECT * FROM hoa_don WHERE is_deleted = 0 AND "
                + "(ma_hoa_don LIKE ? OR ma_nhan_vien LIKE ? OR ma_khach_hang LIKE ? OR ngay_xuat LIKE ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new OrderDTO(
                            rs.getString("ma_hoa_don"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ma_khach_hang"),
                            rs.getString("tong_tien"),
                            rs.getString("ngay_xuat"),
                            rs.getString("tong_loi_nhuan"),
                            rs.getBoolean("is_deleted")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public static void restoreProductsFromCanceledOrder(String OrderID) {
        String query = """
            SELECT ma_san_pham, ma_serial
            FROM chi_tiet_hoa_don
            WHERE ma_hoa_don = ? AND is_deleted = 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, OrderID);
            ResultSet rs = stmt.executeQuery();

            HashMap<String, Integer> quantityMap = new HashMap<>();

            while (rs.next()) {
                String maSanPham = rs.getString("ma_san_pham");
                String maSerial = rs.getString("ma_serial");

                // Đặt lại is_deleted = 0 trong danh_sach_san_pham
                try (PreparedStatement updateSerial = conn.prepareStatement(
                        "UPDATE danh_sach_san_pham SET is_deleted = 0 WHERE ma_serial = ?")) {
                    updateSerial.setString(1, maSerial);
                    updateSerial.executeUpdate();
                }

                // Tính lại số lượng
                quantityMap.put(maSanPham, quantityMap.getOrDefault(maSanPham, 0) + 1);
            }

            // Cộng lại số lượng tồn sản phẩm
            for (Map.Entry<String, Integer> entry : quantityMap.entrySet()) {
                try (PreparedStatement updateProduct = conn.prepareStatement(
                        "UPDATE san_pham SET so_luong = so_luong + ? WHERE ma_san_pham = ?")) {
                    updateProduct.setInt(1, entry.getValue());
                    updateProduct.setString(2, entry.getKey());
                    updateProduct.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
