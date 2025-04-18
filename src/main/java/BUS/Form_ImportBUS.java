package BUS;

import DAO.EmployeeDAO;
import DAO.Form_ImportDAO;
import DAO.ProductDAO;
import DAO.SuppliersDAO;
import DTO.ProductDTO;
import DTO.SuppliersDTO;

import GUI.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Form_ImportBUS {
    private final Form_ImportDAO formImportDAO;
    private final ProductDAO productDAO;
    private final EmployeeDAO employeeDAO;
    private final SuppliersDAO suppliersDAO;

    public Form_ImportBUS() {
        this.formImportDAO = new Form_ImportDAO();
        this.productDAO = new ProductDAO();
        this.employeeDAO = new EmployeeDAO();
        this.suppliersDAO = new SuppliersDAO();
    }

    // Gọi từ Form_ImportDAO
    public String generateNextImportID() {
        return formImportDAO.generateNextImportID();
    }

    // Gọi từ EmployeeDAO
    public String getEmployeeName(String employeeID) {
        if (employeeID == null || employeeID.trim().isEmpty()) {
            return "";
        }
        return employeeDAO.getEmployeeNameByID(employeeID);
    }

    // Gọi từ SuppliersDAO và định dạng
    public String[] getSupplierNames() {
        List<String> supplierNames = new ArrayList<>();
        ArrayList<SuppliersDTO> suppliers = suppliersDAO.getAllSuppliers();
        for (SuppliersDTO supplier : suppliers) {
            supplierNames.add(supplier.getsuppliersID() + " - " + supplier.getfullname());
        }
        return supplierNames.toArray(new String[0]);
    }

    // Gọi từ ProductDAO và nhóm theo nhà cung cấp
    public Map<String, List<ProductDTO>> loadSupplierProducts() {
        Map<String, List<ProductDTO>> map = new HashMap<>();
        ArrayList<ProductDTO> products = productDAO.getAllProducts();
        for (ProductDTO product : products) {
            String supplierID = product.getMaNCC();
            map.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(product);
        }
        return map;
    }

    // Kiểm tra dữ liệu nhập hàng
    public boolean validateImportData(String supplierID, List<Object[]> productData) {
        if (supplierID == null || supplierID.trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra nhà cung cấp có bị xóa không
        SuppliersDTO supplier = suppliersDAO.getSupplierByID(supplierID);
        if (supplier == null || supplier.getIsDeleted() == 1) {
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
                // Kiểm tra sản phẩm thuộc nhà cung cấp còn hoạt động
                String productID = (String) product[0];
                ProductDTO productDTO = productDAO.getProduct(productID);
                if (productDTO == null) {
                    return false;
                }
                // Kiểm tra nhà cung cấp của sản phẩm
                SuppliersDTO productSupplier = suppliersDAO.getSupplierByID(productDTO.getMaNCC());
                if (productSupplier == null || productSupplier.getIsDeleted() == 1) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        
        return true;
    }

    // Gọi từ Form_ImportDAO để lưu và từ ProductDAO để cập nhật số lượng
    public boolean saveImport(String importID, String employeeID, String supplierID,
                              int totalAmount, String receiptDate, List<Object[]> productData) {
        Object[][] productArray = productData.toArray(new Object[0][]);
        boolean saved = formImportDAO.saveImport(importID, employeeID, supplierID, totalAmount, receiptDate, productArray);
        if (saved) {
            // Cập nhật số lượng sản phẩm sau khi lưu thành công
            for (Object[] product : productData) {
                String productID = (String) product[0];
                int quantity = (Integer) product[2];
//                System.out.println(quantity+productID);
                productDAO.updateProductQuantity(productID, quantity);
            }
        }
        return saved;
    }

    // Gọi từ ProductDAO
    public List<Object[]> loadAllProducts() {
        List<Object[]> products = new ArrayList<>();
        ArrayList<ProductDTO> productList = productDAO.getAllProducts();
        for (ProductDTO product : productList) {
            // Kiểm tra xem nhà cung cấp của sản phẩm có bị xóa không
            String supplierID = product.getMaNCC();
            SuppliersDTO supplier = suppliersDAO.getSupplierByID(supplierID);
            if (supplier != null && supplier.getIsDeleted() == 0) {
                products.add(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    Utils.formatCurrency(Integer.parseInt(product.getGia()))
                });
            }
        }
        return products;
    }
    // Gọi từ ProductDAO
    public Object[] getProductDetails(String productId) {
        ProductDTO product = productDAO.getProduct(productId);
        if (product != null) {
            // Kiểm tra nhà cung cấp của sản phẩm
            String supplierID = product.getMaNCC();
            SuppliersDTO supplier = suppliersDAO.getSupplierByID(supplierID);
            if (supplier != null && supplier.getIsDeleted() == 0) {
                return new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    Utils.formatCurrency(Integer.parseInt(product.getGia())),
                    product.gettenNCC(),
                    product.getAnh()
                };
            }
        }
        return null;
    }
    // Gọi từ ProductDAO
    public boolean updateProductQuantity(String productId, int quantity) {
        return productDAO.updateProductQuantity(productId, quantity);
    }

    // Gọi từ SuppliersDAO
    public String getSupplierIDByProduct(String productId) {
        return suppliersDAO.getSupplierIDByProduct(productId);
    }

    // Xác thực sản phẩm để thêm
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

    // Tính tổng tiền
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