
package DTO;

public class ImportDTO {
    
    private String importID;
    private String employeeID;
    private String supplierID;
    private String totalmoney;
    private String receiptdate;
    
    public ImportDTO() {
        this.importID = "";
        this.employeeID = "";
        this.supplierID = "";
        this.totalmoney = "";
        this.receiptdate = "";
    }
    
    public ImportDTO(String importID, String employeeID, String supplierID, String totalmoney, String receiptdate) {
        this.importID = importID;
        this.employeeID = employeeID;
        this.supplierID = supplierID;
        this.totalmoney = totalmoney;
        this.receiptdate = receiptdate;
    }
    
    public String getimportID() {
        return importID;
    }
    
    public void setimportID(String importID) {
        this.importID = importID;
    }
    
    public String getemployeeID() {
        return employeeID;
    }
    
    public void setemployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
    
    public String getsupplierID() {
        return supplierID;
    }
    
    public void setsupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
    
    public String gettotalmoney() {
        return totalmoney;
    }
    
    public void settotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }
    
    public String getreceiptdate() {
        return receiptdate;
    }
    
    public void setreceiptdate(String receiptdate) {
        this.receiptdate = receiptdate;
    }
}
