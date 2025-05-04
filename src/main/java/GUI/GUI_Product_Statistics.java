package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI_Product_Statistics extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private CustomTable productStatsTable;
    private JComboBox<String> productFilterCombo;
    private CustomButton filterButton, exportButton;
    private JSpinner limitSpinner;

    public GUI_Product_Statistics() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        // Combobox lọc loại sản phẩm
        productFilterCombo = new CustomCombobox<>(new String[]{"Bán chạy", "Bán chậm", "Sắp hết hàng"});
        productFilterCombo.setPreferredSize(new Dimension(150, 30));

        filterButton = new CustomButton("Áp dụng");
        filterButton.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Lọc theo:"));
        filterPanel.add(productFilterCombo);
        filterPanel.add(filterButton);

        // Spinner để nhập số lượng cần hiển thị
//       JTextField limitSpinner = new JTextField(20);
//
//        filterPanel.add(new JLabel("Hiển thị:"));
//        filterPanel.add(limitSpinner);
//        filterPanel.add(new JLabel("sản phẩm"));
        
        topPanel.add(filterPanel, BorderLayout.CENTER);

        exportButton = new CustomButton("Xuất báo cáo");
        exportButton.setPreferredSize(new Dimension(150, 30));
        topPanel.add(exportButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Số lượng bán", "Tồn kho", "Doanh thu"};
        productStatsTable = new CustomTable(columnNames);

        JScrollPane scrollPane = new CustomScrollPane(productStatsTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL TỔNG HỢP ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Tổng hợp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tổng số sản phẩm
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tổng sản phẩm:"), gbc);
        gbc.gridx = 1;
        JLabel totalProductLabel = new JLabel("0");
        botPanel.add(totalProductLabel, gbc);

        // Tổng doanh thu
        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tổng doanh thu:"), gbc);
        gbc.gridx = 1;
        JLabel totalRevenueLabel = new JLabel("0 VND");
        botPanel.add(totalRevenueLabel, gbc);

        // Tổng số lượng đã bán
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tổng SL bán:"), gbc);
        gbc.gridx = 1;
        JLabel totalSoldLabel = new JLabel("0");
        botPanel.add(totalSoldLabel, gbc);

        // Thêm panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Load dữ liệu mẫu
        loadSampleData();
    }

    private void loadSampleData() {
        productStatsTable.clearTable();

        productStatsTable.addRow(new Object[]{"SP001", "Áo thun trắng", 120, 30, "24,000,000 VND"});
        productStatsTable.addRow(new Object[]{"SP002", "Quần jean xanh", 85, 40, "17,000,000 VND"});
        productStatsTable.addRow(new Object[]{"SP003", "Giày sneaker", 10, 90, "3,000,000 VND"});

        updateSummary(3, "44,000,000 VND", 215);
    }

    private void updateSummary(int totalProducts, String totalRevenue, int totalSold) {
        for (Component comp : botPanel.getComponents()) {
            if (comp instanceof JLabel label) {
                if (label.getText().equals("0")) {
                    label.setText(String.valueOf(totalProducts));
                    totalProducts = -1; // tránh cập nhật label khác
                } else if (label.getText().equals("0 VND")) {
                    label.setText(totalRevenue);
                } else if (label.getText().equals("0")) {
                    label.setText(String.valueOf(totalSold));
                }
            }
        }
    }
}
