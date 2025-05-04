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
            if (emp.getEmployeeID() == employeeID) {
                return emp;
            }
        }
        return null; // Không tìm thấy
    }

    public boolean updateEmployee(EmployeeDTO employee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeID() == employee.getEmployeeID()) {
                employeeList.set(i, employee);
                EmployeeDAO dao = new EmployeeDAO();
                try {
                    dao.updateEmployee(employee); // Cập nhật trong DB

                    // Cập nhật lại danh sách nhân viên từ DB sau khi sửa

                    this.employeeList = EmployeeDAO.getAllEmployees();

                    return true; // Cập nhật thành công
                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi cập nhật nhân viên: " + e.getMessage());
                    return false; // Lỗi khi cập nhật
                }
            }
        }
        return false; // Không tìm thấy nhân viên để cập nhật
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
        DataFormatter dataFormatter = new DataFormatter(); // Dùng DataFormatter để đọc đúng định dạng
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                EmployeeDTO employee = new EmployeeDTO();
    
                // Đọc các cột từ Excel
                Cell fullNameCell = row.getCell(1);
                Cell addressCell = row.getCell(2);
                Cell phoneCell = row.getCell(3);
                Cell chucVuCell = row.getCell(4); // Đọc cột chức vụ (nếu có)
    
                // Gán giá trị cho EmployeeDTO
                employee.setFullName(fullNameCell != null ? dataFormatter.formatCellValue(fullNameCell).trim() : "");
                employee.setAddress(addressCell != null ? dataFormatter.formatCellValue(addressCell).trim() : "");
                employee.setPhone(phoneCell != null ? dataFormatter.formatCellValue(phoneCell).trim() : "");
                employee.setImage("");
    
                // Xử lý cột chuc_vu
                String chucVu = chucVuCell != null ? dataFormatter.formatCellValue(chucVuCell).trim() : "";
                employee.setChucVu(chucVu); // Gán chuỗi rỗng nếu chuc_vu trống hoặc không có
    
                // Kiểm tra dữ liệu bắt buộc
                if (employee.getFullName().isEmpty() || employee.getAddress().isEmpty() || employee.getPhone().isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }
    
                // Kiểm tra trùng số điện thoại
                if (EmployeeDAO.isPhoneNumberExists(employee.getPhone())) {
                    System.out.println("Số điện thoại " + employee.getPhone() + " đã tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }
    
                employeesToImport.add(employee);
            }
            boolean success = EmployeeDAO.importEmployees(employeesToImport);
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

        // Lấy dữ liệu từ database
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
        String Anh = employee.getImage();
//        String address = employee.getAddress();

        // Tên không được chứa số
        if (fullName.matches(".*\\d+.*")) {
            JOptionPane.showMessageDialog(null,
                    "Tên nhân viên không được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra số điện thoại phải bắt đầu bằng 0 và chỉ chứa số
        if (!phone.matches("^0\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Số điện thoại phải bắt đầu bằng số 0 và chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Anh == null) {
            JOptionPane.showMessageDialog(null,
                    "Ảnh nhân viên không được để trống.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
