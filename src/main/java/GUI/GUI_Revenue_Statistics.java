package GUI;

import BUS.OrderBUS;
import BUS.StatistiscBUS;
import DTO.OrderDTO;
import GUI.Utils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

public class GUI_Revenue_Statistics extends JPanel {

    private OrderBUS OrderBUS = new OrderBUS();
    private JPanel topPanel, midPanel, botPanel;
    private CustomTable statsTable;
    private CustomButton filterButton, exportButton;
    private DefaultTableModel tableModel;
    private JLabel totalRevenueLabel;
    private JLabel totalProfitLabel;
    private JLabel invoiceCountLabel;

    // Thay đổi: sử dụng JDateChooser
    private JDateChooser fromDateChooser, toDateChooser;

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

        // Sử dụng JDateChooser thay vì JDatePicker
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("yyyy-MM-dd");
        fromDateChooser.setPreferredSize(new Dimension(150, 30));

        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("yyyy-MM-dd");
        toDateChooser.setPreferredSize(new Dimension(150, 30));

        // Nút lọc
        filterButton = new CustomButton("Áp dụng");
        filterButton.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(toDateChooser);
        filterPanel.add(filterButton);

        topPanel.add(filterPanel, BorderLayout.CENTER);

        // Nút xuất báo cáo
        exportButton = new CustomButton("Reload");
        exportButton.setPreferredSize(new Dimension(150, 30));
        topPanel.add(exportButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ THỐNG KÊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã HĐ", "Mã NV", "Mã KH", "Tổng Tiền", "Ngày Xuất", "Tổng Lợi Nhuận"};
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

        // Load dữ liệu ban đầu
        loadOrder();

        // Lắng nghe sự kiện nút lọc
        filterButton.addActionListener(e -> {
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();
            Date currentDate = new Date();
            if (from == null || to == null) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu hoặc ngày kết thúc không được để trống");
                return;
            }
            if (from.after(to)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
                return;
            }
            if (from.after(currentDate) || to.after(currentDate)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu hoặc ngày kết thúc phải trước ngày hiện tại");
                return;
            }
            filterDataByDateRange(from, to);

        });
        exportButton.addActionListener(e -> {
            fromDateChooser.setDate(null);
            toDateChooser.setDate(null);
            loadOrder();
        });
    }

    private void filterDataByDateRange(Date fromDate, Date toDate) {
        String fromDateString = new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
        String toDateString = new SimpleDateFormat("yyyy-MM-dd").format(toDate);

        ArrayList<OrderDTO> filteredOrders = StatistiscBUS.getOrdersByDateRange(fromDateString, toDateString);
        tableModel.setRowCount(0);

        for (OrderDTO order : filteredOrders) {
            tableModel.addRow(new Object[]{
                order.getorderID(),
                order.getemployeeID(),
                order.getcustomerID(),
                order.gettotalmoney(),
                order.getissuedate(),
                order.gettotalprofit()
            });
        }

        int count = filteredOrders.size();
        String totalRevenue = StatistiscBUS.tongDoanhThu(filteredOrders);
        String totalProfit = StatistiscBUS.tongLoiNhuan(filteredOrders);
        updateSummary(count, totalRevenue, totalProfit);
    }

    public void loadOrder() {
        ArrayList<OrderDTO> order = OrderBUS.getAllOrderVerify();
        tableModel.setRowCount(0);
        for (OrderDTO odr : order) {
            tableModel.addRow(new Object[]{
                odr.getorderID(),
                odr.getemployeeID(),
                odr.getcustomerID(),
                Utils.formatCurrency(odr.gettotalmoney()),
                odr.getissuedate(),
                Utils.formatCurrency(odr.gettotalprofit())
            });
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
