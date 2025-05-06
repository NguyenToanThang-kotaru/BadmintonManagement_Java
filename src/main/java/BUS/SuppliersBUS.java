package BUS;

import DAO.ProductDAO;
import DAO.SuppliersDAO;
import DTO.ProductDTO;
import DTO.SuppliersDTO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

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
    
      public static ArrayList<String> getAllNCCNames() {
          return SuppliersDAO.getAllNCCNames();
      }



    public boolean exportToExcel(String filePath) {
        try (Connection conn = DatabaseConnection.getConnection();
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DanhSachNhaCungCap");
            
            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ma_nha_cung_cap", "ten_nha_cung_cap", "dia_chi", "so_dien_thoai", "is_deleted"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
    
            // Lấy dữ liệu từ database
            String sql = "SELECT * FROM nha_cung_cap";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getString("ma_nha_cung_cap"));
                row.createCell(1).setCellValue(rs.getString("ten_nha_cung_cap"));
                row.createCell(2).setCellValue(rs.getString("dia_chi"));
                row.createCell(3).setCellValue(rs.getString("so_dien_thoai"));
                row.createCell(4).setCellValue(rs.getInt("is_deleted"));
            }
    
            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
    
            // Ghi vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean importSuppliersFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean success = true;
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua dòng tiêu đề
    
                String supplierID = getCellValueAsString(row.getCell(0));
                String supplierName = getCellValueAsString(row.getCell(1));
                String address = getCellValueAsString(row.getCell(2));
                String phone = getCellValueAsString(row.getCell(3));
                int isDeleted = (int) row.getCell(4).getNumericCellValue();
    
                // Validation cho số điện thoại
                try {
                    validatePhoneNumber(phone);
                } catch (IllegalArgumentException e) {
                    System.err.println("Lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    success = false;
                    continue; // Bỏ qua dòng không hợp lệ
                }
    
                // Nếu mã nhà cung cấp rỗng, tự động sinh mã mới
                if (supplierID == null || supplierID.trim().isEmpty()) {
                    supplierID = generateSupplierID();
                }
    
                // Gọi DAO để thêm vào database
                suppliersDAO.insertSupplier(supplierID, supplierName, address, phone, isDeleted);
            }
            return success;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void validatePhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        if (!phone.startsWith("0")) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 0!");
        }
        if (!Pattern.matches("^[0-9]{10}$", phone)) {
            throw new IllegalArgumentException("Số điện thoại phải có đúng 10 chữ số và chỉ chứa số!");
        }
    }

    private String getCellValueAsString(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            return String.valueOf((int) cell.getNumericCellValue());
        default:
            return "";
    }
}

}