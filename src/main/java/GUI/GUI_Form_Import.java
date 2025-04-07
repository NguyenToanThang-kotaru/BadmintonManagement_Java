package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import BUS.Form_ImportBUS;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import DAO.Form_ImportDAO;
import DTO.ProductDTO;

public class GUI_Form_Import extends JDialog {
    private JLabel lblMaNhapHang, lblTongTien;
    private JTable productsTable;
    private DefaultTableModel tableModel;
    private CustomButton btnThemSP, btnLuu, btnHuy;
    private CustomCombobox<String> cbNhaCungCap;

    private Map<String, List<ProductDTO>> supplierProductsMap;
    private int totalAmount;
    private Form_ImportDAO dao;
    private Form_ImportBUS bus;
    private String currentUser; 

    public GUI_Form_Import(JPanel parent, String username) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Nhập Hàng Mới", true);
        setSize(900, 650); // Tăng kích thước cửa sổ
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10)); // Thêm khoảng cách giữa các thành phần
        setBackground(Color.WHITE);
        bus = new Form_ImportBUS();
        dao = new Form_ImportDAO();

        currentUser = username;
        supplierProductsMap = bus.loadSupplierProducts();
        totalAmount = 0;
        
        // Main panel với padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // 1. Panel thông tin cơ bản
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // 2. Panel danh sách sản phẩm
        JPanel productPanel = createProductPanel();
        mainPanel.add(productPanel, BorderLayout.CENTER);
        
        // 3. Panel nút bấm
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new TitledBorder("Thông tin nhập hàng"));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Generate next import ID
        String nextImportID = bus.generateNextImportID();
        
        // Row 1: Mã nhập hàng và Nhân viên
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã nhập hàng:"), gbc);
        
        gbc.gridx = 1;
        lblMaNhapHang = new JLabel(nextImportID);
        lblMaNhapHang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblMaNhapHang, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Nhân viên:"), gbc);
        
        gbc.gridx = 3;
        JLabel lblNhanVien = new JLabel(currentUser + " - " + bus.getEmployeeName(currentUser));
        lblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblNhanVien, gbc);
        
        // Row 2: Ngày nhập và Nhà cung cấp
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Ngày nhập:"), gbc);
        
        gbc.gridx = 1;
        JLabel lblNgayNhap = new JLabel(LocalDate.now().toString());
        panel.add(lblNgayNhap, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Nhà cung cấp:"), gbc);
        
        gbc.gridx = 3;
        cbNhaCungCap = new CustomCombobox<>(dao.getSupplierNames());
        cbNhaCungCap.setPreferredSize(new Dimension(250, 30));
        panel.add(cbNhaCungCap, gbc);
        
        // Row 3: Tổng tiền và nút thêm sản phẩm
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tổng tiền:"), gbc);
        
        gbc.gridx = 1;
        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongTien.setForeground(new Color(0, 100, 0));
        panel.add(lblTongTien, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        btnThemSP = new CustomButton("+ Thêm sản phẩm");
        btnThemSP.setPreferredSize(new Dimension(150, 30));
        btnThemSP.addActionListener(e -> showAddProductDialog());
        panel.add(btnThemSP, gbc);
        
        // Event listeners
        cbNhaCungCap.addActionListener(e -> {
            tableModel.setRowCount(0);
            updateTotalAmount(0);
        });
        
        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Danh sách sản phẩm nhập"));
        panel.setBackground(Color.WHITE);
        
        // Products table
        String[] columnNames = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Đặt màu nền xen kẽ cho các dòng
        productsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        
        btnLuu = new CustomButton("Lưu phiếu nhập");
        btnLuu.setCustomColor(new Color(0, 120, 215));
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.addActionListener(e -> saveImport());
        
        btnHuy = new CustomButton("Hủy");
        btnHuy.setCustomColor(new Color(220, 0, 0));
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnHuy.addActionListener(e -> dispose());
        
        panel.add(btnLuu);
        panel.add(btnHuy);
        
        return panel;
    }
    
    private void showAddProductDialog() {
        String selectedSupplier = (String) cbNhaCungCap.getSelectedItem();
        if (selectedSupplier == null || selectedSupplier.isEmpty() || selectedSupplier.equals("Không có nhà cung cấp nào")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp hợp lệ trước!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String supplierID = selectedSupplier.split(" - ")[0];
        List<ProductDTO> products = supplierProductsMap.get(supplierID);

        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhà cung cấp này không có sản phẩm nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

      // Tạo dialog thêm sản phẩm
      JDialog dialog = new JDialog(this, "Thêm sản phẩm", true);
      dialog.setSize(500, 300);
      dialog.setLocationRelativeTo(this);
      dialog.setLayout(new GridBagLayout());
      dialog.getContentPane().setBackground(Color.WHITE);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.anchor = GridBagConstraints.WEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      // Product selection
      gbc.gridx = 0; gbc.gridy = 0;
      dialog.add(new JLabel("Sản phẩm:"), gbc);

      gbc.gridx = 1; gbc.gridwidth = 2;
      JComboBox<ProductDTO> cbProducts = new JComboBox<>(products.toArray(new ProductDTO[0]));
      cbProducts.setRenderer(new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                  boolean isSelected, boolean cellHasFocus) {
              super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
              if (value instanceof ProductDTO) {
                  ProductDTO product = (ProductDTO) value;
                  setText(product.getProductID() + " - " + product.getProductName());
              }
              return this;
          }
      });
      cbProducts.setPreferredSize(new Dimension(300, 25));
      dialog.add(cbProducts, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        dialog.add(new JLabel("Số lượng:"), gbc);

        gbc.gridx = 1;
        JTextField txtQuantity = new JTextField(10);
        txtQuantity.setToolTipText("Nhập số lượng sản phẩm");
        dialog.add(txtQuantity, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Đơn giá:"), gbc);

        gbc.gridx = 1;
        JLabel lblPrice = new JLabel("0");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dialog.add(lblPrice, gbc);

        // Total
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Thành tiền:"), gbc);

        gbc.gridx = 1;
        JLabel lblTotal = new JLabel("0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotal.setForeground(new Color(0, 100, 0));
        dialog.add(lblTotal, gbc);
    // Buttons
    gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.CENTER;

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    CustomButton btnAdd = new CustomButton("Thêm");
    btnAdd.setPreferredSize(new Dimension(100, 30));
    CustomButton btnCancel = new CustomButton("Hủy");
    btnCancel.setPreferredSize(new Dimension(100, 30));

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
                JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(dialog, "Số lượng phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String importID = lblMaNhapHang.getText();
    String supplierID = ((String) cbNhaCungCap.getSelectedItem()).split(" - ")[0];
    String receiptDate = LocalDate.now().toString();

    // Prepare product data
    List<Object[]> productData = new ArrayList<>();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        try {
            String priceStr = tableModel.getValueAt(i, 3).toString().replaceAll("[^0-9]", "");
            int price = Integer.parseInt(priceStr);

            productData.add(new Object[]{
                    tableModel.getValueAt(i, 0), // Mã SP
                    tableModel.getValueAt(i, 1), // Tên SP
                    tableModel.getValueAt(i, 2), // Số lượng
                    price // Đơn giá (đã xử lý)
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu sản phẩm không hợp lệ ở dòng " + (i+1), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // Save to database
    boolean success = bus.saveImport(importID, currentUser, supplierID, totalAmount, receiptDate, productData);

    if (success) {
        JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập! Vui lòng kiểm tra lại dữ liệu.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
}