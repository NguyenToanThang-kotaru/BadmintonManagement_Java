package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
   

    public EmployeeDTO() {
        this.employeeID = "";
        this.fullName = "";
        this.address = "";
        this.phone = "";
  
    }

  public EmployeeDTO(String employeeID, String fullName, String address, String phone) {
    this.employeeID = employeeID;
    this.fullName = fullName;
    this.address = address;
    this.phone = phone;
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
}
