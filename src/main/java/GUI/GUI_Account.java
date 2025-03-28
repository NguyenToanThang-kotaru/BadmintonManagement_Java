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
    private AccountDTO accountChoosing;

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

        searchField = new CustomSearch(275, 20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Tài Khoản"); // Nút thêm tài khoản
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
        CustomScrollPane scrollPane = new CustomScrollPane(accountTable);
        // ========== PANEL CHI TIẾT TÀI KHOẢN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Tài Khoản"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeLabel = new JLabel("Chọn Tài Khoản");
        botPanel.add(employeeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tài Khoản: "), gbc);
        gbc.gridx = 1;
        JLabel usernameLabel = new JLabel("");
        botPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Mật Khẩu: "), gbc);
        gbc.gridx = 1;
        JLabel passwordLabel = new JLabel("");
        botPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Quyền Tài Khoản: "), gbc);
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

                String tenNhanVien = (String) accountTable.getValueAt(selectedRow, 1);
                String taikhoan = (String) accountTable.getValueAt(selectedRow, 2);
                String matkhau = (String) accountTable.getValueAt(selectedRow, 3);
                String quyen = (String) accountTable.getValueAt(selectedRow, 4);
                accountChoosing = new AccountDTO(taikhoan, matkhau,tenNhanVien,quyen);
                // Hiển thị dữ liệu trên giao diện
                employeeLabel.setText(tenNhanVien);
                usernameLabel.setText(taikhoan);
                passwordLabel.setText(matkhau);
                roleComboBox.setText(quyen);
                botPanel.add(buttonPanel, gbc);
            }
        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Tải dữ liệu tài khoản lên bảng
        loadAccounts();

        addButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng thêm nhân viên chưa được triển khai!");
            GUI_Form_Account GFA = new GUI_Form_Account(this,null);
            GFA.setVisible(true);
        });

        editButton.addActionListener(e -> {
            GUI_Form_Account GFA = new GUI_Form_Account(this, accountChoosing);     
            GFA.setVisible(true);
        });


        deleteButton.addActionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();

            // Lấy dữ liệu từ bảng (đảm bảo lấy ID đúng cột)
            String username = (String) accountTable.getValueAt(selectedRow, 2); // Giả sử cột 2 là tài khoản

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
//                if (accountBUS.deleteAccount(username)) { // Giả sử có hàm xóa trong BUS
//                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
//                    loadAccounts(); // Cập nhật lại bảng
//                } else {
//                    JOptionPane.showMessageDialog(this, "Xóa thất bại, vui lòng thử lại!");
//                }
                System.out.println("Da xo account") ;
            }
        });

    }

    // Phương thức tải danh sách tài khoản từ database lên bảng
    private void loadAccounts() {
        List<AccountDTO> accounts = accountBUS.getAllAccounts(); // Lấy danh sách tài khoản
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi cập nhật
        int index = 0;
        for (AccountDTO acc : accounts) {
            tableModel.addRow(new Object[]{index++, acc.getFullname(),
                acc.getUsername(), acc.getPassword(), acc.getTenquyen()});
            
        }
    }
}
