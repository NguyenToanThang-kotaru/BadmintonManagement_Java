package BUS;

import DAO.Permission2DAO;
import DTO.PermissionDTO;
import DAO.PermissionDAO;
import DTO.AccountDTO;
import DTO.ActionDTO;
import DTO.Permission2DTO;

import java.util.ArrayList;
import java.util.List;

public class PermissionBUS {

    public static Permission2DTO getPermissionByName(String name) {
        return Permission2DAO.getPermissionByName(name);
    }
    
    public static String countAllAccountsByPer(String id) {
        return Permission2DAO.countAllAccountsByPer(id);
    }

    public static Boolean updatePermission(Permission2DTO per) {
        return Permission2DAO.updatePermission(per);
    }
    
    public static Permission2DTO getPermissionByID(String id) {
        return Permission2DAO.getPermissionByID(id);
    }

    public static int deletePermission(Permission2DTO per) {
        String totalAccount = countAllAccountsByPer(per.getID());
        if (totalAccount.equals("0")) {
            if (Permission2DAO.deletePermission(per) && FunctionBUS.deleteFunction(per)) {
                return 1;
            }
            return 0;
        }
        return 2;
    }

    public static Boolean addPermission(Permission2DTO per) {
        return Permission2DAO.addPermission(per);
    }

    public static String countDistinctFunctionsByPermission(String id) {
        return Permission2DAO.countDistinctFunctionsByPermission(id);
    }

    public static String generateID() {
        ArrayList<Permission2DTO> permissions = Permission2DAO.getAllPermissions();
        int nextNumber = permissions.size() + 1; // Bắt đầu từ 1 nếu danh sách rỗng

        // Định dạng số thành 2 chữ số (01, 02,...)
        String formattedNumber = String.format("%02d", nextNumber);
        return "R" + formattedNumber; // Ví dụ: R01, R02
    }

    public static ArrayList<Permission2DTO> getAllPermissions() {
        return Permission2DAO.getAllPermissions();
    }

    public static List<PermissionDTO> searchPermission(String keyword) {
        return PermissionDAO.searchPermission(keyword);
    }
}
