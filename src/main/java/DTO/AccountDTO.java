package DTO;

public class AccountDTO {

    private String username;
    private String password;
    private String ID;
    private String tenquyen;

    public AccountDTO() {
        username = "";
        password = "";
        ID = "";
        tenquyen = "";
    }

    public AccountDTO(String username, String password, String ID, String tenquyen) {
        this.username = username;
        this.password = password;
        this.ID = ID;
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
        return ID;
    }

    public void setFullname(String ID) {
        this.ID = ID;
    }
    
    public String getTenquyen() {
        return tenquyen;
    }
    
    
    public void setTenquyen (String Tenquyen) {
        this.tenquyen = tenquyen;
    }
}
