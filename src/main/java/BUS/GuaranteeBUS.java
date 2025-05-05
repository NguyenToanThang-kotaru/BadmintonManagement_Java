/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.GuaranteeDAO;
import DTO.GuaranteeDTO;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

/**
 *
 * @author ADMIN
 */
public class GuaranteeBUS {

    public static GuaranteeDTO getGuarantee(String BaohanhID) {
        GuaranteeDAO dao = new GuaranteeDAO();
        return dao.getGuarantee(BaohanhID);
    }

    public static ArrayList<GuaranteeDTO> getAllGuarantee() {
        return GuaranteeDAO.getAllGuarantee();
    }

    public static void updateGuarantee(GuaranteeDTO guarantee) {
        GuaranteeDAO dao = new GuaranteeDAO();
        dao.updateGuarantee(guarantee);
    }

    public static ArrayList<GuaranteeDTO> searchGuarantees(String keyword) {
        return GuaranteeDAO.searchGuarantees(keyword);
    }
    
     public static Boolean addGuarantee(String ma_serial) {
         return GuaranteeDAO.addGuarantee(ma_serial);
     }

     
     public boolean exportToExcel(String filePath) {
    try (Connection conn = DatabaseConnection.getConnection();
         Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachBaoHanh");
        
        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ma_bao_hanh", "ma_serial", "ly_do_bao_hanh", "thoi_gian_bao_hanh", "trang_thai", "is_deleted"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lấy dữ liệu từ database
        String sql = "SELECT * FROM bao_hanh";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("ma_bao_hanh"));
            row.createCell(1).setCellValue(rs.getString("ma_serial"));
            row.createCell(2).setCellValue(rs.getString("ly_do_bao_hanh"));
            row.createCell(3).setCellValue(rs.getInt("thoi_gian_bao_hanh"));
            row.createCell(4).setCellValue(rs.getString("trang_thai"));
            row.createCell(5).setCellValue(rs.getInt("is_deleted"));
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
