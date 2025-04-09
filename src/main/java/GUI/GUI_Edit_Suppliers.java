package GUI;

import BUS.Edit_SuppliersBUS;
import DTO.SuppliersDTO;
import javax.swing.*;
import java.awt.*;

public class GUI_Edit_Suppliers extends JDialog {
    
    private JLabel lblSupplierID; // Thay JTextField bằng JLabel
    private JTextField txtName, txtAddress, txtPhone;
    private CustomButton btnSave, btnCancel;
    private Edit_SuppliersBUS editSuppliersBUS;
    private GUI_Suppliers parentPanel;

    public GUI_Edit_Suppliers(GUI_Suppliers parent, SuppliersDTO supplier) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Chỉnh Sửa Nhà Cung Cấp", true);
        
        editSuppliersBUS = new Edit_SuppliersBUS();
        parentPanel = parent;

        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Chỉnh Sửa Thông Tin Nhà Cung Cấp");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        // Khởi tạo các trường nhập liệu với dữ liệu hiện có
        lblSupplierID = new JLabel(supplier.getsuppliersID()); // Không cho phép sửa mã NCC
        txtName = new JTextField(supplier.getfullname(), 20);
        txtAddress = new JTextField(supplier.getaddress(), 20);
        txtPhone = new JTextField(supplier.getphone(), 15);

        addComponent("Mã Nhà Cung Cấp:", lblSupplierID, gbc);
        addComponent("Tên Nhà Cung Cấp:", txtName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

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

        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());

        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> {
            String supplierID = lblSupplierID.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SuppliersDTO updatedSupplier = new SuppliersDTO(supplierID, name, address, phone);
            editSuppliersBUS.updateSupplier(updatedSupplier);
            JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thành công!");
            dispose();
            parentPanel.loadSuppliers(); // Cập nhật lại danh sách trong GUI_Suppliers
        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbcNew) {
        gbcNew.gridx = 0;
        gbcNew.gridy++;
        gbcNew.anchor = GridBagConstraints.WEST;
        add(new JLabel(label), gbcNew);

        gbcNew.gridx = 1;
        add(component, gbcNew);
    }
}