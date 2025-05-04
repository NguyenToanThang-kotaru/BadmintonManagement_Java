package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

import org.apache.poi.ss.usermodel.Row;

public class CustomerBUS {

    public List<CustomerDTO> getAllCustomer() {
        return CustomerDAO.getAllCustomer();
    }

    public void updateCustomer(CustomerDTO customer) {
        CustomerDAO dao = new CustomerDAO();
        dao.updateCustomer(customer);
    }

    public CustomerDTO getCustomerByPhone(String phone) {
        return CustomerDAO.getCustomerByPhone(phone);
    }
    
    public String getNextCustomerID() {
        return CustomerDAO.getNextCustomerID();
    }
    
    public CustomerDTO getCustomerByID(String id) {
        return CustomerDAO.getCustomer(id);
    }
    
    public void addCustomer(CustomerDTO customer) {
        CustomerDAO.addCustomer(customer);
    }
    
    public boolean deleteCustomer(String orderID) {
        CustomerDAO customer = new CustomerDAO();
        return customer.deleteCustomer(orderID);
    }
    
    public List<CustomerDTO> searchCustomer(String keyword) {
        return CustomerDAO.searchCustomer(keyword);
    }
    
   public boolean exportToExcel(String filePath) {
    try (Connection conn = DatabaseConnection.getConnection();
         Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachKhachHang");
        
        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ma_khach_hang", "ten_khach_hang", "so_dien_thoai", "is_deleted"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lấy dữ liệu từ database
        String sql = "SELECT * FROM khach_hang";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("ma_khach_hang"));
            row.createCell(1).setCellValue(rs.getString("ten_khach_hang"));
            row.createCell(2).setCellValue(rs.getString("so_dien_thoai"));
            row.createCell(3).setCellValue(rs.getInt("is_deleted"));
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
