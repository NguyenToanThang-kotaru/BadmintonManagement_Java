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
        String[] columnNames = {"Mã BH", "Mã Serial", "Lý do bảo hành", "Thời gian bảo hành"};

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

        gbc.gridx = 0;
        gbc.gridy = 4;
        botPanel.add(new JLabel("Thời gian bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel StatusTime = new JLabel("");
        botPanel.add(StatusTime, gbc);

        // Nút sửa
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        fixButton = new CustomButton("Sửa");
        fixButton.setCustomColor(Color.RED);
//        botPanel.add(fixButton, gbc);

        // ========== THÊM MỌI THỨ VÀO MAIN PANEL ==========
        add(topPanel);
        add(midPanel);
        add(botPanel);

        fixButton.addActionListener(e -> {
            int selectedRow = warrantyTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một bảo hành để sửa!");
                return;
            }

            // Lấy mã bảo hành từ bảng
            String guaranteeID = (String) warrantyTable.getValueAt(selectedRow, 0);

            // Lấy dữ liệu bảo hành từ database
            GuaranteeDTO guarantee = GuaranteeDAO.getGuarantee(guaranteeID);

            GUI_FormGuarantee fixForm = new GUI_FormGuarantee((JFrame) SwingUtilities.getWindowAncestor(this), this, guarantee);
            fixForm.setVisible(true);
        });

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
                    StatusTime.setText(guarantee.getTGBH());
                    botPanel.add(fixButton, gbc);

                }
            }
        });

    }

    private void loadGuaranteeData() {
        List<GuaranteeDTO> guaranteeList = GuaranteeDAO.getAllGuarantee();
        for (GuaranteeDTO guarantee : guaranteeList) {
            tableModel.addRow(new Object[]{
                guarantee.getBaohanhID(), guarantee.getSerialID(), guarantee.getLydo(), guarantee.getTGBH()
            });
        }
    }
//

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
