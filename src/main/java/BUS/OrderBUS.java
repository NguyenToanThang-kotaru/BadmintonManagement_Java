package BUS;

import DAO.OrderDAO;
import DAO.CustomerDAO;
import DTO.CustomerDTO;
import DTO.OrderDTO;
import java.util.ArrayList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

public class OrderBUS {

    private OrderDAO dao = new OrderDAO();
    private CustomerDAO customer = new CustomerDAO();
    
    public ArrayList<OrderDTO> getAllOrderVerify(){
        return dao.getAllOrderVerify();
    }
    
    public long getTotalSpentByCustomer(CustomerDTO cus){
        return dao.getTotalSpentByCustomer(cus);
    }
    
    public int getQuantityOrderForCus(CustomerDTO cus){
        return dao.getQuantityOrderForCus(cus);
    }
    
    public ArrayList<OrderDTO> getAllOrder() {
        return dao.getAllOrder();
    }

    public void updateOrder(OrderDTO order) {
        dao.updateOrder(order);
        dao.updateTotalProfit(order.getorderID());
    }

    public String getCustomerNameByID(String customerID) {
        return customer.getCustomerNameByID(customerID);
    }
    
    public boolean deleteOrder(String orderID) {
        return dao.deleteOrder(orderID);
    }
    
    public void addOrder(OrderDTO order) {
        if (order.gettotalprofit() == null || order.gettotalprofit().trim().isEmpty()) {
            order.settotalprofit("0");
        }
        dao.insertOrder(order);
    }
    
    public String getNextOrderID() {
        return new OrderDAO().getNextOrderID();
    }
    
    public List<OrderDTO> searchOrder(String keyword) {
        return OrderDAO.searchOrder(keyword);
    }
    
    public void getOrder(String mahd) {
        dao.getOrder(mahd);
    }

    public void rollbackCanceledOrder(String OrderID) {
        OrderDAO.restoreProductsFromCanceledOrder(OrderID);
    }
    
    public boolean exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

        try {
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã HĐ", "Mã NV", "Mã KH", "Tổng Tiền", "Ngày Xuất", "Tổng Lợi Nhuận", "Trạng Thái"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            List<OrderDTO> orders = getAllOrder();
            int rowNum = 1;
            for (OrderDTO o : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(o.getorderID());
                row.createCell(1).setCellValue(o.getemployeeID());
                row.createCell(2).setCellValue(o.getcustomerID());
                row.createCell(3).setCellValue(o.gettotalmoney());
                row.createCell(4).setCellValue(o.getissuedate());
                row.createCell(5).setCellValue(o.gettotalprofit());
                row.createCell(6).setCellValue(o.getis_deleted() ? "Đã hủy" : "Đã hoàn thành");
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