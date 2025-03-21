package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI_MainLayout extends JFrame {

    private CustomSidebar Sidebar;
    private CustomTittleBar tittleBar;
    
    public GUI_MainLayout(JFrame login) {
        setTitle("Quản Lý Kho Hàng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        setUndecorated(true);
        
        // ================================ Title Bar ================================
        tittleBar = new CustomTittleBar(this);

        // ================================ CustomSidebar ================================
        Sidebar = new CustomSidebar(login,this);

        // ================================ Content ================================
        JPanel contentPanel = new JPanel(new BorderLayout());

        Sidebar.statisticsPanel = new JPanel();
        Sidebar.statisticsPanel.setBackground(Color.CYAN);
        Sidebar.statisticsPanel.add(new JLabel("Thống kê doanh thu"));

        Sidebar.productPanel = new JPanel();
        Sidebar.productPanel.setBackground(Color.GREEN);
        Sidebar.productPanel.add(new JLabel("Danh sách sản phẩm"));

        Sidebar.orderPanel = new JPanel();
        Sidebar.orderPanel.setBackground(Color.ORANGE);
        Sidebar.orderPanel.add(new JLabel("Danh sách đơn hàng"));

        Sidebar.employeePanel = new GUI_Employee();
        Sidebar.customerPanel = new GUI_Customer();

        Sidebar.supplierPanel = new JPanel();
        Sidebar.supplierPanel.setBackground(Color.YELLOW);
        Sidebar.supplierPanel.add(new JLabel("Nhà cung cấp"));

        Sidebar.importPanel = new JPanel();
        Sidebar.importPanel.setBackground(Color.PINK);
        Sidebar.importPanel.add(new JLabel("Hóa đơn nhập"));

        Sidebar.promotionPanel = new JPanel();
        Sidebar.promotionPanel.setBackground(Color.BLUE);
        Sidebar.promotionPanel.add(new JLabel("Khuyến mãi"));

        /*Sidebar.customerPanel = new JPanel();
        Sidebar.customerPanel.setBackground(Color.RED);
        Sidebar.customerPanel.add(new JLabel("Khách hàng"));*/

        Sidebar.accountPanel = new GUI_Account();

        Sidebar.repairPanel = new JPanel();
        Sidebar.repairPanel.setBackground(Color.DARK_GRAY);
        Sidebar.repairPanel.add(new JLabel("Bảo hành"));

        Sidebar.rolePanel = new JPanel();
        Sidebar.rolePanel.setBackground(Color.LIGHT_GRAY);
        Sidebar.rolePanel.add(new JLabel("Phân quyền"));
        for (Component comp : Sidebar.panel2.getComponents()) {
            if (comp instanceof JLabel menuLabel) {
                menuLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        contentPanel.removeAll(); // Xóa nội dung cũ

                        switch (menuLabel.getText()) {
                            case "Thống kê" ->
                                contentPanel.add(Sidebar.statisticsPanel, BorderLayout.CENTER);
                            case "Sản Phẩm" ->
                                contentPanel.add(Sidebar.productPanel, BorderLayout.CENTER);
                            case "Đơn Hàng" ->
                                contentPanel.add(Sidebar.orderPanel, BorderLayout.CENTER);
                            case "Nhân Viên" ->
                                contentPanel.add(Sidebar.employeePanel, BorderLayout.CENTER);
                            case "Nhà Cung Cấp" ->
                                contentPanel.add(Sidebar.supplierPanel, BorderLayout.CENTER);
                            case "Hóa Đơn Nhập" ->
                                contentPanel.add(Sidebar.importPanel, BorderLayout.CENTER);
                            case "Khuyến Mãi" ->
                                contentPanel.add(Sidebar.promotionPanel, BorderLayout.CENTER);
                            case "Khách Hàng" ->
                                contentPanel.add(Sidebar.customerPanel, BorderLayout.CENTER);
                            case "Tài Khoản" ->
                                contentPanel.add(Sidebar.accountPanel, BorderLayout.CENTER);
                            case "Bảo Hành" ->
                                contentPanel.add(Sidebar.repairPanel, BorderLayout.CENTER);
                            case "Phân Quyền" ->
                                contentPanel.add(Sidebar.rolePanel, BorderLayout.CENTER);
                            default ->
                                contentPanel.add(new JLabel("Chưa có nội dung"), BorderLayout.CENTER);
                        }

                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                });
            }
        }

        add(tittleBar, BorderLayout.NORTH);
        add(Sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
}
