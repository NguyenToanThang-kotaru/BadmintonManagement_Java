/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import BUS.CustomerBUS;
import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import DTO.OrderDTO;
import DTO.DetailOrderDTO;
import DTO.ProductDTO;
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
    
    public ArrayList<Object[]> getProductStatistics() {
        ArrayList<Object[]> result = new ArrayList<>();

        String query = """
            SELECT sp.ma_san_pham, sp.ten_san_pham, sp.so_luong, SUM(cthd.so_luong) AS tong_so_luong_ban, SUM(cthd.loi_nhuan) AS tong_loi_nhuan
            FROM chi_tiet_hoa_don cthd
            JOIN san_pham sp ON cthd.ma_san_pham = sp.ma_san_pham
            JOIN hoa_don hd ON cthd.ma_hoa_don = hd.ma_hoa_don
            WHERE hd.is_deleted = 0
            GROUP BY sp.ma_san_pham, sp.ten_san_pham, sp.so_luong
        """;

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("ma_san_pham"));
                product.setProductName(rs.getString("ten_san_pham"));
                product.setSoluong(rs.getString("so_luong"));

                int soLuongBan = rs.getInt("tong_so_luong_ban");
                int loiNhuan = rs.getInt("tong_loi_nhuan");

                result.add(new Object[] { product, soLuongBan, loiNhuan });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
