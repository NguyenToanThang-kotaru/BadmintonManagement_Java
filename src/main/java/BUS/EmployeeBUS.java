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

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Bỏ qua dòng tiêu đề
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                EmployeeDTO employee = new EmployeeDTO();

                // Đọc dữ liệu từ các cột
                Cell fullNameCell = row.getCell(1); // Cột "Họ Tên"
                Cell addressCell = row.getCell(2);  // Cột "Địa Chỉ"
                Cell phoneCell = row.getCell(3);    // Cột "SĐT"

                // Kiểm tra và gán dữ liệu
                employee.setFullName(fullNameCell != null ? fullNameCell.toString() : "");
                employee.setAddress(addressCell != null ? addressCell.toString() : "");
                employee.setPhone(phoneCell != null ? phoneCell.toString() : "");
                employee.setImage(""); // Hình ảnh mặc định là rỗng vì Excel không chứa hình ảnh
                employee.setChucVu("Nhân viên"); // Gán giá trị mặc định cho chuc_vu

                // Kiểm tra dữ liệu hợp lệ
                if (employee.getFullName().isEmpty() || employee.getAddress().isEmpty() || employee.getPhone().isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }

                employeesToImport.add(employee);
            }

            // Gọi DAO để lưu danh sách nhân viên vào DB
            boolean success = EmployeeDAO.importEmployees(employeesToImport);
            if (success) {
                // Cập nhật lại danh sách nhân viên trong BUS
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