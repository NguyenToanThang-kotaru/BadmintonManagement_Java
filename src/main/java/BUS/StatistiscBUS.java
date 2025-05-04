package BUS;

import DTO.OrderDTO;
import GUI.Utils.DateOnlyUtils;
import com.mysql.cj.util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatistiscBUS {

    public static ArrayList<OrderDTO> getOrdersByDateRange() {
        return null;
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
}
