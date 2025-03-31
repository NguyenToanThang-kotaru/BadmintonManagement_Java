/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import DTO.AccountDTO;
import DTO.EmployeeDTO;
import DAO.EmployeeDAO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Thang Nguyen
 */
public class GUI_Form_Employee extends JDialog {

    private JTextField txtEmployeeName, txtAddress, txtPhone;
    private JLabel title, lblEmployeeName;
    private CustomButton btnSave, btnCancel;

    public GUI_Form_Employee(JPanel parent, EmployeeDTO employee) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), employee == null ? "Thêm Tài Khoản" : "Sửa Tài Khoản", true);
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
        title = new JLabel(employee == null ? "THÊM NHÂN VIÊN" : "SỬA NHÂN VIÊN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Nếu employee != null => Sửa tài khoản
        txtEmployeeName = new JTextField(20);
        txtAddress = new JTextField(20);
        txtPhone = new JPasswordField(20);

        if (employee != null) {
            lblEmployeeName.setText(employee.getFullName());
            txtAddress.setText(employee.getPhone());
            txtPhone.setText(employee.getAddress());
//            String quyen = employee.getTenquyen();
//            System.out.println(quyen);

//            if (quyen != null) {
//                cbRole.setSelectedItem(quyen);
//            } else {
//                cbRole.setSelectedIndex(-1); // Không chọn mục nào nếu không hợp lệ
//            }
        }

        addComponent("Tên Nhân Viên:", txtEmployeeName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton(employee == null ? "Thêm" : "Cập Nhật");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        btnCancel.addActionListener(e -> dispose());

//        if (employee == null) {
            btnSave.addActionListener(e -> {
                String name = txtEmployeeName.getText().trim();
                String address = txtAddress.getText().trim();
                String phone = txtPhone.getText().trim();
                EmployeeDTO newEmployee = new EmployeeDTO(null, name, address, phone);
                EmployeeDAO.addEmployee(newEmployee);
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
//        }
//        btnSave.addActionListener(e -> {
//            if (employee == null) {
//                saveNewAccount();
//            } else {
//                updateAccount(employee);
//            }
//        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {

        gbc.gridx = 0;
        gbc.gridy++; // Giữ thứ tự đúng
        gbc.anchor = GridBagConstraints.WEST;

        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }
}
