package GUI;

import BUS.AccountBUS;
import DAO.AccountDAO;
import DAO.PermissionDAO;
import DTO.EmployeeDTO;
import javax.swing.*;
import java.awt.*;
import DTO.AccountDTO;
import DAO.EmployeeDAO;
import DTO.PermissionDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI_Form_Account extends JDialog {

    private JTextField txtEditPassword;
    private JPasswordField txtRePassword, txtPassword;
    private JLabel title, lblEmployeeName, txtAccount;
    private CustomCombobox<String> cbRole, cbEmployeeName;
    private CustomButton btnSave, btnCancel;

    public GUI_Form_Account(JPanel parent, AccountDTO account) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), account == null ? "Thêm Tài Khoản" : "Sửa Tài Khoản", true);
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
        title = new JLabel(account == null ? "THÊM TÀI KHOẢN" : "SỬA TÀI KHOẢN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Nếu account != null => Sửa tài khoản
        lblEmployeeName = new JLabel();
        java.util.List<EmployeeDTO> employees = EmployeeDAO.getEmployeesWithoutAccount();
        String[] names = new String[employees.size()];
        int i1 = 0;
        for (EmployeeDTO emp : employees) {
            names[i1] = emp.getFullName();
            System.out.println("names[i]");
            i1++;
        }
        cbEmployeeName = new CustomCombobox<>(names);
        txtAccount = new JLabel();
        txtPassword = new JPasswordField(20);
        txtRePassword = new JPasswordField(20);
        txtEditPassword = new JTextField(20);
//        cbRole = new CustomCombobox<>();
        java.util.List<PermissionDTO> permissions = PermissionDAO.getAllPermissions();
        String[] roles = new String[permissions.size()];
        int i = 0;
        for (PermissionDTO per : permissions) {
            roles[i] = per.getName(); // Lấy tên quyền và gán vào mảng roles
            i++;
        }
//        for (String role : roles) {
//            cbRole.addItem(role);
//        }
//        cbRole = new CustomCombobox<>(roles.toArray(new String[0]));
        cbRole = new CustomCombobox<>(roles);

        if (account != null) {
            lblEmployeeName.setText(account.getFullname());
            txtAccount.setText(account.getUsername());
            txtPassword.setText(account.getPassword());
            txtRePassword.setText(account.getPassword());
            cbRole.setSelectedItem(account.getPermission().getName());
            txtEditPassword.setText(account.getPassword());
//            String quyen = account.getTenquyen();
//            System.out.println(quyen);

//            if (quyen != null) {
//                cbRole.setSelectedItem(quyen);
//            } else {
//                cbRole.setSelectedIndex(-1); // Không chọn mục nào nếu không hợp lệ
//            }
        }
        if (account != null) {
            addComponent("Nhân Viên:", lblEmployeeName, gbc);
            addComponent("Tài Khoản: ", txtAccount, gbc);
            addComponent("Mật Khẩu:", txtEditPassword, gbc);
        } else {
            addComponent("Nhân Viên:", cbEmployeeName, gbc);
            addComponent("Mật Khẩu: ", txtPassword, gbc);
            addComponent("Nhập Lại Mật Khẩu:", txtRePassword, gbc);
        }

        addComponent("Quyền:", cbRole, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton(account == null ? "Thêm" : "Cập Nhật");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (account == null) {
                    System.out.println(cbEmployeeName.getSelectedItem().toString());
                    char[] passwordChars = txtPassword.getPassword();
                    String password = new String(passwordChars);
                    System.out.println(password);
                    if (AccountBUS.addAccount(cbEmployeeName.getSelectedItem().toString(), password, cbRole.getSelectedItem().toString())) {
                        System.out.println("Thanh cong");
                    } else {
                        System.out.println("That bai");
                    }
                } else { 
                    String username = txtAccount.getText();
                    String password = txtEditPassword.getText();
                    String role = cbRole.getSelectedItem().toString();
                    System.out.println(username+password+role);
                    if (AccountBUS.updateAccount(username, password, role) == true) {
                        System.out.println("Thanh cong");
                        dispose();
                        
                    }
                    else {
                        System.out.println("That bai");
                    }
                }
            }
        });
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
