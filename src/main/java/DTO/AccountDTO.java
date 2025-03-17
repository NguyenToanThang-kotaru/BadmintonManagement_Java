package DTO;

public class AccountDTO {

    private String username;
    private String password;
    private String fullname;
    private String tenquyen;

    public AccountDTO() {
        username = "";
        password = "";
        fullname = "";
        tenquyen = "";
    }

    public AccountDTO(String username, String password, String fullname, String tenquyen) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.tenquyen = tenquyen;
    }

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
    
    public String getTenquyen() {
        return tenquyen;
    }
    
    
    public void setTenquyen (String Tenquyen) {
        this.tenquyen = tenquyen;
    }
}
