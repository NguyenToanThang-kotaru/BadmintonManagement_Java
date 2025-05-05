/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class Permission2DTO {
    String ID;
    String name;
    String Uname;
    ArrayList<FunctionDTO> function;

    public Permission2DTO(String ID, String name, String Uname, ArrayList<FunctionDTO> function) {
        this.ID = ID;
        this.name = name;
        this.Uname = Uname;
        this.function = function;
    }
    
    public Permission2DTO(){
        
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String Uname) {
        this.Uname = Uname;
    }

    public ArrayList<FunctionDTO> getFunction() {
        return function;
    }

    public void setFunction(ArrayList<FunctionDTO> function) {
        this.function = function;
    }
    
}
