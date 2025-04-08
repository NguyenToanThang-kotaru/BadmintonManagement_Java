package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import java.util.ArrayList;
import java.util.List;

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
}
