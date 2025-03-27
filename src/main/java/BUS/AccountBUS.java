package BUS;

import DAO.AccountDAO;
import DTO.AccountDTO;
import java.util.List;
import javax.swing.JOptionPane;

public class AccountBUS {

    public Boolean ValidationLogin(String username, String password) {
        String usernameRegex = "^[a-zA-Z0-9]{5,}$";
        String passwordRegex = "^[a-zA-Z0-9]{6,}$";
        if (!username.matches(usernameRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Username chỉ được chứa chữ cái và số, tối thiểu 3 ký tự.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Password phải có ít nhất 8 ký tự, chứa ít nhất 1 chữ cái và 1 số.",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public Boolean HaveAccount(String username, String password) {
        if (ValidationLogin(username, password)) {
            if (AccountDAO.getAccount(username, password) != null) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } else {
            return false;
        }
    }

    public List<AccountDTO> getAllAccounts() {
        return AccountDAO.getAllAccounts();
    }

    public void updateAccount(AccountDTO account) {
        AccountDAO dao = new AccountDAO();
        dao.updateAccount(account);
    }

}
