package GUI;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Connection.DatabaseConnection;
import DTO.ImportDTO;
import DTO.ProductDTO;

public class GUI_Form_Import extends JDialog {
    private final JLabel lblMaNhapHang, lblNgayNhap, lblNhanVien, lblTongTien;
    private JTable productsTable;
    private DefaultTableModel tableModel;
    private CustomButton btnThemSP, btnLuu, btnHuy;
    private CustomCombobox<String> cbNhaCungCap;
    private String currentUser;
    private Map<String, List<ProductDTO>> supplierProductsMap;
    private int totalAmount;

    public GUI_Form_Import(JPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Nhập Hàng Mới", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Initialize data
        currentUser = getCurrentUser();
        supplierProductsMap = loadSupplierProducts();
        totalAmount = 0;

        // Top panel - basic info
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Generate next import ID
        String nextImportID = generateNextImportID();

        // Add components
        addLabelAndField(topPanel, gbc, "Mã nhập hàng:", lblMaNhapHang = new JLabel(nextImportID));
        addLabelAndField(topPanel, gbc, "Nhân viên:", lblNhanVien = new JLabel(currentUser + " - " + getEmployeeName(currentUser)));
        addLabelAndField(topPanel, gbc, "Ngày nhập:", lblNgayNhap = new JLabel(LocalDate.now().toString()));
        addLabelAndField(topPanel, gbc, "Nhà cung cấp:", cbNhaCungCap = new CustomCombobox<>(getSupplierNames()));
        addLabelAndField(topPanel, gbc, "Tổng tiền:", lblTongTien = new JLabel("0"));

        // Add product button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        btnThemSP = new CustomButton("+ Thêm sản phẩm");
        btnThemSP.addActionListener(e -> showAddProductDialog());
        topPanel.add(btnThemSP, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Center panel - products table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm nhập"));
        centerPanel.setBackground(Color.WHITE);

        // Products table
        String[] columnNames = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(25);
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        centerPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel - buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnLuu = new CustomButton("Lưu phiếu nhập");
        btnLuu.setCustomColor(new Color(0, 120, 215));
        btnLuu.addActionListener(e -> saveImport());
        
        btnHuy = new CustomButton("Hủy");
        btnHuy.setCustomColor(new Color(220, 0, 0));
        btnHuy.addActionListener(e -> dispose());

        bottomPanel.add(btnLuu);
        bottomPanel.add(btnHuy);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        cbNhaCungCap.addActionListener(e -> {
            // Clear products when supplier changes
            tableModel.setRowCount(0);
            updateTotalAmount(0);
        });
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private String generateNextImportID() {
        String query = "SELECT ma_nhap_hang FROM nhap_hang ORDER BY ma_nhap_hang DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastID = rs.getString("ma_nhap_hang");
                int num = Integer.parseInt(lastID.substring(4)) + 1;
                return String.format("NH%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NH001"; // Default if no records exist
    }

    private String getCurrentUser() {
        // In a real application, this would come from the login session
        return "NV016"; // Example user
    }

    private String getEmployeeName(String employeeID) {
        String query = "SELECT ten_nhan_vien FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ten_nhan_vien");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String[] getSupplierNames() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT ma_nha_cung_cap, ten_nha_cung_cap FROM nha_cung_cap";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                suppliers.add(rs.getString("ma_nha_cung_cap") + " - " + rs.getString("ten_nha_cung_cap"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers.toArray(new String[0]);
    }

    private Map<String, List<ProductDTO>> loadSupplierProducts() {
        Map<String, List<ProductDTO>> map = new HashMap<>();
        String query = "SELECT ma_san_pham, ten_san_pham, gia, ma_nha_cung_cap FROM san_pham ORDER BY ma_nha_cung_cap, ma_san_pham";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String supplierID = rs.getString("ma_nha_cung_cap");
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("ma_san_pham"));
                product.setProductName(rs.getString("ten_san_pham"));
                product.setGia(rs.getString("gia"));
                product.setMaNCC(supplierID);
                
                map.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void showAddProductDialog() {
        String selectedSupplier = (String) cbNhaCungCap.getSelectedItem();
        if (selectedSupplier == null || selectedSupplier.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp trước!");
            return;
        }

        // Extract supplier ID from the combo box item
        String supplierID = selectedSupplier.split(" - ")[0];
        List<ProductDTO> products = supplierProductsMap.get(supplierID);
        
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhà cung cấp này không có sản phẩm nào!");
            return;
        }

        // Create dialog
        JDialog dialog = new JDialog(this, "Thêm sản phẩm", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Product combo box
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<ProductDTO> cbProducts = new JComboBox<>(products.toArray(new ProductDTO[0]));
        cbProducts.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProductDTO) {
                    ProductDTO product = (ProductDTO) value;
                    setText(product.getProductID() + " - " + product.getProductName());
                }
                return this;
            }
        });
        dialog.add(cbProducts, gbc);

        // Quantity field
        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("Số lượng:"), gbc);
        
        gbc.gridx = 1;
        JTextField txtQuantity = new JTextField(10);
        dialog.add(txtQuantity, gbc);

        // Price and total
        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("Đơn giá:"), gbc);
        
        gbc.gridx = 1;
        JLabel lblPrice = new JLabel("0");
        dialog.add(lblPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("Thành tiền:"), gbc);
        
        gbc.gridx = 1;
        JLabel lblTotal = new JLabel("0");
        dialog.add(lblTotal, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        CustomButton btnAdd = new CustomButton("Thêm");
        CustomButton btnCancel = new CustomButton("Hủy");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, gbc);

        // Initial price display
        ProductDTO initialProduct = (ProductDTO) cbProducts.getSelectedItem();
        if (initialProduct != null) {
            lblPrice.setText(Utils.formatCurrency(initialProduct.getGia()));
        }

        // Event listeners
        cbProducts.addActionListener(e -> {
            ProductDTO selected = (ProductDTO) cbProducts.getSelectedItem();
            if (selected != null) {
                lblPrice.setText(Utils.formatCurrency(selected.getGia()));
                updateProductTotal(lblTotal, lblPrice.getText(), txtQuantity.getText());
            }
        });

        txtQuantity.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            
            private void update() {
                updateProductTotal(lblTotal, lblPrice.getText(), txtQuantity.getText());
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                ProductDTO selectedProduct = (ProductDTO) cbProducts.getSelectedItem();
                int quantity = Integer.parseInt(txtQuantity.getText());
                
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!");
                    return;
                }
                
                int price = Integer.parseInt(selectedProduct.getGia());
                int rowTotal = price * quantity;
                
                // Add to table
                tableModel.addRow(new Object[]{
                    selectedProduct.getProductID(),
                    selectedProduct.getProductName(),
                    quantity,
                    Utils.formatCurrency(price),
                    Utils.formatCurrency(rowTotal)
                });
                
                // Update total amount
                updateTotalAmount(totalAmount + rowTotal);
                
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên dương!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void updateProductTotal(JLabel lblTotal, String priceStr, String quantityStr) {
        try {
            int price = Integer.parseInt(priceStr.replaceAll("[^0-9]", ""));
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);
            lblTotal.setText(Utils.formatCurrency(price * quantity));
        } catch (NumberFormatException e) {
            lblTotal.setText("0");
        }
    }

    private void updateTotalAmount(int newTotal) {
        totalAmount = newTotal;
        lblTongTien.setText(Utils.formatCurrency(totalAmount));
    }

    private void saveImport() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một sản phẩm!");
            return;
        }

        String importID = lblMaNhapHang.getText();
        String employeeID = currentUser;
        String supplierID = ((String) cbNhaCungCap.getSelectedItem()).split(" - ")[0];
        String receiptDate = LocalDate.now().toString();

        // Save to database
        try {
            // 1. Save main import record
            String importQuery = "INSERT INTO nhap_hang (ma_nhap_hang, ma_nhan_vien, ma_nha_cung_cap, tong_tien, ngay_nhap) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            // 2. Save import details
            String detailQuery = "INSERT INTO chi_tiet_nhap_hang (ma_chi_tiet_nhap_hang, ma_nhap_hang, ma_san_pham, so_luong, gia) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            try (PreparedStatement importStmt = conn.prepareStatement(importQuery);
                 PreparedStatement detailStmt = conn.prepareStatement(detailQuery)) {
                
                // Insert main import record
                importStmt.setString(1, importID);
                importStmt.setString(2, employeeID);
                importStmt.setString(3, supplierID);
                importStmt.setInt(4, totalAmount);
                importStmt.setString(5, receiptDate);
                importStmt.executeUpdate();
                
                // Insert each product detail
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String detailID = importID + String.format("%02d", i+1);
                    String productID = (String) tableModel.getValueAt(i, 0);
                    int quantity = (Integer) tableModel.getValueAt(i, 2);
                    int price = Integer.parseInt(tableModel.getValueAt(i, 3).toString().replaceAll("[^0-9]", ""));
                    
                    detailStmt.setString(1, detailID);
                    detailStmt.setString(2, importID);
                    detailStmt.setString(3, productID);
                    detailStmt.setInt(4, quantity);
                    detailStmt.setInt(5, price);
                    detailStmt.executeUpdate();
                }
                
                conn.commit(); // Commit transaction
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công!");
                dispose();
            } catch (Exception e) {
                conn.rollback(); // Rollback on error
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập: " + e.getMessage());
        }
    }
}