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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Nhập Hàng Mới");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);
        
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        addComponent("Mã Nhập Hàng:", txtMaNhapHang = new JLabel(generateNextImportID()), gbc);
        addComponent("Nhân Viên:", lblNhanVien = new JLabel(getCurrentUser()), gbc);
        addComponent("Ngày Nhập:", txtNgayNhap = new JLabel(LocalDate.now().toString()), gbc);
        addComponent("Nhà Cung Cấp:", cbNhaCungCap = new JComboBox<>(fetchSuppliers()), gbc);
        addComponent("Sản Phẩm:", cbSanPham = new JComboBox<>(), gbc);
        cbNhaCungCap.addActionListener(e -> updateProductList());
        addComponent("Số Lượng:", txtSoLuong = new JTextField(12), gbc);
        addComponent("Tổng Tiền:", lblTongTien = new JLabel("0"), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        styleButton(btnSave);
        styleButton(btnCancel);
        
        btnCancel.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(component, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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