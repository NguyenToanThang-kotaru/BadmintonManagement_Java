/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import BUS.CustomerBUS;
import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import DTO.OrderDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 *
 * @author Thang Nguyen
 */
public class StatistiscDAO {

    private CustomerBUS cusBUS = new CustomerBUS();

    public ArrayList<CustomerDTO> getCustomerByDateRange(String from, String to) {
        ArrayList<OrderDTO> orders = getOrdersByDateRange(from, to);
        System.out.println(orders);

        ArrayList<CustomerDTO> cus = new ArrayList<CustomerDTO>();
        for (OrderDTO ord : orders) {
            cus.add(cusBUS.getCustomerByID(ord.getcustomerID()));
        }
        System.out.println(cus);
        return cus;
    }

    public static ArrayList<OrderDTO> getOrdersByDateRange(String from, String to) {
        ArrayList<OrderDTO> orders = new ArrayList<>();
        System.out.println(from);
        System.out.println(to);
        String query = "SELECT * FROM hoa_don WHERE is_deleted = 0 AND DATE(ngay_xuat) BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập tham số cho câu truy vấn
            stmt.setString(1, from); // Ngày bắt đầu
            stmt.setString(2, to);   // Ngày kết thúc

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
}
