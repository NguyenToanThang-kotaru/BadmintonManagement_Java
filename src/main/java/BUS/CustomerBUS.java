package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Danh sách khách hàng");

    try {
        Row headerRow = sheet.createRow(0);
        String[] columns = {"STT", "Mã KH", "Họ Tên", "SĐT"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        List<CustomerDTO> customers = getAllCustomer();
        int rowNum = 1;
        for (CustomerDTO ctm : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(ctm.getcustomerID());
            row.createCell(2).setCellValue(ctm.getFullName());
            row.createCell(3).setCellValue(ctm.getPhone());
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