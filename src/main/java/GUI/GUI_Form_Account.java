package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI_Form_Account extends JDialog {
    private JTextField txtEmployeeName, txtAccount;
    private JPasswordField txtPassword;
    private final CustomCombobox<String> cbRole;
    private CustomButton btnSave, btnCancel;

    public GUI_Form_Account(JPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Thêm Tài Khoản", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addComponent("Nhân Viên:", txtEmployeeName = new JTextField(20), gbc);
        addComponent("Mật Khẩu:", txtAccount = new JTextField(20), gbc);
        addComponent("Nhập lại mật Khẩu:", txtPassword = new JPasswordField(20), gbc);
        
        // Sửa lỗi dấu ";" và dùng CustomCombobox đúng kiểu
        cbRole = new CustomCombobox<>(new String[]{"Admin", "Nhân Viên", "Quản Lý"});
        addComponent("Quyền:", cbRole, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton("Lưu");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        btnCancel.addActionListener(e -> dispose());
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(component, gbc);
    }
}
