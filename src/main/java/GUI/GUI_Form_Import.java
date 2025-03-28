package GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class GUI_Form_Import extends JDialog {

    private final JLabel txtMaNhapHang, txtNgayNhap, lblNhanVien, lblTongTien;
    private JTextField txtSoLuong;
    private CustomButton btnSave, btnCancel;
    private CustomCombobox<String> cbNhaCungCap,cbSanPham;

    public GUI_Form_Import(JPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Nhập Hàng", true);

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel title = new JLabel("Nhập Hàng Mới");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        txtSoLuong = new JTextField(10);
        lblTongTien = new JLabel("0");
        cbNhaCungCap = new CustomCombobox<>(fetchSuppliers());
        cbSanPham = new CustomCombobox<>(fetchProduct());
        addComponent("Mã Nhập Hàng:", txtMaNhapHang = new JLabel(generateNextImportID()), gbc);
        addComponent("Nhân Viên:", lblNhanVien = new JLabel(getCurrentUser()), gbc);
        addComponent("Ngày Nhập:", txtNgayNhap = new JLabel(LocalDate.now().toString()), gbc);

        addComponent("Nhà Cung Cấp:", cbNhaCungCap, gbc);
        addComponent("Sản Phẩm:", cbSanPham, gbc);
        addComponent("Số Lượng:", txtSoLuong, gbc);
        addComponent("Tổng Tiền:", lblTongTien, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton("Thêm");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
        cbNhaCungCap.addActionListener(e -> updateProductList());
        btnCancel.addActionListener(e -> dispose());
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbcNew) {
        gbcNew.gridx = 0;
        gbcNew.gridy++; // Giữ thứ tự đúng
        gbcNew.anchor = GridBagConstraints.WEST;

        add(new JLabel(label), gbcNew);

        gbcNew.gridx = 1;

        add(component, gbcNew);
    }

    private String generateNextImportID() {
        return "NH009";
    }

    private String[] fetchSuppliers() {
        return new String[]{"NCC001", "NCC002", "NCC003"};
    }
    private String[] fetchProduct() {
        return new String[]{"Vot1", "Cau1", "88D Game Proodcsioocs"};
    }

    private void updateProductList() {
    }

    private String getCurrentUser() {
        return "NV021";
    }
}
