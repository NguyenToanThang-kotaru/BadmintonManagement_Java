/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.OrderDTO;
import java.sql.Connection;
import java.sql.Date;
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
        String query = "SELECT * FROM hoa_don";
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

    public void updateCustomer(OrderDTO order) {
        String sql = "UPDATE hoa_don SET ma_hoa_don = ?, ma_nhan_vien = ?, ma_khach_hang = ?, tong_tien = ? WHERE ngay_xuat = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getorderID());
            stmt.setString(2, order.getemployeeID());
            stmt.setString(3, order.getcustomerID());
            stmt.setString(4, order.gettotalmoney());
            stmt.setString(5, order.getissuedate());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(OrderDTO order) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
