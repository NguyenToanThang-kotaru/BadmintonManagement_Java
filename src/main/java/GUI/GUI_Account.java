package GUI;

import DTO.AccountDTO;
import BUS.AccountBUS;
import DAO.AccountDAO;
import DAO.PermissionDAO;
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
    private CustomButton deleteButton, addButton, editButton, reloadButton, importButton, exportButton;
    private CustomSearch searchField;
    private AccountBUS accountBUS;
    private AccountDTO accountChoosing;
    private AccountDAO AccountDAO;

    public GUI_Account(List<String> a) {
        accountBUS = new AccountBUS();
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
        importButton = new CustomButton("Nhập Excel");
        exportButton = new CustomButton("Xuất Excel");
        if (a.contains("xem_tk")) {
            leftButtonPanel.add(reloadButton);
            leftButtonPanel.add(importButton);
            leftButtonPanel.add(exportButton);
        }
        topPanel.add(leftButtonPanel, BorderLayout.WEST);

        searchField = new CustomSearch(275, 20);
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Tài Khoản");
        if (a.contains("them_tk")) {
            topPanel.add(addButton, BorderLayout.EAST);
        } else {
            System.out.println("Khong co them tk");
        }

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
        if (a.contains("xoa_tk")) {
            buttonPanel.add(deleteButton, BorderLayout.WEST);
        }
        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        if (a.contains("sua_tk")) {
            buttonPanel.add(editButton, BorderLayout.EAST);
        }

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
                PermissionDTO temp = PermissionDAO.getPermissionByName(quyen);
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

        if (a.contains("xem_tk")) {
            loadAccounts();
        }

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
            AccountDAO.deleteAccount(accountChoosing.getUsername());
            loadAccounts();
        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText();
            ArrayList<AccountDTO> ketQua = AccountDAO.searchAccounts(keyword);
            capNhatBangTaiKhoan(ketQua);
        });

        // Thêm sự kiện cho nút Nhập Excel
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".xlsx") || f.isDirectory();
                }
                public String getDescription() {
                    return "Excel Files (*.xlsx)";
                }
            });
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (accountBUS.importAccountsFromExcel(file)) {
                    JOptionPane.showMessageDialog(this, "Nhập tài khoản từ Excel thành công!");
                    loadAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi nhập tài khoản từ Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Thêm sự kiện cho nút Xuất Excel
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".xlsx") || f.isDirectory();
                }
                public String getDescription() {
                    return "Excel Files (*.xlsx)";
                }
            });
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                    file = new File(file.getAbsolutePath() + ".xlsx");
                }
                if (accountBUS.exportAccountsToExcel(file)) {
                    JOptionPane.showMessageDialog(this, "Xuất tài khoản ra Excel thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất tài khoản ra Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadAccounts() {
        List<AccountDTO> accounts = AccountDAO.getAllAccounts();
        tableModel.setRowCount(0);
        int index = 1;
        for (AccountDTO acc : accounts) {
            tableModel.addRow(new Object[]{index++, acc.getFullname(),
                    acc.getUsername(), acc.getPassword(), acc.getPermission().getName()});
        }
    }

    private void capNhatBangTaiKhoan(List<AccountDTO> accounts) {
        tableModel.setRowCount(0);
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
}