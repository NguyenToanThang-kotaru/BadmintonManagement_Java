package GUI;

import BUS.SuppliersBUS;
import DAO.ProductDAO;
import DTO.ProductDTO;
import DTO.SuppliersDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GUI_Detail_Suppliers extends JDialog {
    
    private SuppliersBUS suppliersBUS;
    private SuppliersDTO supplier;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JLabel imageLabel;
    private JLabel productIDLabel, nameLabel, priceLabel, quantityLabel, tsktLabel, supplierNameLabel, categoryLabel, totalLabel;
    private JLabel totalImportLabel, profitLabel;
    private JPanel detailPanel, placeholderPanel, mainPanel;

    public GUI_Detail_Suppliers(GUI_Suppliers parent, SuppliersDTO supplier) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Danh Sách Sản Phẩm Nhà Cung Cấp", true);
        this.supplier = supplier;
        suppliersBUS = new SuppliersBUS();

        setSize(900, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông Tin Nhà Cung Cấp"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Mã Nhà Cung Cấp:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(supplier.getsuppliersID()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Tên Nhà Cung Cấp:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(supplier.getfullname()), gbc);

        String[] columnNames = {"Mã SP", "Tên SP", "Giá bán", "Giá nhập", "Số Lượng", "Thông Số Kỹ Thuật"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(productTableModel);
        
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setRowHeight(25);

        TableColumnModel columnModel = productTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        loadProducts();

        placeholderPanel = new JPanel();
        placeholderPanel.setBackground(Color.WHITE);
        placeholderPanel.setPreferredSize(new Dimension(0, 200));

        detailPanel = new JPanel(new BorderLayout(20, 0));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Chi Tiết Sản Phẩm"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel leftPanel = new JPanel(null);
        leftPanel.setPreferredSize(new Dimension(310, 200));
        leftPanel.setBackground(Color.WHITE);

        imageLabel = new JLabel();
        imageLabel.setBounds(30, 10, 230, 180);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        leftPanel.add(imageLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 5, 5, 5);
        gbcInfo.anchor = GridBagConstraints.WEST;

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 0;
        rightPanel.add(new JLabel("Mã sản phẩm: "), gbcInfo);
        gbcInfo.gridx = 1;
        productIDLabel = new JLabel("Chọn sản phẩm");
        rightPanel.add(productIDLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;
        rightPanel.add(new JLabel("Tên sản phẩm: "), gbcInfo);
        gbcInfo.gridx = 1;
        nameLabel = new JLabel("");
        nameLabel.setPreferredSize(new Dimension(300, 20));
        rightPanel.add(nameLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 2;
        rightPanel.add(new JLabel("Giá bán: "), gbcInfo);
        gbcInfo.gridx = 1;
        priceLabel = new JLabel("");
        rightPanel.add(priceLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 3;
        rightPanel.add(new JLabel("Số lượng: "), gbcInfo);
        gbcInfo.gridx = 1;
        quantityLabel = new JLabel("");
        rightPanel.add(quantityLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 4;
        rightPanel.add(new JLabel("Tổng giá bán: "), gbcInfo);
        gbcInfo.gridx = 1;
        totalLabel = new JLabel("");
        rightPanel.add(totalLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 5;
        rightPanel.add(new JLabel("Tổng giá nhập: "), gbcInfo);
        gbcInfo.gridx = 1;
        totalImportLabel = new JLabel("");
        rightPanel.add(totalImportLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 6;
        rightPanel.add(new JLabel("Tiền lời: "), gbcInfo);
        gbcInfo.gridx = 1;
        profitLabel = new JLabel("");
        rightPanel.add(profitLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 7;
        rightPanel.add(new JLabel("Tên NCC: "), gbcInfo);
        gbcInfo.gridx = 1;
        supplierNameLabel = new JLabel("");
        rightPanel.add(supplierNameLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 8;
        rightPanel.add(new JLabel("Thông số kỹ thuật: "), gbcInfo);
        gbcInfo.gridx = 1;
        tsktLabel = new JLabel("");
        rightPanel.add(tsktLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 9;
        rightPanel.add(new JLabel("Tên loại: "), gbcInfo);
        gbcInfo.gridx = 1;
        categoryLabel = new JLabel("");
        rightPanel.add(categoryLabel, gbcInfo);

        detailPanel.add(leftPanel, BorderLayout.WEST);
        detailPanel.add(rightPanel, BorderLayout.CENTER);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productID = (String) productTable.getValueAt(selectedRow, 0);
                    ProductDTO product = ProductDAO.getProduct(productID);
        
                    if (product != null) {
                        productIDLabel.setText(product.getProductID());
                        nameLabel.setText(product.getProductName());
                        priceLabel.setText(Utils.formatCurrency(Integer.parseInt(product.getGia())) + " VND");
                        quantityLabel.setText(product.getSoluong());

                        try {
                            int gia = Integer.parseInt(product.getGia());
                            int giaGoc = Integer.parseInt(product.getGiaGoc());
                            int soLuong = Integer.parseInt(product.getSoluong());
                            long totalSellPrice = (long) gia * soLuong;
                            long totalImportPrice = (long) giaGoc * soLuong;
                            totalLabel.setText(Utils.formatCurrencyLong(totalSellPrice) + " VND");
                            totalImportLabel.setText(Utils.formatCurrencyLong(totalImportPrice) + " VND");

                            long profit = totalSellPrice - totalImportPrice;
                            double profitPercentage = totalImportPrice > 0 ? (double) profit / totalImportPrice * 100 : 0;
                            profitLabel.setText(Utils.formatCurrencyLong(profit) + " VND (" + String.format("%.2f", profitPercentage) + "%)");
                        } catch (NumberFormatException ex) {
                            totalLabel.setText("Lỗi dữ liệu");
                            totalImportLabel.setText("Lỗi dữ liệu");
                            profitLabel.setText("Lỗi dữ liệu");
                        }

                        supplierNameLabel.setText(product.gettenNCC());
                        tsktLabel.setText(product.getTSKT());
                        categoryLabel.setText(product.getTL());
        
                        String imageFileName = product.getAnh();
                        String imagePath = "images/" + (imageFileName != null && !imageFileName.isEmpty() ? imageFileName : "default_product.png");
                        File imageFile = new File(imagePath);
        
                        if (imageFile.exists()) {
                            ImageIcon productIcon = new ImageIcon(imagePath);
                            Image img = productIcon.getImage().getScaledInstance(230, 180, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(img));
                            imageLabel.setText("");
                        } else {
                            imageLabel.setIcon(null);
                            imageLabel.setText("Không có ảnh");
                        }
        
                        mainPanel.remove(placeholderPanel);
                        mainPanel.add(detailPanel, BorderLayout.SOUTH);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID: " + productID, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        mainPanel.remove(detailPanel);
                        mainPanel.add(placeholderPanel, BorderLayout.SOUTH);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                } else {
                    mainPanel.remove(detailPanel);
                    mainPanel.add(placeholderPanel, BorderLayout.SOUTH);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(placeholderPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        CustomButton btnClose = new CustomButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        List<ProductDTO> products = suppliersBUS.getProductsBySupplier(supplier.getsuppliersID());
        productTableModel.setRowCount(0);
        for (ProductDTO product : products) {
            productTableModel.addRow(new Object[]{
                product.getProductID(),
                product.getProductName(),
                Utils.formatCurrency(Integer.parseInt(product.getGia())) + " VND",
                Utils.formatCurrency(Integer.parseInt(product.getGiaGoc())) + " VND",
                product.getSoluong(),
                product.getTSKT()
            });
        }
    }
}