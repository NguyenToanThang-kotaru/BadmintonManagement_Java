package GUI;

import BUS.SuppliersBUS;
import DAO.ProductDAO;
import DTO.ProductDTO;
import DTO.SuppliersDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class GUI_Detail_Suppliers extends JDialog {
    
    private SuppliersBUS suppliersBUS;
    private SuppliersDTO supplier;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private ProductDetailPanel detailPanel; // Sử dụng ProductDetailPanel thay vì tự tạo
    private JPanel placeholderPanel, mainPanel;

    public GUI_Detail_Suppliers(GUI_Suppliers parent, SuppliersDTO supplier) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Danh Sách Sản Phẩm Nhà Cung Cấp", true);
        this.supplier = supplier;
        suppliersBUS = new SuppliersBUS();

        // Tăng chiều cao cửa sổ để hiển thị nhiều sản phẩm hơn
        setSize(900, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Main panel để chứa các thành phần
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Supplier Info Panel
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

        // Product Table
        String[] columnNames = {"Mã SP", "Tên SP", "Giá", "Số Lượng", "Thông Số Kỹ Thuật"};
        productTableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(productTableModel);
        
        // Tăng kích thước font chữ của bảng
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setRowHeight(25); // Tăng chiều cao hàng

        // Điều chỉnh độ rộng cột
        TableColumnModel columnModel = productTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // Mã SP
        columnModel.getColumn(1).setPreferredWidth(250); // Tên SP
        columnModel.getColumn(2).setPreferredWidth(100); // Giá
        columnModel.getColumn(3).setPreferredWidth(80);  // Số Lượng
        columnModel.getColumn(4).setPreferredWidth(300); // Thông Số Kỹ Thuật

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        loadProducts();

        // Placeholder panel (khoảng trắng ban đầu)
        placeholderPanel = new JPanel();
        placeholderPanel.setBackground(Color.WHITE);
        placeholderPanel.setPreferredSize(new Dimension(0, 200)); // Chừa sẵn khoảng trắng cao 200px

        // Sử dụng ProductDetailPanel thay vì tự tạo detailPanel
        detailPanel = new ProductDetailPanel(null, null); // Không cần ActionListener và Form_ImportBUS
        // Ẩn các thành phần không cần thiết
        detailPanel.getTxtQuantity().setVisible(false);
        detailPanel.getLblTotal().setVisible(false);
        JLabel lblThanhTien = (JLabel) detailPanel.getLblTotal().getParent().getComponent(0); // Nhãn "THÀNH TIỀN:"
        lblThanhTien.setVisible(false);
        detailPanel.getLblTotal().getParent().setVisible(false); // Ẩn toàn bộ bottomPanel chứa "THÀNH TIỀN"
        detailPanel.getComponent(1).setVisible(false); // Ẩn nút "Thêm SP" (CustomButton)

        // Thêm sự kiện khi chọn hàng trong bảng
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Tránh sự kiện được gọi nhiều lần
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productID = (String) productTable.getValueAt(selectedRow, 0);

                    ProductDTO product = ProductDAO.getProduct(productID);

                    if (product != null) {
                        // Cập nhật thông tin chi tiết trong ProductDetailPanel
                        detailPanel.getLblProductId().setText(product.getProductID());
                        detailPanel.getLblProductName().setText(product.getProductName());
                        detailPanel.getLblPrice().setText(String.valueOf(product.getGia()));
                        // ProductDetailPanel không có trường "Số lượng", nhưng chúng ta có thể bỏ qua vì không bắt buộc
                        detailPanel.getLblSupplier().setText(product.gettenNCC());

                        // Cập nhật hình ảnh
                        String imageFileName = product.getAnh();
                        String imagePath = "/images/" + (imageFileName != null ? imageFileName : "default_product.png");
                        URL imageUrl = getClass().getResource(imagePath);
                        ImageIcon icon = (imageUrl != null) ? new ImageIcon(imageUrl) : null;
                        detailPanel.getLblProductImage().setIcon(
                            icon != null ? new ImageIcon(icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH)) : null
                        );
                        detailPanel.getLblProductImage().setText(icon == null ? "Không có ảnh" : "");

                        // Thay thế placeholderPanel bằng detailPanel
                        mainPanel.remove(placeholderPanel);
                        mainPanel.add(detailPanel, BorderLayout.SOUTH);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } else {
                        // Thông báo lỗi nếu không tìm thấy sản phẩm
                        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID: " + productID, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        // Hiển thị lại placeholderPanel
                        mainPanel.remove(detailPanel);
                        mainPanel.add(placeholderPanel, BorderLayout.SOUTH);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                } else {
                    // Nếu không có hàng nào được chọn, hiển thị lại placeholderPanel
                    mainPanel.remove(detailPanel);
                    mainPanel.add(placeholderPanel, BorderLayout.SOUTH);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });

        // Thêm các thành phần vào mainPanel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(placeholderPanel, BorderLayout.SOUTH); // Ban đầu hiển thị khoảng trắng

        // Thêm mainPanel vào cửa sổ
        add(mainPanel, BorderLayout.CENTER);

        // Close Button
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
                product.getGia(),
                product.getSoluong(),
                product.getTSKT()
            });
        }
    }
}