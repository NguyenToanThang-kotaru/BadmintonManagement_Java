package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI_Customer_Statistics extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private CustomTable customerStatsTable;
    private JComboBox<String> customerFilterCombo;
    private JSpinner limitSpinner;
    private CustomButton filterButton, exportButton;

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

        // Combobox kiểu thống kê
        customerFilterCombo = new CustomCombobox<>(new String[]{"Mua nhiều nhất", "Mua ít nhất"});
        customerFilterCombo.setPreferredSize(new Dimension(150, 30));

        // Spinner chọn số lượng khách
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(5, 1, 100, 1);
        limitSpinner = new JSpinner(spinnerModel);
        limitSpinner.setPreferredSize(new Dimension(60, 30));

        filterButton = new CustomButton("Áp dụng");
        filterButton.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Thống kê:"));
        filterPanel.add(customerFilterCombo);
        filterPanel.add(new JLabel("Hiển thị:"));
        filterPanel.add(limitSpinner);
        filterPanel.add(new JLabel("khách hàng"));
        filterPanel.add(filterButton);

        topPanel.add(filterPanel, BorderLayout.CENTER);

        exportButton = new CustomButton("Xuất báo cáo");
        exportButton.setPreferredSize(new Dimension(150, 30));
        topPanel.add(exportButton, BorderLayout.EAST);

        // ===== BẢNG HIỂN THỊ =====
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã KH", "Tên khách hàng", "Số hóa đơn", "Tổng tiền đã mua"};
        customerStatsTable = new CustomTable(columnNames);

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
        JLabel totalCustomerLabel = new JLabel("0");
        botPanel.add(totalCustomerLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tổng số hóa đơn:"), gbc);
        gbc.gridx = 1;
        JLabel totalInvoiceLabel = new JLabel("0");
        botPanel.add(totalInvoiceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tổng doanh thu:"), gbc);
        gbc.gridx = 1;
        JLabel totalRevenueLabel = new JLabel("0 VND");
        botPanel.add(totalRevenueLabel, gbc);

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Load dữ liệu mẫu
        loadSampleData();
    }

    private void loadSampleData() {
        customerStatsTable.clearTable();

        customerStatsTable.addRow(new Object[]{"KH001", "Nguyễn Văn A", 12, "25,000,000 VND"});
        customerStatsTable.addRow(new Object[]{"KH002", "Trần Thị B", 8, "15,000,000 VND"});
        customerStatsTable.addRow(new Object[]{"KH003", "Lê Văn C", 3, "5,000,000 VND"});

        updateSummary(3, 23, "45,000,000 VND");
    }

    private void updateSummary(int totalCustomers, int totalInvoices, String totalRevenue) {
        for (Component comp : botPanel.getComponents()) {
            if (comp instanceof JLabel label) {
                switch (label.getText()) {
                    case "0" -> label.setText(String.valueOf(totalCustomers));
                    case "0 VND" -> label.setText(totalRevenue);
                }
            }
        }
    }
}
