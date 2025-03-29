package DAO;

import Connection.DatabaseConnection;
import DTO.AccountDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {

    public static AccountDTO getAccount(String username, String password) {
        String query = "SELECT * " // Thêm khoảng trắng
                + "FROM tai_khoan AS tk "
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen "
                + "JOIN nhan_vien AS nv ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ? AND tk.mat_khau = ? ;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AccountDTO(
                            rs.getString("ten_dang_nhap"),
                            rs.getString("mat_khau"),
                            rs.getString("ten_nhan_vien"),
                            rs.getString("ten_quyen")
                    //                        rs.getString("ten_nhan_vien")
                    );
                }
            }
        } catch (SQLException e) {
        }
        return null; // Không tìm thấy tài khoản
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<AccountDTO> getAllAccounts() {
        ArrayList<AccountDTO> accounts = new ArrayList<>();
        String query = "SELECT * "
                + "FROM tai_khoan AS tk "
                + "JOIN nhan_vien AS nv ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen;";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(new AccountDTO(
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("ten_nhan_vien"),
                        rs.getString("ten_quyen")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return accounts;
    }

    public static void deleteAccount(String username) {
        String sql = "DELETE FROM tai_khoan WHERE ten_dang_nhap = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra để debug
        }
    }

    public static void updateAccount(AccountDTO account) {
        String sql = "UPDATE tai_khoan SET ten_dang_nhap = ?, mat_khau = ?, ma_quyen = ? WHERE ten_dang_nhap = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getTenquyen()); // Chưa rõ cách bạn lưu quyền, có thể cần sửa
            stmt.setString(4, account.getUsername()); // WHERE điều kiện phải đúng

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Nên log lỗi để debug dễ hơn
        }
    }

}
