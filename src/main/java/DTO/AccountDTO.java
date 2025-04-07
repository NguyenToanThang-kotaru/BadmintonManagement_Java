package DTO;

public class AccountDTO {
    private String username;
    private String password;
    private String fullname;  // Thay ID bằng fullname cho rõ nghĩa
    private PermissionDTO permission;  // Thay String tenquyen bằng PermissionDTO

    public AccountDTO() {
        this("", "", "", null);
    }

    // Constructor mới nhận PermissionDTO
    public AccountDTO(String username, String password, String fullname, PermissionDTO permission) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.permission = permission;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public PermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(PermissionDTO permission) {
        this.permission = permission;
    }

    // Phương thức tiện ích để lấy tên quyền (nếu cần)
    public String getRoleName() {
        return permission != null ? permission.getName() : "";
    }
    
    // Phương thức kiểm tra có chức năng nào không
    public boolean hasFunction(String functionCode) {
        return permission != null && permission.getChucNang()!= null 
               && permission.getChucNang().contains(functionCode);
    }
}