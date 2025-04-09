package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import BUS.Form_ImportBUS;
import DAO.Form_ImportDAO;
import DTO.ProductDTO;
import Connection.DatabaseConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI_Form_Import extends JDialog {
    private JLabel lblMaNhapHang, lblTongTien;
    private JTable productsTable, allProductsTable;
    private DefaultTableModel importTableModel, productTableModel;
    private CustomButton btnThemSP, btnLuu, btnHuy;
    private JLabel lblProductImage, lblProductId, lblProductName, lblSupplier, lblPrice;
    private JTextField txtQuantity;
    private JLabel lblTotal;
    
    private int totalAmount;
    private Form_ImportDAO dao;
    private Form_ImportBUS bus;
    private String currentUser;
    private GUI_Import parentImportPanel; // Thêm tham chiếu đến GUI_Import

    public GUI_Form_Import(GUI_Import parentImportPanel, String username) {
        super((Frame) SwingUtilities.getWindowAncestor(parentImportPanel), "Nhập Hàng Mới", true);
        this.parentImportPanel = parentImportPanel; // Lưu tham chiếu
        setSize(1100, 750);
        setLocationRelativeTo(parentImportPanel);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        bus = new Form_ImportBUS();
        dao = new Form_ImportDAO();
        currentUser = username;
        totalAmount = 0;
        
        // Gán mã nhập hàng tự động
        lblMaNhapHang = new JLabel(dao.generateNextImportID());
        lblMaNhapHang.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        
        // Main panel với BoxLayout để kiểm soát chiều cao tốt hơn
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // 1. Top panel - Thông tin nhập hàng (co lại)
        JPanel infoPanel = createInfoPanel();
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(infoPanel);
        
        // 2. Center panel - Danh sách sản phẩm và sản phẩm đang chọn
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(Color.WHITE);
        
        // Panel trái - Danh sách tất cả sản phẩm (chiếm 60% chiều rộng)
        JPanel allProductsPanel = createAllProductsPanel();
        allProductsPanel.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(allProductsPanel, BorderLayout.WEST);
        
        // Panel phải - Danh sách sản phẩm nhập (chiếm 40% chiều rộng)
        JPanel importProductsPanel = createImportProductsPanel();
        importProductsPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(importProductsPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel);
        
        // 3. Product detail panel (cố định chiều cao)
        JPanel productDetailPanel = createProductDetailPanel();
        productDetailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        mainPanel.add(productDetailPanel);
        
        // 4. Button panel
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
        
        // Load danh sách sản phẩm
        loadAllProducts();
    }
        
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new CompoundBorder(
            new TitledBorder("Thông tin nhập hàng"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        // Dòng 1
        JPanel maNhapHangPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maNhapHangPanel.setBackground(Color.WHITE);
        maNhapHangPanel.add(new JLabel("Mã nhập hàng:"));
        maNhapHangPanel.add(lblMaNhapHang); // Sử dụng lblMaNhapHang đã khởi tạo
        panel.add(maNhapHangPanel);
        
        JPanel nhanVienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nhanVienPanel.setBackground(Color.WHITE);
        nhanVienPanel.add(new JLabel("Nhân viên:"));
        JLabel lblNhanVien = new JLabel(currentUser + " - " + bus.getEmployeeName(currentUser));
        lblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nhanVienPanel.add(lblNhanVien);
        panel.add(nhanVienPanel);
        
        // Dòng 2
        JPanel ngayNhapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ngayNhapPanel.setBackground(Color.WHITE);
        ngayNhapPanel.add(new JLabel("Ngày nhập:"));
        JLabel lblNgayNhap = new JLabel(LocalDate.now().toString());
        lblNgayNhap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ngayNhapPanel.add(lblNgayNhap);
        panel.add(ngayNhapPanel);
        
        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tongTienPanel.setBackground(Color.WHITE);
        tongTienPanel.add(new JLabel("Tổng tiền:"));
        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(0, 100, 0));
        tongTienPanel.add(lblTongTien);
        panel.add(tongTienPanel);
        
        return panel;
    }

    private JPanel createAllProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
            new TitledBorder("Danh sách sản phẩm"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(Color.WHITE);
        
        // Table model
        String[] columns = {"Mã SP", "Tên SP", "Đơn giá"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        allProductsTable = new JTable(productTableModel);
        allProductsTable.setRowHeight(30);
        allProductsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allProductsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Center align và đặt chiều rộng cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        TableColumnModel columnModel = allProductsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Mã SP
        columnModel.getColumn(1).setPreferredWidth(350); // Tên SP
        columnModel.getColumn(2).setPreferredWidth(150); // Đơn giá
        
        for (int i = 0; i < allProductsTable.getColumnCount(); i++) {
            allProductsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Row selection listener
        allProductsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = allProductsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayProductDetails(
                        productTableModel.getValueAt(selectedRow, 0).toString(),
                        productTableModel.getValueAt(selectedRow, 1).toString(),
                        productTableModel.getValueAt(selectedRow, 2).toString()
                    );
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(allProductsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createImportProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
            new TitledBorder("Danh sách sản phẩm nhập"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(Color.WHITE);
        
        // Table model
        String[] columns = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        importTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productsTable = new JTable(importTableModel);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Center align và đặt chiều rộng cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        TableColumnModel columnModel = productsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Mã SP
        columnModel.getColumn(1).setPreferredWidth(200); // Tên SP
        columnModel.getColumn(2).setPreferredWidth(80);  // Số lượng
        columnModel.getColumn(3).setPreferredWidth(120); // Đơn giá
        columnModel.getColumn(4).setPreferredWidth(150); // Thành tiền
        
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(productsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createProductDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new CompoundBorder(
            new TitledBorder("Thông tin sản phẩm đang chọn"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        // Panel hình ảnh
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(200, 200));
        imagePanel.setBackground(Color.WHITE);
        
        lblProductImage = new JLabel();
        lblProductImage.setHorizontalAlignment(JLabel.CENTER);
        lblProductImage.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        java.net.URL defaultImageUrl = getClass().getResource("/images/default_product.png");
        if (defaultImageUrl != null) {
            ImageIcon defaultIcon = new ImageIcon(defaultImageUrl);
            Image img = defaultIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblProductImage.setIcon(new ImageIcon(img));
        } else {
            lblProductImage.setIcon(null);
            lblProductImage.setText("Không có ảnh mặc định");
        }
        
        imagePanel.add(lblProductImage, BorderLayout.CENTER);
        
        // Panel thông tin chi tiết
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Dòng 1: Mã sản phẩm
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailPanel.add(createInfoLabel("Mã sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        lblProductId = new JLabel("Chọn sản phẩm từ danh sách");
        lblProductId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(lblProductId, gbc);
        
        // Dòng 2: Tên sản phẩm
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailPanel.add(createInfoLabel("Tên sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        lblProductName = new JLabel();
        lblProductName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(lblProductName, gbc);
        
        // Dòng 3: Nhà cung cấp
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailPanel.add(createInfoLabel("Nhà cung cấp:"), gbc);
        
        gbc.gridx = 1;
        lblSupplier = new JLabel();
        lblSupplier.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(lblSupplier, gbc);
        
        // Dòng 4: Đơn giá
        gbc.gridx = 0;
        gbc.gridy = 3;
        detailPanel.add(createInfoLabel("Đơn giá:"), gbc);
        
        gbc.gridx = 1;
        lblPrice = new JLabel();
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(lblPrice, gbc);
        
        // Dòng 5: Số lượng
        gbc.gridx = 0;
        gbc.gridy = 4;
        detailPanel.add(createInfoLabel("Số lượng:"), gbc);
        
        gbc.gridx = 1;
        txtQuantity = new JTextField(5);
        txtQuantity.setHorizontalAlignment(JTextField.RIGHT);
        txtQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailPanel.add(txtQuantity, gbc);
        
        // Dòng 6: THÀNH TIỀN và nút Thêm SP
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.insets = new Insets(5, 5, 5, 0);

        // THÀNH TIỀN
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 0;
        gbcBottom.anchor = GridBagConstraints.WEST;
        JLabel lblTotalLabel = createInfoLabel("THÀNH TIỀN:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(lblTotalLabel, gbcBottom);

        gbcBottom.gridx = 1;
        lblTotal = new JLabel("0");
        lblTotal.setForeground(new Color(0, 100, 0));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(lblTotal, gbcBottom);
        
        // Nút Thêm SP
        gbcBottom.gridx = 2;
        gbcBottom.weightx = 1.0;
        gbcBottom.insets = new Insets(5, 0, 5, 5);
        gbcBottom.anchor = GridBagConstraints.EAST;
        btnThemSP = new CustomButton("Thêm SP");
        btnThemSP.setPreferredSize(new Dimension(150, 35));
        btnThemSP.addActionListener(e -> addProductToImport());
        bottomPanel.add(btnThemSP, gbcBottom);
        
        // Panel chính của thông tin
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(detailPanel, BorderLayout.CENTER);
        infoPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Thêm document listener để tính tổng tiền
        txtQuantity.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotal(); }
            
            private void updateTotal() {
                try {
                    int price = Integer.parseInt(lblPrice.getText().replaceAll("[^0-9]", ""));
                    int quantity = txtQuantity.getText().isEmpty() ? 0 : Integer.parseInt(txtQuantity.getText());
                    lblTotal.setText(Utils.formatCurrency(price * quantity));
                } catch (NumberFormatException ex) {
                    lblTotal.setText("0");
                }
            }
        });
        
        panel.add(imagePanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
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

    private void loadAllProducts() {
        try {
            // Clear table
            productTableModel.setRowCount(0);
            
            // Load products from database
            String query = "SELECT ma_san_pham, ten_san_pham, gia FROM san_pham";
            var conn = DatabaseConnection.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                productTableModel.addRow(new Object[]{
                    rs.getString("ma_san_pham"),
                    rs.getString("ten_san_pham"),
                    Utils.formatCurrency(rs.getInt("gia"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayProductDetails(String productId, String productName, String price) {
        try {
            // Load product details from database
            String query = "SELECT sp.*, ncc.ten_nha_cung_cap FROM san_pham sp " +
                          "JOIN nha_cung_cap ncc ON sp.ma_nha_cung_cap = ncc.ma_nha_cung_cap " +
                          "WHERE sp.ma_san_pham = ?";
            var conn = DatabaseConnection.getConnection();
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, productId);
            var rs = stmt.executeQuery();
            
            if (rs.next()) {
                lblProductId.setText(productId);
                lblProductName.setText(productName);
                lblSupplier.setText(rs.getString("ten_nha_cung_cap"));
                lblPrice.setText(price+ " VND");
                
                // Load image from resources based on hinh_anh column
                String imageFileName = rs.getString("hinh_anh");
                String imagePath = "/images/" + imageFileName; // Đường dẫn tài nguyên bắt đầu bằng "/"
                java.net.URL imageUrl = getClass().getResource(imagePath);
                
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    lblProductImage.setIcon(new ImageIcon(img));
                    lblProductImage.setText(""); // Xóa text nếu có ảnh
                } else {
                    // Nếu không tìm thấy ảnh, dùng ảnh mặc định
                    java.net.URL defaultImageUrl = getClass().getResource("/images/default_product.png");
                    if (defaultImageUrl != null) {
                        ImageIcon defaultIcon = new ImageIcon(defaultImageUrl);
                        Image img = defaultIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                        lblProductImage.setIcon(new ImageIcon(img));
                        lblProductImage.setText("");
                    } else {
                        lblProductImage.setIcon(null);
                        lblProductImage.setText("Không có ảnh");
                    }
                }
                
                // Reset quantity and total
                txtQuantity.setText("");
                lblTotal.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addProductToImport() {
        try {
            // Kiểm tra xem sản phẩm đã được chọn chưa
            if (lblProductId.getText().isEmpty() || lblProductId.getText().equals("Chọn sản phẩm từ danh sách")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm từ danh sách", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Kiểm tra số lượng có bỏ trống hay không
            String quantityText = txtQuantity.getText().trim();
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số lượng không được để trống", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Kiểm tra số lượng có phải là số nguyên dương hay không
            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương lớn hơn 0", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số lượng phải là một số nguyên hợp lệ", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Nếu vượt qua các kiểm tra, tiến hành thêm sản phẩm
            String productId = lblProductId.getText();
            String productName = lblProductName.getText();
            int price = Integer.parseInt(lblPrice.getText().replaceAll("[^0-9]", ""));
            
            int total = price * quantity;
            
            // Add to import table
            importTableModel.addRow(new Object[]{
                productId,
                productName,
                quantity,
                Utils.formatCurrency(price),
                Utils.formatCurrency(total)
            });
            
            // Update total amount
            totalAmount += total;
            lblTongTien.setText(Utils.formatCurrency(totalAmount));
            
            // Update quantity in database
            boolean updated = dao.updateProductQuantity(productId, quantity);
            if (updated) {
                // Refresh product list
                loadAllProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật số lượng sản phẩm trong database", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
            // Clear selection and details
            allProductsTable.clearSelection();
            lblProductId.setText("");
            lblProductName.setText("");
            lblSupplier.setText("");
            lblPrice.setText("");
            lblProductImage.setIcon(null);
            txtQuantity.setText("");
            lblTotal.setText("0");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm sản phẩm: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveImport() {
        if (importTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một sản phẩm", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String importID = lblMaNhapHang.getText();
        String receiptDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        List<Object[]> productData = new ArrayList<>();
        for (int i = 0; i < importTableModel.getRowCount(); i++) {
            try {
                String priceStr = importTableModel.getValueAt(i, 3).toString().replaceAll("[^0-9]", "");
                int price = Integer.parseInt(priceStr);
                
                productData.add(new Object[]{
                    importTableModel.getValueAt(i, 0),
                    importTableModel.getValueAt(i, 1),
                    importTableModel.getValueAt(i, 2),
                    price
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Dữ liệu sản phẩm không hợp lệ ở dòng " + (i+1), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String supplierID = "";
        try {
            String firstProductId = importTableModel.getValueAt(0, 0).toString();
            String query = "SELECT ma_nha_cung_cap FROM san_pham WHERE ma_san_pham = ?";
            var conn = DatabaseConnection.getConnection();
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, firstProductId);
            var rs = stmt.executeQuery();
            
            if (rs.next()) {
                supplierID = rs.getString("ma_nha_cung_cap");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy mã nhà cung cấp", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Truyền productData trực tiếp dưới dạng List<Object[]>
        boolean success = bus.saveImport(importID, currentUser, supplierID, totalAmount, receiptDate, productData);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            parentImportPanel.loadImport();
            loadAllProducts();
            importTableModel.setRowCount(0);
            totalAmount = 0;
            lblTongTien.setText("0");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}