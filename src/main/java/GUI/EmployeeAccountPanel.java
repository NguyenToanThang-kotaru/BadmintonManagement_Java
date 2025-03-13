package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.MatteBorder;

public class EmployeeAccountPanel extends JPanel {

    public EmployeeAccountPanel(JFrame parentFrame) {
        setLayout(new BorderLayout());

        // ====== Title Bar ======
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(50, 50, 50));
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        // ====== Left Panel (Icon + Title) ======
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(false);

        String iconLogo = "src/main/resources/images/LOGOAPP.png";
        ImageIcon appIcon = new ImageIcon(iconLogo);
        Image imgLogo = appIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        appIcon = new ImageIcon(imgLogo);

        JLabel iconLabel = new JLabel(appIcon);
        JLabel titleLabel = new JLabel(" Quản Lý Nhân Viên");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // ====== Right Panel (Control Buttons) ======
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightPanel.setOpaque(false);

        JLabel minimizeLabel = createTitleBarButton("–", parentFrame, JFrame.ICONIFIED);
        JLabel maximizeLabel = createTitleBarButton("⬜", parentFrame, -2);
        JLabel closeLabel = createTitleBarButton("X", parentFrame, -1);
        closeLabel.setBackground(Color.RED);

        rightPanel.add(minimizeLabel);
        rightPanel.add(maximizeLabel);
        rightPanel.add(closeLabel);

        titleBar.add(leftPanel, BorderLayout.WEST);
        titleBar.add(rightPanel, BorderLayout.EAST);
        add(titleBar, BorderLayout.NORTH);

        // ====== Content Panel ======
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ====== Table ======
        String[] columnNames = {"Mã nhân viên", "Tên", "Địa chỉ", "SĐT", "Ngày tham gia"};
        Object[][] data = {
            {"NV001", "Nguyễn Văn A", "Hà Nội", "0987654321", "2023-01-10"},
            {"NV002", "Trần Thị B", "Hải Phòng", "0912345678", "2023-02-15"}
        };
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== Button Panel ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        String[] buttonLabels = {"Thêm", "Sửa", "Xóa", "Chi Tiết"};
        String[] buttonIcons = {"EAadd.png", "EAedit.png", "EAdelete.png", "EAdetail.png"};

        for (int i = 0; i < buttonLabels.length; i++) {
            final String labelName = buttonLabels[i];
            String iconPath = "src/main/resources/images/" + buttonIcons[i];
            ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
            JLabel label = new JLabel(labelName, icon, JLabel.LEFT);
            label.setOpaque(true);
            label.setBackground(buttonPanel.getBackground());
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            label.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    label.setBackground(new Color(120, 120, 120));
                    label.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e) {
                    label.setBackground(buttonPanel.getBackground());
                    label.setForeground(Color.BLACK);
                }
                public void mousePressed(MouseEvent e) {
                    label.setBackground(new Color(90, 90, 90));
                }
                public void mouseReleased(MouseEvent e) {
                    label.setBackground(new Color(120, 120, 120));
                }
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked: " + labelName);
                }
            });
            buttonPanel.add(label);
        }
        contentPanel.add(buttonPanel, BorderLayout.NORTH);

        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JLabel createTitleBarButton(String text, JFrame frame, int actionState) {
        JLabel button = new JLabel(text, SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(30, 30));
        button.setOpaque(true);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
            }
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }
            public void mouseClicked(MouseEvent e) {
                if (actionState == -1) {
                    frame.dispose();
                } else if (actionState == -2) {
                    frame.setExtendedState(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
                } else {
                    frame.setState(actionState);
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Nhân Viên");
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new EmployeeAccountPanel(frame));
            frame.setVisible(true);
        });
    }
}
