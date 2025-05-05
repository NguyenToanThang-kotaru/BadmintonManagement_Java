package GUI;

import BUS.PermissionBUS;
import DAO.AccountDAO;
import DTO.AccountDTO;
import DTO.FunctionDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI_MainLayout extends JFrame {

    private CustomSidebar Sidebar;
    private CustomTittleBar tittleBar;

    public GUI_MainLayout(JFrame login, String username, String password) {
        AccountDTO logned = AccountDAO.getAccount(username, password);
        ArrayList<String> Functions = new ArrayList<>();

        for (FunctionDTO a : logned.getPermission().getFunction()) {
            Functions.add(a.getName());
        }

        setTitle("Quản Lý Kho Hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        setUndecorated(true);

        // ================================ Title Bar ================================
        tittleBar = new CustomTittleBar(this);
        System.out.println();
        // ================================ CustomSidebar ================================
        Sidebar = new CustomSidebar(login, this, Functions);

        // ================================ Content ================================
        JPanel contentPanel = new JPanel(new BorderLayout());

        Sidebar.statisticsPanel = new GUI_Statistics();

        Sidebar.promotionPanel = new JPanel();
        Sidebar.promotionPanel.setBackground(Color.BLUE);
        Sidebar.promotionPanel.add(new JLabel("Khuyến mãi"));

        Sidebar.accountPanel = new GUI_Account();

        Sidebar.supplierPanel = new GUI_Suppliers();

        Sidebar.customerPanel = new GUI_Customer(logned);

        Sidebar.productPanel = new GUI_Product(logned);

        Sidebar.orderPanel = new GUI_Order(logned, (GUI_Product) Sidebar.productPanel);

        Sidebar.repairPanel = new GUI_Guarantee(logned);

        Sidebar.importPanel = new GUI_Import(logned, (GUI_Product) Sidebar.productPanel);

        Sidebar.employeePanel = new GUI_Employee(logned);

        Sidebar.rolePanel = new GUI_Permission(logned);
        for (Component comp : Sidebar.panel2.getComponents()) {
            if (comp instanceof JLabel menuLabel) {
                menuLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        contentPanel.removeAll(); // Xóa nội dung cũ

                        switch (menuLabel.getText()) {
                            case "Thống Kê" ->
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
