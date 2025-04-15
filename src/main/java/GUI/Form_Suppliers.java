package GUI;

import BUS.SuppliersBUS;
import DTO.SuppliersDTO;

import javax.swing.*;
import java.awt.*;  

public class Form_Suppliers extends JDialog {
    
    private JLabel lblSupplierID;
    private JTextField txtName, txtAddress, txtPhone;
    private CustomButton btnSave, btnCancel;
    private SuppliersBUS suppliersBUS;
    private GUI_Suppliers parent;

    public Form_Suppliers(GUI_Suppliers parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Thêm Nhà Cung Cấp", true);
        this.parent = parent;
        suppliersBUS = new SuppliersBUS();

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

        JLabel title = new JLabel("Thêm Thông Tin Nhà Cung Cấp");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        lblSupplierID = new JLabel(suppliersBUS.generateSupplierID());
        txtName = new JTextField(20);
        txtAddress = new JTextField(20);
        txtPhone = new JTextField(15);

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

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            try {
                SuppliersDTO supplier = new SuppliersDTO(
                        lblSupplierID.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtPhone.getText()
                );
                suppliersBUS.addSupplier(supplier);
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
                dispose();
                parent.loadSuppliers();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }
}