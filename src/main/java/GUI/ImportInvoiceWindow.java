package GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ImportInvoiceWindow extends JPanel {
    private JLabel txtMaNhapHang, txtNgayNhap, lblNhanVien, lblTongTien;
    private JTextField txtSoLuong;
    private JComboBox<String> cbNhaCungCap, cbSanPham;
    
    public ImportInvoiceWindow() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Mã Nhập Hàng:"), gbc);
        gbc.gridx = 1;
        txtMaNhapHang = new JLabel(generateNextImportID());
        add(txtMaNhapHang, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1;
        lblNhanVien = new JLabel(getCurrentUser());
        add(lblNhanVien, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Ngày Nhập:"), gbc);
        gbc.gridx = 1;
        txtNgayNhap = new JLabel(LocalDate.now().toString());
        add(txtNgayNhap, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Nhà Cung Cấp:"), gbc);
        gbc.gridx = 1;
        cbNhaCungCap = new JComboBox<>(fetchSuppliers());
        add(cbNhaCungCap, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Sản Phẩm:"), gbc);
        gbc.gridx = 1;
        cbSanPham = new JComboBox<>();
        add(cbSanPham, gbc);

        cbNhaCungCap.addActionListener(e -> updateProductList());

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Số Lượng:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField(12);
        add(txtSoLuong, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Tổng Tiền:"), gbc);
        gbc.gridx = 1;
        lblTongTien = new JLabel("0");
        add(lblTongTien, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.insets = new Insets(5, 10, 5, 10);
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 0;
        
        JButton btnSave = new CustomButton("Lưu");
        JButton btnCancel = new CustomButton("Hủy");
        
        buttonPanel.add(btnSave, gbcBtn);
        gbcBtn.gridx = 1;
        buttonPanel.add(btnCancel, gbcBtn);

        btnCancel.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private String generateNextImportID() {
        return "NH009";
    }

    private String[] fetchSuppliers() {
        return new String[]{"NCC001", "NCC002", "NCC003"};
    }

    private void updateProductList() {
    }

    private String getCurrentUser() {
        return "NV021";
    }
}
