package GUI;

import DTO.AccountDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import BUS.AccountBUS;
import DAO.AccountDAO;
import DAO.PermissionDAO;
import DTO.PermissionDTO;
import java.util.ArrayList;

public class GUI_Account extends JPanel {

    // Khai báo các thành phần giao diện
    private JPanel midPanel, topPanel, botPanel;
    private JTable accountTable;
    private DefaultTableModel tableModel;
//    private JComboBox<String> roleComboBox;
    private CustomButton deleteButton, addButton, editButton, reloadButton;
    private CustomSearch searchField;
    private AccountBUS accountBUS;
    private AccountDTO accountChoosing;
    private AccountDAO AccountDAO;

    public GUI_Account(List<String> a) {
        accountBUS = new AccountBUS(); // Khởi tạo đối tượng BUS để lấy dữ liệu tài khoản
        System.out.println(a);
        // Cấu hình layout chính
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút thêm) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        reloadButton = new CustomButton("Tải lại trang");
        if (a.contains("xem_tk")) {
            topPanel.add(reloadButton, BorderLayout.WEST);
        }

        searchField = new CustomSearch(275, 20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Tài Khoản"); // Nút thêm tài khoản
        if (a.contains("them_tk")) {
            topPanel.add(addButton, BorderLayout.EAST);
            System.out.println("Co them tai khoan");
        } else {
            System.out.println("Khong co them tk");
        }

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
        if (a.contains("xoa_tk")) {
            buttonPanel.add(deleteButton, BorderLayout.WEST);
        }

        // Nút sửa (căn phải)
        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        if (a.contains("sua_tk")) {
            buttonPanel.add(editButton, BorderLayout.EAST);
        }

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
                PermissionDTO temp = PermissionDAO.getPermissionByName(quyen);
                accountChoosing = new AccountDTO(taikhoan, matkhau, tenNhanVien, temp);
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
        if (a.contains("xem_tk")) {
            loadAccounts();
        }

        addButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng thêm nhân viên chưa được triển khai!");
            GUI_Form_Account GFA = new GUI_Form_Account(this, null);
            GFA.setVisible(true);
        });

        editButton.addActionListener(e -> {
            GUI_Form_Account GFA = new GUI_Form_Account(this, accountChoosing);
            GFA.setVisible(true);
        });

        reloadButton.addActionListener(e -> {
            loadAccounts();
        });

        deleteButton.addActionListener(e -> {
            AccountDAO.deleteAccount(accountChoosing.getUsername());
            loadAccounts();
        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText();
            ArrayList<AccountDTO> ketQua = AccountDAO.searchAccounts(keyword);
            capNhatBangTaiKhoan(ketQua); // Hiển thị kết quả tìm được trên bảng
        });

    }

    // Phương thức tải danh sách tài khoản từ database lên bảng
    private void loadAccounts() {
        List<AccountDTO> accounts = AccountDAO.getAllAccounts(); // Lấy danh sách tài khoản
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi cập nhật
        int index = 1;
        for (AccountDTO acc : accounts) {
            tableModel.addRow(new Object[]{index++, acc.getFullname(),
                acc.getUsername(), acc.getPassword(), acc.getPermission().getName()});

        }
    }

    private void capNhatBangTaiKhoan(List<AccountDTO> accounts) {
    tableModel.setRowCount(0); // Xóa dữ liệu cũ
    int index = 1;
    for (AccountDTO acc : accounts) {
        tableModel.addRow(new Object[]{
            index++,
            acc.getFullname(),
            acc.getUsername(),
            acc.getPassword(),
            acc.getPermission().getName()
        });
    }
}

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Quản lý bảo hành");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(900, 600);
//            frame.setLocationRelativeTo(null);
//            frame.setContentPane(new GUI_Account());
//            frame.setVisible(true);
//        });
//    }
}
