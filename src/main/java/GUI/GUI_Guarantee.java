package GUI;

import DAO.GuaranteeDAO;
import DTO.GuaranteeDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.table.TableColumnModel;
import java.util.List;

public class GUI_Guarantee extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable warrantyTable;
    private CustomTable tableModel;
    private JComboBox<String> warrantyStatusComboBox;
    private CustomButton saveButton, fixButton;
    private CustomSearch searchField;

    public GUI_Guarantee() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Thanh tìm kiếm
        searchField = new CustomSearch(250, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã BH", "Mã Serial", "Trạng thái", "Lý do"};

        tableModel = new CustomTable(columnNames);
        warrantyTable = tableModel.getAccountTable();

        TableColumnModel columnModel = warrantyTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(warrantyTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết bảo hành"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Mã Serial
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Serial: "), gbc);
        gbc.gridx = 1;
        JLabel serialLabel = new JLabel("Chưa chọn");
        botPanel.add(serialLabel, gbc);

        // Ngày mua
        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Ngày mua: "), gbc);
        gbc.gridx = 1;
        JLabel purchaseDateLabel = new JLabel("Chưa chọn");
        botPanel.add(purchaseDateLabel, gbc);

        // Trạng thái bảo hành
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tình trạng bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel StatusLabel = new JLabel("Chưa bảo hành");
        botPanel.add(StatusLabel, gbc);

        // Lý do bảo hành
        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel textReasonLabel = new JLabel("None");
        botPanel.add(textReasonLabel, gbc);

        // Nút sửa
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        fixButton = new CustomButton("Sửa");
        fixButton.setCustomColor(Color.RED);
//        botPanel.add(fixButton, gbc);

        // ========== THÊM MỌI THỨ VÀO MAIN PANEL ==========
        add(topPanel);
        add(midPanel);
        add(botPanel);
        fixButton.addActionListener(e -> showEditForm());
        loadGuaranteeData();

        // Xử lý sự kiện chọn dòng trong bảng
        // Xử lý sự kiện chọn dòng trong bảng
        warrantyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Ngăn sự kiện kích hoạt nhiều lần
                int selectedRow = warrantyTable.getSelectedRow();
                if (selectedRow != -1) {
                    String guaranteeID = (String) warrantyTable.getValueAt(selectedRow, 0);
                    GuaranteeDTO guarantee = GuaranteeDAO.getGuarantee(guaranteeID);

                    serialLabel.setText(guarantee.getSerialID());
                    textReasonLabel.setText(guarantee.getLydo());
                    botPanel.add(fixButton, gbc);

                }
            }
        });

    }

    private void showEditForm() {
        JDialog fixForm = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa", true);
        fixForm.setSize(400, 250);
        fixForm.setLayout(new GridBagLayout());
        fixForm.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        fixForm.add(new JLabel("Mã Serial: "), gbc);
        gbc.gridx = 1;
        JTextField serialField = new JTextField(15);
        fixForm.add(serialField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fixForm.add(new JLabel("Tình trạng bảo hành: "), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Bảo hành", "Chưa bảo hành"});
        fixForm.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        fixForm.add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        JTextField reasonField = new JTextField(15);
        fixForm.add(reasonField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        saveButton = new CustomButton("Lưu");

        saveButton.addActionListener(e -> {
//        int selectedRow = warrantyTable.getAccountTable().getSelectedRow();
//        if (selectedRow != -1) {
//            // Cập nhật giá trị trong bảng
//            warrantyTable.getAccountTable().setValueAt(serialField.getText(), selectedRow, 1);
//            warrantyTable.getAccountTable().setValueAt(statusComboBox.getSelectedItem(), selectedRow, 2);
//            textReasonLabel.setText(reasonField.getText());
//        }
            fixForm.dispose();
        });

        fixForm.add(saveButton, gbc);

        fixForm.setVisible(true);
    }

    private void loadGuaranteeData() {
        List<GuaranteeDTO> guaranteeList = GuaranteeDAO.getAllGuarantee();
        for (GuaranteeDTO guarantee : guaranteeList) {
            tableModel.addRow(new Object[]{
                guarantee.getBaohanhID(), guarantee.getSerialID(), guarantee.gettrangthai(), guarantee.getLydo()
            });
        }
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
