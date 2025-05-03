package DAO;

import DTO.EmployeeDTO;
import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public static Boolean addEmployee(EmployeeDTO employee) {
        String sql = "INSERT INTO nhan_vien (ma_nhan_vien, ten_nhan_vien, dia_chi, so_dien_thoai, hinh_anh, chuc_vu) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String newID = generateNewEmployeeID(); // Tạo ID mới

            stmt.setString(1, newID); // Sử dụng ID mới
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getAddress());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getImage());
            stmt.setString(6, employee.getChucVu()); // Thêm chuc_vu

            stmt.executeUpdate();
            System.out.println("Thêm nhân viên thành công với ID: " + newID);
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deleteEmployee(String employeeID) {
        String query = "UPDATE nhan_vien SET is_deleted = 1 WHERE ma_nhan_vien = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            stmt.executeUpdate();
            System.out.println("Xoa thanh cong");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static EmployeeDTO getEmployeeByName(String Name) {
        String query = "SELECT * FROM nhan_vien WHERE ten_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, Name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ten_nhan_vien"),
                            rs.getString("dia_chi"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("hinh_anh"),
                            rs.getString("chuc_vu")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EmployeeDTO getEmployee(String sdt) {
        String query = "SELECT * FROM nhan_vien WHERE so_dien_thoai = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sdt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ten_nhan_vien"),
                            rs.getString("dia_chi"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("hinh_anh"),
                            rs.getString("chuc_vu")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<EmployeeDTO> getAllEmployees() {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String query = "SELECT * FROM nhan_vien WHERE is_deleted = 0;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new EmployeeDTO(
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nhan_vien"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("hinh_anh"),
                        rs.getString("chuc_vu")
                ));
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public static Boolean updateEmployee(EmployeeDTO employee) {
        String sql = "UPDATE nhan_vien SET ten_nhan_vien = ?, dia_chi = ?, so_dien_thoai = ?, hinh_anh = ?, chuc_vu = ? WHERE ma_nhan_vien = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getAddress());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getImage());
            stmt.setString(5, employee.getChucVu()); // Thêm chuc_vu
            stmt.setString(6, employee.getEmployeeID());
            stmt.executeUpdate();
            System.out.println("Cập nhật nhân viên thành công.");
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String generateNewEmployeeID() {
        String query = "SELECT ma_nhan_vien FROM nhan_vien ORDER BY ma_nhan_vien DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("ma_nhan_vien"); // Ví dụ: "NV005"

                // Cắt bỏ "NV", chỉ lấy số
                int number = Integer.parseInt(lastID.substring(2));

                // Tạo ID mới với định dạng NVXXX
                return String.format("NV%03d", number + 1); // Ví dụ: "NV006"
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã nhân viên mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "NV001"; // Nếu không có nhân viên nào, bắt đầu từ "NV001"
    }

    public static ArrayList<EmployeeDTO> getEmployeesWithoutAccount() {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String query = "SELECT * FROM nhan_vien nv "
                + "LEFT JOIN tai_khoan tk ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap IS NULL AND nv.is_deleted = 0;";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new EmployeeDTO(
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nhan_vien"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("hinh_anh"),
                        rs.getString("chuc_vu")
                ));
            }
            System.out.println("Lấy danh sách nhân viên chưa có tài khoản thành công.");
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhân viên chưa có tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }
    
    public static String getEmployeeNameByID(String employeeID) {
        String query = "SELECT ten_nhan_vien FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("ten_nhan_vien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ArrayList<EmployeeDTO> searchEmployee(String keyword) {
        ArrayList<EmployeeDTO> employee = new ArrayList<>();
        String query = "SELECT * FROM nhan_vien WHERE is_deleted = 0 AND " +
                      "(ma_nhan_vien LIKE ? OR ten_nhan_vien LIKE ? OR dia_chi LIKE ? OR so_dien_thoai LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employee.add(new EmployeeDTO(
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nhan_vien"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("hinh_anh"),
                        rs.getString("chuc_vu")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    public static boolean importEmployees(List<EmployeeDTO> employees) {
        String sql = "INSERT INTO nhan_vien (ma_nhan_vien, ten_nhan_vien, dia_chi, so_dien_thoai, hinh_anh, chuc_vu, is_deleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            String lastID = getLastEmployeeID(conn); // Lấy mã lớn nhất một lần
            int number = lastID != null ? Integer.parseInt(lastID.substring(2)) : 0;
            for (EmployeeDTO employee : employees) {
                number++;
                String newID = String.format("NV%03d", number);
                stmt.setString(1, newID);
                stmt.setString(2, employee.getFullName());
                stmt.setString(3, employee.getAddress());
                stmt.setString(4, employee.getPhone());
                stmt.setString(5, employee.getImage() != null ? employee.getImage() : "");
                // Gán chuỗi rỗng cho chuc_vu nếu không có giá trị
                stmt.setString(6, employee.getChucVu() != null ? employee.getChucVu() : "");
                stmt.addBatch();
                employee.setEmployeeID(newID);
            }
            stmt.executeBatch();
            conn.commit();
            System.out.println("Nhập danh sách nhân viên từ Excel thành công.");
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi nhập danh sách nhân viên từ Excel: " + e.getMessage());
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

    
    private static String getLastEmployeeID(Connection conn) throws SQLException {
        String query = "SELECT ma_nhan_vien FROM nhan_vien WHERE is_deleted = 0 ORDER BY ma_nhan_vien DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("ma_nhan_vien");
            }
        }
        return null;
    } public static boolean isPhoneNumberExists(String phone) {
        String query = "SELECT COUNT(*) FROM nhan_vien WHERE so_dien_thoai = ? AND is_deleted = 0";
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