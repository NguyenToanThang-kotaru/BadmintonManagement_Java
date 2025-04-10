package BUS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Connection.DatabaseConnection;
import DTO.ProductDTO;
import GUI.Utils;
import DAO.Form_ImportDAO;
import DAO.ProductDAO;

public class Form_ImportBUS {
    private final Form_ImportDAO dao;
    private final ProductDAO productDAO;

    public Form_ImportBUS() {
        this.dao = new Form_ImportDAO();
        this.productDAO = new ProductDAO();
    }

    public String generateNextImportID() {
        return dao.generateNextImportID();
    }

    public String getEmployeeName(String employeeID) {
        if (employeeID == null || employeeID.trim().isEmpty()) {
            return "";
        }
        return dao.getEmployeeName(employeeID);
    }

    public String[] getSupplierNames() {
        return dao.getSupplierNames();
    }

    public Map<String, List<ProductDTO>> loadSupplierProducts() {
        return dao.loadSupplierProducts();
    }

    public boolean validateImportData(String supplierID, List<Object[]> productData) {
        if (supplierID == null || supplierID.trim().isEmpty()) {
            return false;
        }
        
        if (productData == null || productData.isEmpty()) {
            return false;
        }
        
        for (Object[] product : productData) {
            if (product == null || product.length < 4) {
                return false;
            }
            
            try {
                int quantity = (Integer) product[2];
                if (quantity <= 0) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        
        return true;
    }

    // Sửa phương thức saveImport để nhận List<Object[]> thay vì Object[][]
    public boolean saveImport(String importID, String employeeID, String supplierID, 
                              int totalAmount, String receiptDate, List<Object[]> productData) {
        // Chuyển List<Object[]> thành Object[][] để tương thích với DAO
        Object[][] productArray = productData.toArray(new Object[0][]);
        return dao.saveImport(importID, employeeID, supplierID, totalAmount, receiptDate, productArray);
    }

    public List<Object[]> loadAllProducts() {
        List<Object[]> products = new ArrayList<>();
        String query = "SELECT ma_san_pham, ten_san_pham, gia FROM san_pham WHERE is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Object[]{
                    rs.getString("ma_san_pham"),
                    rs.getString("ten_san_pham"),
                    Utils.formatCurrency(rs.getInt("gia"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public Object[] getProductDetails(String productId) {
        ProductDTO product = productDAO.getProduct(productId);
        if (product != null) {
            return new Object[]{
                product.getProductID(),
                product.getProductName(),
                Utils.formatCurrency(Integer.parseInt(product.getGia())),
                product.gettenNCC(),
                product.getAnh()
            };
        }
        return null;
    }

    public boolean updateProductQuantity(String productId, int quantity) {
        return productDAO.updateProductQuantity(productId, quantity);
    }

    public String getSupplierIDByProduct(String productId) {
        String query = "SELECT ma_nha_cung_cap FROM san_pham WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ma_nha_cung_cap");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String validateProductToAdd(String productId, String quantityText) {
        if (productId == null || productId.isEmpty() || productId.equals("Chọn sản phẩm từ danh sách")) {
            return "Vui lòng chọn một sản phẩm từ danh sách";
        }
    
        if (quantityText == null || quantityText.trim().isEmpty()) {
            return "Số lượng không được để trống";
        }
    
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                return "Số lượng phải là số nguyên dương lớn hơn 0";
            }
        } catch (NumberFormatException e) {
            return "Số lượng phải là một số nguyên hợp lệ";
        }
    
        return null; // Không có lỗi
    }

    public String calculateTotal(String priceText, String quantityText) {
        try {
            int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
            int quantity = quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText);
            return Utils.formatCurrency(price * quantity);
        } catch (NumberFormatException e) {
            return "0";
        }
    }
}