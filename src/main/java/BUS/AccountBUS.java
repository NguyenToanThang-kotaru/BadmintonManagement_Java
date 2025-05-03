package BUS;

import DAO.AccountDAO;
import DAO.EmployeeDAO;
import DAO.PermissionDAO;
import DTO.AccountDTO;
import DTO.EmployeeDTO;
import DTO.PermissionDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public boolean importAccountsFromExcel(File file) {
        List<AccountDTO> accountsToImport = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter(); // Sử dụng DataFormatter để chuẩn hóa giá trị ô
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                AccountDTO account = new AccountDTO();

                Cell fullNameCell = row.getCell(1); // Tên nhân viên
                Cell usernameCell = row.getCell(2); // Tên đăng nhập (ma_nhan_vien)
                Cell passwordCell = row.getCell(3); // Mật khẩu
                Cell roleCell = row.getCell(4); // Quyền

                String fullName = fullNameCell != null ? dataFormatter.formatCellValue(fullNameCell).trim() : "";
                String username = usernameCell != null ? dataFormatter.formatCellValue(usernameCell).trim() : "";
                String password = passwordCell != null ? dataFormatter.formatCellValue(passwordCell).trim() : "";
                String roleName = roleCell != null ? dataFormatter.formatCellValue(roleCell).trim() : "";

                // Kiểm tra dữ liệu bắt buộc
                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || roleName.isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }

                // Kiểm tra nhân viên tồn tại
                EmployeeDTO employee = EmployeeDAO.getEmployeeByName(fullName);
                if (employee == null) {
                    System.out.println("Nhân viên " + fullName + " không tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }

                // Kiểm tra quyền tồn tại
                PermissionDTO permission = PermissionDAO.getPermissionByName(roleName);
                if (permission == null) {
                    System.out.println("Quyền " + roleName + " không tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }

                // Kiểm tra tài khoản đã tồn tại
                if (AccountDAO.getAccount(employee.getEmployeeID(), password) != null) {
                    System.out.println("Tài khoản " + employee.getEmployeeID() + " đã tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }

                account.setUsername(employee.getEmployeeID());
                account.setPassword(password);
                account.setFullname(fullName);
                account.setPermission(permission);
                accountsToImport.add(account);
            }
            boolean success = AccountDAO.importAccounts(accountsToImport);
            return success;
        } catch (Exception e) {
            System.err.println("Lỗi khi nhập Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportAccountsToExcel(File file) {
        List<AccountDTO> accounts = AccountDAO.exportAccounts();
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(file)) {
            Sheet sheet = workbook.createSheet("Accounts");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Tên Nhân Viên", "Tên Đăng Nhập", "Mật Khẩu", "Quyền"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowIndex = 1;
            for (AccountDTO account : accounts) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rowIndex - 1);
                row.createCell(1).setCellValue(account.getFullname());
                row.createCell(2).setCellValue(account.getUsername());
                row.createCell(3).setCellValue(account.getPassword());
                row.createCell(4).setCellValue(account.getPermission().getName());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(fos);
            System.out.println("Xuất danh sách tài khoản ra Excel thành công.");
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xuất Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}