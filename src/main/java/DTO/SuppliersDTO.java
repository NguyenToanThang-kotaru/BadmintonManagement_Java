package DTO;

public class SuppliersDTO {
    
    private String suppliersID;
    private String fullname;
    private String address;
    private String phone;
    private int isDeleted; // Thêm thuộc tính isDeleted
    
    public SuppliersDTO() {
        this.suppliersID = "";
        this.fullname = "";
        this.address = "";
        this.phone = "";
        this.isDeleted = 0; // Mặc định là 0 (chưa xóa)
    }
    
    public SuppliersDTO(String suppliersID, String fullname, String address, String phone) {
        this.suppliersID = suppliersID;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.isDeleted = 0; // Mặc định là 0 (chưa xóa)
    }
    
    public SuppliersDTO(String suppliersID, String fullname, String address, String phone, int isDeleted) {
        this.suppliersID = suppliersID;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.isDeleted = isDeleted; // Constructor mới với isDeleted
    }
    
    public String getsuppliersID() {
        return suppliersID;
    }
    
    public void setsuppliersID(String suppliersID) {
        this.suppliersID = suppliersID;
    }
    
    public String getfullname() {
        return fullname;
    }
    
    public void setfullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getaddress() {
        return address;
    }
    
    public void setaddress(String address) {
        this.address = address;
    }
    
    public String getphone() {
        return phone;
    }
    
    public void setphone(String phone) {
        this.phone = phone;
    }
    
    public int getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}