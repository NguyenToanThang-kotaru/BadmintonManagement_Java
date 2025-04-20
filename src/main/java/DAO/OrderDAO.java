package DAO;

import DTO.OrderDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAO {
    
    public static OrderDTO getOrder(String orderID) {
        String query = "SELECT * FROM hoa_don WHERE ma_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderDTO(
                            rs.getString("ma_hoa_don"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ma_khach_hang"),
                            rs.getString("tong_tien"),
                            rs.getString("ngay_xuat"),
                            rs.getString("tong_loi_nhuan")
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
        String query = "SELECT * FROM hoa_don WHERE is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                order.add(new OrderDTO(
                        rs.getString("ma_hoa_don"),
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ma_khach_hang"),
                        rs.getString("tong_tien"),
                        rs.getString("ngay_xuat"),
                        rs.getString("tong_loi_nhuan")
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

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement sumStmt = conn.prepareStatement(sumProfitQuery)) {
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement sumStmt = conn.prepareStatement(sumProfitQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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
        String query = "SELECT * FROM hoa_don WHERE is_deleted = 0 AND " +
                       "(ma_hoa_don LIKE ? OR ma_nhan_vien LIKE ? OR ma_khach_hang LIKE ? OR ngay_xuat LIKE ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

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
                        rs.getString("tong_loi_nhuan") 
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}