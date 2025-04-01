package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
    private String image;
   

    public EmployeeDTO() {
        this.employeeID = "";
        this.fullName = "";
        this.address = "";
        this.phone = "";
        this.image = "";
  
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

  public EmployeeDTO(String employeeID, String fullName, String address, String phone, String image) {
    this.employeeID = employeeID;
    this.fullName = fullName;
    this.address = address;
    this.phone = phone;
    this.image = image;
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
