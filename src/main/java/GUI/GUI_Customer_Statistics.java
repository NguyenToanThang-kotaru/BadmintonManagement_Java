package GUI;

import BUS.CustomerBUS;
import BUS.OrderBUS;
import BUS.StatistiscBUS;
import DTO.CustomerDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class GUI_Customer_Statistics extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private CustomTable customerStatsTable;
    private CustomButton filterButton, exportButton;
    private JDateChooser fromDateChooser, toDateChooser;
    private JLabel totalCustomerLabel, totalInvoiceLabel, totalRevenueLabel;
    private CustomerBUS customerBUS = new CustomerBUS();
    private OrderBUS orderBUS = new OrderBUS();
    private StatistiscBUS statBUS = new StatistiscBUS();
    private DefaultTableModel tableModel;

    public GUI_Customer_Statistics() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ===== PANEL TRÊN CÙNG =====
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        // JDateChooser lọc theo thời gian
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("yyyy-MM-dd");
        fromDateChooser.setPreferredSize(new Dimension(150, 30));

        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("yyyy-MM-dd");
        toDateChooser.setPreferredSize(new Dimension(150, 30));

        filterButton = new CustomButton("Áp dụng");
        filterButton.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(toDateChooser);
        filterPanel.add(filterButton);

        topPanel.add(filterPanel, BorderLayout.CENTER);

        exportButton = new CustomButton("Reload");
        exportButton.setPreferredSize(new Dimension(150, 30));
        topPanel.add(exportButton, BorderLayout.EAST);

        // ===== BẢNG HIỂN THỊ =====
        midPanel = new JPanel(new BorderLayout());
        midPanel.setPreferredSize(new Dimension(0, 780));
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã KH", "Tên khách hàng", "Số hóa đơn", "Tổng tiền đã mua"};
        customerStatsTable = new CustomTable(columnNames);
        tableModel = customerStatsTable.getTableModel();
        JScrollPane scrollPane = new CustomScrollPane(customerStatsTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL TỔNG HỢP =====
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Tổng hợp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tổng khách hàng:"), gbc);
        gbc.gridx = 1;
        totalCustomerLabel = new JLabel("0");
        botPanel.add(totalCustomerLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tổng số hóa đơn:"), gbc);
        gbc.gridx = 1;
        totalInvoiceLabel = new JLabel("0");
        botPanel.add(totalInvoiceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tổng doanh thu:"), gbc);
        gbc.gridx = 1;
        totalRevenueLabel = new JLabel("0 VND");
        botPanel.add(totalRevenueLabel, gbc);

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Load dữ liệu mẫu
        loadSampleData();

        // Sự kiện lọc
        filterButton.addActionListener(e -> {
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();
            System.out.println(from);
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
            loadSampleData();
        });
    }

    private void filterDataByDateRange(Date fromDate, Date toDate) {
        String fromDateString = new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
        String toDateString = new SimpleDateFormat("yyyy-MM-dd").format(toDate);
        // TODO: Gọi BUS xử lý thống kê trong khoảng thời gian
        ArrayList<CustomerDTO> customer = statBUS.getCustomerByDateRange(fromDateString, toDateString); // Tạm thời dùng lại dữ liệu mẫu
        tableModel.setRowCount(0);
        int index = 0;
        int totalInvoice = 0;
        long totalSpent = 0;
        for (CustomerDTO ctm : customer) {
            tableModel.addRow(new Object[]{ctm.getcustomerID(), ctm.getFullName(),
                orderBUS.getQuantityOrderForCus(ctm),
                Utils.formatCurrencyLong(orderBUS.getTotalSpentByCustomer(ctm))});
            totalInvoice += orderBUS.getQuantityOrderForCus(ctm);
            totalSpent += orderBUS.getTotalSpentByCustomer(ctm);
            index++;
        }

        updateSummary(index, totalInvoice, Utils.formatCurrencyLong(totalSpent));
    }

    private void loadSampleData() {
        customerStatsTable.clearTable();
        ArrayList<CustomerDTO> customer = customerBUS.getAllCustomer();
        tableModel.setRowCount(0);
        int index = 0;
        int totalInvoice = 0;
        long totalSpent = 0;
        for (CustomerDTO ctm : customer) {
            tableModel.addRow(new Object[]{ctm.getcustomerID(), ctm.getFullName(),
                orderBUS.getQuantityOrderForCus(ctm),
                Utils.formatCurrencyLong(orderBUS.getTotalSpentByCustomer(ctm))});
            totalInvoice += orderBUS.getQuantityOrderForCus(ctm);
            totalSpent += orderBUS.getTotalSpentByCustomer(ctm);
            index++;
        }

        updateSummary(index, totalInvoice, Utils.formatCurrencyLong(totalSpent));
    }

    private void updateSummary(int totalCustomers, int totalInvoices, String totalRevenue) {
        totalCustomerLabel.setText(String.valueOf(totalCustomers));
        totalInvoiceLabel.setText(String.valueOf(totalInvoices));

        totalRevenueLabel.setText(String.valueOf(totalRevenue));
    }
}
