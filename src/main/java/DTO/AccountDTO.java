package DTO;

public class AccountDTO {

    private String username;
    private String password;
 

    public AccountDTO() {
        username = "";
        password = "";
    }

    public AccountDTO(String username, String password) {
        this.username = username;
        this.password = password;

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
}
