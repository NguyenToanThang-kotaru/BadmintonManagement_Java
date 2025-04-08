package BUS;

import java.util.List;
import java.util.Map;

import DTO.ProductDTO;
import DAO.Form_ImportDAO; 

public class Form_ImportBUS {
    private Form_ImportDAO dao;

    public Form_ImportBUS() {
        this.dao = new Form_ImportDAO();
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

    public boolean saveImport(String importID, String employeeID, String supplierID, 
                           int totalAmount, String receiptDate, List<Object[]> productData) {
        // Validate before saving
        if (!validateImportData(supplierID, productData)) {
            return false;
        }
        
        // Convert List<Object[]> to Object[][]
        Object[][] productArray = productData.toArray(new Object[productData.size()][]);
        
        return dao.saveImport(importID, employeeID, supplierID, totalAmount, receiptDate, productArray);
    }
}