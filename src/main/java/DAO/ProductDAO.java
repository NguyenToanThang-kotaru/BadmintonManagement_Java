package DAO;

import DTO.ProductDTO;

import Connection.DatabaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Lớp này dùng để kết nối database và lấy dữ liệu sản phẩm
public class ProductDAO {

    public static Boolean addProduct(ProductDTO product) {
        String findMaLoaiSQL = "SELECT ma_loai FROM loai WHERE ten_loai = ?";
        String findMaNCCSQL = "SELECT ma_nha_cung_cap FROM nha_cung_cap WHERE ten_nha_cung_cap = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement findMaLoaiStmt = conn.prepareStatement(findMaLoaiSQL); PreparedStatement findMaNCCStmt = conn.prepareStatement(findMaNCCSQL)) {

            findMaLoaiStmt.setString(1, product.getTL()); // TL là tên loại
            ResultSet rs = findMaLoaiStmt.executeQuery();
            String maLoai = null;

            findMaNCCStmt.setString(1, product.gettenNCC()); // tenNCC là tên nhà cung cấp
            ResultSet rs2 = findMaNCCStmt.executeQuery();
            String maNCC = null;

            if (rs.next()) {
                maLoai = rs.getString("ma_loai");
            } else {
                System.out.println("Không tìm thấy mã loại cho tên loại: " + product.getTL());
                return false; // Dừng lại nếu không tìm thấy mã loại
            }

            if (rs2.next()) {
                maNCC = rs2.getString("ma_nha_cung_cap");
            } else {
                System.out.println("Không tìm thấy mã nhà cung cấp cho tên nhà cung cấp: " + product.gettenNCC());
                return false; // Dừng lại nếu không tìm thấy mã NCC
            }

            //Kiểm tra sản phẩm trùng tên đã bị xóa mềm ớ ớ á á
            String checkDeletedSQL = "SELECT ma_san_pham FROM san_pham WHERE ten_san_pham = ? AND is_deleted = 1";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkDeletedSQL)) {
                checkStmt.setString(1, product.getProductName());
                ResultSet checkRS = checkStmt.executeQuery();
                if (checkRS.next()) {
                    String existingID = checkRS.getString("ma_san_pham");

                    // Lật cờ is_deleted thành 0
                    String restoreSQL = "UPDATE san_pham SET is_deleted = 0 WHERE ma_san_pham = ?";
                    try (PreparedStatement restoreStmt = conn.prepareStatement(restoreSQL)) {
                        restoreStmt.setString(1, existingID);
                        restoreStmt.executeUpdate();
                    }

                    System.out.println("Khôi phục sản phẩm đã bị xóa mềm với ID: " + existingID);
                    return true;
                }
            }

            // Tiếp tục thêm sản phẩm
            String sql = "INSERT INTO san_pham (ma_san_pham, ten_san_pham, gia, so_luong, ma_nha_cung_cap, thong_so_ki_thuat, ma_loai, hinh_anh, gia_goc, khuyen_mai, is_deleted, thoi_gian_bao_hanh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                String newID = generateNewProductID(); // Tạo ID mới

                stmt.setString(1, newID);
                stmt.setString(2, product.getProductName());
                stmt.setDouble(3, Double.parseDouble(product.getGia())); // Chuyển String thành Double
                stmt.setInt(4, Integer.parseInt(product.getSoluong())); // Chuyển String thành Int
                stmt.setString(5, maNCC);
                stmt.setString(6, product.getTSKT());
                stmt.setString(7, maLoai);
                stmt.setString(8, product.getAnh());
                stmt.setString(9, product.getgiaGoc());
                stmt.setString(10, product.getkhuyenMai());
                stmt.setInt(11, 0); // Gán mặc định là 0
                stmt.setString(12, product.getTGBH());
                stmt.executeUpdate();
                System.out.println("Thêm sản phẩm thành công với ID: " + newID);
                return true;

            } catch (SQLException e) {
                System.out.println("Lỗi thêm sản phẩm: " + e.getMessage());
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn mã loại: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deleteProduct(String productID) {
        String query = "UPDATE san_pham SET is_deleted = 1 WHERE ma_san_pham = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            stmt.executeUpdate();
            System.out.println("Xóa sản phẩm thành công");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String generateNewProductID() {
        String query = "SELECT ma_san_pham FROM san_pham ORDER BY ma_san_pham DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("ma_san_pham"); // Ví dụ: "SP005"

                int number = Integer.parseInt(lastID.substring(2));

                // Tạo ID mới với định dạng SPXXX
                return String.format("SP%03d", number + 1); // Ví dụ: "SP006"
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã sản phẩm mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "SP001"; // Nếu không có sản phẩm nào, bắt đầu từ "SP001"
    }

    // Lấy thông tin của một sản phẩm
    public static ProductDTO getProduct(String ProductID) {
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, sp.so_luong, sp.ma_nha_cung_cap, "
                + "sp.thong_so_ki_thuat, sp.ma_loai, lsp.ten_loai, sp.hinh_anh, ncc.ten_nha_cung_cap, sp.gia_goc, sp.khuyen_mai, sp.thoi_gian_bao_hanh "
                + "FROM san_pham sp "
                + "JOIN loai lsp ON sp.ma_loai = lsp.ma_loai "
                + "LEFT JOIN nha_cung_cap ncc ON sp.ma_nha_cung_cap = ncc.ma_nha_cung_cap "
                + "WHERE sp.ma_san_pham = ? AND sp.is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ProductID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String supplierName = rs.getString("ten_nha_cung_cap");
                    if (supplierName == null) {
                        supplierName = "Nhà cung cấp đã xóa";
                    }
                    return new ProductDTO(
                            rs.getString("ma_san_pham"),
                            rs.getString("ten_san_pham"),
                            rs.getString("gia"),
                            rs.getString("so_luong"),
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("thong_so_ki_thuat"),
                            rs.getString("ma_loai"),
                            rs.getString("ten_loai"),
                            rs.getString("hinh_anh"),
                            supplierName,
                            rs.getString("gia_goc"),
                            rs.getString("khuyen_mai"),
                            rs.getString("thoi_gian_bao_hanh")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categoryList = new ArrayList<>();
        String query = "SELECT ten_loai FROM loai";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categoryList.add(rs.getString("ten_loai"));  // Lưu tên loại vào danh sách
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return categoryList;
    }
//
//    public static ArrayList<String> getAllNCCNames() {
//        ArrayList<String> NCCList = new ArrayList<>();
//        String query = "SELECT ten_nha_cung_cap FROM nha_cung_cap WHERE is_deleted = 0";
//
//        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                NCCList.add(rs.getString("ten_nha_cung_cap"));  // Lưu tên nhà cung cấp vào danh sách
//            }
//        } catch (SQLException e) {
//            System.out.println("Lỗi lấy danh sách nhà cung cấp: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return NCCList;
//    }

    // Lấy danh sách tất cả sản phẩm
    public static ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> products = new ArrayList<>();
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, sp.so_luong, sp.ma_nha_cung_cap, "
                + "sp.thong_so_ki_thuat, sp.ma_loai, lsp.ten_loai, sp.hinh_anh, ncc.ten_nha_cung_cap, sp.gia_goc, sp.khuyen_mai, sp.thoi_gian_bao_hanh "
                + "FROM san_pham sp "
                + "JOIN loai lsp ON sp.ma_loai = lsp.ma_loai "
                + "LEFT JOIN nha_cung_cap ncc ON sp.ma_nha_cung_cap = ncc.ma_nha_cung_cap "
                + "WHERE sp.is_deleted = 0 ORDER BY sp.ma_san_pham ASC"; // Chỉ lọc sản phẩm chưa bị xóa
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String supplierName = rs.getString("ten_nha_cung_cap");
                if (supplierName == null) {
                    supplierName = "Nhà cung cấp đã xóa"; // Giá trị mặc định
                }
                products.add(new ProductDTO(
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        rs.getString("gia"),
                        rs.getString("so_luong"),
                        rs.getString("ma_nha_cung_cap"),
                        rs.getString("thong_so_ki_thuat"),
                        rs.getString("ma_loai"),
                        rs.getString("ten_loai"),
                        rs.getString("hinh_anh"),
                        supplierName,
                        rs.getString("gia_goc"),
                        rs.getString("khuyen_mai"),
                        rs.getString("thoi_gian_bao_hanh")
                ));
            }
//            System.out.println("Lấy danh sách sản phẩm thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Cập nhật thông tin sản phẩm
    public static void updateProduct(ProductDTO product) {
        String findMaLoaiSQL = "SELECT ma_loai FROM loai WHERE ten_loai = ?";
        String findMaNCCSQL = "SELECT ma_nha_cung_cap FROM nha_cung_cap WHERE ten_nha_cung_cap = ?";
        String updateProductSQL = "UPDATE san_pham SET ten_san_pham = ?, gia = ?, so_luong = ?, ma_nha_cung_cap = ?, thong_so_ki_thuat = ?, ma_loai = ?, hinh_anh = ?, gia_goc = ?, khuyen_mai = ?, thoi_gian_bao_hanh = ? WHERE ma_san_pham = ? AND is_deleted = 0";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement findMaLoaiStmt = conn.prepareStatement(findMaLoaiSQL); PreparedStatement findMaNCCStmt = conn.prepareStatement(findMaNCCSQL); PreparedStatement updateProductStmt = conn.prepareStatement(updateProductSQL)) {

            // Tìm ma_loai từ ten_loai
            findMaLoaiStmt.setString(1, product.getTL());
            ResultSet rs = findMaLoaiStmt.executeQuery();
            String maLoai = null;

            findMaNCCStmt.setString(1, product.gettenNCC());
            ResultSet rs2 = findMaNCCStmt.executeQuery();
            String maNCC = null;

            if (rs.next()) {
                maLoai = rs.getString("ma_loai");  // Lấy ma_loai dưới dạng String
            } else {
                System.out.println("Không tìm thấy mã loại cho tên loại: " + product.getTL());
                return; // Không tiếp tục cập nhật nếu không tìm thấy
            }

            if (rs2.next()) {
                maNCC = rs2.getString("ma_nha_cung_cap");
            } else {
                System.out.println("Không tìm thấy mã NCC cho tên NCC: " + product.gettenNCC());
                return; // Không tiếp tục cập nhật nếu không tìm thấy
            }

            updateProductStmt.setString(1, product.getProductName());
            updateProductStmt.setDouble(2, Double.parseDouble(product.getGia())); // Chuyển String thành Double
            updateProductStmt.setInt(3, Integer.parseInt(product.getSoluong())); // Chuyển String thành Int
            updateProductStmt.setString(4, maNCC);
            updateProductStmt.setString(5, product.getTSKT());
            updateProductStmt.setString(6, maLoai); // Cập nhật ma_loai tìm được
            updateProductStmt.setString(7, product.getAnh());
            updateProductStmt.setString(8, product.getgiaGoc());
            updateProductStmt.setString(9, product.getkhuyenMai());
            updateProductStmt.setString(10, product.getTGBH());
            updateProductStmt.setString(11, product.getProductID());

            int rowsUpdated = updateProductStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật sản phẩm thành công.");
            } else {
                System.out.println("Không có sản phẩm nào được cập nhật. Kiểm tra lại ID!");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lấy đường dẫn ảnh sản phẩm
    public static String getProductImage(String productID) {
        String imagePath = null;
        String query = "SELECT hinh_anh FROM san_pham WHERE ma_san_pham = ? AND is_deleted = 0";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imagePath = rs.getString("hinh_anh"); // Lấy tên file ảnh
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public boolean updateProductQuantity(String productId, int quantity) {
        String query = "UPDATE san_pham SET so_luong = so_luong + ? WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            System.out.println(quantity);
            stmt.setString(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update product quantity: " + e.getMessage());
            return false;
        }
    }

    public boolean generateSerials(String productId, int quantity) {
        String lastSerialQuery = "SELECT ma_serial FROM danh_sach_san_pham ORDER BY ma_serial DESC LIMIT 1";
        String insertSerialQuery = "INSERT INTO danh_sach_san_pham (ma_serial, ma_san_pham, is_deleted) VALUES (?, ?, 0)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Lấy mã serial cuối cùng
            int startNumber = 1;
            try (PreparedStatement lastSerialStmt = conn.prepareStatement(lastSerialQuery); ResultSet rs = lastSerialStmt.executeQuery()) {
                if (rs.next()) {
                    String lastSerial = rs.getString("ma_serial");
                    startNumber = Integer.parseInt(lastSerial.substring(2)) + 1;
                }
            }

            // Tạo và lưu các mã serial mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSerialQuery)) {
                for (int i = 0; i < quantity; i++) {
                    String newSerial = String.format("SE%03d", startNumber + i);
                    insertStmt.setString(1, newSerial);
                    insertStmt.setString(2, productId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }

    public static List<String> getAvailableSerials(String maSanPham, int soLuong) {
        List<String> serials = new ArrayList<>();
        String query = "SELECT ma_serial FROM danh_sach_san_pham WHERE ma_san_pham = ? AND is_deleted = 0 ORDER BY ma_serial ASC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maSanPham);
            stmt.setInt(2, soLuong);
            System.out.print(soLuong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                serials.add(rs.getString("ma_serial"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serials;
    }

    public static void markSerialsAsUsed(List<String> serials) {
        String query = "UPDATE danh_sach_san_pham SET is_deleted = 1 WHERE ma_serial = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            for (String serial : serials) {
                stmt.setString(1, serial);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateStockAfterSale(String productId, int quantity) {
        String query = "UPDATE san_pham SET so_luong = so_luong - ? WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static ArrayList<ProductDTO> searchProducts(String keyword) {
        ArrayList<ProductDTO> products = new ArrayList<>();

        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, sp.so_luong, "
                + "sp.ma_nha_cung_cap, sp.thong_so_ki_thuat, sp.ma_loai, "
                + "lsp.ten_loai, sp.hinh_anh, ncc.ten_nha_cung_cap, sp.gia_goc, sp.khuyen_mai, sp.thoi_gian_bao_hanh "
                + "FROM san_pham sp "
                + "JOIN loai lsp ON sp.ma_loai = lsp.ma_loai "
                + "JOIN nha_cung_cap ncc ON sp.ma_nha_cung_cap = ncc.ma_nha_cung_cap "
                + "WHERE sp.is_deleted = 0 AND "
                + "(sp.ma_san_pham LIKE ? OR sp.ten_san_pham LIKE ? OR lsp.ten_loai LIKE ? OR ncc.ten_nha_cung_cap LIKE ?)"
                + "ORDER BY sp.ma_san_pham ASC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new ProductDTO(
                            rs.getString("ma_san_pham"),
                            rs.getString("ten_san_pham"),
                            rs.getString("gia"),
                            rs.getString("so_luong"),
                            rs.getString("ma_nha_cung_cap"),
                            rs.getString("thong_so_ki_thuat"),
                            rs.getString("ma_loai"),
                            rs.getString("ten_loai"),
                            rs.getString("hinh_anh"),
                            rs.getString("ten_nha_cung_cap"),
                            rs.getString("gia_goc"),
                            rs.getString("khuyen_mai"),
                            rs.getString("thoi_gian_bao_hanh")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public static ArrayList<String> getSerialsForProduct(String productID) {
        ArrayList<String> serials = new ArrayList<>();
        String query = "SELECT ma_serial FROM danh_sach_san_pham WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    serials.add(rs.getString("ma_serial"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách serial: " + e.getMessage());
            e.printStackTrace();
        }
        return serials;
    }

    public boolean increaseStock(String productId, int quantity) {
        String query = "UPDATE san_pham SET so_luong = so_luong + ? WHERE ma_san_pham = ? AND is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi tăng số lượng: " + e.getMessage());
            return false;
        }
    }

    // Đánh dấu serial là chưa sử dụng
    public void unmarkSerialsAsUsed(List<String> serials) {
        String query = "UPDATE danh_sach_san_pham SET is_deleted = 0 WHERE ma_serial = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            for (String serial : serials) {
                stmt.setString(1, serial);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Lỗi mở lại serial: " + e.getMessage());
        }
    }

    public static boolean importProductsFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis);
             Connection conn = DatabaseConnection.getConnection()) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            conn.setAutoCommit(false);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String maSanPham = dataFormatter.formatCellValue(row.getCell(0)).trim();
                String tenSanPham = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String gia = dataFormatter.formatCellValue(row.getCell(2)).trim();
                String soLuong = dataFormatter.formatCellValue(row.getCell(3)).trim();
                String maNhaCungCap = dataFormatter.formatCellValue(row.getCell(4)).trim();
                String thongSoKyThuat = dataFormatter.formatCellValue(row.getCell(5)).trim();
                String maLoai = dataFormatter.formatCellValue(row.getCell(6)).trim();
                String hinhAnh = dataFormatter.formatCellValue(row.getCell(7)).trim();
                String isDeleted = dataFormatter.formatCellValue(row.getCell(8)).trim();
                String giaGoc = dataFormatter.formatCellValue(row.getCell(9)).trim();
                String khuyenMai = dataFormatter.formatCellValue(row.getCell(10)).trim();
                String thoiGianBaoHanh = dataFormatter.formatCellValue(row.getCell(11)).trim();

                // Kiểm tra dữ liệu bắt buộc
                if (tenSanPham.isEmpty() || gia.isEmpty() || soLuong.isEmpty() || maNhaCungCap.isEmpty() || maLoai.isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }

                // Kiểm tra mã loại và mã nhà cung cấp tồn tại
                String checkLoaiSQL = "SELECT ma_loai FROM loai WHERE ma_loai = ? AND is_deleted = 0";
                String checkNCCSQL = "SELECT ma_nha_cung_cap FROM nha_cung_cap WHERE ma_nha_cung_cap = ? AND is_deleted = 0";
                try (PreparedStatement checkLoaiStmt = conn.prepareStatement(checkLoaiSQL);
                     PreparedStatement checkNCCStmt = conn.prepareStatement(checkNCCSQL)) {
                    checkLoaiStmt.setString(1, maLoai);
                    ResultSet rsLoai = checkLoaiStmt.executeQuery();
                    if (!rsLoai.next()) {
                        System.out.println("Mã loại " + maLoai + " không tồn tại hoặc đã bị xóa, bỏ qua dòng " + (rowIndex + 1));
                        continue;
                    }

                    checkNCCStmt.setString(1, maNhaCungCap);
                    ResultSet rsNCC = checkNCCStmt.executeQuery();
                    if (!rsNCC.next()) {
                        System.out.println("Mã nhà cung cấp " + maNhaCungCap + " không tồn tại hoặc đã bị xóa, bỏ qua dòng " + (rowIndex + 1));
                        continue;
                    }
                }

                // Tạo mã sản phẩm mới nếu maSanPham trống
                if (maSanPham.isEmpty()) {
                    String lastIDQuery = "SELECT ma_san_pham FROM san_pham ORDER BY ma_san_pham DESC LIMIT 1";
                    try (PreparedStatement lastIDStmt = conn.prepareStatement(lastIDQuery);
                         ResultSet rs = lastIDStmt.executeQuery()) {
                        if (rs.next()) {
                            String lastID = rs.getString("ma_san_pham");
                            int number = Integer.parseInt(lastID.substring(2)) + 1;
                            maSanPham = String.format("SP%03d", number);
                        } else {
                            maSanPham = "SP001";
                        }
                    }
                }

                // Kiểm tra sản phẩm đã tồn tại
                String checkExistSQL = "SELECT ma_san_pham, is_deleted FROM san_pham WHERE ma_san_pham = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkExistSQL)) {
                    checkStmt.setString(1, maSanPham);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int existingIsDeleted = rs.getInt("is_deleted");
                        if (existingIsDeleted == 0) {
                            System.out.println("Sản phẩm " + maSanPham + " đã tồn tại và chưa bị xóa, bỏ qua dòng " + (rowIndex + 1));
                            continue;
                        } else {
                            // Cập nhật sản phẩm đã bị xóa mềm
                            String updateSQL = "UPDATE san_pham SET ten_san_pham = ?, gia = ?, so_luong = ?, ma_nha_cung_cap = ?, thong_so_ki_thuat = ?, ma_loai = ?, hinh_anh = ?, is_deleted = ?, gia_goc = ?, khuyen_mai = ?, thoi_gian_bao_hanh = ? WHERE ma_san_pham = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                                updateStmt.setString(1, tenSanPham);
                                updateStmt.setDouble(2, Double.parseDouble(gia));
                                updateStmt.setInt(3, Integer.parseInt(soLuong));
                                updateStmt.setString(4, maNhaCungCap);
                                updateStmt.setString(5, thongSoKyThuat);
                                updateStmt.setString(6, maLoai);
                                updateStmt.setString(7, hinhAnh);
                                updateStmt.setInt(8, Integer.parseInt(isDeleted));
                                updateStmt.setDouble(9, Double.parseDouble(giaGoc));
                                updateStmt.setString(10, khuyenMai);
                                updateStmt.setInt(11, Integer.parseInt(thoiGianBaoHanh));
                                updateStmt.setString(12, maSanPham);
                                updateStmt.executeUpdate();
                            }
                            continue;
                        }
                    }
                }

                // Thêm sản phẩm mới
                String insertSQL = "INSERT INTO san_pham (ma_san_pham, ten_san_pham, gia, so_luong, ma_nha_cung_cap, thong_so_ki_thuat, ma_loai, hinh_anh, is_deleted, gia_goc, khuyen_mai, thoi_gian_bao_hanh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    insertStmt.setString(1, maSanPham);
                    insertStmt.setString(2, tenSanPham);
                    insertStmt.setDouble(3, Double.parseDouble(gia));
                    insertStmt.setInt(4, Integer.parseInt(soLuong));
                    insertStmt.setString(5, maNhaCungCap);
                    insertStmt.setString(6, thongSoKyThuat);
                    insertStmt.setString(7, maLoai);
                    insertStmt.setString(8, hinhAnh);
                    insertStmt.setInt(9, Integer.parseInt(isDeleted));
                    insertStmt.setDouble(10, Double.parseDouble(giaGoc));
                    insertStmt.setString(11, khuyenMai);
                    insertStmt.setInt(12, Integer.parseInt(thoiGianBaoHanh));
                    insertStmt.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }
}
