package BUS;

import DAO.AccountDAO;
import DAO.EmployeeDAO;
//import DAO.PermissionDAO;
import DTO.AccountDTO;
import DTO.EmployeeDTO;
import DTO.Permission2DTO;
//import DTO.PermissionDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;
import DAO.Permission2DAO;

import org.apache.poi.ss.usermodel.Cell;
import java.io.FileInputStream;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.ss.usermodel.DataFormatter;

public class AccountBUS {

    public Boolean ValidationLogin(String username, String password) {
        String usernameRegex = "^[a-zA-Z0-9]{5,}$";
        String passwordRegex = "^[a-zA-Z0-9]{6,}$";
        if (!username.matches(usernameRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Username chỉ được chứa chữ cái và số, tối thiểu 5 ký tự.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Password phải có ít nhất 6 ký tự, chứa ít nhất 1 chữ cái và 1 số.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public Boolean HaveAccount(String username, String password) {
        if (ValidationLogin(username, password)) {
            if (AccountDAO.getAccount(username, password) != null) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } else {
            return false;
        }
    }

    public List<AccountDTO> getAllPlayers_No_Account() {
        List<AccountDTO> a = AccountDAO.getAllAccounts();
        return a;
    }

    public static Boolean addAccount(String username, String password, String tenquyen) {

        System.out.println(tenquyen);
        Permission2DTO a = Permission2DAO.getPermissionByName(tenquyen);
        EmployeeDTO e = new EmployeeDTO(EmployeeDAO.getEmployeeByName(username));
        if (username != null && password != null && tenquyen != null) {
            if (AccountDAO.addAccount(e.getEmployeeID(), password, a.getID()) == true) {
                return true;
            }
        }
        return false;
    }

    public static Boolean updateAccount(String username, String password, String maquyen) {
        Permission2DTO a = Permission2DAO.getPermissionByName(maquyen);
        return AccountDAO.updateAccount(username, password, a.getID()) == true;
    }

    public static boolean validateAccount(AccountDTO account) {
        String password = account.getPassword();

        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Mật khẩu không được để trống.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.trim().length() < 6) {
            JOptionPane.showMessageDialog(null,
                    "Mật khẩu phải có ít nhất 6 ký tự.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean exportAccountsToExcel(File file) {
    try (Connection conn = DatabaseConnection.getConnection();
         Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachTaiKhoan");
        
        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ma_tai_khoan", "ten_dang_nhap", "mat_khau", "ma_quyen", "is_deleted"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lấy dữ liệu từ database
        String sql = "SELECT * FROM tai_khoan";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("ma_tai_khoan"));
            row.createCell(1).setCellValue(rs.getString("ten_dang_nhap"));
            row.createCell(2).setCellValue(rs.getString("mat_khau"));
            row.createCell(3).setCellValue(rs.getString("ma_quyen"));
            row.createCell(4).setCellValue(rs.getInt("is_deleted"));
        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi vào file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        return true;
    } catch (SQLException | IOException e) {
        e.printStackTrace();
        return false;
    }
}
}