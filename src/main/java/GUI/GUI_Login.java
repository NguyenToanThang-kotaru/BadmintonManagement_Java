package GUI;

import BUS.AccountBUS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI_Login extends JFrame {
    private static String currentUsername; 
    private final CustomTittleBar tittleBar;
    private AccountBUS checkLogin;
    public GUI_Login() {
        // Cấu hình cửa sổ
        setTitle("Đăng nhập");
        setSize(734, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        // ===== THÊM THANH TIÊU ĐỀ =====
        tittleBar = new CustomTittleBar(this);

        // Panel chính chứa 2 phần
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        getContentPane().add(mainPanel);

        // ==== PHẦN BÊN TRÁI (Hình ảnh + Tiêu đề) ==== 
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(173, 216, 230)); // Màu xanh
        leftPanel.setLayout(new BorderLayout());

//        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
//        jLabel5.setForeground(new java.awt.Color(0, 153, 153));
//        jLabel5.setText("QUẢN LÝ ");
//
//        jLabel6.setFont(new java.awt.Font("Segoe^ UI", 3, 30)); // NOI186N
//        jLabel6.setForeground(new java.awt.Color(255, 204, 102));
//        jLabel6.setText("THƯ VIỆN")
        JLabel title = new javax.swing.JLabel();
        title.setFont(new java.awt.Font("Segoe UI", 3, 36));
        title.setForeground(new java.awt.Color(0, 153, 153));
        title.setText("QUẢN LÝ");

        JLabel subtitle = new javax.swing.JLabel();
        subtitle.setFont(new java.awt.Font("Segoe UI", 3, 36));
        subtitle.setForeground(new java.awt.Color(255, 204, 102));
        subtitle.setText("CẦU LÔNG");
        subtitle.setHorizontalAlignment(SwingConstants.RIGHT);

        ImageIcon originalIcon = new ImageIcon("src/main/resources/images/appLogo.png");
        Image originalImage = originalIcon.getImage();
        int newWidth = 200;
        int newHeight = 150;
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        // Icon giả lập (Thay bằng ảnh thật nếu có)
        ImageIcon icon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(icon);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(title);

        textPanel.add(subtitle);
        textPanel.setOpaque(false);

        leftPanel.add(textPanel, BorderLayout.NORTH);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // ==== PHẦN BÊN PHẢI (Form đăng nhập) ====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(255, 255, 204)); // Màu vàng nhạt
        rightPanel.setLayout(null);

        JLabel loginTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Roboto", Font.BOLD, 27));
        loginTitle.setBounds(100, 100, 200, 30);
        rightPanel.add(loginTitle);

        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setBounds(30, 190, 120, 25);
        rightPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 190, 180, 25);
        rightPanel.add(userField);

        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setBounds(30, 230, 120, 25);
        rightPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 230, 180, 25);
        rightPanel.add(passField);

        CustomButton loginButton = new CustomButton("Đăng nhập");
        loginButton.setBounds(130, 290, 140, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
//        loginButton.setForeground(Color.WHITE);
//        loginButton.setBackground(new Color(0, 150, 255));    
//        loginButton.setFocusPainted(false);

        loginButton.addActionListener((ActionEvent e) -> {
            checkLogin(userField, passField); // Gọi hàm check khi bấm nút
        });

        userField.addActionListener(e -> checkLogin(userField, passField));
        passField.addActionListener(e -> checkLogin(userField, passField));
        userField.setText("NV001");
        passField.setText("123456");
        rightPanel.add(loginButton);
        add(tittleBar, BorderLayout.NORTH);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

    }

    private void checkLogin(JTextField userField, JTextField passField) {
        String username = userField.getText();  
        String password = passField.getText();
        checkLogin = new AccountBUS();
        if (username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập tài khoản và mật khẩu!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (checkLogin.HaveAccount(username, password)){
            currentUsername = username;
            this.setVisible(false);
            GUI_MainLayout mainLayout = new GUI_MainLayout(this,username, password);
            mainLayout.setVisible(true);
            userField.setText("");
            passField.setText("");
        }
//        if (AccountDAO.getAccount(username, password) != null) {
//            //Chay vao frame GUI_MainLayout
//            this.setVisible(false);
//            GUI_MainLayout mainLayout = new GUI_MainLayout(this);
//            mainLayout.setVisible(true);
//            userField.setText("");
//            passField.setText("");
//        } else if (username.isEmpty() || password.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
    }
    
    public static void main(String[] args) {
        GUI_Login a = new GUI_Login();
        a.setVisible(true);
    }
    public static String getCurrentUsername() {
        return currentUsername;
    }
    
}
