package BUS;

import DAO.AccountDAO;
import DAO.EmployeeDAO;
import DAO.PermissionDAO;
import DTO.AccountDTO;
import DTO.EmployeeDTO;
import DTO.PermissionDTO;

import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AccountBUS {

    public Boolean ValidationLogin(String username, String password) {
        String usernameRegex = "^[a-zA-Z0-9]{5,}$";
        String passwordRegex = "^[a-zA-Z0-9]{6,}$";
        if (!username.matches(usernameRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Username chỉ được chứa chữ cái và số, tối thiểu 3 ký tự.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Password phải có ít nhất 8 ký tự, chứa ít nhất 1 chữ cái và 1 số.",
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
        PermissionDTO a = new PermissionDTO(PermissionDAO.getPermissionByName(tenquyen));
        EmployeeDTO e = EmployeeDAO.getEmployeeByName(username);
        if (AccountDAO.addAccount(e.getEmployeeID(), password, a.getID()) == true)
            return true;
        else
            System.out.println("Khong co cc j het");
        return false;
    }

    public static Boolean updateAccount(String username, String password, String maquyen) {
        PermissionDTO a = new PermissionDTO(PermissionDAO.getPermissionByName(maquyen));
        return AccountDAO.updateAccount(username, password, a.getID()) == true;
    }

    public boolean exportAccountsToExcel(String filePath) {
        List<AccountDTO> accounts = AccountDAO.getAllAccounts();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DanhSachTaiKhoan");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Tên Nhân Viên", "Tài Khoản", "Mật Khẩu", "Quyền"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Điền dữ liệu
            int rowNum = 1;
            for (AccountDTO account : accounts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(account.getFullname());
                row.createCell(2).setCellValue(account.getUsername());
                row.createCell(3).setCellValue(account.getPassword());
                row.createCell(4).setCellValue(account.getPermission().getName());
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xuất Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}