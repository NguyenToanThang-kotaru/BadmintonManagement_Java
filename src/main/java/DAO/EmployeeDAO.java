package DAO;

import DTO.EmployeeDTO;
import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public static Boolean addEmployee(EmployeeDTO employee) {
        String sql = "INSERT INTO nhan_vien (ma_nhan_vien, ten_nhan_vien, dia_chi, so_dien_thoai, hinh_anh, chuc_vu, is_deleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String newID = generateNewEmployeeID();
            stmt.setString(1, newID);
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getAddress());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getImage() != null ? employee.getImage() : "");
            stmt.setString(6, employee.getChucVu() != null ? employee.getChucVu() : "");
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
        String query = "UPDATE nhan_vien SET is_deleted = 1 WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            stmt.executeUpdate();
            System.out.println("Xóa thành công");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static EmployeeDTO getEmployeeByName(String name) {
        String query = "SELECT * FROM nhan_vien WHERE ten_nhan_vien = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
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
        String query = "SELECT * FROM nhan_vien WHERE so_dien_thoai = ? AND is_deleted = 0";
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
        String query = "SELECT * FROM nhan_vien WHERE is_deleted = 0";
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
            stmt.setString(4, employee.getImage() != null ? employee.getImage() : "");
            stmt.setString(5, employee.getChucVu() != null ? employee.getChucVu() : "");
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
        String query = "SELECT MAX(CAST(SUBSTRING(ma_nhan_vien, 3) AS UNSIGNED)) AS max_id FROM nhan_vien FOR UPDATE";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxNumber = rs.getInt("max_id");
                int nextNumber = maxNumber + 1;
                String newID = String.format("NV%03d", nextNumber);
                // Kiểm tra xem mã đã tồn tại chưa
                while (isEmployeeIDExists(newID)) {
                    nextNumber++;
                    newID = String.format("NV%03d", nextNumber);
                }
                return newID;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã nhân viên mới: " + e.getMessage());
            e.printStackTrace();
        }
        return "NV001"; // Mặc định nếu bảng rỗng
    }

    private static boolean isEmployeeIDExists(String employeeID) {
        String query = "SELECT COUNT(*) FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra mã nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<EmployeeDTO> getEmployeesWithoutAccount() {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String query = "SELECT * \n"
                + "FROM nhan_vien nv \n"
                + "LEFT JOIN tai_khoan tk ON nv.ma_nhan_vien = tk.ten_dang_nhap \n"
                + "WHERE tk.ten_dang_nhap IS NULL \n"
                + "   OR nv.is_deleted = 1;";
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
        String query = "SELECT ten_nhan_vien FROM nhan_vien WHERE ma_nhan_vien = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
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
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String query = "SELECT * FROM nhan_vien WHERE is_deleted = 0 AND (ma_nhan_vien LIKE ? OR ten_nhan_vien LIKE ? OR dia_chi LIKE ? OR so_dien_thoai LIKE ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static boolean importEmployees(List<EmployeeDTO> employees, List<Integer> isDeletedList) {
        String sql = "INSERT INTO nhan_vien (ma_nhan_vien, ten_nhan_vien, dia_chi, so_dien_thoai, hinh_anh, chuc_vu, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            // Lấy mã lớn nhất một lần duy nhất và tăng dần
            int maxNumber = getMaxEmployeeNumber(conn);
            for (int i = 0; i < employees.size(); i++) {
                EmployeeDTO employee = employees.get(i);
                int isDeleted = isDeletedList.get(i);
                maxNumber++;
                String newID = String.format("NV%03d", maxNumber);
                // Kiểm tra xem mã đã tồn tại chưa
                while (isEmployeeIDExists(newID)) {
                    maxNumber++;
                    newID = String.format("NV%03d", maxNumber);
                }
                stmt.setString(1, newID);
                stmt.setString(2, employee.getFullName());
                stmt.setString(3, employee.getAddress());
                stmt.setString(4, employee.getPhone());
                stmt.setString(5, employee.getImage() != null ? employee.getImage() : "");
                stmt.setString(6, employee.getChucVu() != null ? employee.getChucVu() : "");
                stmt.setInt(7, isDeleted);
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

    private static int getMaxEmployeeNumber(Connection conn) throws SQLException {
        String query = "SELECT MAX(CAST(SUBSTRING(ma_nhan_vien, 3) AS UNSIGNED)) AS max_id FROM nhan_vien FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id");
            }
        }
        return 0; // Nếu bảng rỗng, bắt đầu từ NV001
    }

    public static boolean isPhoneNumberExists(String phone) {
        String query = "SELECT COUNT(*) FROM nhan_vien WHERE so_dien_thoai = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
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
