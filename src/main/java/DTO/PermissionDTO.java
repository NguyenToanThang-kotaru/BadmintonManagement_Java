package DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PermissionDTO {

    private String ID;
    private String Name;
    private List<String> chucNang; // Danh sách chức năng
    private String slChucNang;       // Số lượng chức năng (tính từ danh sách chucNang)
    private String slTk;            // Số lượng tài khoản có quyền này

    // Constructor đầy đủ
    public PermissionDTO(String ID, String Name, List<String> chucNang, String slTk) {
        this.ID = ID;
        this.Name = Name;
        this.chucNang = List.copyOf(chucNang); // Tạo bản sao bất biến
        this.slChucNang = String.valueOf(chucNang.size());
        this.slTk = slTk;
    }

    public PermissionDTO() {
        this.ID = "";
        this.Name = "Test";
        this.chucNang = new ArrayList<>();
        this.chucNang.add("xem_sp"); // Thêm phần tử sau khi khởi tạo
        this.slChucNang = String.valueOf(this.chucNang.size());
        this.slTk = "0";
    }

    public PermissionDTO(PermissionDTO a) {
        this.ID = a.ID;
        this.Name = a.Name;
        this.chucNang = new ArrayList<>(a.chucNang); // Tạo bản sao của List
        this.slChucNang = a.slChucNang;
        this.slTk = a.slTk;
    }

    // Constructor không có slTk
    public PermissionDTO(String ID, String Name, List<String> chucNang) {
        this(ID, Name, chucNang, "0");
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public List<String> getChucNang() {
        return Collections.unmodifiableList(chucNang); // Trả về danh sách chỉ đọc
    }

    public String getSlChucNang() {
        return slChucNang;
    }

    public String getSlTk() {
        return slTk;
    }

    // Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setChucNang(List<String> chucNang) {
        this.chucNang = List.copyOf(chucNang);
        this.slChucNang = String.valueOf(chucNang.size()); // Tự động cập nhật số lượng chức năng
    }

    public void setSlTk(String slTk) {
        this.slTk = slTk;
    }
}
