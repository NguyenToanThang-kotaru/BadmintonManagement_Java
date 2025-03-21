package GUI;

import BUS.AccountBUS;
import DTO.AccountDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import BUS.AccountBUS;

public class GUI_Account extends JPanel {

    // Khai báo các thành phần giao diện
    private JPanel midPanel, topPanel, botPanel;
    private JTable accountTable;
    private DefaultTableModel tableModel;
//    private JComboBox<String> roleComboBox;
    private CustomButton deleteButton, addButton, editButton;
    private CustomSearch searchField;
    private AccountBUS accountBUS;

    public GUI_Account() {
        accountBUS = new AccountBUS(); // Khởi tạo đối tượng BUS để lấy dữ liệu tài khoản

        // Cấu hình layout chính
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));
        
        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút thêm) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275,20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm tài khoản"); // Nút thêm tài khoản
        topPanel.add(addButton, BorderLayout.EAST);
        
        // ========== BẢNG HIỂN THỊ DANH SÁCH TÀI KHOẢN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Nhân viên", "Tài khoản", "Mật khẩu", "Quyền"};
        CustomTable customTable = new CustomTable(columnNames);
        accountTable = customTable.getAccountTable(); // Lấy JTable từ CustomTable
        tableModel = customTable.getTableModel(); // Lấy model của bảng

        midPanel.add(customTable, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT TÀI KHOẢN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết tài khoản"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên nhân viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeLabel = new JLabel("Chọn tài khoản");
        botPanel.add(employeeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tài khoản: "), gbc);
        gbc.gridx = 1;
        JLabel usernameLabel = new JLabel("");
        botPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Mật khẩu: "), gbc);
        gbc.gridx = 1;
        JLabel passwordLabel = new JLabel("");
        botPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Quyền tài khoản: "), gbc);
        gbc.gridx = 1;
        JLabel roleComboBox = new JLabel("");
        botPanel.add(roleComboBox, gbc);
        // Tạo panel chứa hai nút
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false); // Để không ảnh hưởng đến màu nền

        // Nút xóa (căn trái)
        deleteButton = new CustomButton("Xoá");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        // Nút sửa (căn phải)
        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.EAST);

        // Thêm panel vào `botPanel`
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Trải dài 2 cột
        gbc.fill = GridBagConstraints.HORIZONTAL; // Căn chỉnh full chiều ngang
        

        // Xử lý sự kiện chọn tài khoản trong bảng
        accountTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy dữ liệu từ bảng và chuyển đổi sang String một cách an toàn
                Object value = accountTable.getValueAt(selectedRow, 0);
                String valueStr = String.valueOf(value);
                String tenNhanVien = (String) accountTable.getValueAt(selectedRow, 1);
                String taiKhoan = (String) accountTable.getValueAt(selectedRow, 2);
                String matKhau = (String) accountTable.getValueAt(selectedRow, 3);
                String quyen = (String) accountTable.getValueAt(selectedRow, 4);

                // Hiển thị dữ liệu trên giao diện
                employeeLabel.setText(valueStr + " - " + tenNhanVien);
                usernameLabel.setText(taiKhoan);
                passwordLabel.setText(matKhau);
                roleComboBox.setText(quyen);
                botPanel.add(buttonPanel, gbc);
            }
        });

//        saveButton.addActionListener(e -> {
//            int selectedRow = accountTable.getSelectedRow();
//            if (selectedRow != -1) {
//                // Lấy dữ liệu từ giao diện
        ////                int employeeID = Integer.parseInt(employeeLabel.getText().split(" - ")[0]);
//                String employeeID = employeeLabel.getText();
//                String username = usernameLabel.getText();
//                String password = passwordLabel.getText();
//                String fullname = passwordLabel.getText();
//                String role = (String) roleComboBox.getSelectedItem();
//
//                // Tạo đối tượng DTO
//                AccountDTO account = new AccountDTO(employeeID, username, password,fullname, role);
//
//                // Cập nhật vào database
//                accountBUS.updateAccount(account);
//
//                // Tải lại bảng
//                loadAccounts();
//                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!");
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để chỉnh sửa!");
//            }
//        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Tải dữ liệu tài khoản lên bảng
        loadAccounts();
    }

    // Phương thức tải danh sách tài khoản từ database lên bảng
    private void loadAccounts() {
        List<AccountDTO> accounts = accountBUS.getAllAccounts(); // Lấy danh sách tài khoản
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi cập nhật
        int index = 0;
        String no = "";
        for (AccountDTO acc : accounts) {
            tableModel.addRow(new Object[]{index++, acc.getFullname(),
                acc.getUsername(), acc.getPassword(), acc.getTenquyen()});
        }
    }
}
