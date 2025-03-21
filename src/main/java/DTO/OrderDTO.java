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
    private double totalmoney;
    private Date issuedate;
    
    public OrderDTO(String orderID, String employeeID, String customerID, double totalmoney, Date issuedate) {
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
    
    public String setcustomerID() {
        return customerID;
    }
    
    public void setcustomerID(String customerID) {
        this.customerID = customerID;
    }
    
    public double gettotalmoney() {
        return totalmoney;
    }
    
    public void settotalmoney(double totalmoney) {
        this.totalmoney = totalmoney;
    }
    
    public Date getissuedate() {
        return issuedate;
    }
    
    public void setissuedate(Date issuedate) {
        this.issuedate = issuedate;
    }

    public String getcustomerID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
