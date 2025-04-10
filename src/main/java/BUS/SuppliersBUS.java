package BUS;

import DAO.ProductDAO;
import DAO.SuppliersDAO;
import DTO.ProductDTO;
import DTO.SuppliersDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import Connection.DatabaseConnection;

public class SuppliersBUS {

    private SuppliersDAO suppliersDAO;
    private ProductDAO productDAO;

    public SuppliersBUS() {
        suppliersDAO = new SuppliersDAO();
        productDAO = new ProductDAO();
    }

    public List<SuppliersDTO> getAllSuppliers() {
        return SuppliersDAO.getAllSuppliers();
    }

    public void addSupplier(SuppliersDTO supplier) {
        validateAddSupplier(supplier);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO nha_cung_cap (ma_nha_cung_cap, ten_nha_cung_cap, dia_chi, so_dien_thoai, is_deleted) VALUES (?, ?, ?, ?, 0)")) {
            stmt.setString(1, supplier.getsuppliersID());
            stmt.setString(2, supplier.getfullname());
            stmt.setString(3, supplier.getaddress());
            stmt.setString(4, supplier.getphone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm nhà cung cấp: " + e.getMessage());
        }
    }
    private void validateAddSupplier(SuppliersDTO supplier) {
        
        checkNotEmpty(supplier);
        if (isSupplierIDExists(supplier.getsuppliersID())) {
            throw new IllegalArgumentException("Mã nhà cung cấp đã tồn tại!");
        }
        if (!Pattern.matches("^[\\p{L}0-9\\s]+$", supplier.getfullname())) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được chứa ký tự đặc biệt (ngoại trừ dấu tiếng Việt)!");
        }
        if (!Pattern.matches("^[\\p{L}0-9\\s,]+$", supplier.getaddress())) {
            throw new IllegalArgumentException("Địa chỉ không được chứa ký tự đặc biệt (ngoại trừ dấu phẩy và dấu tiếng Việt)!");
        }
        if (!Pattern.matches("^[0-9]{10}$", supplier.getphone())) {
            throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số và chỉ chứa số!");
        }
    }
    // Kiểm tra các trường không được để trống
    private void checkNotEmpty(SuppliersDTO supplier) {
        if (supplier.getsuppliersID() == null || supplier.getsuppliersID().isEmpty()) {
            throw new IllegalArgumentException("Mã nhà cung cấp không được để trống!");
        }
        if (supplier.getfullname() == null || supplier.getfullname().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống!");
        }
        if (supplier.getaddress() == null || supplier.getaddress().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống!");
        }
        if (supplier.getphone() == null || supplier.getphone().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
    }

    // Kiểm tra mã NCC có tồn tại chưa
    private boolean isSupplierIDExists(String supplierID) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM nha_cung_cap WHERE ma_nha_cung_cap = ? AND is_deleted = 0")) {
            stmt.setString(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra mã nhà cung cấp: " + e.getMessage());
        }
        return false;
    }
    public List<ProductDTO> getProductsBySupplier(String supplierID) {
        List<ProductDTO> allProducts = productDAO.getAllProducts();
        List<ProductDTO> supplierProducts = new ArrayList<>();
        for (ProductDTO product : allProducts) {
            if (product.getMaNCC().equals(supplierID)) {
                supplierProducts.add(product);
            }
        }
        return supplierProducts;
    }

    public String generateSupplierID() {
        return suppliersDAO.generateSupplierID();
    }
}