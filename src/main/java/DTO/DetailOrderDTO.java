package DTO;

public class DetailOrderDTO {
    
    private String detailorderID;
    private String productID;
    private String orderID;
    private String serialID;
    private String amount;
    private String price;
    
    public DetailOrderDTO() {
        this.detailorderID = "";
        this.productID = "";
        this.orderID = "";
        this.serialID = "";
        this.amount = "";
        this.price = "";
    }
    
    public DetailOrderDTO(String detailorderID, String productID, String orderID, String serialID, String amount, String price) {
        this.detailorderID = detailorderID;
        this.productID = productID;
        this.orderID = orderID;
        this.serialID = serialID;
        this.amount = amount;
        this.price = price;
    }
    
    public String getdetailorderID() {
        return detailorderID;
    }
    
    public void setdetailorderID(String detailorderID) {
        this.detailorderID = detailorderID;
    }
    
    public String getproductID() {
        return productID;
    }
    
    public void setproductID(String productID) {
        this.productID = productID;
    }
    
    public String getorderID(){
        return orderID;
    }
    
    public void setorderID(String orderID) {
        this.orderID = orderID;
    }
    
    public String getserialID() {
        return serialID;
    }
    
    public void setserialID(String serialID) {
        this.serialID = serialID;
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
