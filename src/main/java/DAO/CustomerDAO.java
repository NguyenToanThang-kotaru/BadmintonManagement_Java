package DAO;

import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    public static CustomerDTO getCustomer(String  maKhachHang) {
        String query = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maKhachHang);
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
        String query = "SELECT * FROM khach_hang WHERE is_deleted = 0";
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
        String sql = "UPDATE khach_hang SET ma_khach_hang = ?, ten_khach_hang = ?, so_dien_thoai = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getcustomerID());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    public String getCustomerNameByID(String customerID) {
        String customerName = "";
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT ten_khach_hang FROM khach_hang WHERE ma_khach_hang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customerName = rs.getString("ten_khach_hang");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerName;
    }
    
    public static CustomerDTO getCustomerByPhone(String phone) {
        String query = "SELECT * FROM khach_hang WHERE so_dien_thoai = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, phone);
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
    
    public static String getNextCustomerID() {
        String query = "SELECT MAX(ma_khach_hang) AS max_id FROM khach_hang";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String maxId = rs.getString("max_id"); // VD: KH009
                if (maxId != null) {
                    int num = Integer.parseInt(maxId.substring(2)) + 1;
                    return String.format("KH%03d", num);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KH001"; // Nếu chưa có khách nào
    }
    
    public static void addCustomer(CustomerDTO customer) {
        String query = "INSERT INTO khach_hang (ma_khach_hang, ten_khach_hang, so_dien_thoai, email, is_deleted) VALUES (?, ?, ?, ?,0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getcustomerID());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail() != null ? customer.getEmail() : "");
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteCustomer(String customerID) {
        String queery = "UPDATE khach_hang SET is_deleted = 1 WHERE ma_khach_hang = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(queery)) {
            stmt.setString(1, customerID);
            stmt.executeUpdate();
            System.out.println("Xoa thanh cong");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<CustomerDTO> searchCustomer(String keyword) {
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        String query = "SELECT * FROM khach_hang WHERE is_deleted = 0 AND " +
                      "(ma_khach_hang LIKE ? OR ten_khach_hang LIKE ? OR so_dien_thoai LIKE ? OR email LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(new CustomerDTO(
                        rs.getString("ma_khach_hang"),
                        rs.getString("ten_khach_hang"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }
}
