package GUI;

import BUS.Form_ImportBUS;
import DTO.SuppliersDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Form_Import extends JDialog {
    private InfoPanel infoPanel;
    private ImportProductsPanel importProductsPanel;
    private ProductDetailPanel productDetailPanel;
    private AllProductsPanel allProductsPanel;
    private CustomButton btnLuu, btnHuy;
    
    private Form_ImportBUS bus;
    private String currentUser;
    private GUI_Import parentImportPanel;
    private int totalAmount = 0;

    public Form_Import(GUI_Import parentImportPanel, String username) {
        super((Frame) SwingUtilities.getWindowAncestor(parentImportPanel), "Nhập Hàng Mới", true);
        this.parentImportPanel = parentImportPanel;
        this.currentUser = username;
        this.bus = new Form_ImportBUS();
        
        initializeUI();
        loadAllProducts();
    }

    private void initializeUI() {
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Info Panel
        infoPanel = new InfoPanel(bus.generateNextImportID(), currentUser, bus);
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(infoPanel);

        // Center Panel (All Products + Import Products)
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(Color.WHITE);

        allProductsPanel = new AllProductsPanel();
        allProductsPanel.setPreferredSize(new Dimension(600, 300));
        allProductsPanel.getAllProductsTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && allProductsPanel.getAllProductsTable().getSelectedRow() >= 0) {
                int row = allProductsPanel.getAllProductsTable().getSelectedRow();
                displayProductDetails(
                    allProductsPanel.getProductTableModel().getValueAt(row, 0).toString(),
                    allProductsPanel.getProductTableModel().getValueAt(row, 1).toString(),
                    allProductsPanel.getProductTableModel().getValueAt(row, 2).toString()
                );
            }
        });
        centerPanel.add(allProductsPanel, BorderLayout.WEST);

        importProductsPanel = new ImportProductsPanel();
        importProductsPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(importProductsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel);

        // Product Detail Panel
        productDetailPanel = new ProductDetailPanel(e -> addProductToImport(), bus);
        productDetailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        mainPanel.add(productDetailPanel);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainPanel.add(buttonPanel);

        add(mainPanel);
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
        List<Object[]> products = bus.loadAllProducts();
        allProductsPanel.getProductTableModel().setRowCount(0);
        products.forEach(allProductsPanel.getProductTableModel()::addRow);
    }

    private void displayProductDetails(String productId, String productName, String price) {
        Object[] details = bus.getProductDetails(productId);
        if (details != null) {
            productDetailPanel.getLblProductId().setText((String) details[0]);
            productDetailPanel.getLblProductName().setText((String) details[1]);
            productDetailPanel.getLblPrice().setText((String) details[2]); // Giá bán
            productDetailPanel.getLblOriginalPrice().setText((String) details[5]); // Giá gốc
            productDetailPanel.getLblSupplier().setText((String) details[3]);
            loadProductImage((String) details[4]);
            productDetailPanel.getTxtQuantity().setText("");
            productDetailPanel.getLblTotal().setText("0");
        }
    }

    private void loadProductImage(String imageFileName) {
        String imagePath = "images/" + (imageFileName != null && !imageFileName.isEmpty() ? imageFileName : "default_product.png");
        File imageFile = new File(imagePath);
        
        if (imageFile.exists()) {
            ImageIcon productIcon = new ImageIcon(imagePath);
            Image img = productIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            productDetailPanel.getLblProductImage().setIcon(new ImageIcon(img));
            productDetailPanel.getLblProductImage().setText("");
        } else {
            productDetailPanel.getLblProductImage().setIcon(null);
            productDetailPanel.getLblProductImage().setText("Không có ảnh");
        }
    }

    private void addProductToImport() {
        try {
            String productId = productDetailPanel.getLblProductId().getText();
            String quantityText = productDetailPanel.getTxtQuantity().getText().trim();
            String validationError = bus.validateProductToAdd(productId, quantityText);
            if (validationError != null) {
                JOptionPane.showMessageDialog(this, validationError, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            String productName = productDetailPanel.getLblProductName().getText();
            String supplierName = productDetailPanel.getLblSupplier().getText();
            int price = Integer.parseInt(productDetailPanel.getLblOriginalPrice().getText().replaceAll("[^0-9]", "")); // Dùng giá gốc
            int quantity = Integer.parseInt(quantityText);
            int total = price * quantity;
    
            importProductsPanel.getImportTableModel().addRow(new Object[]{
                productId, productName, quantity, Utils.formatCurrency(price), Utils.formatCurrency(total), supplierName
            });
            totalAmount += total;
            infoPanel.getLblTongTien().setText(Utils.formatCurrency(totalAmount));
    
            resetProductDetail();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveImport() {
        if (importProductsPanel.getImportTableModel().getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            String importID = infoPanel.getLblMaNhapHang().getText();
            String receiptDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            List<Object[]> productData = new ArrayList<>();
            for (int i = 0; i < importProductsPanel.getImportTableModel().getRowCount(); i++) {
                String priceStr = importProductsPanel.getImportTableModel().getValueAt(i, 3).toString().replaceAll("[^0-9]", "");
                productData.add(new Object[]{
                    importProductsPanel.getImportTableModel().getValueAt(i, 0), // productId
                    importProductsPanel.getImportTableModel().getValueAt(i, 1), // productName
                    importProductsPanel.getImportTableModel().getValueAt(i, 2), // quantity
                    Integer.parseInt(priceStr)                           // price
                });
            }
    
            if (bus.saveImport(importID, currentUser, totalAmount, receiptDate, productData)) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentImportPanel.loadImport();
                loadAllProducts();
                // Cập nhật danh sách sản phẩm trong GUI_Product
                GUI_Product productPanel = GUI_Product.getInstance();
                if (productPanel != null) {
                    productPanel.loadProductData();
                }
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetProductDetail() {
        allProductsPanel.getAllProductsTable().clearSelection();
        productDetailPanel.getLblProductId().setText("Chọn sản phẩm từ danh sách");
        productDetailPanel.getLblProductName().setText("");
        productDetailPanel.getLblSupplier().setText("");
        productDetailPanel.getLblPrice().setText("");
        productDetailPanel.getLblProductImage().setIcon(null);
        productDetailPanel.getTxtQuantity().setText("");
        productDetailPanel.getLblTotal().setText("0");
    }

    private void resetForm() {
        importProductsPanel.getImportTableModel().setRowCount(0);
        totalAmount = 0;
        infoPanel.getLblTongTien().setText("0");
        dispose();
    }
}