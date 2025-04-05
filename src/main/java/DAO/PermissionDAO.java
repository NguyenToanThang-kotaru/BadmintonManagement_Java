/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.EmployeeDTO;
import DTO.PermissionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class PermissionDAO {

    public static PermissionDTO getPermission(String maquyen) {
        String query = "SELECT q.ma_quyen, q.ten_quyen, cn.ten_chuc_nang, "
                + "(SELECT COUNT(*) FROM tai_khoan WHERE ma_quyen = q.ma_quyen AND is_deleted = 0) AS sl_tk "
                + "FROM quyen q "
                + "LEFT JOIN phan_quyen pq ON q.ma_quyen = pq.ma_quyen "
                + "LEFT JOIN chuc_nang cn ON pq.ma_chuc_nang = cn.ma_chuc_nang "
                + "WHERE q.ma_quyen = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, maquyen);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> chucNangList = new ArrayList<>();
                String tenQuyen = "";
                String slTk = "0";

                while (rs.next()) {
                    if (tenQuyen.isEmpty()) {
                        tenQuyen = rs.getString("ten_quyen");
                        slTk = rs.getString("sl_tk");
                    }
                    String chucNang = rs.getString("ten_chuc_nang");
                    if (chucNang != null) {
                        chucNangList.add(chucNang);
                    }
                }

                if (!tenQuyen.isEmpty()) {
                    return new PermissionDTO(maquyen, tenQuyen, chucNangList, slTk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean deletePermission(String maquyen) {
        String sql = "UPDATE quyen SET is_deleted = 1 WHERE ma_quyen = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maquyen);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra để debug
        }
        return false;
    }

    public static PermissionDTO getPermissionByName(String tenquyen) {
        String query = "SELECT q.ma_quyen, q.ten_quyen, cn.ten_chuc_nang, "
                + "(SELECT COUNT(*) FROM tai_khoan WHERE ma_quyen = q.ma_quyen AND is_deleted = 0) AS sl_tk "
                + "FROM quyen q "
                + "LEFT JOIN phan_quyen pq ON q.ma_quyen = pq.ma_quyen "
                + "LEFT JOIN chuc_nang cn ON pq.ma_chuc_nang = cn.ma_chuc_nang "
                + "WHERE q.ten_quyen = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tenquyen);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> chucNangList = new ArrayList<>();
                String maQuyen = "";
                String slTk = "0";

                while (rs.next()) {
                    if (maQuyen.isEmpty()) {
                        maQuyen = rs.getString("ma_quyen");
                        slTk = rs.getString("sl_tk");
                    }
                    String chucNang = rs.getString("ten_chuc_nang");
                    if (chucNang != null) {
                        chucNangList.add(chucNang);
                    }
                }

                if (!maQuyen.isEmpty()) {
                    return new PermissionDTO(maQuyen, tenquyen, chucNangList, slTk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<PermissionDTO> getAllPermissions() {
        ArrayList<PermissionDTO> permissions = new ArrayList<>();
        String query = "SELECT q.ma_quyen, q.ten_quyen, "
                + "(SELECT COUNT(*) FROM tai_khoan WHERE ma_quyen = q.ma_quyen AND is_deleted = 0) AS sl_tk "
                + "FROM quyen q "
                + "WHERE q.is_deleted = 0";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maQuyen = rs.getString("ma_quyen");
                String tenQuyen = rs.getString("ten_quyen");
                String slTk = rs.getString("sl_tk");

                // Lấy danh sách chức năng
                List<String> chucNangList = getChucNangByMaQuyen(conn, maQuyen);

                permissions.add(new PermissionDTO(maQuyen, tenQuyen, chucNangList, slTk));
            }
            System.out.println("Lấy danh sách quyền thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách quyền: " + e.getMessage());
            e.printStackTrace();
        }
        return permissions;
    }

// Phương thức hỗ trợ lấy danh sách chức năng theo mã quyền
    private static List<String> getChucNangByMaQuyen(Connection conn, String maQuyen) throws SQLException {
        List<String> chucNangList = new ArrayList<>();
        String query = "SELECT cn.ten_chuc_nang FROM phan_quyen pq "
                + "JOIN chuc_nang cn ON pq.ma_chuc_nang = cn.ma_chuc_nang "
                + "WHERE pq.ma_quyen = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maQuyen);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    chucNangList.add(rs.getString("ten_chuc_nang"));
                }
            }
        }
        return chucNangList;
    }

    public static void main(String[] args) {
        // Gọi hàm lấy thông tin quyền với mã quyền "1"
        PermissionDTO permission = PermissionDAO.getPermission("1");

        if (permission != null) {
            System.out.println("Mã quyền: " + permission.getID());
            System.out.println("Tên quyền: " + permission.getName());
            System.out.println("Số lượng quyền: " + permission.getSlChucNang());
            System.out.println("Danh sách chức năng:");
            for (String chucNang : permission.getChucNang()) {
                System.out.println(" - " + chucNang);
            }
        } else {
            System.out.println("Không tìm thấy quyền với mã quyền = 1.");
        }
    }

}
