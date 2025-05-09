package DTO;

public class EmployeeDTO {

    private String employeeID;
    private String fullName;
    private String address;
    private String phone;
    private String image;
    private String chucVu;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String employeeID, String fullName, String address, String phone, String image) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.chucVu = "Nhân viên";
    }

    public EmployeeDTO(EmployeeDTO e) {
        this.employeeID = e.employeeID;
        this.fullName = e.fullName;
        this.address = e.address;
        this.phone = e.phone;
        this.image = e.image;
        this.chucVu = e.chucVu;
    }

    public EmployeeDTO(String employeeID, String fullName, String address, String phone, String image, String chucVu) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.chucVu = chucVu;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}
