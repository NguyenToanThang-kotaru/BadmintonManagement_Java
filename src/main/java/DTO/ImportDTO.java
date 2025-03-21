package DTO;

import java.sql.Date;

public class ImportDTO {
    private String importID;
    private String employeeID;
    private String supplierID;
    private double totalMoney;
    private Date importDate;

    public ImportDTO(String importID, String employeeID, String supplierID, double totalMoney, Date importDate) {
        this.importID = importID;
        this.employeeID = employeeID;
        this.supplierID = supplierID;
        this.totalMoney = totalMoney;
        this.importDate = importDate;
    }

    public String getImportID() {
        return importID;
    }

    public void setImportID(String importID) {
        this.importID = importID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }
}
