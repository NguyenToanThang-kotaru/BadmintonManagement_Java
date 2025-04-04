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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class PermissionDAO {
    public static PermissionDTO getPermission(String maquyen) {
        String query = "SELECT q.ma_quyen, q.ten_quyen, cn.ten_chuc_nang " +
                       "FROM quyen q " +
                       "LEFT JOIN phan_quyen pq ON q.ma_quyen = pq.ma_quyen " +
                       "LEFT JOIN chuc_nang cn ON pq.ma_chuc_nang = cn.ma_chuc_nang " +
                       "WHERE q.ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maquyen);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> chucNangList = new ArrayList<>();
                String tenQuyen = "";
                while (rs.next()) {
                    if (tenQuyen.isEmpty()) {
                        tenQuyen = rs.getString("ten_quyen");
                    }
                    String chucNang = rs.getString("ten_chuc_nang");
                    if (chucNang != null) {
                        chucNangList.add(chucNang);
                    }
                }
                return new PermissionDTO(maquyen, tenQuyen, chucNangList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
   public static PermissionDTO getPermissionID(String tenquyen) {
    String query = "SELECT q.ma_quyen, q.ten_quyen, cn.ten_chuc_nang " +
                   "FROM quyen q " +
                   "LEFT JOIN phan_quyen pq ON q.ma_quyen = pq.ma_quyen " +
                   "LEFT JOIN chuc_nang cn ON pq.ma_chuc_nang = cn.ma_chuc_nang " +
                   "WHERE q.ten_quyen = ?";
    try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, tenquyen);
        try (ResultSet rs = stmt.executeQuery()) {
            List<String> chucNangList = new ArrayList<>();
            String maQuyen = "";
            while (rs.next()) {
                if (maQuyen.isEmpty()) {  // Kiểm tra maQuyen chứ không phải tenQuyen
                    maQuyen = rs.getString("ma_quyen");
                }
                String chucNang = rs.getString("ten_chuc_nang");
                if (chucNang != null) {
                    chucNangList.add(chucNang);
                }
            }
            return !maQuyen.isEmpty() ? new PermissionDTO(maQuyen, tenquyen, chucNangList) : null;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    
    public static ArrayList<PermissionDTO> getAllPermissions() {
        ArrayList<PermissionDTO> permissions = new ArrayList<>();
        String query = "SELECT DISTINCT ma_quyen, ten_quyen FROM quyen WHERE is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maQuyen = rs.getString("ma_quyen");
                String tenQuyen = rs.getString("ten_quyen");
                PermissionDTO permission = getPermission(maQuyen);
                permissions.add(permission);
            }
            System.out.println("Lấy danh sách quyền thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách quyền: " + e.getMessage());
            e.printStackTrace();
        }
        return permissions;
    }
    public static void main(String[] args) {
        // Gọi hàm lấy thông tin quyền với mã quyền "1"
        PermissionDTO permission = PermissionDAO.getPermission("1");

        if (permission != null) {
            System.out.println("Mã quyền: " + permission.getID());
            System.out.println("Tên quyền: " + permission.getName());
            System.out.println("Danh sách chức năng:");
            for (String chucNang : permission.getChucNang()) {
                System.out.println(" - " + chucNang);
            }   
        } else {
            System.out.println("Không tìm thấy quyền với mã quyền = 1.");
        }
    }
}
