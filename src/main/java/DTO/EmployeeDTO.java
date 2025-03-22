package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
    private String accountID;
   

    public EmployeeDTO() {
        this.employeeID = "";
        this.fullName = "";
        this.address = "";
        this.phone = "";
        this.accountID = "";
  
    }

  public EmployeeDTO(String employeeID, String fullName, String address, String phone, String accountID) {
    this.employeeID = employeeID;
    this.fullName = fullName;
    this.address = address;
    this.phone = phone;
    this.accountID = accountID;
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
}
