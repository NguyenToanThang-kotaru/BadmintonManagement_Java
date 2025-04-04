package DAO;

import Connection.DatabaseConnection;
import DTO.AccountDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {

    public static Boolean addAccount(String username, String password, String maquyen) {
        String query = "INSERT INTO tai_khoan (ma_tai_khoan, ten_dang_nhap, mat_khau, ma_quyen, is_deleted) "
                + "VALUES (?, ?, ?, ?, 0);";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, generateNewAccountID());
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, maquyen);

            int rowsInserted = stmt.executeUpdate(); // SỬA executeQuery() thành executeUpdate()
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen "
                + "WHERE tk.is_deleted = 0;";

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
            e.printStackTrace();
        }
        return accounts;
    }

    public static void deleteAccount(String username) {
        String sql = "UPDATE tai_khoan SET is_deleted =1 WHERE ten_dang_nhap = ?;";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra để debug
        }
    }

    public static Boolean updateAccount(String username,String password, String maquyen) {
        String sql = "UPDATE tai_khoan SET mat_khau = ?, ma_quyen = ? WHERE ten_dang_nhap = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setString(2, maquyen);
            stmt.setString(3, username);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Nên log lỗi để debug dễ hơn
            return false;
        }
    }

    private static String generateNewAccountID() {
        String query = "SELECT ma_tai_khoan FROM tai_khoan ORDER BY ma_tai_khoan DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("ma_tai_khoan"); // Ví dụ: "NV005"

                // Cắt bỏ "TK", chỉ lấy số
                int number = Integer.parseInt(lastID.substring(2));

                // Tạo ID mới với định dạng NVXXX
                return String.format("TK%03d", number + 1); // Ví dụ: "NV006"
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã nhân viên mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "TK001"; // Nếu không có nhân viên nào, bắt đầu từ "NV001"
    }

}
