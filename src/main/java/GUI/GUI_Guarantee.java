package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.table.TableColumnModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class GUI_Guarantee extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private CustomTable warrantyTable;
    private JTextField reasonField;
    private JLabel serialLabel, purchaseDateLabel;
    private CustomButton saveButton;
    private JComboBox<String> warrantyStatusComboBox;

    public GUI_Guarantee() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Sản phẩm bảo hành", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã BH", "Mã Serial", "Bảo Hành", "Đổi Trả"};

        warrantyTable = new CustomTable(columnNames); // Sửa lại

        TableColumnModel columnModel = warrantyTable.getAccountTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof String) {
                    String text = (String) value;
                    setText(text); // Hiển thị nội dung gốc
                    if (text.equals("Chưa bảo hành") || text.equals("Chưa đổi trả")) {
                        setForeground(Color.GREEN); // Màu xanh lá cây
                    } else {
                        setForeground(Color.RED); // Màu đỏ
                    }
                }
            }
        };
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        warrantyTable.getAccountTable().getColumnModel().getColumn(2).setCellRenderer(statusRenderer);
        warrantyTable.getAccountTable().getColumnModel().getColumn(3).setCellRenderer(statusRenderer);

        // Thêm dữ liệu giả lập
        addFakeData();

        // ScrollPane để bảng có thanh cuộn
        JScrollPane scrollPane = new JScrollPane(warrantyTable.getAccountTable());
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết bảo hành"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Serial
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Serial: "), gbc);
        gbc.gridx = 1;
        serialLabel = new JLabel("Chưa chọn");
        botPanel.add(serialLabel, gbc);

        // Ngày mua
        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Ngày mua: "), gbc);
        gbc.gridx = 1;
        purchaseDateLabel = new JLabel("Chưa chọn");
        botPanel.add(purchaseDateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        botPanel.add(new JLabel("Tình trạng bảo hành: "), gbc);

        gbc.gridx = 1;
        String[] warrantyStatusOptions = {"Bảo hành", "Chưa bảo hành"};
        warrantyStatusComboBox = new JComboBox<>(warrantyStatusOptions);
        warrantyStatusComboBox.setSelectedIndex(1); // Mặc định là "Chưa bảo hành"
        botPanel.add(warrantyStatusComboBox, gbc);

        // Lý do bảo hành
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        reasonField = new JTextField(20);
        botPanel.add(reasonField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        saveButton = new CustomButton("💾 Lưu");
        botPanel.add(saveButton, gbc);

        // Sự kiện chọn dòng trong bảng
        warrantyTable.getAccountTable().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = warrantyTable.getAccountTable().getSelectedRow();
            if (selectedRow != -1) {
                serialLabel.setText((String) warrantyTable.getAccountTable().getValueAt(selectedRow, 1));
                purchaseDateLabel.setText("2025-01-15"); // Giả lập ngày mua
            }
        });

        // ========== THÊM MỌI THỨ VÀO MAINPANEL ==========
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);
    }

    private void addFakeData() {
        warrantyTable.addRow(new Object[]{"BH001", "SN123456", "Đang bảo hành", "Chưa đổi trả"});
        warrantyTable.addRow(new Object[]{"BH002", "SN654321", "Chưa bảo hành", "Đã đổi trả"});
        warrantyTable.addRow(new Object[]{"BH003", "SN789123", "Chưa bảo hành", "Chưa đổi trả"});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý bảo hành");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new GUI_Guarantee());
            frame.setVisible(true);
        });
    }
}
