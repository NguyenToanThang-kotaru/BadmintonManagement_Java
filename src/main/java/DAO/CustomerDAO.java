package DAO;

import DTO.CustomerDTO;

import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public static CustomerDTO getCustomer(String maKhachHang) {
        String query = "SELECT * FROM khach_hang WHERE ma_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDTO(
                            rs.getString("ma_khach_hang"),
                            rs.getString("ten_khach_hang"),
                            rs.getString("so_dien_thoai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<CustomerDTO> getAllCustomer() {
        ArrayList<CustomerDTO> customer = new ArrayList<>();
        String query = "SELECT * FROM khach_hang WHERE is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customer.add(new CustomerDTO(
                        rs.getString("ma_khach_hang"),
                        rs.getString("ten_khach_hang"),
                        rs.getString("so_dien_thoai")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public void updateCustomer(CustomerDTO customer) {
        String sql = "UPDATE khach_hang SET ma_khach_hang = ?, ten_khach_hang = ? WHERE so_dien_thoai = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getcustomerID());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhone());
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
                            rs.getString("so_dien_thoai")
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
        String query = "INSERT INTO khach_hang (ma_khach_hang, ten_khach_hang, so_dien_thoai, is_deleted) VALUES (?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getcustomerID());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhone());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteCustomer(String customerID) {
        String query = "UPDATE khach_hang SET is_deleted = 1 WHERE ma_khach_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerID);
            stmt.executeUpdate();
            System.out.println("Xóa thành công");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<CustomerDTO> searchCustomer(String keyword) {
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        String query = "SELECT * FROM khach_hang WHERE is_deleted = 0 AND " +
                      "(ma_khach_hang LIKE ? OR ten_khach_hang LIKE ? OR so_dien_thoai LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(new CustomerDTO(
                        rs.getString("ma_khach_hang"),
                        rs.getString("ten_khach_hang"),
                        rs.getString("so_dien_thoai")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static boolean importCustomers(List<CustomerDTO> customers, List<Integer> isDeletedList) {
        String sql = "INSERT INTO khach_hang (ma_khach_hang, ten_khach_hang, so_dien_thoai, is_deleted) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            // Lấy mã khách hàng lớn nhất hiện tại
            String maxId = null;
            String query = "SELECT MAX(ma_khach_hang) AS max_id FROM khach_hang";
            try (PreparedStatement maxStmt = conn.prepareStatement(query);
                 ResultSet rs = maxStmt.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getString("max_id");
                }
            }
            int nextNum = (maxId == null) ? 1 : Integer.parseInt(maxId.substring(2)) + 1;

            // Thêm từng khách hàng với mã tăng dần
            for (int i = 0; i < customers.size(); i++) {
                CustomerDTO customer = customers.get(i);
                int isDeleted = isDeletedList.get(i);
                String newID = String.format("KH%03d", nextNum);
                stmt.setString(1, newID);
                stmt.setString(2, customer.getFullName());
                stmt.setString(3, customer.getPhone());
                stmt.setInt(4, isDeleted);
                stmt.addBatch();
                customer.setcustomerID(newID);
                nextNum++; // Tăng mã cho bản ghi tiếp theo
            }
            stmt.executeBatch();
            conn.commit();
            System.out.println("Nhập danh sách khách hàng từ Excel thành công.");
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi nhập danh sách khách hàng từ Excel: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Đã rollback giao dịch.");
                } catch (SQLException ex) {
                    System.out.println("Lỗi khi rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isPhoneNumberExists(String phone) {
        String query = "SELECT COUNT(*) FROM khach_hang WHERE so_dien_thoai = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra số điện thoại: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}