package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Connection.DatabaseConnection;

public class CustomerBUS {

    public ArrayList<CustomerDTO> getAllCustomer() {
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
    
    public boolean deleteCustomer(String customerID) {
        CustomerDAO customer = new CustomerDAO();
        return customer.deleteCustomer(customerID);
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

    public boolean importCustomersFromExcel(File file) {
        List<CustomerDTO> customersToImport = new ArrayList<>();
        List<Integer> isDeletedList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                CustomerDTO customer = new CustomerDTO();

                // Đọc các cột từ Excel theo cấu trúc bảng khach_hang
                Cell tenKhachHangCell = row.getCell(1);
                Cell soDienThoaiCell = row.getCell(2);
                Cell isDeletedCell = row.getCell(3);

                // Gán giá trị cho CustomerDTO
                String fullName = tenKhachHangCell != null ? dataFormatter.formatCellValue(tenKhachHangCell).trim() : "";
                String phone = soDienThoaiCell != null ? dataFormatter.formatCellValue(soDienThoaiCell).trim() : "";
                customer.setFullName(fullName);
                customer.setPhone(phone);
                customer.setcustomerID(""); // Mã sẽ được sinh tự động trong DAO

                // Xử lý is_deleted
                int isDeleted = 0; // Mặc định là 0
                if (isDeletedCell != null) {
                    String isDeletedStr = dataFormatter.formatCellValue(isDeletedCell).trim();
                    try {
                        isDeleted = Integer.parseInt(isDeletedStr);
                        if (isDeleted != 0 && isDeleted != 1) {
                            isDeleted = 0; // Nếu giá trị không hợp lệ, đặt về 0
                            System.out.println("Giá trị is_deleted không hợp lệ tại dòng " + (rowIndex + 1) + ", đặt mặc định là 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Giá trị is_deleted không phải số tại dòng " + (rowIndex + 1) + ", đặt mặc định là 0.");
                    }
                }
                isDeletedList.add(isDeleted);

                // Kiểm tra dữ liệu bắt buộc
                if (fullName.isEmpty() || phone.isEmpty()) {
                    System.out.println("Dòng " + (rowIndex + 1) + " thiếu thông tin, bỏ qua.");
                    continue;
                }

                // Kiểm tra định dạng số điện thoại (bắt đầu bằng 0, đúng 10 chữ số)
                if (!phone.matches("^0\\d{9}$")) {
                    System.out.println("Số điện thoại " + phone + " không hợp lệ tại dòng " + (rowIndex + 1) + ", bỏ qua.");
                    continue;
                }

                // Kiểm tra trùng số điện thoại
                if (CustomerDAO.isPhoneNumberExists(phone)) {
                    System.out.println("Số điện thoại " + phone + " đã tồn tại, bỏ qua dòng " + (rowIndex + 1));
                    continue;
                }

                customersToImport.add(customer);
            }
            boolean success = CustomerDAO.importCustomers(customersToImport, isDeletedList);
            return success;
        } catch (Exception e) {
            System.err.println("Lỗi khi nhập Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}