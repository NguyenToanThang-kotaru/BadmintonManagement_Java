package DAO;

import Connection.DatabaseConnection;
import DTO.EmployeeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO {

    public static void addEmployee(EmployeeDTO employee) {
        String sql = "INSERT INTO nhan_vien (ma_nhan_vien, ten_nhan_vien, dia_chi, so_dien_thoai, hinh_anh) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String newID = generateNewEmployeeID(); // Tạo ID mới

            stmt.setString(1, newID); // Sử dụng ID mới
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getAddress());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getImage());

            stmt.executeUpdate();
            System.out.println("Thêm nhân viên thành công với ID: " + newID);
        } catch (SQLException e) {
            System.out.println("Lỗi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static EmployeeDTO getEmployee(String maNhanVien) {
        String query = "SELECT * FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ten_nhan_vien"),
                            rs.getString("dia_chi"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("hinh_anh")
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
        String query = "SELECT * FROM nhan_vien";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new EmployeeDTO(
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nhan_vien"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("hinh_anh")
                ));
            }
            System.out.println("Lấy danh sách nhân viên thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public static void updateEmployee(EmployeeDTO employee) {
        String sql = "UPDATE nhan_vien SET ten_nhan_vien = ?, dia_chi = ?, so_dien_thoai = ?, ma_tai_khoan = ?, ma_quyen = ? WHERE ma_nhan_vien = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getEmployeeID());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getAddress());// Chuyển về vị trí đúng

            stmt.executeUpdate();
            System.out.println("Cập nhật nhân viên thành công.");
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật nhân viên: " + e.getMessage());
            e.printStackTrace();
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

}
