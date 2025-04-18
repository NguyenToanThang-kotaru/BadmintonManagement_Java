package BUS;

import DAO.Edit_SuppliersDAO;
import DTO.SuppliersDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Connection.DatabaseConnection;

public class Edit_SuppliersBUS {
    
    private Edit_SuppliersDAO editSuppliersDAO;

    public Edit_SuppliersBUS() {
        editSuppliersDAO = new Edit_SuppliersDAO();
    }

    public void updateSupplier(SuppliersDTO supplier) {
        validateUpdateSupplier(supplier);
        editSuppliersDAO.updateSupplier(supplier);
    }

    private void validateUpdateSupplier(SuppliersDTO supplier) {
        // Kiểm tra tất cả các trường không được để trống
        checkNotEmpty(supplier);

        // Kiểm tra tên NCC: không chứa ký tự đặc biệt (ngoại trừ dấu tiếng Việt) và không trùng với NCC khác
        if (!Pattern.matches("^[\\p{L}0-9\\s]+$", supplier.getfullname())) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được chứa ký tự đặc biệt (ngoại trừ dấu tiếng Việt)!");
        }
        if (isSupplierNameExists(supplier.getfullname(), supplier.getsuppliersID())) {
            throw new IllegalArgumentException("Tên nhà cung cấp đã tồn tại!");
        }

        // Kiểm tra địa chỉ: chỉ chữ cái, số, khoảng trắng, dấu phẩy và dấu tiếng Việt
        if (!Pattern.matches("^[\\p{L}0-9\\s,]+$", supplier.getaddress())) {
            throw new IllegalArgumentException("Địa chỉ không được chứa ký tự đặc biệt (ngoại trừ dấu phẩy và dấu tiếng Việt)!");
        }

        // Kiểm tra số điện thoại: chỉ số, đủ 10 chữ số
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

    // Kiểm tra tên NCC có trùng không (ngoại trừ chính nó)
    private boolean isSupplierNameExists(String supplierName, String supplierID) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM nha_cung_cap WHERE ten_nha_cung_cap = ? AND ma_nha_cung_cap != ? AND is_deleted = 0")) {
            stmt.setString(1, supplierName);
            stmt.setString(2, supplierID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra tên nhà cung cấp: " + e.getMessage());
        }
        return false;
    }
}