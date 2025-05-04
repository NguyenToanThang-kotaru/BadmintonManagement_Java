package DTO;

public class AccountDTO {
    private String username;
    private String password;
    private String fullname; 
    private Permission2DTO permission;  

    public AccountDTO() {
        this("", "", "", null);
    }

    // Constructor mới nhận PermissionDTO
    public AccountDTO(String username, String password, String fullname, Permission2DTO permission) {
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

    public Permission2DTO getPermission() {
        return permission;
    }

    public void setPermission(Permission2DTO permission) {
        this.permission = permission;
    }

    // Phương thức tiện ích để lấy tên quyền (nếu cần)
    public String getRoleName() {
        return permission != null ? permission.getName() : "";
    }
    
}