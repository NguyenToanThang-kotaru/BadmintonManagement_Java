package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
}