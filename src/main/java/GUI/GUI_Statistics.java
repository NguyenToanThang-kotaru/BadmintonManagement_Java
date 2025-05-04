package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI_Statistics extends JPanel {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public GUI_Statistics() {
//        setTitle("Hệ Thống Thống Kê");
//        setSize(1000, 700);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        
        initComponents();
    }
    
    private void initComponents() {
        // 1. Tạo thanh điều hướng trên cùng
        JPanel navPanel = createNavigationPanel();
        
        // 2. Tạo panel chính sử dụng CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // 3. Thêm các panel thống kê vào mainPanel
        mainPanel.add(createDoanhThuPanel(), "doanhThu");
        mainPanel.add(createSanPhamPanel(), "sanPham");
        mainPanel.add(createKhachHangPanel(), "khachHang");
        
        // 4. Thêm các component vào frame
        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        
        // Các nút chuyển đổi
        JButton btnDoanhThu = createNavButton("DOANH THU");
        JButton btnSanPham = createNavButton("SẢN PHẨM");
        JButton btnKhachHang = createNavButton("KHÁCH HÀNG");
        
        // Thêm action listener
        btnDoanhThu.addActionListener(e -> cardLayout.show(mainPanel, "doanhThu"));
        btnSanPham.addActionListener(e -> cardLayout.show(mainPanel, "sanPham"));
        btnKhachHang.addActionListener(e -> cardLayout.show(mainPanel, "khachHang"));
        
        panel.add(btnDoanhThu);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnSanPham);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnKhachHang);
        
        return panel;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    
    // Các phương thức tạo panel thống kê
    private JPanel createDoanhThuPanel() {
        GUI_Revenue_Statistics panel = new GUI_Revenue_Statistics();
        return panel;
    }
    
    private JPanel createSanPhamPanel() {
        GUI_Product_Statistics panel = new GUI_Product_Statistics();
        return panel;
    }
    
    private JPanel createKhachHangPanel() {
        GUI_Customer_Statistics panel = new GUI_Customer_Statistics();
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI_Statistics().setVisible(true);
        });
    }
}