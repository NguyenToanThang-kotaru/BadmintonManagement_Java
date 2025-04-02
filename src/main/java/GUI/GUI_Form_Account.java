package GUI;

import DAO.PermissionDAO;
import javax.swing.*;
import java.awt.*;
import DTO.AccountDTO;
import DTO.PermissionDTO;
import java.util.ArrayList;

public class GUI_Form_Account extends JDialog {

    private JTextField txtEmployeeName, txtAccount;
    private JPasswordField txtPassword;
    private JLabel title, lblEmployeeName;
    private CustomCombobox<String> cbRole;
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
        txtEmployeeName = new JTextField(20);
        txtAccount = new JTextField(20);
        txtPassword = new JPasswordField(20);

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
            cbRole.setSelectedItem(account.getTenquyen());
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
        } else {
            addComponent("Nhân Viên:", txtEmployeeName, gbc);
        }
        addComponent("Tên Đăng Nhập:", txtAccount, gbc);
        addComponent("Mật Khẩu:", txtPassword, gbc);
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

//        btnSave.addActionListener(e -> {
//            if (account == null) {
//                saveNewAccount();
//            } else {
//                updateAccount(account);
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
