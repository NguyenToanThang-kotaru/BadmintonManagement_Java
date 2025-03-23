package DTO;

public class DetailImportDTO {
    
    private String detailimportID;
    private String importID;
    private String productID;
    private String amount;
    private String price;
    
    public DetailImportDTO() {
        this.detailimportID = "";
        this.importID = "";
        this.productID = "";
        this.productID = "";
        this.amount = "";
        this.price = "";
    }
    
    public DetailImportDTO(String detailimportID, String importID, String productID, String amount, String price) {
        this.detailimportID = detailimportID;
        this.importID = importID;
        this.productID = productID;
        this.productID = productID;
        this.amount = amount;
        this.price = price;
    }
    
    public String getdetailimportID() {
        return detailimportID;
    }
    
    public void setdetailimportID(String detailimportID) {
        this.detailimportID = detailimportID;
    }
    
    public String getimportID() {
        return importID;
    }
    
    public void setimportID(String importID) {
        this.importID = importID;
    }
    
    public String getproductID() {
        return productID;
    }
    
    public void setproductID() {
        this.productID = productID;
    }
    
    public String getamount() {
        return amount;
    }
    
    public void setamount(String amount) {
        this.amount = amount;
    }
    
    public String getprice() {
        return price;
    }
    
    public void setprice(String price) {
        this.price = price;
    }
}
