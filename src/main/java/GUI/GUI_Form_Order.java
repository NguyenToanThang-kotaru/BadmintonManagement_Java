package GUI;

import BUS.OrderBUS;
import DTO.OrderDTO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GUI_Form_Order extends JDialog {

    private final JLabel txtMaHoaDon, txtNgayXuat, lblNhanVien, lblTongTien;
    private JTextField txtSoLuong, txtMaKhachHang, txtTenKhachHang, txtSoDienThoai;
    private CustomButton btnAdd, btnDelete, btnExport, btnCancel;
    private CustomCombobox<String> cbMaLoai, cbSanPham;
    private JPanel panelOrder;
    private OrderBUS orderBUS;
    private OrderDTO currentOrder;

    public GUI_Form_Order(JPanel parent, OrderDTO order) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Tạo Hóa Đơn", true);
        this.orderBUS = new OrderBUS();
        this.currentOrder = order;

        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelMain = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel(currentOrder == null ? "Tạo Hóa Đơn" : "Sửa Hóa Đơn");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        panelMain.add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        txtSoLuong = new JTextField(10);
        lblTongTien = new JLabel("0");
        txtMaKhachHang = new JTextField(10);
        txtTenKhachHang = new JTextField(10);
        txtSoDienThoai = new JTextField(10);
        cbMaLoai = new CustomCombobox<>(fetchCategories().toArray(new String[0]));
        cbSanPham = new CustomCombobox<>(fetchProducts("").toArray(new String[0]));

        txtMaHoaDon = new JLabel(currentOrder == null ? generateNextOrderID() : currentOrder.getorderID()); 
        lblNhanVien = new JLabel(currentOrder == null ? getCurrentUser() : order.getemployeeID());
        txtNgayXuat = new JLabel(currentOrder == null ? LocalDate.now().toString() : order.getissuedate());

        addComponent("Mã Hóa Đơn:", txtMaHoaDon, panelMain, gbc);
        addComponent("Nhân Viên:", lblNhanVien, panelMain, gbc);
        addComponent("Ngày Xuất:", txtNgayXuat, panelMain, gbc);
        addComponent("Mã Khách Hàng:", txtMaKhachHang, panelMain, gbc);
        addComponent("Tên Khách Hàng:", txtTenKhachHang, panelMain, gbc);
        addComponent("Số Điện Thoại:", txtSoDienThoai, panelMain, gbc);
        addComponent("Loại Sản Phẩm:", cbMaLoai, panelMain, gbc);
        addComponent("Sản Phẩm:", cbSanPham, panelMain, gbc);
        addComponent("Số Lượng:", txtSoLuong, panelMain, gbc);
        addComponent("Tổng Tiền:", lblTongTien, panelMain, gbc);

        panelOrder = new JPanel();
        panelOrder.setBorder(BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"));
        panelOrder.setPreferredSize(new Dimension(550, 200));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panelMain.add(panelOrder, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new CustomButton("Thêm");
        btnDelete = new CustomButton("Xóa");
        btnExport = new CustomButton("Xuất Hóa Đơn");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnCancel);

        gbc.gridy++;
        panelMain.add(buttonPanel, gbc);

        add(panelMain, BorderLayout.CENTER);

        if (currentOrder != null) {
            // Gán dữ liệu nếu đang sửa
            txtMaKhachHang.setText(currentOrder.getcustomerID());
            lblTongTien.setText(currentOrder.gettotalmoney());
            txtNgayXuat.setText(currentOrder.getissuedate());
        }

        btnCancel.addActionListener(e -> dispose());

        btnExport.addActionListener(e -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setorderID(txtMaHoaDon.getText());
            orderDTO.setemployeeID(lblNhanVien.getText());
            orderDTO.setcustomerID(txtMaKhachHang.getText());
            orderDTO.settotalmoney(lblTongTien.getText());
            orderDTO.setissuedate(txtNgayXuat.getText());

            if (currentOrder == null) {
                orderBUS.addOrder(orderDTO);
                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
            } else {
                orderBUS.updateOrder(orderDTO);
                JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!");
            }
            dispose();
        });
    }

    private void addComponent(String label, JComponent component, JPanel panel, GridBagConstraints gbcNew) {
        gbcNew.gridx = 0;
        gbcNew.gridy++;
        gbcNew.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbcNew);
        gbcNew.gridx = 1;
        panel.add(component, gbcNew);
    }

    private List<String> fetchCategories() {
        return List.of("Loai 1", "Loai 2", "Loai 3");
    }

    private List<String> fetchProducts(String loai) {
        return List.of("SanPham A", "SanPham B");
    }

    private String getCurrentUser() {
        return "NV001"; // bạn lấy từ session thật nếu có
    }

    private String generateNextOrderID() {
        return "HD" + System.currentTimeMillis();
        
    }
}
