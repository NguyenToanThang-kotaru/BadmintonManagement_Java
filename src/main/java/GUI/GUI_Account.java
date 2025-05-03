package GUI;

import DTO.AccountDTO;
import BUS.AccountBUS;
import DAO.AccountDAO;
import DAO.Permission2DAO;
import DAO.PermissionDAO;
import DTO.Permission2DTO;
import DTO.PermissionDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class GUI_Account extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private CustomButton deleteButton, addButton, editButton, reloadButton, exportButton;
    private CustomSearch searchField;
    private AccountBUS accountBUS;
    private AccountDTO accountChoosing;
    private AccountDAO AccountDAO;

    public GUI_Account() {
        accountBUS = new AccountBUS(); // Khởi tạo đối tượng BUS để lấy dữ liệu tài khoản
//        System.out.println(a);
        // Cấu hình layout chính
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtonPanel.setOpaque(false);
        reloadButton = new CustomButton("Tải lại trang");
        exportButton = new CustomButton("Xuất Excel");
       
        topPanel.add(leftButtonPanel, BorderLayout.WEST);
            topPanel.add(reloadButton, BorderLayout.WEST);

        searchField = new CustomSearch(275, 20);
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Tài Khoản"); // Nút thêm tài khoản
        topPanel.add(addButton, BorderLayout.EAST);
//            System.out.println("Co them tai khoan");\

        // ========== BẢNG HIỂN THỊ DANH SÁCH TÀI KHOẢN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"STT", "Nhân viên", "Tài khoản", "Mật khẩu", "Quyền"};
        CustomTable customTable = new CustomTable(columnNames);
        accountTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();
        midPanel.add(customTable, BorderLayout.CENTER);
        CustomScrollPane scrollPane = new CustomScrollPane(accountTable);

        // ========== PANEL CHI TIẾT TÀI KHOẢN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Tài Khoản"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

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

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        deleteButton = new CustomButton("Xoá");
        deleteButton.setCustomColor(new Color(220, 0, 0));

        buttonPanel.add(deleteButton, BorderLayout.WEST);

        // Nút sửa (căn phải)
        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        accountTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow != -1) {
                String tenNhanVien = (String) accountTable.getValueAt(selectedRow, 1);
                String taikhoan = (String) accountTable.getValueAt(selectedRow, 2);
                String matkhau = (String) accountTable.getValueAt(selectedRow, 3);
                String quyen = (String) accountTable.getValueAt(selectedRow, 4);
                Permission2DTO temp = Permission2DAO.getPermissionByName(quyen);
                accountChoosing = new AccountDTO(taikhoan, matkhau, tenNhanVien, temp);
                employeeLabel.setText(tenNhanVien);
                usernameLabel.setText(taikhoan);
                passwordLabel.setText(matkhau);
                roleComboBox.setText(quyen);
                botPanel.add(buttonPanel, gbc);
            }
        });

        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(botPanel);


        // Tải dữ liệu tài khoản lên bảng
        loadAccounts();

        addButton.addActionListener(e -> {
            Form_Account GFA = new Form_Account(this, null);
            GFA.setVisible(true);
        });

        editButton.addActionListener(e -> {
            Form_Account GFA = new Form_Account(this, accountChoosing);
            GFA.setVisible(true);
        });

        reloadButton.addActionListener(e -> {
            loadAccounts();
        });

        deleteButton.addActionListener(e -> {
            if (helped.confirmDelete("Bạn có chắc muốn xóa tài khoản này?")) {
                AccountDAO.deleteAccount(accountChoosing.getUsername());
                loadAccounts();
            }

        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText();
            ArrayList<AccountDTO> ketQua = AccountDAO.searchAccounts(keyword);
            capNhatBangTaiKhoan(ketQua);
        });

        // Sự kiện cho nút Xuất Excel
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                File file = new File(filePath);
                boolean success = accountBUS.exportAccountsToExcel(file);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    // Phương thức tải danh sách tài khoản từ database lên bảng
    public void loadAccounts() {
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
