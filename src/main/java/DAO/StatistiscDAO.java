/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import DTO.OrderDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class StatistiscDAO {

//    public static int get
    
    public static ArrayList<CustomerDTO> getCustomerByDateRange(String fromDate, String toDate) {
        ArrayList<CustomerDTO> list = new ArrayList<>();
        String sql = """
        SELECT DISTINCT kh.ma_khach_hang, kh.ten_khach_hang, kh.so_dien_thoai
        FROM khach_hang kh
        JOIN hoa_don hd ON kh.ma_khach_hang = hd.ma_khach_hang
        WHERE hd.is_deleted = 0 AND hd.ngay_xuat BETWEEN ? AND ?
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ma_khach_hang");
                String name = rs.getString("ten_khach_hang");
                String phone = rs.getString("so_dien_thoai");

                CustomerDTO customer = new CustomerDTO(id, name, phone);
                list.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<OrderDTO> getOrdersByDateRange(String fromDate, String toDate) {
        ArrayList<OrderDTO> orderList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Kết nối database
            conn = DatabaseConnection.getConnection();

            // Câu truy vấn SQL (điều chỉnh theo cấu trúc bảng của bạn)
            String sql = "SELECT hd.ma_hoa_don, hd.ngay_xuat, hd.tong_tien, hd.tong_loi_nhuan, "
                    + "hd.ma_khach_hang, hd.ma_nhan_vien, hd.is_deleted "
                    + "FROM hoa_don hd "
                    + "WHERE hd.ngay_xuat BETWEEN ? AND ? "
                    + "AND hd.is_deleted = 0 "
                    + "ORDER BY hd.ngay_xuat DESC";

            pst = conn.prepareStatement(sql);
            pst.setString(1, fromDate);
            pst.setString(2, toDate);

            rs = pst.executeQuery();

            // Duyệt kết quả và thêm vào danh sách
            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setorderID(rs.getString("ma_hoa_don"));
                order.setissuedate(rs.getString("ngay_xuat"));
                order.settotalmoney(rs.getString("tong_tien"));
                order.settotalprofit(rs.getString("tong_loi_nhuan"));
                order.setcustomerID(rs.getString("ma_khach_hang"));
                order.setemployeeID(rs.getString("ma_nhan_vien"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        } finally {
            // Đóng các kết nối
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return orderList;
    }
}
