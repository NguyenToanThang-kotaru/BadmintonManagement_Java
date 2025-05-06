package GUI;

import BUS.ProductBUS;
import DTO.ProductDTO;
import BUS.StatistiscBUS;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;

public class GUI_Product_Statistics extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private CustomTable productStatsTable;
    private JComboBox<String> productFilterCombo;
    private CustomButton filterButton, exportButton, resetButton;
    private DefaultTableModel tableModel;
    private ProductBUS productBUS = new ProductBUS();
    private JLabel totalProductLabel, totalRevenueLabel, totalSoldLabel;
    private JDateChooser fromDateChooser, toDateChooser;
    private ArrayList<ProductDTO> statistics;
    private StatistiscBUS statBUS = new StatistiscBUS();

    public GUI_Product_Statistics() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== TOP PANEL ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 100)); // Tăng chiều cao để chứa date chooser
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);

        // Date chooser
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        fromDateChooser.setPreferredSize(new Dimension(120, 30));

        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("dd/MM/yyyy");
        toDateChooser.setPreferredSize(new Dimension(120, 30));

        // Combobox lọc
        ArrayList<String> Cate = productBUS.getAllCategoryNames();
        Cate.add(0, "Tất cả");
        productFilterCombo = new JComboBox<>(Cate.toArray(new String[0]));
        productFilterCombo.setPreferredSize(new Dimension(150, 30));

        // Các nút chức năng
        filterButton = new CustomButton("Lọc");
        filterButton.setPreferredSize(new Dimension(80, 30));

        resetButton = new CustomButton("Đặt lại");
        resetButton.setPreferredSize(new Dimension(80, 30));

        exportButton = new CustomButton("Reload");
        exportButton.setPreferredSize(new Dimension(100, 30));

        // Thêm components vào filter panel
        
        filterPanel.add(fromDateChooser);
        filterPanel.add(toDateChooser);
        filterPanel.add(new JLabel("Loại:"));
        filterPanel.add(productFilterCombo);
        filterPanel.add(filterButton);
        filterPanel.add(resetButton);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(exportButton, BorderLayout.EAST);

        // ========== MID PANEL (Bảng thống kê) ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Số lượng bán", "Tồn kho", "Doanh thu"};
        productStatsTable = new CustomTable(columnNames);
        tableModel = productStatsTable.getTableModel();
        JScrollPane scrollPane = new CustomScrollPane(productStatsTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== BOTTOM PANEL (Tổng hợp) ==========
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
        totalProductLabel = new JLabel("0");
        botPanel.add(totalProductLabel, gbc);

        // Tổng doanh thu
        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tổng doanh thu:"), gbc);
        gbc.gridx = 1;
        totalRevenueLabel = new JLabel("0 VND");
        botPanel.add(totalRevenueLabel, gbc);

        // Tổng số lượng đã bán
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tổng SL bán:"), gbc);
        gbc.gridx = 1;
        totalSoldLabel = new JLabel("0");
        botPanel.add(totalSoldLabel, gbc);

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Load dữ liệu ban đầu
        loadProductStatistics();
        fromDateChooser.setVisible(false);
        toDateChooser.setVisible(false);
        // Xử lý sự kiện
        filterButton.addActionListener(e -> filterCategory(productFilterCombo.getSelectedItem().toString()));
        resetButton.addActionListener(e -> resetFilters());
    }

//    private void filterProducts() {
//        Date fromDate = fromDateChooser.getDate();
//        Date toDate = toDateChooser.getDate();
//        String filterType = (String) productFilterCombo.getSelectedItem();
//        
//        // Validate dates
//        if (fromDate != null && toDate != null && fromDate.after(toDate)) {
//            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        // Format dates
//        String fromStr = fromDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(fromDate) : null;
//        String toStr = toDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(toDate) : null;
//        
//        // Filter logic (cần implement trong ProductBUS)
//        List<ProductDTO> filteredProducts = productBUS.getFilteredProducts(filterType, fromStr, toStr);
//        
//        // Update table
//        tableModel.setRowCount(0);
//        int totalSold = 0;
//        double totalRevenue = 0;
//        
//        for (ProductDTO product : filteredProducts) {
//            tableModel.addRow(new Object[]{
//                product.getProductID(),
//                product.getProductName(),
//                product.getSoldQuantity(),
//                product.getStockQuantity(),
//                Utils.formatCurrency(product.getRevenue())
//            });
//            
//            totalSold += product.getSoldQuantity();
//            totalRevenue += product.getRevenue();
//        }
//        
//        updateSummary(filteredProducts.size(), Utils.formatCurrency(totalRevenue), totalSold);
//    }
    private void resetFilters() {
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
        productFilterCombo.setSelectedIndex(0);
        loadProductStatistics();
    }

    private void filterCategory(String Category) {
        
        tableModel.setRowCount(0);
        statistics = statBUS.filterProductForCate(statistics, Category);
        int totalSold = 0;
        double totalRevenue = 0;
        for (ProductDTO row : statistics) {

            tableModel.addRow(new Object[]{
                row.getProductID(),
                row.getProductName(),
                //                soldQuantity,
                statBUS.getTotalQuantityByProductId(row.getProductID()),
                row.getSoluong(),
                statBUS.getTotalProfitByProductId(row.getProductID())
//                Utils.formatCurrencyLong(profit)
            });
            totalSold += statBUS.getTotalQuantityByProductId(row.getProductID());
//            totalSold += soldQuantity;
            totalRevenue += statBUS.getTotalProfitByProductId(row.getProductID());
        }
        updateSummary(statistics.size(), GUI.Utils.formatCurrencyDouble(totalRevenue), totalSold);
        if(Category == "Tất cả"){
            loadProductStatistics();
        }
    }

    private void loadProductStatistics() {

        statistics = statBUS.getProductStatistics();

        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        int totalSold = 0;
        double totalRevenue = 0;

        for (ProductDTO row : statistics) {

            tableModel.addRow(new Object[]{
                row.getProductID(),
                row.getProductName(),
                //                soldQuantity,
                statBUS.getTotalQuantityByProductId(row.getProductID()),
                row.getSoluong(),
                statBUS.getTotalProfitByProductId(row.getProductID())
//                Utils.formatCurrencyLong(profit)
            });
            totalSold += statBUS.getTotalQuantityByProductId(row.getProductID());
//            totalSold += soldQuantity;
            totalRevenue += statBUS.getTotalProfitByProductId(row.getProductID());
        }
        updateSummary(statistics.size(), GUI.Utils.formatCurrencyDouble(totalRevenue), totalSold);
    }

    private void updateSummary(int totalProducts, String formattedRevenue, int totalSold) {
        totalProductLabel.setText(String.valueOf(totalProducts));
        totalRevenueLabel.setText(formattedRevenue);
        totalSoldLabel.setText(String.valueOf(totalSold)
        );
    }
}
