package DTO;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class PermissionDTO {

    private String ID;
    private String Name;
    private List<String> chucNang; // Danh sách chức năng

    // Constructor đầy đủ
    public PermissionDTO(String ID, String Name, List<String> chucNang) {
        this.ID = ID;
        this.Name = Name;
        this.chucNang = chucNang;
    }

    // Constructor sao chép
    public PermissionDTO(PermissionDTO a) {
        this.ID = a.getID();
        this.Name = a.getName();
        this.chucNang = a.getChucNang();
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public List<String> getChucNang() {
        return chucNang;
    }

    // Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setChucNang(List<String> chucNang) {
        this.chucNang = chucNang;
    }
}
