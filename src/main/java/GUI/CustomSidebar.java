package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Thang Nguyen
 */
public class CustomSidebar extends JPanel {

    private JLabel selectedLabel = null;
    private List<String> menuItems;
    public JPanel statisticsPanel, productPanel, orderPanel,
            supplierPanel, importPanel, promotionPanel,
            customerPanel, accountPanel, rolePanel,
            repairPanel, employeePanel;
    private JLabel titleMenu;
    public JPanel panel1, panel2, panel3;

    public CustomScrollPane scrollPane;

    public CustomSidebar(JFrame login, JFrame Main_Layout, ArrayList<String> functions) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 600));

        panel1 = new JPanel();
        panel1.setBackground(Color.LIGHT_GRAY);
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        titleMenu = new JLabel("MENU");
        titleMenu.setFont(new Font("Roboto", Font.BOLD, 30));
        titleMenu.setForeground(new Color(0, 153, 153));
        titleMenu.setBorder(new MatteBorder(0, 0, 2, 0, new Color(0, 153, 100)));
        panel1.add(titleMenu);
        // ====== panel2: Danh sách menu ======

        // ====== Danh sách menu từ ArrayList ======
        menuItems = new ArrayList<String>();
//        menuItems.add("Thống Kê");
//        menuItems.add("Sản Phẩm");
//        menuItems.add("Tài Khoản");
//        menuItems.add("Nhà Cung Cấp");
//        menuItems.add("Đơn Hàng");
//        menuItems.add("Nhân Viên");
//        menuItems.add("Hóa Đơn Nhập");
//        menuItems.add("Khách Hàng");
//        menuItems.add("Bảo Hành");
//        menuItems.add("Phân Quyền");
// Kiểm tra và thêm từng mục dựa trên userFunctions
        for (String funcCode : functions) {
            switch (funcCode) {
                case "Quản lý thống kê":
                    menuItems.add("Thống Kê");
                    break;
                case "Quản lý sản phẩm":
                    if (!menuItems.contains("Sản Phẩm")) {
                        menuItems.add("Sản Phẩm");
                    }
                    break;
                case "Quản lý tài khoản":
                    if (!menuItems.contains("Tài Khoản")) {
                        menuItems.add("Tài Khoản");
                    }
                    break;
                case "Quản lý nhà cung cấp":
                    if (!menuItems.contains("Nhà Cung Cấp")) {
                        menuItems.add("Nhà Cung Cấp");
                    }
                    break;
                case "Quản lý đơn hàng":
                    if (!menuItems.contains("Đơn Hàng")) {
                        menuItems.add("Đơn Hàng");
                    }
                    break;
                case "Quản lý nhân viên":
                    if (!menuItems.contains("Nhân Viên")) {
                        menuItems.add("Nhân Viên");
                    }
                    break;
                case "Quản lý hóa đơn nhập":
                    if (!menuItems.contains("Hóa Đơn Nhập")) {
                        menuItems.add("Hóa Đơn Nhập");
                    }
                    break;
                case "Quản lý khách hàng":
                    if (!menuItems.contains("Khách Hàng")) {
                        menuItems.add("Khách Hàng");
                    }
                    break;
                case "Quản lý bảo hành":
                    if (!menuItems.contains("Bảo Hành")) {
                        menuItems.add("Bảo Hành");
                    }
                    break;
                case "Quản lý phân quyền":
                    if (!menuItems.contains("Phân Quyền")) {
                        menuItems.add("Phân Quyền");
                    }
                    break;
            }
        }

        panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setBackground(Color.LIGHT_GRAY);

        for (String item : menuItems) {
            // Định nghĩa đường dẫn của icon tương ứng
            String iconPath = "src/main/resources/images/";
            switch (item) {
                case "Thống Kê":
                    iconPath += "icontk.png";
                    break;
                case "Sản Phẩm":
                    iconPath += "icon_sanpham.png";
                    break;
                case "Đơn Hàng":
                    iconPath += "icon_donhang.png";
                    break;
                case "Nhân Viên":
                    iconPath += "icon_nhanvien.png";
                    break;
                case "Nhà Cung Cấp":
                    iconPath += "icon_supplier.png";
                    break;
                case "Khách Hàng":
                    iconPath += "icon_khachhang.png";
                    break;
                case "Tài Khoản":
                    iconPath += "icon_account.png";
                    break;
                case "Bảo Hành":
                    iconPath += "icon_baohanh.png";
                    break;
                case "Hóa Đơn Nhập":
                    iconPath += "hoadonnhap.png";
                    break;
                case "Phân Quyền":
                    iconPath += "icon_role.png";
                    break;
                default:
                    iconPath = null;
            }

            // Tạo JLabel có icon
            JLabel label;
            if (iconPath != null) {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                label = new JLabel(item, icon, JLabel.LEFT);
            } else {
                label = new JLabel(item, JLabel.LEFT);
            }

            label.setFont(new Font("Roboto", Font.BOLD, 19));
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            label.setForeground(Color.BLACK);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            label.setPreferredSize(new Dimension(200, 40));
//            final boolean[] isClicked = {false};

            // Hiệu ứng hover
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (label != selectedLabel) { // Chỉ đổi màu khi chưa được chọn
                        label.setBackground(new Color(0, 153, 153));
                        label.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (label != selectedLabel) { // Chỉ reset màu nếu chưa được chọn
                        label.setBackground(Color.LIGHT_GRAY);
                        label.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // Reset màu của menu trước đó nếu có
                    if (selectedLabel != null) {
                        selectedLabel.setBackground(Color.LIGHT_GRAY);
                        selectedLabel.setForeground(Color.BLACK);
                    }

                    // Gán label hiện tại là selectedLabel
                    selectedLabel = label;
                    label.setBackground(new Color(0, 153, 153)); // Đổi màu khi chọn
                    label.setForeground(Color.WHITE);
                }
            });

            panel2.add(label);
            panel2.add(Box.createVerticalStrut(5));
        }
        // ====== Tạo JScrollPane cho menu ======
        scrollPane = new CustomScrollPane(panel2);

        // ====== Thêm JScrollPane vào sidebar ======
        // ====== panel3: Nút thoát ======
        panel3 = new JPanel();
        panel3.setBackground(Color.LIGHT_GRAY);
        panel3.setPreferredSize(new Dimension(this.getWidth(), 50));

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.RED);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));

        logoutButton.addActionListener((ActionEvent e) -> {
            logout(login, Main_Layout); // Gọi hàm check khi bấm nút
        });

        panel3.add(logoutButton);

        // ====== Sắp xếp các panel trong sidebar ======
        add(panel1, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH); // Đặt ở dưới cùng
    }

    private void logout(JFrame login, JFrame Main_Layout) {
        login.setVisible(true);
        Main_Layout.setVisible(false);
    }

}
