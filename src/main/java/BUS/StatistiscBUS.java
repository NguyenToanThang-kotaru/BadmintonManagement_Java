package BUS;

import DAO.StatistiscDAO;
import DTO.CustomerDTO;
import DTO.OrderDTO;
import DTO.ProductDTO;
import GUI.Utils.DateOnlyUtils;
import com.mysql.cj.util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatistiscBUS {
    private StatistiscDAO statDAO = new StatistiscDAO();
    
    //Thống kê dựa trên khách hàng
    public ArrayList<CustomerDTO> getCustomerByDateRange(String fromDate, String toDate) {
        return statDAO.getCustomerByDateRange(fromDate, toDate);
    }
    
//    public static int getOrderByCustomer(CustomerDTO cus) {
//        return StatistiscDAO.getOrderByCustomer();
//    }

    //Thống kê dựa trên hóa đơn
    public static ArrayList<OrderDTO> getOrdersByDateRange(String fromDate, String toDate) {
        return StatistiscDAO.getOrdersByDateRange(fromDate, toDate);
    }

    public static String tinhTiLePhanTram(double phanTu, double tong) {
        if (tong == 0) {
            return "0%"; // Tránh chia cho 0
        }
        double tiLe = (phanTu / tong) * 100;
        return tiLe + "%";
    }

    public static String tongLoiNhuan(ArrayList<OrderDTO> danhSachHoaDon) {
        long totalProfit = 0;
        for (OrderDTO ord : danhSachHoaDon) {
            totalProfit += Double.parseDouble(ord.gettotalprofit());
        }
        return GUI.Utils.formatCurrencyLong(totalProfit);
    }

    public static String tongDoanhThu(ArrayList<OrderDTO> danhSachHoaDon) {
        long totalRevenue = 0;
        for (OrderDTO ord : danhSachHoaDon) {
            totalRevenue += Double.parseDouble(ord.gettotalmoney());
        }
        return GUI.Utils.formatCurrencyLong(totalRevenue);
    }
    
    public ArrayList<ProductDTO> getProductStatistics() {
        return statDAO.getProductStatistics();
    }
    
    public int getTotalQuantityByProductId(String productId) {
        return statDAO.getTotalQuantityByProductId(productId);
    }
    
    public double getTotalProfitByProductId(String productId) {
        return statDAO.getTotalProfitByProductId(productId);
    }
    public ArrayList<ProductDTO> filterProductForCate(ArrayList<ProductDTO> list,String cate) {
        return statDAO.filterProductForCate(list ,cate);
    }
}
