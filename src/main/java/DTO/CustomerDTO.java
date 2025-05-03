package DTO;

public class CustomerDTO {

    private String customerID;
    private String fullName;
    private String phone;

    public CustomerDTO() {
        this.customerID = "";
        this.fullName = "";
        this.phone = "";
    }

    public CustomerDTO(String customerID, String fullName, String phone) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getcustomerID() {
        return customerID;
    }

    public void setcustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}