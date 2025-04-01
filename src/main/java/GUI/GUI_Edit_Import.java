
    package GUI;
    
    import javax.swing.*;
    import java.awt.*;
    import java.time.LocalDate;
    import BUS.ImportBUS;
    import DTO.ImportDTO;
    
    public class GUI_Edit_Import extends JDialog {
        private final ImportDTO importDTO;
        private final GUI_Import parent;
        private final JLabel txtMaNhapHang, txtNgayNhap, lblNhanVien, lblTongTien;
        private JTextField txtSoLuong;
        private CustomButton btnSave, btnCancel;
        private CustomCombobox<String> cbNhaCungCap, cbSanPham;
    
        public GUI_Edit_Import(GUI_Import parent, ImportDTO importDTO) {
            super((Frame) SwingUtilities.getWindowAncestor(parent), "Sửa Phiếu Nhập", true);
            this.importDTO = importDTO;
            this.parent = parent;
    
            setSize(500, 400);
            setLocationRelativeTo(parent);
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            
            // Title
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JLabel title = new JLabel("Sửa Phiếu Nhập");
            title.setFont(new Font("Arial", Font.BOLD, 18));
            title.setForeground(new Color(52, 73, 94));
            add(title, gbc);
            
            // Form fields
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            
            txtSoLuong = new JTextField(10);
            lblTongTien = new JLabel(importDTO.gettotalmoney());
            cbNhaCungCap = new CustomCombobox<>(fetchSuppliers());
            cbSanPham = new CustomCombobox<>(fetchProduct());
            
            addComponent("Mã Nhập Hàng:", txtMaNhapHang = new JLabel(importDTO.getimportID()), gbc);
            addComponent("Nhân Viên:", lblNhanVien = new JLabel(importDTO.getemployeeID()), gbc);
            addComponent("Ngày Nhập:", txtNgayNhap = new JLabel(importDTO.getreceiptdate()), gbc);
            addComponent("Nhà Cung Cấp:", cbNhaCungCap, gbc);
            addComponent("Sản Phẩm:", cbSanPham, gbc);
            addComponent("Số Lượng:", txtSoLuong, gbc);
            addComponent("Tổng Tiền:", lblTongTien, gbc);
    
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            btnSave = new CustomButton("Lưu");
            btnCancel = new CustomButton("Hủy");
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
    
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            add(buttonPanel, gbc);
            
            // Set current values
            cbNhaCungCap.setSelectedItem(importDTO.getsupplierID());
            
            // Event handlers
            btnSave.addActionListener(e -> saveChanges());
            btnCancel.addActionListener(e -> dispose());
            cbNhaCungCap.addActionListener(e -> updateProductList());
        }
    
        private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.WEST;
            add(new JLabel(label), gbc);
    
            gbc.gridx = 1;
            add(component, gbc);
        }
    
        private String[] fetchSuppliers() {
            return new String[]{"NCC001", "NCC002", "NCC003"};
        }
    
        private String[] fetchProduct() {
            return new String[]{"Vot1", "Cau1", "88D Game Proodcsioocs"};
        }
    
        private void updateProductList() {
            // Implementation to update products based on selected supplier
        }
    
        private void saveChanges() {
            // Get updated values
            String newSupplierID = (String) cbNhaCungCap.getSelectedItem();
            String newTotalMoney = lblTongTien.getText();
            
            // Update DTO
            importDTO.setsupplierID(newSupplierID);
            importDTO.settotalmoney(newTotalMoney);
            
            // Save to database
            ImportBUS importBUS = new ImportBUS();
            importBUS.updateImport(importDTO);
            
            // Refresh parent
            parent.loadImport();
            dispose();
        }
    }

//     

