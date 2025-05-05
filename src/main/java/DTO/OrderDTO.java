package DTO;

public class OrderDTO {
    
    private String orderID;
    private String employeeID;
    private String customerID;
    private String totalmoney;
    private String issuedate;
    private String totalprofit;
    private boolean is_deleted;
    
    public OrderDTO() {
        this.orderID = "";
        this.employeeID = "";
        this.customerID = "";
        this.totalmoney = "";
        this.issuedate = "";
        this.totalprofit = "";
        this.is_deleted = false;
    }
    
    public OrderDTO(String orderID, String employeeID, String customerID, String totalmoney, String issuedate, String totalprofit, boolean is_deleted) {
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.totalmoney = totalmoney;
        this.issuedate = issuedate;
        this.totalprofit = totalprofit;
        this.is_deleted = is_deleted;
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
    
    public String gettotalprofit() {
        return totalprofit;
    }
    
    public void settotalprofit(String totalprofit) {
        this.totalprofit = totalprofit;
    }
    
    public boolean getis_deleted() {
        return is_deleted;
    }
    
    public void setis_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
}
