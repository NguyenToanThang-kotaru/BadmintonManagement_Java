package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
    private String startDate;

    public EmployeeDTO() {
        this.employeeID = "";
        this.fullName = "";
        this.address = "";
        this.phone = "";
        this.startDate = "";
    }

    public EmployeeDTO(String employeeID, String fullName, String address, String phone, String startDate) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.startDate = startDate;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}