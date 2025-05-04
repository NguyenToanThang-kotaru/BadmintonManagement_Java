/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ActionDAO;
import DTO.AccountDTO;
import DTO.ActionDTO;
import DTO.FunctionDTO;
import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class ActionBUS {

    public static ArrayList<ActionDTO> getPermissionActions(AccountDTO account, String functionUnsignedName) {
//        ArrayList<String> actions = new ArrayList<>();
        for (FunctionDTO perm : account.getPermission().getFunction()) {
            if (perm.getName().equals(functionUnsignedName)) {
                return perm.getActions(); // "Add", "Edit", "Delete", v.v.
            }
        }
        return null;
    }

    public static ArrayList<ActionDTO> getAllActions() {
        return ActionDAO.getAllActions();
    }

    public static ActionDTO getActionByName(String name) {
        return ActionDAO.getActionByName(name);
    }
}
