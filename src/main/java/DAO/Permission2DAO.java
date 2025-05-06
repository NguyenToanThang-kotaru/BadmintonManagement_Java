/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import BUS.FunctionBUS;
import Connection.DatabaseConnection;
import DTO.ActionDTO;
import DTO.FunctionDTO;
import DTO.Permission2DTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thang Nguyen
 */
public class Permission2DAO {

    public static Boolean deletePermission(Permission2DTO per) {
        String query = "DELETE FROM quyen2 where ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, per.getID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean updatePermission(Permission2DTO per) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Kết nối database
            conn = DatabaseConnection.getConnection();

            // Câu lệnh SQL update
            String query = "UPDATE `quyen2` SET `ten_quyen`=?, `ten_khong_dau`=? WHERE `ma_quyen`=?";

            // Tạo prepared statement
            pstmt = conn.prepareStatement(query);

            // Thiết lập các tham số
            pstmt.setString(1, per.getName());         // ten_quyen
            pstmt.setString(2, per.getUname());     // ten_khong_dau
            pstmt.setString(3, per.getID());         // ma_quyen (điều kiện WHERE)

            FunctionBUS.deleteFunction(per);
            FunctionBUS.addFunction(per);
            // Thực thi câu lệnh update
            int rowsAffected = pstmt.executeUpdate();

            // Trả về true nếu cập nhật thành công ít nhất 1 dòng
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Đóng kết nối và statement
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String countAllAccountsByPer(String ID) {
        String query = "SELECT COUNT(*) AS total FROM tai_khoan where ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("total");
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String countDistinctFunctionsByPermission(String permissionID) {
        String query = "SELECT COUNT(ma_chuc_nang) AS total FROM phan_quyen2 WHERE ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, permissionID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("total");
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static Permission2DTO getPermissionByName(String name) {
        String sql = ("SELECT * FROM quyen2 where ten_quyen = ?");
//        ArrayList<Permission2DTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("ma_quyen");
                ArrayList<FunctionDTO> func = FunctionDAO.getFunctionsAndActionsByPermission(ID);
                if (func == null) {
                    func = new ArrayList<>();
                }
                Permission2DTO per = new Permission2DTO(
                        ID,
                        rs.getString("ten_quyen"),
                        ("ten_khong_dau"),
                        func
                );
                return per;
            }
            return null;
//            return per;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Permission2DTO getPermissionByID(String id) {
        String sql = ("SELECT * FROM quyen2 where ma_quyen = ?");
//        ArrayList<Permission2DTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("ma_quyen");
                ArrayList<FunctionDTO> func = FunctionDAO.getFunctionsAndActionsByPermission(ID);
                if (func == null) {
                    func = new ArrayList<>();
                }
                Permission2DTO per = new Permission2DTO(
                        ID,
                        rs.getString("ten_quyen"),
                        ("ten_khong_dau"),
                        func
                );
                return per;
            }
            return null;
//            return per;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Permission2DTO> getAllPermissions() {
        String sql = ("SELECT * FROM quyen2");
        ArrayList<Permission2DTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ID = rs.getString("ma_quyen");
                ArrayList<FunctionDTO> func = FunctionDAO.getFunctionsAndActionsByPermission(ID);
                Permission2DTO per = new Permission2DTO(
                        ID,
                        rs.getString("ten_quyen"),
                        rs.getString("ten_khong_dau"),
                        func
                );
                list.add(per);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Permission2DTO> searchPermission(String tenQuyen) {
        List<Permission2DTO> permissions = new ArrayList<>();

        String query = "SELECT * FROM quyen WHERE ten_quyen LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + tenQuyen + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permission2DTO permission = new Permission2DTO();
                    permission.setID(rs.getString("ma_quyen"));
                    permission.setName(rs.getString("ten_quyen"));
                    permission.setUname(rs.getString("ten_khong_dau"));

                    permissions.add(permission);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return permissions;
    }

    public static Boolean addPermission(Permission2DTO per) {
        String sql = ("INSERT INTO `quyen2`(`ma_quyen`, `ten_quyen`, `ten_khong_dau`) "
                + "VALUES (?,?,?)");
        ArrayList<FunctionDTO> func = per.getFunction();
        FunctionBUS.addFunction(per);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, per.getID());
            stmt.setString(2, per.getName());
            stmt.setString(3, per.getUname());
            int rs = stmt.executeUpdate();
            if (rs > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        ArrayList<Permission2DTO> permissions = getAllPermissions();

        if (permissions != null && !permissions.isEmpty()) {
            for (Permission2DTO per : permissions) {
                System.out.println("Mã quyền: " + per.getID());
//                System.out.println("Tên quyền: " + per.getName());
                System.out.println("Tên không dấu: " + per.getUname());

                System.out.println("Danh sách chức năng:");
                int i = 1;
                for (FunctionDTO func : per.getFunction()) {

                    System.out.println(i + "  - " + func.getUname()); // thay bằng phương thức phù hợp trong FunctionDTO
                    for (ActionDTO act : func.getActions()) {

                        System.out.println("  - " + act.getUname()); // thay bằng phương thức phù hợp trong FunctionDTO

                    }
                    i++;
                }
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("Không tìm thấy quyền nào.");
        }
//        FunctionDAO.getFunctionByID("F01");

    }

}
