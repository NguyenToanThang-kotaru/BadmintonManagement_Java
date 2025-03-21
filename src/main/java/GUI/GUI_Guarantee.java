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
    private JLabel serialLabel, purchaseDateLabel, textReasonLabel;
    private CustomButton saveButton;
    private CustomButton fixButton;
    private JComboBox<String> warrantyStatusComboBox;
    private CustomSearch searchField;

    public GUI_Guarantee() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding trên-dưới 10px
        topPanel.setBackground(Color.WHITE);
        // Thanh tìm kiếm (70%)
        searchField = new CustomSearch(250, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
//        searchField.setPreferredSize(new Dimension(0, 30));
        topPanel.add(searchField, BorderLayout.CENTER);

        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã BH", "Mã Serial", "Bảo Hành",};

        warrantyTable = new CustomTable(columnNames); // Sửa lại

        TableColumnModel columnModel = warrantyTable.getAccountTable().getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(80);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof String) {
                    String text = (String) value;
                    setText(text); // Hiển thị nội dung gốc
                    if (text.equals("Chưa bảo hành")) {
                        setForeground(Color.GREEN); // Màu xanh lá cây
                    } else {
                        setForeground(Color.RED); // Màu đỏ
                    }
                }
            }
        };
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        warrantyTable.getAccountTable().getColumnModel().getColumn(2).setCellRenderer(statusRenderer);

//        // Thêm dữ liệu giả lập
//        addFakeData();

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
        textReasonLabel = new JLabel("None");
        botPanel.add(textReasonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        fixButton = new CustomButton("Sửa");
        fixButton.setCustomColor(Color.RED);
//        botPanel.add(fixButton, gbc);

        // Sự kiện chọn dòng trong bảng
        warrantyTable.getAccountTable().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = warrantyTable.getAccountTable().getSelectedRow();
            if (selectedRow != -1) {
                serialLabel.setText((String) warrantyTable.getAccountTable().getValueAt(selectedRow, 1));
                purchaseDateLabel.setText("2025-01-15");
                botPanel.add(fixButton, gbc);
                
            }
        });

        // ========== THÊM MỌI THỨ VÀO MAINPANEL ==========
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        fixButton.addActionListener(e -> showEditForm());

    }

    private void showEditForm() {
        JDialog fixformBaohanh = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa", true);
        fixformBaohanh.setSize(400, 250);
        fixformBaohanh.setLayout(new GridBagLayout());
        fixformBaohanh.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Label + Field Mã Serial
        gbc.gridx = 0;
        gbc.gridy = 0;
        fixformBaohanh.add(new JLabel("Mã Serial: "), gbc);
        gbc.gridx = 1;
        JTextField serialField = new JTextField(15);
        serialField.setText(serialLabel.getText()); // Hiển thị Serial hiện tại
        fixformBaohanh.add(serialField, gbc);

        // Label + ComboBox Tình trạng bảo hành
        gbc.gridx = 0;
        gbc.gridy = 1;
        fixformBaohanh.add(new JLabel("Tình trạng bảo hành: "), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Bảo hành", "Chưa bảo hành"});
        statusComboBox.setSelectedItem(warrantyStatusComboBox.getSelectedItem());
        fixformBaohanh.add(statusComboBox, gbc);

        // Label + Field Lý do bảo hành
        gbc.gridx = 0;
        gbc.gridy = 2;
        fixformBaohanh.add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        JTextField reasonField = new JTextField(15);
        reasonField.setText(textReasonLabel.getText());
        fixformBaohanh.add(reasonField, gbc);

        // Nút Lưu
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        saveButton = new CustomButton("Lưu");
        botPanel.add(saveButton, gbc);
        saveButton.addActionListener(e -> {
            int selectedRow = warrantyTable.getAccountTable().getSelectedRow();
            if (selectedRow != -1) {
                // Cập nhật giá trị trong bảng
                warrantyTable.getAccountTable().setValueAt(serialField.getText(), selectedRow, 1);
                warrantyTable.getAccountTable().setValueAt(statusComboBox.getSelectedItem(), selectedRow, 2);
                textReasonLabel.setText(reasonField.getText());
            }
            fixformBaohanh.dispose();
        });
        fixformBaohanh.add(saveButton, gbc);

        fixformBaohanh.setVisible(true);
    }

//    private void addFakeData() {
//        warrantyTable.addRow(new Object[]{"BH001", "SN123456", "Đang bảo hành"});
//        warrantyTable.addRow(new Object[]{"BH002", "SN654321", "Chưa bảo hành"});
//        warrantyTable.addRow(new Object[]{"BH003", "SN789123", "Chưa bảo hành"});
//    }

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
