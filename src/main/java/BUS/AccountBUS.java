package BUS;

import DAO.AccountDAO;
import DAO.EmployeeDAO;
import DAO.PermissionDAO;
import DTO.AccountDTO;
import DTO.EmployeeDTO;
import DTO.PermissionDTO;

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

    public List<AccountDTO> getAllPlayers_No_Account() {
        List<AccountDTO> a = AccountDAO.getAllAccounts();
        return a;
    }

    public static Boolean addAccount(String username, String password, String tenquyen) {
        System.out.println(tenquyen);
        PermissionDTO a= new PermissionDTO(PermissionDAO.getPermissionByName(tenquyen));
        EmployeeDTO e = EmployeeDAO.getEmployeeByName(username);
        if (AccountDAO.addAccount(e.getEmployeeID(), password, a.getID())==true)
            return true;
        else
            System.out.println("Khong co cc j het");
        return false;
    }

    public static Boolean updateAccount(String username,String password, String maquyen) {
        PermissionDTO a = new PermissionDTO(PermissionDAO.getPermissionByName(maquyen));
        return AccountDAO.updateAccount(username, password, a.getID())==true;
    }

}
