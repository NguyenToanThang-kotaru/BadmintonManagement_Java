package GUI;

import BUS.CustomerBUS;
import DTO.CustomerDTO;

import javax.swing.*;
import java.awt.*;

public class Form_Customer extends JDialog {

    private JTextField txtCustomerID, txtFullName, txtSDT;
    private JLabel title;
    private CustomButton btnSave, btnCancel;
    private CustomerBUS customerBUS = new CustomerBUS();

    public Form_Customer(JPanel parent, CustomerDTO customer) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), customer == null ? "Thêm Khách Hàng" : "Sửa Khách Hàng", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Đổi tiêu đề form
        title = new JLabel(customer == null ? "THÊM KHÁCH HÀNG" : "SỬA KHÁCH HÀNG");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Nếu customer != null => Sửa khách hàng
        txtCustomerID = new JTextField(20);
        txtFullName = new JTextField(20);
        txtSDT = new JTextField(20);

        if (customer != null) {
            txtCustomerID.setText(customer.getcustomerID());
            txtCustomerID.setEditable(false); // Không cho sửa
            txtCustomerID.setForeground(Color.BLACK);
            txtCustomerID.setBackground(Color.WHITE); 
            txtFullName.setText(customer.getFullName());
            txtSDT.setText(customer.getPhone());
        } else {
            txtCustomerID.setText(customerBUS.getNextCustomerID());
            txtCustomerID.setEditable(false); // Không cho sửa
            txtCustomerID.setForeground(Color.BLACK);
            txtCustomerID.setBackground(Color.WHITE);

        }

        addComponent("Mã Khách Hàng:", txtCustomerID, gbc);
        addComponent("Tên Khách Hàng:", txtFullName, gbc);
        addComponent("Số Điện Thoại:", txtSDT, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton(customer == null ? "Thêm" : "Cập Nhật");
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
            String customerID = txtCustomerID.getText().trim();
            String fullName = txtFullName.getText().trim();
            String phone = txtSDT.getText().trim();

            if (fullName.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CustomerDTO newCustomer = new CustomerDTO(customerID, fullName, phone);

            if (customer == null) {
                customerBUS.addCustomer(newCustomer);
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            } else {
                customerBUS.updateCustomer(newCustomer);
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            }
            dispose();
        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE;  

        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  

        add(component, gbc);
    }
}