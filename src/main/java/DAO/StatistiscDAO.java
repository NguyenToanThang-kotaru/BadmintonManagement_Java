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
import java.sql.SQLException;

/**
 *
 * @author Thang Nguyen
 */
public class StatistiscDAO {
    
    private CustomerBUS cusBUS = new CustomerBUS();
    
    public ArrayList<Object[]> filterProductForCate(ArrayList<ProductDTO> product, String cate) {
        ArrayList<Object[]> kq = new ArrayList<Object[]>();
        String sql = "SELECT sp.*, loai.* FROM `san_pham` sp "
                + "JOIN loai ON sp.ma_loai = loai.ma_loai "
                + "WHERE loai.ten_loai = '?';";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cate);
            try (ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    String PD_ID = rs.getString("sp.ma_san_pham");
                    for (ProductDTO pd : product) {
                        if (PD_ID.equals(pd.getProductID())) {
                            ProductDTO newProduct = new ProductDTO();
                            newProduct.setProductID(rs.getString("ma_san_pham"));
                            newProduct.setProductName(rs.getString("ten_san_pham"));
                            newProduct.setSoluong(rs.getString("so_luong"));
                            
                            int soLuongBan = rs.getInt("tong_so_luong_ban");
                            int loiNhuan = rs.getInt("tong_loi_nhuan");
                            
                            kq.add(new Object[]{product, soLuongBan, loiNhuan});
                        }
                    }
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }
    
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
    
    public ArrayList<ProductDTO> getProductStatistics() {
        ArrayList<ProductDTO> result = new ArrayList<>();
        
        String query = "SELECT cthd.*, sp.* FROM chi_tiet_hoa_don cthd\n"
                + "JOIN san_pham sp ON sp.ma_san_pham = cthd.ma_san_pham ";
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("ma_san_pham"));
                product.setProductName(rs.getString("ten_san_pham"));
                product.setSoluong(rs.getString("so_luong"));
                
                if (!result.contains(rs)) {
                    result.add(product);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
