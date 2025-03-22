package DAO;

import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    public static CustomerDTO getCustomer(int maKhachHang) {
        String query = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDTO(
                            rs.getString("ma_khach_hang"),
                            rs.getString("ten_khach_hang"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("email")  
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<CustomerDTO> getAllCustomer() {
        ArrayList<CustomerDTO> customer = new ArrayList<>();
        String query = "SELECT * FROM khach_hang";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customer.add(new CustomerDTO(
                        rs.getString("ma_khach_hang"),
                        rs.getString("ten_khach_hang"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return customer;
    }

    public void updateCustomer(CustomerDTO customer) {
        String sql = "UPDATE customer SET ma_khach_hang = ?, ten_khach_hang = ?, so_dien_thoai = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getcustomerID());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

}
