package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DAO.PermissionDAO;
import DTO.PermissionDTO;

public class GUI_Permission extends JPanel {

    // Khai báo các thành phần giao diện
    private JPanel midPanel, topPanel, botPanel;
    private JTable permissionTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> roleComboBox;
    private CustomButton detailPermissionButton, editButton, addButton, deleteButton, reloadButton;
    private CustomSearch searchField;
    private PermissionDTO permissionChoosing;

    public GUI_Permission() {
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
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Tên quyền", "Số lượng quyền", "Số lượng tài khoản"};
        CustomTable customTable = new CustomTable(columnNames);
        permissionTable = customTable.getAccountTable(); // Lấy JTable từ CustomTable
        tableModel = customTable.getTableModel(); // Lấy model của bảng

        midPanel.add(customTable, BorderLayout.CENTER);

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

                permissionChoosing = new PermissionDTO(PermissionDAO.getPermissionByName(tenQuyen));

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
            GUI_Form_Permission a = new GUI_Form_Permission(this, null);
            a.setVisible(true);
        });

        editButton.addActionListener(e -> {
            GUI_Form_Permission a = new GUI_Form_Permission(this, permissionChoosing);
            a.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            if (PermissionDAO.deletePermission(permissionChoosing.getID()) == true) {
                System.out.println("Da xoa thanh cong");
            }
            loadPermissions();
        });

        reloadButton.addActionListener(e -> {
            loadPermissions();
        });
    }

    // Phương thức tải danh sách tài khoản từ database lên bảng
    private void loadPermissions() {
        List<PermissionDTO> permissions = PermissionDAO.getAllPermissions(); // Lấy danh sách tài khoản
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi cập nhật
        int index = 1;
        for (PermissionDTO per : permissions) {
            tableModel.addRow(new Object[]{index++, per.getName(), per.getSlChucNang(), per.getSlTk()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý bảo hành");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new GUI_Permission());
            frame.setVisible(true);
        });
    }
}
