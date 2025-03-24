package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
    private String accountID;
    private String powerID;
   

    public EmployeeDTO() {
        this.employeeID = "";
        this.fullName = "";
        this.address = "";
        this.phone = "";
        this.accountID = "";
        this.powerID = "";
  
    }

  public EmployeeDTO(String employeeID, String fullName, String address, String phone, String accountID, String powerID) {
    this.employeeID = employeeID;
    this.fullName = fullName;
    this.address = address;
    this.phone = phone;
    this.accountID = accountID;
    this.powerID = powerID;
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

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    
    public String getpowerID() {
        return powerID;
    }
    
    public void setpowerID(String powerID) {
        this.powerID = powerID;
    }
}
