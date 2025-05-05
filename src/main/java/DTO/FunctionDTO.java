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
public class FunctionDTO {
    String ID;
    String name;
    String Uname;
    ArrayList<ActionDTO> actions;
    
    public ArrayList<ActionDTO> getActions() {
        return actions;
    }

    public FunctionDTO(String ID, String name, String Uname, ArrayList<ActionDTO> actions) {
        this.ID = ID;
        this.name = name;
        this.Uname = Uname;
        this.actions = actions;
    }

    public FunctionDTO() {
        
    }
    
    public void setActions(ArrayList<ActionDTO> actions) {
        this.actions = actions;
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
    
}
