package GUI;

import BUS.ActionBUS;
import DTO.PermissionDTO;
import BUS.PermissionBUS;
import DAO.Permission2DAO;
import DTO.AccountDTO;
import DTO.ActionDTO;
import DTO.Permission2DTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI_Permission extends JPanel {

    // Khai báo các thành phần giao diện
    private JPanel midPanel, topPanel, botPanel;
    private JTable permissionTable;
    private DefaultTableModel tableModel;
    private CustomButton detailPermissionButton, editButton, addButton, deleteButton, reloadButton;
    private CustomSearch searchField;
    private Permission2DTO permissionChoosing;

    public GUI_Permission(AccountDTO acc) {
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
        topPanel.add(reloadButton, BorderLayout.WEST);

        searchField = new CustomSearch(275, 20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.white);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm quyền"); // Nút thêm 
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH TÀI KHOẢN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setPreferredSize(new Dimension(0, 780));
        midPanel.setBackground(Color.WHITE);
//        midPanel.setMinimumSize(new Dimension(600, 200));
//        midPanel.setPreferredSize(new Dimension(600, 200));
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Tên quyền", "Số lượng quyền", "Số lượng tài khoản"};
        CustomTable customTable = new CustomTable(columnNames);
        permissionTable = customTable.getAccountTable(); // Lấy JTable từ CustomTable
        tableModel = customTable.getTableModel(); // Lấy model của bảng
//        midPanel.add(customTable, BorderLayout.CENTER);
        CustomScrollPane scrollPane = new CustomScrollPane(permissionTable);
        midPanel.add(scrollPane,BorderLayout.CENTER);
        midPanel.setMinimumSize(new Dimension(200, 450));
        // ========== PANEL CHI TIẾT TÀI KHOẢN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết quyền"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên quyền: "), gbc);
        gbc.gridx = 1;
        JLabel permissionLabel = new JLabel("Chọn quyền");
        botPanel.add(permissionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Số lượng quyền: "), gbc);
        gbc.gridx = 1;
        JLabel countFunction = new JLabel("");
        botPanel.add(countFunction, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Số lượng tài khoản: "), gbc);
        gbc.gridx = 1;
        JLabel countAccount = new JLabel("");
        botPanel.add(countAccount, gbc);

        // ========== PANEL BUTTON ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.CENTER);

        detailPermissionButton = new CustomButton("Xem Chi Tiết Quyền");
        detailPermissionButton.setCustomColor(new Color(0, 120, 215));
        buttonPanel.add(detailPermissionButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

//         Xử lý sự kiện chọn tài khoản trong bảng
        permissionTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = permissionTable.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy dữ liệu từ bảng và chuyển đổi sang String một cách an toàn
                String tenQuyen = (String) permissionTable.getValueAt(selectedRow, 1);
                String soluongQuyen = (String) permissionTable.getValueAt(selectedRow, 2);
                String soluongTaiKhoan = (String) permissionTable.getValueAt(selectedRow, 3);

                permissionChoosing = Permission2DAO.getPermissionByName(tenQuyen);

                // Hiển thị dữ liệu trên giao diện
                permissionLabel.setText(tenQuyen);
                countFunction.setText(soluongQuyen);
                countAccount.setText(soluongTaiKhoan);
                botPanel.add(buttonPanel, gbc);
            }
        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Tải dữ liệu tài khoản lên bảng
        loadPermissions();
        addButton.addActionListener(e -> {
            Form_Permission a = new Form_Permission(this, null);
            a.setVisible(true);
        });

        editButton.addActionListener(e -> {
            Form_Permission a = new Form_Permission(this, permissionChoosing);
            a.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            if (helped.confirmDelete("Bạn có chắc muốn xóa quyền này") == true) {
                int rs = PermissionBUS.deletePermission(permissionChoosing);
                if (rs == 1) {
                    System.out.println("Da xoa thanh cong");
                    loadPermissions();
                } else if (rs == 2) {
                    JOptionPane.showMessageDialog(this,
                            "Quyền này hiện còn tài khoản đang được sử dụng",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thất bại trong việc xóa quyền",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        });

        detailPermissionButton.addActionListener(e -> {
            GUI_DetailPermission dp = new GUI_DetailPermission(permissionChoosing);
            dp.setVisible(true);
        });

        reloadButton.addActionListener(e -> {
            loadPermissions();
        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                searchPermission(keyword);
            } else {
                loadPermissions(); // Nếu ô tìm kiếm trống, load lại toàn bộ khách hàng
            }
        });
        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(acc, "Quản lý phân quyền");

        boolean canAdd = false, canEdit = false, canDelete = false, canWatch = false;

        if (actions != null) {
            for (ActionDTO action : actions) {
                switch (action.getName()) {
                    case "Thêm" ->
                        canAdd = true;
                    case "Sửa" ->
                        canEdit = true;
                    case "Xóa" ->
                        canDelete = true;
                    case "Xem" ->
                        canWatch = true;
                }
            }
        }

        addButton.setVisible(canAdd);
        editButton.setVisible(canEdit);
        deleteButton.setVisible(canDelete);
        scrollPane.setVisible(canWatch);
//        reloadButton.setVisible(false);
    }

    // Phương thức tải danh sách tài khoản từ database lên bảng
    public void loadPermissions() {
        List<Permission2DTO> permissions = Permission2DAO.getAllPermissions(); // Lấy danh sách tài khoản
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi cập nhật
        int index = 1;
        for (Permission2DTO per : permissions) {
            tableModel.addRow(new Object[]{index++, per.getName(),
                PermissionBUS.countDistinctFunctionsByPermission(per.getID()),
                PermissionBUS.countAllAccountsByPer(per.getID())});

        }
    }

    private void searchPermission(String keyword) {
        List<PermissionDTO> permission = PermissionBUS.searchPermission(keyword);
        tableModel.setRowCount(0);
        int index = 1;
        for (PermissionDTO per : permission) {
            tableModel.addRow(new Object[]{index++, per.getName(), per.getSlChucNang(), per.getSlTk()});
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Quản lý bảo hành");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(900, 600);
//            frame.setLocationRelativeTo(null);
//            frame.setContentPane(new GUI_Permission());
//            frame.setVisible(true);
//        });
//    }
}
