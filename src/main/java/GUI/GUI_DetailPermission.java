package GUI;

import DTO.PermissionDTO;
import BUS.PermissionBUS;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUI_DetailPermission extends JFrame {

    public GUI_DetailPermission(PermissionDTO permission) {
        setTitle("Chi Tiết Quyền");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitle = new JLabel("CHI TIẾT QUYỀN: " + permission.getName());
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(lblTitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Function list
        JTextArea txt = new JTextArea();
        List<String> functions = permission.getChucNang();

        for (int i = 0; i < functions.size(); i++) {
            String line = (i + 1) + ". " + PermissionBUS.decodeFunctionName(functions.get(i)) + "\n";
            txt.setText(txt.getText() + line); // Cập nhật nội dung mỗi lần lặp
        }
        CustomScrollPane scrollPane = new CustomScrollPane(txt);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. Close button
        CustomButton btnClose = new CustomButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
