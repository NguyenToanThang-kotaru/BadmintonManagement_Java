package DAO;

import Connection.DatabaseConnection;
import DTO.OrderDTO;
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
                            rs.getString("ngay_xuat")
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
                        rs.getString("ngay_xuat")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public void updateOrder(OrderDTO order) {
        String sql = "UPDATE hoa_don SET ma_nhan_vien = ?, ma_khach_hang = ?, tong_tien = ? WHERE ma_hoa_don = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getemployeeID());
            stmt.setString(2, order.getcustomerID());
            stmt.setString(3, order.gettotalmoney());
            stmt.setString(4, order.getorderID());

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
        String sql = "INSERT INTO hoa_don (ma_hoa_don, ma_nhan_vien, ma_khach_hang, tong_tien, is_deleted) VALUES (?, ?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getorderID());
            stmt.setString(2, order.getemployeeID());
            stmt.setString(3, order.getcustomerID());
            stmt.setString(4, order.gettotalmoney());
            System.out.print("da luu thanh cong");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("That bai cmnr");
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
}