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
        String query = "SELECT `ten_dang_nhap`, `mat_khau`" 
                     + "FROM tai_khoan "
                     + "WHERE `ten_dang_nhap` = ? AND `mat_khau` = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();  
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AccountDTO(
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau")
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
        String query = "SELECT `ten_dang_nhap`, `mat_khau`" 
                     + "FROM `tai_khoan` ";
//                     + "WHERE `Tên Đăng Nhập` = ? AND `Mật Khẩu` = ?;";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(new AccountDTO(
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau")
                ));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return accounts;
    }

    public void updateAccount(AccountDTO account) {
        String sql = "UPDATE accounts SET username = ?, password = ?, rankID = ? WHERE employeeID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            stmt.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

}
