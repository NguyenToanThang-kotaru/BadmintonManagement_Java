/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.Date;

public class OrderDTO {
    
    private String orderID;
    private String employeeID;
    private String customerID;
    private String totalmoney;
    private String issuedate;
    
    public OrderDTO() {
        this.orderID = "";
        this.employeeID = "";
        this.customerID = "";
        this.totalmoney = "";
        this.issuedate = "";
    }
    
    public OrderDTO(String orderID, String employeeID, String customerID, String totalmoney, String issuedate) {
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.totalmoney = totalmoney;
        this.issuedate = issuedate;
    }
    
    public String getorderID() {
        return orderID;
    }
    
    public void setorderID (String orderID) {
        this.orderID = orderID;
    }
    
    public String getemployeeID() {
        return employeeID;
    }
    
    public void setemployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
    
    public String getcustomerID() {
        return customerID;
    }
    
    public void setcustomerID(String customerID) {
        this.customerID = customerID;
    }
    
    public String gettotalmoney() {
        return totalmoney;
    }
    
    public void settotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }
    
    public String getissuedate() {
        return issuedate;
    }
    
    public void setissuedate(String issuedate) {
        this.issuedate = issuedate;
    }
}
