package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import java.util.ArrayList;
import java.util.List;

public class EmployeeBUS {
    private List<EmployeeDTO> employeeList;

    public EmployeeBUS() {
        this.employeeList = new ArrayList<>();
    }

    public List<EmployeeDTO> getAllEmployees() {
        return new ArrayList<>(employeeList);
    }

    public void updateEmployee(EmployeeDTO employee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeID().equals(employee.getEmployeeID())) {
                employeeList.set(i, employee);
                return;
            }
        }
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employeeList = new ArrayList<>(employees);
    }
}
