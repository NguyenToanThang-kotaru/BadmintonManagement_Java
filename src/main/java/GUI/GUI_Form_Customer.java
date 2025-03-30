package GUI;

import javax.swing.*;
import java.awt.*;
import DTO.CustomerDTO;

public class GUI_Form_Customer extends JDialog {

    private JTextField txtCustomerID, txtFullName, txtSDT, txtEmail;
    private JLabel title;
    private CustomCombobox<String> cbRole;
    private CustomButton btnSave, btnCancel;

    public GUI_Form_Customer(JPanel parent, CustomerDTO customer) {
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
        txtEmail = new JTextField(20);

        if (customer != null) {
            txtCustomerID.setText(customer.getcustomerID());
            txtFullName.setText(customer.getFullName());
            txtSDT.setText(customer.getPhone());
            txtEmail.setText(customer.getEmail());
        }

        addComponent("Mã Khách Hàng:", txtCustomerID, gbc);
        addComponent("Tên Khách Hàng:", txtFullName, gbc);
        addComponent("Số Điện Thoại:", txtSDT, gbc);
        addComponent("Email:", txtEmail, gbc);

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

//        btnSave.addActionListener(e -> {
//            if (customer == null) {
//                saveNewCustomer();
//            } else {
//                updateCustomer(customer);
//            }
//        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;  // Không mở rộng label
        gbc.fill = GridBagConstraints.NONE;  

        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;  // Cho phép JTextField mở rộng
        gbc.fill = GridBagConstraints.HORIZONTAL;  

        add(component, gbc);
    }
}