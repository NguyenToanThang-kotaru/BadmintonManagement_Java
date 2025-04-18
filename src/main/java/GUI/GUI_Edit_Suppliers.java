package GUI;

import BUS.Edit_SuppliersBUS;
import DTO.SuppliersDTO;

import javax.swing.*;
import java.awt.*;

public class GUI_Edit_Suppliers extends JDialog {

    private JLabel lblSupplierID;
    private JTextField txtName, txtAddress, txtPhone;
    private CustomButton btnSave, btnCancel;
    private Edit_SuppliersBUS editSuppliersBUS;
    private GUI_Suppliers parent;
    private SuppliersDTO supplier;

    public GUI_Edit_Suppliers(GUI_Suppliers parent, SuppliersDTO supplier) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Sửa Nhà Cung Cấp", true);
        this.parent = parent;
        this.supplier = supplier;
        editSuppliersBUS = new Edit_SuppliersBUS();

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

        JLabel title = new JLabel("Sửa Thông Tin Nhà Cung Cấp");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        lblSupplierID = new JLabel(supplier.getsuppliersID());
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

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            try {
                SuppliersDTO updatedSupplier = new SuppliersDTO(
                        lblSupplierID.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtPhone.getText()
                );
                editSuppliersBUS.updateSupplier(updatedSupplier);
                JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thành công!");
                dispose();
                parent.loadSuppliers();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhà cung cấp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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