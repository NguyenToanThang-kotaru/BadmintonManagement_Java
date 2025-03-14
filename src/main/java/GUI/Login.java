package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import DAO.AccountDAO;
import DTO.AccountDTO;

public class Login extends JFrame {

    private TittleBar tittleBar;

    public Login() {
        // Cấu hình cửa sổ
        setTitle("Đăng nhập");
        setSize(734, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        // ===== THÊM THANH TIÊU ĐỀ =====
        tittleBar = new TittleBar(this);
        add(tittleBar, BorderLayout.NORTH);

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

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(130, 290, 140, 40);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 150, 255));
        loginButton.setFocusPainted(false);

        loginButton.addActionListener((ActionEvent e) -> {
            checkLogin(userField, passField); // Gọi hàm check khi bấm nút
        });

        rightPanel.add(loginButton);
        // Thêm vào mainPanel   
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

    }

    private void checkLogin(JTextField userField, JTextField passField) {
        String username = userField.getText();
        String password = passField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            AccountDTO account = AccountDAO.getAccount(username, password);
            if (account != null) {
                //Chay vao frame Main_Layout
                this.setVisible(false);
                Main_Layout mainLayout = new Main_Layout(this);
                mainLayout.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);                
                
            }}

    }

    public static void main(String[] args) {
        Login a = new Login();
        a.setVisible(true);
    }
}
