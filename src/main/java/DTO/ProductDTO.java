package DTO;

public class ProductDTO {
    private String productID;
    private String productName;
    private String gia;
    private String soluong;
    private String maNCC;
    private String TSKT;
    private String ML;
    private String anh;

    public ProductDTO() {
        this.productID = "";
        this.productName = "";
        this.gia = "";
        this.soluong = "";
        this.maNCC = "";
        this.TSKT = ""; 
        this.ML = "";
        this.anh = "";
    }

    public ProductDTO(String productID, String productName, String gia, String soluong, String maNCC, String TSKT, String ML, String anh) {
        this.productID = productID;
        this.productName = productName;
        this.gia = gia;
        this.soluong = soluong;
        this.maNCC = maNCC;
        this.TSKT = TSKT;
        this.ML = ML;
        this.anh = anh;
    }

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getGia() { return gia; }
    public void setGia(String gia) { this.gia = gia; }

    public String getSoluong() { return soluong; }
    public void setSoluong(String soluong) { this.soluong = soluong; }

    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTSKT() { return TSKT; }
    public void setTSKT(String TSKT) { this.TSKT = TSKT; }

    public String getML() { return ML; }
    public void setML(String ML) { this.ML = ML; }

    public String getAnh() { return anh; }
    public void setAnh(String anh) { this.anh = anh; }
}
