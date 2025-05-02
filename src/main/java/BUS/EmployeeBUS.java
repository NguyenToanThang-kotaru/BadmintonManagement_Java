package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;
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
