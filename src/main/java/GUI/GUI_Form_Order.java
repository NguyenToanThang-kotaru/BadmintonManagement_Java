package GUI;

import BUS.OrderBUS;
import DTO.OrderDTO;
import BUS.ProductBUS;
import DTO.ProductDTO;
import DTO.AccountDTO;
import BUS.EmployeeBUS;
import DAO.EmployeeDAO;
import DTO.CustomerDTO;
import BUS.CustomerBUS;
import DTO.DetailOrderDTO;
import BUS.DetailOrderBUS;
import DAO.DetailOrderDAO;
import DAO.ProductDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class GUI_Form_Order extends JDialog {
    private JLabel lblMaHoaDon, lblNgayXuat, lblNhanVien, lblTongTien;
    private JTable productsTable, allProductsTable;
    private DefaultTableModel orderTableModel, productTableModel;
    private CustomButton btnThemSP, btnLuu, btnHuy;
    private JLabel lblProductImage, lblProductId, lblProductName, lblCategory, lblPrice;
    private JTextField txtQuantity, txtMaKhachHang, txtTenKhachHang, txtSoDienThoai;
    private OrderBUS orderBUS;
    private OrderDTO currentOrder;
    private AccountDTO currentAccount;
    private int totalAmount;
    private ProductBUS productBUS = new ProductBUS();
    private CustomerBUS customerBUS = new CustomerBUS();

    public GUI_Form_Order(JPanel parent, OrderDTO order, AccountDTO account) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), order == null ? "Tạo Hóa Đơn" : "Sửa Hóa Đơn", true);
        this.orderBUS = new OrderBUS();
        this.currentOrder = order;
        this.currentAccount = account;
        this.totalAmount = 0;

        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel infoPanel = createInfoPanel();
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(infoPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(Color.WHITE);

        JPanel allProductsPanel = createAllProductsPanel();
        allProductsPanel.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(allProductsPanel, BorderLayout.WEST);

        JPanel orderProductsPanel = createOrderProductsPanel();
        orderProductsPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(orderProductsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel);

        JPanel productDetailPanel = createProductDetailPanel();
        productDetailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        mainPanel.add(productDetailPanel);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainPanel.add(buttonPanel);

        add(mainPanel);

        loadAllProducts();

        if (currentOrder != null) {
            loadOrderData();
        }
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new CompoundBorder(new TitledBorder("Thông tin hóa đơn"), new EmptyBorder(10, 10, 10, 10)));
        panel.setBackground(Color.WHITE);

        JPanel maHoaDonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maHoaDonPanel.add(new JLabel("Mã hóa đơn:"));
        lblMaHoaDon = new JLabel(currentOrder == null ? orderBUS.getNextOrderID() : currentOrder.getorderID());
        lblMaHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        maHoaDonPanel.add(lblMaHoaDon);
        panel.add(maHoaDonPanel);

        JPanel nhanVienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nhanVienPanel.add(new JLabel("Nhân viên:"));
        lblNhanVien = new JLabel(currentOrder == null ? new EmployeeBUS().getEmployeeNameByID(currentAccount.getUsername()) : EmployeeDAO.getEmployeeNameByID(currentOrder.getemployeeID()));
        lblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nhanVienPanel.add(lblNhanVien);
        panel.add(nhanVienPanel);

        JPanel ngayXuatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ngayXuatPanel.add(new JLabel("Ngày xuất:"));
        lblNgayXuat = new JLabel(currentOrder == null ? LocalDate.now().toString() : currentOrder.getissuedate());
        lblNgayXuat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ngayXuatPanel.add(lblNgayXuat);
        panel.add(ngayXuatPanel);

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tongTienPanel.add(new JLabel("Tổng tiền:"));
        lblTongTien = new JLabel(currentOrder == null ? "0" : currentOrder.gettotalmoney());
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(0, 100, 0));
        tongTienPanel.add(lblTongTien);
        panel.add(tongTienPanel);

        return panel;
    }

    private JPanel createAllProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new TitledBorder("Danh sách sản phẩm"), new EmptyBorder(5, 5, 5, 5)));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Mã SP", "Tên SP", "Loại", "Đơn giá", "Tồn kho"};
        productTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        allProductsTable = new JTable(productTableModel);
        allProductsTable.setRowHeight(30);
        allProductsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allProductsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = allProductsTable.getColumnModel();
        for (int i = 0; i < allProductsTable.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

        allProductsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = allProductsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayProductDetails(
                        productTableModel.getValueAt(selectedRow, 0).toString(),
                        productTableModel.getValueAt(selectedRow, 1).toString(),
                        productTableModel.getValueAt(selectedRow, 2).toString(),
                        productTableModel.getValueAt(selectedRow, 3).toString()
                    );
                }
            }
        });

        panel.add(new JScrollPane(allProductsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrderProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new TitledBorder("Danh sách sản phẩm trong hóa đơn"), new EmptyBorder(5, 5, 5, 5)));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Mã SP", "Tên SP", "Loại", "Số lượng", "Đơn giá", "Thành tiền"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        productsTable = new JTable(orderTableModel);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        productsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String productId = orderTableModel.getValueAt(selectedRow, 0).toString();
                    String productName = orderTableModel.getValueAt(selectedRow, 1).toString();
                    String category = orderTableModel.getValueAt(selectedRow, 2).toString();
                    String quantity = orderTableModel.getValueAt(selectedRow, 3).toString();
                    String price = orderTableModel.getValueAt(selectedRow, 4).toString();

                    lblProductId.setText(productId);
                    lblProductName.setText(productName);
                    lblCategory.setText(category);
                    lblPrice.setText(price);
                    txtQuantity.setText(quantity);

                    String img = productBUS.getProductImage(productId);
                    if (img != null && !img.isEmpty()) {
                        lblProductImage.setIcon(new ImageIcon(new ImageIcon("images/" + img).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
                        lblProductImage.setText("");
                    } else {
                        lblProductImage.setIcon(null);
                        lblProductImage.setText("Không có ảnh");
                    }
                }
            }
        });

        TableColumnModel columnModel = productsTable.getColumnModel();
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {{
                setHorizontalAlignment(JLabel.CENTER);
            }});
        }

        panel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProductDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new CompoundBorder(new TitledBorder("Thông tin sản phẩm đang chọn"), new EmptyBorder(10, 10, 10, 10)));
        panel.setBackground(Color.WHITE);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(200, 200));
        imagePanel.setBackground(Color.WHITE);

        lblProductImage = new JLabel("Không có ảnh", SwingConstants.CENTER);
        lblProductImage.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
        imagePanel.add(lblProductImage);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; detailPanel.add(new JLabel("Mã sản phẩm:"), gbc);
        gbc.gridx = 1; lblProductId = new JLabel("Chọn sản phẩm từ danh sách"); detailPanel.add(lblProductId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; detailPanel.add(new JLabel("Tên sản phẩm:"), gbc);
        gbc.gridx = 1; lblProductName = new JLabel(); detailPanel.add(lblProductName, gbc);
        gbc.gridx = 0; gbc.gridy = 2; detailPanel.add(new JLabel("Loại sản phẩm:"), gbc);
        gbc.gridx = 1; lblCategory = new JLabel(); detailPanel.add(lblCategory, gbc);
        gbc.gridx = 0; gbc.gridy = 3; detailPanel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1; lblPrice = new JLabel(); detailPanel.add(lblPrice, gbc);
        gbc.gridx = 0; gbc.gridy = 4; detailPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1; txtQuantity = new JTextField(10); detailPanel.add(txtQuantity, gbc);
        gbc.gridx = 0; gbc.gridy = 5; detailPanel.add(new JLabel("Số điện thoại KH:"), gbc);
        gbc.gridx = 1; txtSoDienThoai = new JTextField(25); detailPanel.add(txtSoDienThoai, gbc);
        
        txtSoDienThoai.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String phone = txtSoDienThoai.getText().trim();

            // Chỉ xử lý khi đúng 10 số và bắt đầu bằng 0
            if (phone.matches("0\\d{9}")) {
                CustomerDTO customer = customerBUS.getCustomerByPhone(phone);
                if (customer != null) {
                    txtMaKhachHang.setText(customer.getcustomerID());
                    txtTenKhachHang.setText(customer.getFullName());
                    txtTenKhachHang.setEditable(false); // khóa lại nếu tìm thấy
                } else {
                    txtMaKhachHang.setText(customerBUS.getNextCustomerID()); // gán mã mới
                    txtTenKhachHang.setText("");
                    txtTenKhachHang.setEditable(true); // cho nhập tên nếu chưa tồn tại
                }
            } else {
                // Nếu không đúng 10 số → reset
                txtMaKhachHang.setText("");
                txtTenKhachHang.setText("");
                txtTenKhachHang.setEditable(false);
            }
        }
    });

        gbc.gridx = 0; gbc.gridy = 6; detailPanel.add(new JLabel("Mã khách hàng:"), gbc);
        gbc.gridx = 1; txtMaKhachHang = new JTextField(25); txtMaKhachHang.setEditable(false); detailPanel.add(txtMaKhachHang, gbc);

        gbc.gridx = 0; gbc.gridy = 7; detailPanel.add(new JLabel("Tên khách hàng:"), gbc);
        gbc.gridx = 1; txtTenKhachHang = new JTextField(25); txtTenKhachHang.setEditable(false); detailPanel.add(txtTenKhachHang, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bottomPanel.setBackground(Color.WHITE);
        CustomButton btnSuaSL = new CustomButton("Sửa SL");
        btnSuaSL.setCustomColor(new Color(0, 128, 255));
        btnSuaSL.addActionListener(e -> updateQuantityInOrder());
        bottomPanel.add(btnSuaSL);
        btnThemSP = new CustomButton("Thêm SP");
        btnThemSP.addActionListener(e -> addProductToOrder());
        bottomPanel.add(btnThemSP);
        CustomButton btnXoaSP = new CustomButton("Xóa SP");
        btnXoaSP.setCustomColor(new Color(255, 140, 0));
        btnXoaSP.addActionListener(e -> removeSelectedProductFromOrder());
        bottomPanel.add(btnXoaSP);

        panel.add(imagePanel, BorderLayout.WEST);
        panel.add(detailPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        btnLuu = new CustomButton("Lưu hóa đơn");
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.addActionListener(e -> saveOrder());
        btnHuy = new CustomButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnHuy.addActionListener(e -> dispose());
        panel.add(btnLuu);
        panel.add(btnHuy);
        return panel;
    }

    private void displayProductDetails(String productId, String productName, String category, String price) {
        lblProductId.setText(productId);
        lblProductName.setText(productName);
        lblCategory.setText(category);
        lblPrice.setText(price + " VND");

        String img = productBUS.getProductImage(productId);
        if (img != null && !img.isEmpty()) {
            lblProductImage.setIcon(new ImageIcon(new ImageIcon("images/" + img).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            lblProductImage.setText("");
        } else {
            lblProductImage.setIcon(null);
            lblProductImage.setText("Không có ảnh");
        }
    }
    
    private void updateQuantityInOrder() {
        try {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int oldQuantity = Integer.parseInt(orderTableModel.getValueAt(selectedRow, 3).toString());
                int oldPrice = Integer.parseInt(orderTableModel.getValueAt(selectedRow, 4).toString().replaceAll("[^0-9]", ""));
                int oldTotal = oldQuantity * oldPrice;

                int newQuantity = Integer.parseInt(txtQuantity.getText().trim());
                int newTotal = oldPrice * newQuantity;

                // Cập nhật lại dòng
                orderTableModel.setValueAt(newQuantity, selectedRow, 3);
                orderTableModel.setValueAt(formatCurrency(newTotal), selectedRow, 5);

                // Cập nhật tổng tiền
                totalAmount = totalAmount - oldTotal + newTotal;
                lblTongTien.setText(formatCurrency(totalAmount));

            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trong giỏ hàng để sửa.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa số lượng.");
        }
    }

    private void addProductToOrder() {
        try {
            String phone = txtSoDienThoai.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không được bỏ trống!");
                return;
            }
            if (!phone.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!\nPhải bắt đầu bằng 0 và gồm 10 chữ số.");
                return;
            }
            
            int selectedRow = allProductsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ danh sách.");
                return;
            }

            String quantityText = txtQuantity.getText().trim();
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.");
                return;
            }

            int tonKho = Integer.parseInt(productTableModel.getValueAt(selectedRow, 4).toString());
            if (quantity > tonKho) {
                JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho!");
                return;
            }
            
            String productId = lblProductId.getText();
            String productName = lblProductName.getText();
            String category = lblCategory.getText();
            int price = Integer.parseInt(lblPrice.getText().replaceAll("[^0-9]", ""));
            int total = price * quantity;
            orderTableModel.addRow(new Object[]{productId, productName, category, quantity, formatCurrency(price), formatCurrency(total)});
            totalAmount += total;
            lblTongTien.setText(formatCurrency(totalAmount));
            // Khóa thông tin khách
            txtSoDienThoai.setEditable(false);
            txtTenKhachHang.setEditable(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm.");
        }
    }

    private void removeSelectedProductFromOrder() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int thanhTien = Integer.parseInt(orderTableModel.getValueAt(selectedRow, 5).toString().replaceAll("[^0-9]", ""));
            totalAmount -= thanhTien;
            lblTongTien.setText(formatCurrency(totalAmount));
            orderTableModel.removeRow(selectedRow);
            // Mở lại nhập thông tin khách nếu giỏ trống
            if (orderTableModel.getRowCount() == 0) {
                txtSoDienThoai.setEditable(true);
                txtTenKhachHang.setEditable(true);
            }
        }
    }


    private void loadAllProducts() {
        productTableModel.setRowCount(0);
        List<ProductDTO> list = ProductDAO.getAllProducts();
        for (ProductDTO p : list) {
            productTableModel.addRow(new Object[]{
                p.getProductID(),
                p.getProductName(),
                p.getTL(),
                p.getGia(),
                p.getSoluong()
            });
        }
    }

    private void loadOrderData() {
        if (currentOrder != null) {
            List<DetailOrderDTO> chiTietList = DetailOrderBUS.getDetailOrderByOrderID(currentOrder.getorderID());
            for (DetailOrderDTO ct : chiTietList) {
                ProductDTO sp = productBUS.getProductByID(ct.getproductID());
                if (sp != null) {
                    String tenSP = sp.getProductName();
                    String loai = sp.getTL();
                    int soLuong = Integer.parseInt(ct.getamount());
                    int donGia = Integer.parseInt(ct.getprice());
                    int thanhTien = donGia * soLuong;
                    orderTableModel.addRow(new Object[]{
                        ct.getproductID(),
                        tenSP,
                        loai,
                        soLuong,
                        formatCurrency(donGia),
                        formatCurrency(thanhTien)
                    });
                    totalAmount += thanhTien;
                }
            }
            lblTongTien.setText(formatCurrency(totalAmount));
            // Hiển thị thông tin khách hàng từ currentOrder
            CustomerDTO kh = customerBUS.getCustomerByID(currentOrder.getcustomerID());
            if (kh != null) {
                txtMaKhachHang.setText(kh.getcustomerID());
                txtTenKhachHang.setText(kh.getFullName());
                txtSoDienThoai.setText(kh.getPhone());
                txtTenKhachHang.setEditable(false);
                txtSoDienThoai.setEditable(false);
            }
        }
    }

    private void saveOrder() {
        String orderID = lblMaHoaDon.getText();
        String maKH = txtMaKhachHang.getText().trim();
        String tenKH = txtTenKhachHang.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        // 1. Nếu khách hàng chưa tồn tại, thêm vào database
        CustomerDTO existingCustomer = customerBUS.getCustomerByPhone(sdt);
        if (existingCustomer == null) {
            // Trường hợp khách mới => thêm vào
            CustomerDTO newCustomer = new CustomerDTO(maKH, tenKH, sdt, ""); // Có thể thêm email nếu có
            customerBUS.addCustomer(newCustomer);  // Bạn cần tạo hàm addCustomer trong BUS và DAO
        }

        // 2. Lưu hóa đơn
        OrderDTO dto = new OrderDTO();
        dto.setorderID(orderID);
        dto.setemployeeID(currentAccount.getUsername());
        dto.setcustomerID(maKH);
        dto.settotalmoney(String.valueOf(totalAmount));
        dto.setissuedate(LocalDate.now().toString());

        if (currentOrder == null) orderBUS.addOrder(dto);
        else orderBUS.updateOrder(dto);

        // 3. Lưu chi tiết hóa đơn
        DetailOrderBUS detailOrderBUS = new DetailOrderBUS();
        if (currentOrder != null) {
            detailOrderBUS.deleteByOrderID(orderID);
        }
        int baseNumber = DetailOrderDAO.getMaxDetailOrderNumber() + 1;

        for (int i = 0; i < orderTableModel.getRowCount(); i++) {
            DetailOrderDTO detail = new DetailOrderDTO();
            String detailID = String.format("CTHD%03d%03d", baseNumber, i + 1);

            detail.setdetailorderID(detailID);
            detail.setorderID(orderID);
            detail.setproductID(orderTableModel.getValueAt(i, 0).toString());
            detail.setserialID("");
            detail.setamount(orderTableModel.getValueAt(i, 3).toString());
            String priceStr = orderTableModel.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
            detail.setprice(priceStr);

            detailOrderBUS.addDetailOrder(detail);
        }
        
        JOptionPane.showMessageDialog(this, "Lưu hóa đơn thành công!");
        dispose();
    }

    private String formatCurrency(int amount) {
        return String.format("%,d VND", amount);
    }
    
    private boolean hasShownError = false;
}