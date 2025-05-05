package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;

import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

import org.apache.poi.ss.usermodel.Row;

public class ProductBUS {

    public static List<ProductDTO> getAllProducts() {
        return ProductDAO.getAllProducts();
    }

    public static void updateProduct(ProductDTO product) {
        ProductDAO dao = new ProductDAO();
        dao.updateProduct(product);
    }

    public static Boolean addProduct(ProductDTO product) {
        return ProductDAO.addProduct(product);
    }

    public static ProductDTO getProduct(String ProductID) {
        ProductDAO dao = new ProductDAO();
        return dao.getProduct(ProductID); // Trả về đối tượng lấy được từ DAO
    }

    public static ArrayList<ProductDTO> searchProducts(String keyword) {
        return ProductDAO.searchProducts(keyword);
    }

    public static ArrayList<String> getSerialsForProduct(String productID) {
        return ProductDAO.getSerialsForProduct(productID);
    }

    public static ArrayList<String> getAllCategoryNames() {
        return ProductDAO.getAllCategoryNames();
    }

    // Xóa sản phẩm theo mã
    public static boolean deleteProduct(String productID) {
        return ProductDAO.deleteProduct(productID);
    }

    public boolean validateProduct(ProductDTO product) {
        String productName = product.getProductName().trim();
        String gia = product.getGia().trim();
        String gianhap = product.getgiaGoc().trim();
        String khuyenmai = product.getkhuyenMai().trim();
        String soluong = product.getSoluong().trim();
        String HLBH = product.getTGBH().trim();
        String Anh = product.getAnh();

        // Không cho tên chỉ toàn số
        if (productName.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "Tên sản phẩm không được chỉ chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Không chứa ký tự đặc biệt (chỉ cho phép chữ cái, số và khoảng trắng)
        if (!productName.matches("^[\\p{L}0-9\\s+\\-\\.]+$")) {
            JOptionPane.showMessageDialog(null,
                    "Tên sản phẩm không được chứa ký tự đặc biệt.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!gia.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Giá chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!gianhap.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Giá nhập chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!khuyenmai.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Khuyến mãi chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!HLBH.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null,
                    "Hiệu lực bảo hành chỉ được chứa số.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Anh == null) {
            JOptionPane.showMessageDialog(null,
                    "Ảnh sản phẩm không được để trống.",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<ProductDTO> productList = ProductDAO.getAllProducts();
        for (ProductDTO p : productList) {
            if (p.getProductName().trim().equalsIgnoreCase(productName)
                    && (product.getProductID() == null || !product.getProductID().equals(p.getProductID()))) {
                JOptionPane.showMessageDialog(null,
                        "Tên sản phẩm đã tồn tại!",
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public boolean canDeleteProduct(ProductDTO product) {
        try {
            int quantity = Integer.parseInt(product.getSoluong());
            if (quantity > 0) {
                JOptionPane.showMessageDialog(null,
                        "Không thể xóa sản phẩm vì số lượng còn lớn hơn 0.",
                        "Xóa sản phẩm",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Dữ liệu số lượng sản phẩm không hợp lệ.",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //Chỗ Tiến sử dụng để làm:
    public String getProductImage(String productID) {
        return ProductDAO.getProductImage(productID);
    }

    public ProductDTO getProductByID(String id) {
        ProductDAO dao = new ProductDAO();
        return dao.getProduct(id);
    }

    public List<String> getAvailableSerials(String maSanPham, int soLuong) {
        return ProductDAO.getAvailableSerials(maSanPham, soLuong);
    }

    public void markSerialsAsUsed(List<String> serials) {
        ProductDAO.markSerialsAsUsed(serials);
    }

    public boolean reduceStock(String productId, int quantity) {
        ProductDAO dao = new ProductDAO();
        return dao.updateStockAfterSale(productId, quantity);
    }

    public double getProductDiscount(String productID) {
        ProductDTO product = getProductByID(productID);
        if (product != null) {
            try {
                return Double.parseDouble(product.getkhuyenMai());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public double getProductOriginalPrice(String productID) {
        ProductDTO product = getProductByID(productID);
        if (product != null) {
            try {
                return Double.parseDouble(product.getgiaGoc());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean increaseStock(String productId, int quantity) {
        ProductDAO dao = new ProductDAO();
        return dao.increaseStock(productId, quantity);
    }

    public void unmarkSerialsAsUsed(List<String> serials) {
        ProductDAO dao = new ProductDAO();
        dao.unmarkSerialsAsUsed(serials);
    }


    public boolean exportToExcel(String filePath) {
    try (Connection conn = DatabaseConnection.getConnection();
         Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachSanPham");
        
        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ma_san_pham", "ten_san_pham", "gia", "so_luong", "ma_nha_cung_cap", "thong_so_ki_thuat", "ma_loai", "hinh_anh", "is_deleted", "gia_goc", "khuyen_mai", "thoi_gian_bao_hanh"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lấy dữ liệu từ database
        String sql = "SELECT * FROM san_pham";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("ma_san_pham"));
            row.createCell(1).setCellValue(rs.getString("ten_san_pham"));
            row.createCell(2).setCellValue(rs.getInt("gia"));
            row.createCell(3).setCellValue(rs.getInt("so_luong"));
            row.createCell(4).setCellValue(rs.getString("ma_nha_cung_cap"));
            row.createCell(5).setCellValue(rs.getString("thong_so_ki_thuat"));
            row.createCell(6).setCellValue(rs.getString("ma_loai"));
            row.createCell(7).setCellValue(rs.getString("hinh_anh"));
            row.createCell(8).setCellValue(rs.getInt("is_deleted"));
            row.createCell(9).setCellValue(rs.getInt("gia_goc"));
            row.createCell(10).setCellValue(rs.getString("khuyen_mai"));
            row.createCell(11).setCellValue(rs.getInt("thoi_gian_bao_hanh"));
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
}
