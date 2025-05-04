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
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Danh sách bảo hành");

    try {
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã BH", "Mã Serial", "Lý do bảo hành", "Thời gian bảo hành", "Trạng thái bảo hành"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        ArrayList<GuaranteeDTO> guarantees = getAllGuarantee();
        int rowNum = 1;
        for (GuaranteeDTO g : guarantees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(g.getBaohanhID());
            row.createCell(1).setCellValue(g.getSerialID());
            row.createCell(2).setCellValue(g.getLydo());
            row.createCell(3).setCellValue(g.getTGBH());
            row.createCell(4).setCellValue(g.gettrangthai());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            return true;
        }
    } catch (IOException ex) {
        ex.printStackTrace();
        return false;
    } finally {
        try {
            workbook.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
}
