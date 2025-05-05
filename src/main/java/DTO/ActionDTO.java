/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Thang Nguyen
 */
public class ActionDTO {
    String ID;
    String name;
    String Uname;

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

    public ActionDTO(String ID, String name, String Uname) {
        this.ID = ID;
        this.name = name;
        this.Uname = Uname;
    }
    
    public ActionDTO(){
        
    }
    public ActionDTO(ActionDTO a){
        ID = a.ID;
        name = a.name;
        Uname = a.Uname;         
    }
    
}
