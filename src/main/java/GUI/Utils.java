package GUI;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public class DateOnlyUtils {

        public static LocalDate parseDate(String dateString) {
            dateString = dateString.trim();

            // Nếu chuỗi có dấu '/' thì kiểm tra số lượng thành phần
            if (dateString.contains("/")) {
                String[] parts = dateString.split("/");

                if (parts.length == 3) {
                    // dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    return LocalDate.parse(dateString, formatter);
                } else if (parts.length == 2) {
                    // MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                    return LocalDate.parse("01/" + dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    // Gán mặc định ngày = 01
                } else {
                    throw new RuntimeException("Định dạng ngày không hợp lệ: " + dateString);
                }
            } else {
                // yyyy
                if (dateString.length() == 4) {
                    // Mặc định tháng = 01, ngày = 01
                    return LocalDate.parse("01/01/" + dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    throw new RuntimeException("Định dạng năm không hợp lệ: " + dateString);
                }
            }
        }
    }

    public static String formatCurrency(int amount) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(amount);
    }

    public static String formatCurrencyLong(long amount) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(amount);
    }
//     public static String formatCurrencyDouble(double amount) {
//        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//        formatter.applyPattern("#,###");
//        return formatter.format(amount);
//    }

    public static String formatCurrency(String amountStr) {
        try {
            String cleanString = amountStr.replaceAll("[^0-9]", "");
            if (!cleanString.isEmpty()) {
                int amount = Integer.parseInt(cleanString);
                return formatCurrency(amount);
            }
            return "0";
        } catch (NumberFormatException e) {
            return amountStr;
        }
    }

    public static int parseCurrency(String formattedAmount) {
        try {
            return Integer.parseInt(formattedAmount.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
