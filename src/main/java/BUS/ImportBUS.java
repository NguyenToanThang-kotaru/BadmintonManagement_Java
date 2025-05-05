package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import Connection.DatabaseConnection;

public class ImportBUS {
    private final ImportDAO importDAO;
    public ImportBUS() {
        this.importDAO = new ImportDAO();
    }

    public List<ImportDTO> getAllImport() {
        return importDAO.getAllImport();
    }

    public boolean deleteImport(String importID) {
        return importDAO.deleteImportWithProductUpdate(importID);
    }

    public void updateImport(ImportDTO importDTO) {
        ImportDTO oldImport = importDAO.getImport(importDTO.getimportID());
        if (oldImport != null) {
            importDAO.updateImport(importDTO);
        }
    }

    public int calculateImportTotal(String importID) {
        String query = "SELECT so_luong, gia FROM chi_tiet_nhap_hang WHERE ma_nhap_hang = ?";
        int total = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quantity = rs.getInt("so_luong");
                    int price = rs.getInt("gia");
                    total += quantity * price;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    
    public boolean exportToExcel(String filePath) {
        try (Connection conn = DatabaseConnection.getConnection();
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DanhSachPhieuNhap");
            
            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ma_nhap_hang", "ma_nhan_vien", "tong_tien", "ngay_nhap", "is_deleted"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
    
            // Lấy dữ liệu từ database
            String sql = "SELECT * FROM nhap_hang";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getString("ma_nhap_hang"));
                row.createCell(1).setCellValue(rs.getString("ma_nhan_vien"));
                row.createCell(2).setCellValue(rs.getInt("tong_tien"));
                row.createCell(3).setCellValue(rs.getString("ngay_nhap"));
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
}