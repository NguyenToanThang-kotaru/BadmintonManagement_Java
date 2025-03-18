package DTO;

public class EmployeeDTO {

    private int employeeID;
    private String fullName;
    private String address;
    private String phone;
    private int accountID;
   

    public EmployeeDTO() {
        this.employeeID = 0;
        this.fullName = "";
        this.address = "";
        this.phone = "";
        this.accountID = 0;
  
    }

  public EmployeeDTO(int employeeID, String fullName, String address, String phone, int accountID) {
    this.employeeID = employeeID;
    this.fullName = fullName;
    this.address = address;
    this.phone = phone;
    this.accountID = accountID;
}


    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
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

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}
