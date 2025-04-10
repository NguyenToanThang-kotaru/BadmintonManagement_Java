package DAO;

import Connection.DatabaseConnection;
import DTO.AccountDTO;
import DTO.PermissionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        // Truy vấn lấy thông tin tài khoản và quyền cơ bản
        String accountQuery = "SELECT tk.ten_dang_nhap, tk.mat_khau, nv.ten_nhan_vien, "
                + "q.ma_quyen, q.ten_quyen "
                + "FROM tai_khoan AS tk "
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen "
                + "JOIN nhan_vien AS nv ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "WHERE tk.ten_dang_nhap = ? AND tk.mat_khau = ? AND tk.is_deleted = 0";

        // Truy vấn lấy danh sách chức năng của quyền
        String functionQuery = "SELECT ma_chuc_nang FROM phan_quyen WHERE ma_quyen = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement accountStmt = conn.prepareStatement(accountQuery)) {

            // Thiết lập tham số cho truy vấn tài khoản
            accountStmt.setString(1, username);
            accountStmt.setString(2, password);

            try (ResultSet accountRs = accountStmt.executeQuery()) {
                if (accountRs.next()) {
                    // Lấy thông tin cơ bản
                    String roleId = accountRs.getString("ma_quyen");
                    String roleName = accountRs.getString("ten_quyen");

                    // Lấy danh sách chức năng của quyền
                    List<String> functions = new ArrayList<>();
                    try (PreparedStatement functionStmt = conn.prepareStatement(functionQuery)) {
                        functionStmt.setString(1, roleId);
                        try (ResultSet functionRs = functionStmt.executeQuery()) {
                            while (functionRs.next()) {
                                functions.add(functionRs.getString("ma_chuc_nang"));
                            }
                        }
                    }

                    // Tạo PermissionDTO
                    PermissionDTO permission = new PermissionDTO(roleId, roleName, functions);

                    // Tạo và trả về AccountDTO
                    return new AccountDTO(
                            accountRs.getString("ten_dang_nhap"),
                            accountRs.getString("mat_khau"),
                            accountRs.getString("ten_nhan_vien"),
                            permission
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên ghi log lỗi đầy đủ
        }
        return null; // Không tìm thấy tài khoản
    }

    // Lấy danh sách tài khoản cho bảng GUI
    public static ArrayList<AccountDTO> getAllAccounts() {
        ArrayList<AccountDTO> accounts = new ArrayList<>();

        // Truy vấn lấy thông tin tài khoản + quyền
        String accountQuery = "SELECT tk.ten_dang_nhap, tk.mat_khau, nv.ten_nhan_vien, "
                + "q.ma_quyen, q.ten_quyen "
                + "FROM tai_khoan AS tk "
                + "JOIN nhan_vien AS nv ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen "
                + "WHERE tk.is_deleted = 0;";

        // Truy vấn lấy danh sách chức năng của quyền
        String functionQuery = "SELECT ma_chuc_nang FROM phan_quyen WHERE ma_quyen = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement accountStmt = conn.prepareStatement(accountQuery); ResultSet accountRs = accountStmt.executeQuery()) {

            while (accountRs.next()) {
                // Lấy thông tin cơ bản
                String username = accountRs.getString("ten_dang_nhap");
                String password = accountRs.getString("mat_khau");
                String fullname = accountRs.getString("ten_nhan_vien");
                String roleId = accountRs.getString("ma_quyen");
                String roleName = accountRs.getString("ten_quyen");

                // Lấy danh sách chức năng của quyền
                List<String> functions = new ArrayList<>();
                try (PreparedStatement functionStmt = conn.prepareStatement(functionQuery)) {
                    functionStmt.setString(1, roleId);
                    try (ResultSet functionRs = functionStmt.executeQuery()) {
                        while (functionRs.next()) {
                            functions.add(functionRs.getString("ma_chuc_nang"));
                        }
                    }
                }

                // Tạo PermissionDTO
                PermissionDTO permission = new PermissionDTO(roleId, roleName, functions);

                // Tạo AccountDTO và thêm vào danh sách
                accounts.add(new AccountDTO(username, password, fullname, permission));
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

    public static Boolean updateAccount(String username, String password, String maquyen) {
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

    public static ArrayList<AccountDTO> searchAccounts(String keyword) {
        ArrayList<AccountDTO> accounts = new ArrayList<>();

        String accountQuery = "SELECT tk.ten_dang_nhap, tk.mat_khau, nv.ten_nhan_vien, "
                + "q.ma_quyen, q.ten_quyen "
                + "FROM tai_khoan AS tk "
                + "JOIN nhan_vien AS nv ON nv.ma_nhan_vien = tk.ten_dang_nhap "
                + "JOIN quyen AS q ON q.ma_quyen = tk.ma_quyen "
                + "WHERE tk.is_deleted = 0 AND (tk.ten_dang_nhap LIKE ? OR nv.ten_nhan_vien LIKE ? OR q.ten_quyen Like ?)";

        String functionQuery = "SELECT ma_chuc_nang FROM phan_quyen WHERE ma_quyen = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement accountStmt = conn.prepareStatement(accountQuery)) {

            String searchKeyword = "%" + keyword + "%";
            accountStmt.setString(1, searchKeyword);
            accountStmt.setString(2, searchKeyword);
            accountStmt.setString(3, searchKeyword);
            try (ResultSet accountRs = accountStmt.executeQuery()) {
                while (accountRs.next()) {
                    String username = accountRs.getString("ten_dang_nhap");
                    String password = accountRs.getString("mat_khau");
                    String fullname = accountRs.getString("ten_nhan_vien");
                    String roleId = accountRs.getString("ma_quyen");
                    String roleName = accountRs.getString("ten_quyen");

                    List<String> functions = new ArrayList<>();
                    try (PreparedStatement functionStmt = conn.prepareStatement(functionQuery)) {
                        functionStmt.setString(1, roleId);
                        try (ResultSet functionRs = functionStmt.executeQuery()) {
                            while (functionRs.next()) {
                                functions.add(functionRs.getString("ma_chuc_nang"));
                            }
                        }
                    }

                    PermissionDTO permission = new PermissionDTO(roleId, roleName, functions);
                    accounts.add(new AccountDTO(username, password, fullname, permission));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

}
