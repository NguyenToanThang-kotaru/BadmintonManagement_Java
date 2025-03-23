
package DTO;

public class SuppliersDTO {
    
    private String suppliersID;
    private String fullname;
    private String address;
    private String phone;
    
    public SuppliersDTO() {
        this.suppliersID = "";
        this.fullname = "";
        this.address = "";
        this.phone = "";
    }
    
    public SuppliersDTO(String suppliersID, String fullname, String address, String phone) {
        this.suppliersID = suppliersID;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
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
}
