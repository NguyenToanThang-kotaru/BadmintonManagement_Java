package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

import javax.swing.JOptionPane;

public class EmployeeBUS {

    private List<EmployeeDTO> employeeList;

    public EmployeeBUS() {
        this.employeeList = EmployeeDAO.getAllEmployees();
    }

    public List<EmployeeDTO> getAllEmployees() {
        return new ArrayList<>(employeeList);
    }

    public EmployeeDTO getEmployeeByID(String employeeID) {
        for (EmployeeDTO emp : employeeList) {
            if (emp.getEmployeeID().equals(employeeID)) {
                return emp;
            }
        }
        return null;
    }

    public boolean updateEmployee(EmployeeDTO employee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeID().equals(employee.getEmployeeID())) {
                employeeList.set(i, employee);
                EmployeeDAO dao = new EmployeeDAO();
                try {
                    dao.updateEmployee(employee);
                    this.employeeList = EmployeeDAO.getAllEmployees();
                    return true;
                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi cập nhật nhân viên: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    public String getEmployeeNameByID(String employeeID) {
        return EmployeeDAO.getEmployeeNameByID(employeeID);
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employeeList = new ArrayList<>(employees);
    }

    public List<EmployeeDTO> searchEmployee(String keyword) {
        return EmployeeDAO.searchEmployee(keyword);
    }

    public boolean importEmployeesFromExcel(File file) {
        List<EmployeeDTO> employeesToImport = new ArrayList<>();
        List<Integer> isDeletedList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                EmployeeDTO employee = new EmployeeDTO();

                // Đọc các cột từ Excel theo cấu trúc bảng nhan_vien
                Cell tenNhanVienCell = row.getCell(1);
                Cell diaChiCell = row.getCell(2);
                Cell soDienThoaiCell = row.getCell(3);
                Cell isDeletedCell = row.getCell(5);
                Cell chucVuCell = row.getCell(6);

                // Gán giá trị cho EmployeeDTO
                employee.setFullName(tenNhanVienCell != null ? dataFormatter.formatCellValue(tenNhanVienCell).trim() : "");
                employee.setAddress(diaChiCell != null ? dataFormatter.formatCellValue(diaChiCell).trim() : "");
                String phone = soDienThoaiCell != null ? dataFormatter.formatCellValue(soDienThoaiCell).trim() : "";
                employee.setPhone(phone);
                employee.setImage(""); // Đặt hinh_anh là chuỗi rỗng
                employee.setChucVu(chucVuCell != null ? dataFormatter.formatCellValue(chucVuCell).trim() : "");

                // Xử lý is_deleted
                int isDeleted = 0; // Mặc định là 0
                if (isDeletedCell != null) {
                    String isDeletedStr = dataFormatter.formatCellValue(isDeletedCell).trim();
                    try {
                        isDeleted = Integer.parseInt(isDeletedStr);
                        if (isDeleted != 0 && isDeleted != 1) {
                            isDeleted = 0; // Nếu giá trị không hợp lệ, đặt về 0
                            System.out.println("Giá trị is_deleted không hợp lệ tại dòng " + (rowIndex + 1) + ", đặt mặc định là 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Giá trị is_deleted không phải số tại dòng " + (rowIndex + 1) + ", đặt mặc định là 0.");
                    }
                }
                isDeletedList.add(isDeleted);

                // Kiểm tra dữ liệu bắt buộc
                if (employee.getFullName().isEmpty() || employee.getAddress().isEmpty() || employee.getPhone().isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }

                // Kiểm tra định dạng số điện thoại (bắt đầu bằng 0, đúng 10 chữ số)
                if (!phone.matches("^0\\d{9}$")) {
                    System.out.println("Số điện thoại " + phone + " không hợp lệ tại dòng " + (rowIndex + 1) + ", bỏ qua.");
                    continue;
                }

                // Kiểm tra trùng số điện thoại
                if (EmployeeDAO.isPhoneNumberExists(employee.getPhone())) {
                    System.out.println("Số điện thoại " + employee.getPhone() + " đã tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }

                employeesToImport.add(employee);
            }
            boolean success = EmployeeDAO.importEmployees(employeesToImport, isDeletedList);
            if (success) {
                this.employeeList = EmployeeDAO.getAllEmployees();
            }
            return success;
        } catch (Exception e) {
            System.err.println("Lỗi khi nhập Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportToExcel(String filePath) {
        try (Connection conn = DatabaseConnection.getConnection();
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DanhSachNhanVien");
            
            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ma_nhan_vien", "ten_nhan_vien", "dia_chi", "so_dien_thoai", "hinh_anh", "is_deleted", "chuc_vu"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Lấy dữ liệu từ database, bao gồm cả is_deleted = 1
            String sql = "SELECT * FROM nhan_vien";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getString("ma_nhan_vien"));
                row.createCell(1).setCellValue(rs.getString("ten_nhan_vien"));
                row.createCell(2).setCellValue(rs.getString("dia_chi"));
                row.createCell(3).setCellValue(rs.getString("so_dien_thoai"));
                row.createCell(4).setCellValue(rs.getString("hinh_anh"));
                row.createCell(5).setCellValue(rs.getInt("is_deleted"));
                row.createCell(6).setCellValue(rs.getString("chuc_vu"));
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateEmployee(EmployeeDTO employee) {
        String fullName = employee.getFullName().trim();
        String phone = employee.getPhone().trim();

        // Tên không được chứa số
        if (fullName.matches(".*\\d+.*")) {
            JOptionPane.showMessageDialog(null,
                    "Tên nhân viên không được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra số điện thoại phải bắt đầu bằng 0 và đúng 10 chữ số
        if (!phone.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(null,
                    "Số điện thoại phải bắt đầu bằng số 0 và có đúng 10 chữ số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}