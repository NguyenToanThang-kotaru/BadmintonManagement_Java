package GUI;

import BUS.OrderBUS;
import BUS.StatistiscBUS;
import DTO.OrderDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class GUI_Revenue_Statistics extends JPanel {

    private OrderBUS OrderBUS = new OrderBUS();
    private JPanel topPanel, midPanel, botPanel;
    private CustomTable statsTable;
    private CustomButton filterButton, exportButton;
    private DefaultTableModel tableModel;
    private JLabel totalRevenueLabel;
    private JLabel totalProfitLabel;
    private JLabel invoiceCountLabel;

    public GUI_Revenue_Statistics() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG (Bộ lọc) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        // Combobox lọc thời gian
        UtilDateModel fromModel = new UtilDateModel();
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromModel);
        JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanel);

        UtilDateModel toModel = new UtilDateModel();
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toModel);
        JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanel);
        fromDatePicker.setPreferredSize(new Dimension(150, 30));
        toDatePicker.setPreferredSize(new Dimension(150, 30));
        // Nút lọc
        filterButton = new CustomButton("Áp dụng");
        filterButton.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(fromDatePicker);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(toDatePicker);
        filterPanel.add(filterButton);

        topPanel.add(filterPanel, BorderLayout.CENTER);

        // Nút xuất báo cáo
        exportButton = new CustomButton("Xuất báo cáo");
        exportButton.setPreferredSize(new Dimension(150, 30));
        topPanel.add(exportButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ THỐNG KÊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã HĐ", "Mã NV", "Mã KH", "Tổng Tiền", "Ngày Xuất", "Tổng Lợi Nhuận"};
        // Sử dụng CustomTable thay vì JTable thông thường
        statsTable = new CustomTable(columnNames);
        tableModel = statsTable.getTableModel();
        JScrollPane scrollPane = new CustomScrollPane(statsTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL TỔNG HỢP ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Tổng hợp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin tổng hợp
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tổng doanh thu:"), gbc);
        gbc.gridx = 1;
        totalRevenueLabel = new JLabel("0 VND");
        botPanel.add(totalRevenueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tổng lợi nhuận:"), gbc);
        gbc.gridx = 1;
        totalProfitLabel = new JLabel("0 VND");
        botPanel.add(totalProfitLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Số hóa đơn:"), gbc);
        gbc.gridx = 1;
        invoiceCountLabel = new JLabel("0");
        botPanel.add(invoiceCountLabel, gbc);

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Load dữ liệu mẫu (thay bằng dữ liệu thực từ BUS)
        loadOrder();

//        fromDatePicker.addActionListener(e -> {
//            Date fromDate = (Date) fromDatePicker.getModel().getValue();
//            Date toDate = (Date) toDatePicker.getModel().getValue();
//            if (fromDate != null && toDate != null) {
//                // Gọi phương thức lọc lại dữ liệu với ngày mới
//                filterDataByDateRange(fromDate, toDate);
//            }
//        });
//
//        // Lắng nghe sự kiện thay đổi ngày từ picker "Đến ngày"
//        toDatePicker.addActionListener(e -> {
//            Date fromDate = (Date) fromDatePicker.getModel().getValue();
//            Date toDate = (Date) toDatePicker.getModel().getValue();
//            if (fromDate != null && toDate != null) {
//                // Gọi phương thức lọc lại dữ liệu với ngày mới
//                filterDataByDateRange(fromDate, toDate);
//            }
//        });

    }
//
//    private void filterDataByDateRange(Date fromDate, Date toDate) {
//        // Chuyển đổi đối tượng Date thành chuỗi hoặc định dạng phù hợp để lọc dữ liệu
//        String fromDateString = new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
//        String toDateString = new SimpleDateFormat("yyyy-MM-dd").format(toDate);
//
//        // Gọi lại phương thức loadOrder hoặc viết logic lọc mới theo khoảng thời gian
//        ArrayList<OrderDTO> filteredOrders = StatistiscBUS.getOrdersByDateRange(fromDateString, toDateString);
//        tableModel.setRowCount(0); // Xóa bảng trước khi thêm dữ liệu mới
//        for (OrderDTO order : filteredOrders) {
//            tableModel.addRow(new Object[]{
//                order.getorderID(),
//                order.getemployeeID(),
//                order.getcustomerID(),
//                order.gettotalmoney(),
//                order.getissuedate(),
//                order.gettotalprofit()
//            });
//        }
//
//        // Cập nhật tổng doanh thu và lợi nhuận
//        int count = filteredOrders.size();
//        String totalRevenue = StatistiscBUS.tongDoanhThu(filteredOrders);
//        String totalProfit = StatistiscBUS.tongLoiNhuan(filteredOrders);
//        updateSummary(count, totalRevenue, totalProfit);
//    }

    public void loadOrder() {
        ArrayList<OrderDTO> order = OrderBUS.getAllOrder();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (OrderDTO odr : order) {
            tableModel.addRow(new Object[]{odr.getorderID(), odr.getemployeeID(), odr.getcustomerID(), odr.gettotalmoney(), odr.getissuedate(), odr.gettotalprofit()});
        }
        int count = order.size();
        String totalRevenue = StatistiscBUS.tongDoanhThu(order);
        String totalProfit = StatistiscBUS.tongLoiNhuan(order);
        updateSummary(count, totalRevenue, totalProfit);

    }

    private void updateSummary(int invoiceCount, String totalRevenue, String totalProfit) {
        totalRevenueLabel.setText(totalRevenue);
        totalProfitLabel.setText(totalProfit);
        invoiceCountLabel.setText(String.valueOf(invoiceCount));
    }
}
