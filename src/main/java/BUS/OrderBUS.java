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



    public boolean exportToExcel(String filePath) {
    try (Connection conn = DatabaseConnection.getConnection();
         Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("DanhSachHoaDon");
        
        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ma_hoa_don", "ma_nhan_vien", "ma_khach_hang", "tong_tien", "ngay_xuat", "is_deleted", "tong_loi_nhuan"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lấy dữ liệu từ database
        String sql = "SELECT * FROM hoa_don";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("ma_hoa_don"));
            row.createCell(1).setCellValue(rs.getString("ma_nhan_vien"));
            row.createCell(2).setCellValue(rs.getString("ma_khach_hang"));
            row.createCell(3).setCellValue(rs.getInt("tong_tien"));
            row.createCell(4).setCellValue(rs.getString("ngay_xuat"));
            row.createCell(5).setCellValue(rs.getInt("is_deleted"));
            row.createCell(6).setCellValue(rs.getInt("tong_loi_nhuan"));
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