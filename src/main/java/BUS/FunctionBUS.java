/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.FunctionDAO;
import DTO.FunctionDTO;
import DTO.Permission2DTO;
import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class FunctionBUS {
    public static Boolean addFunction(Permission2DTO per){
        return FunctionDAO.addFunction(per);
    }
    public static ArrayList<FunctionDTO> getAllFunctions() {
        return FunctionDAO.getAllFunctions();
    }
    public static FunctionDTO getFunctionByName(String name){
        return FunctionDAO.getFunctionByName(name);
    }
    public static Boolean deleteFunction(Permission2DTO per) {
        return FunctionDAO.deleteFunction(per);
    }
}
