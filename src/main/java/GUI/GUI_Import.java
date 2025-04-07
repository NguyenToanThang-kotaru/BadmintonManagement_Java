package GUI;

import BUS.ImportBUS;
import DTO.ImportDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class GUI_Import extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private JTable importTable;
    private DefaultTableModel tableModel;
    private CustomButton deleteButton, addButton, detailimportButton;
    private CustomSearch searchField;
    private ImportBUS importBUS;
    private ImportDTO importChoosing;
    private String currentUsername;

     public GUI_Import() {
        
        this.currentUsername = GUI_Login.getCurrentUsername();
        importBUS = new ImportBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút thêm) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275, 20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Phiếu Nhập"); // Nút thêm phiếu nhập
        topPanel.add(addButton, BorderLayout.EAST);
        addButton.addActionListener(e -> {
            GUI_Form_Import GFI = new GUI_Form_Import(this, currentUsername);
            GFI.setVisible(true);
        });
        // ========== BẢNG HIỂN THỊ DANH SÁCH PHIẾU NHẬP ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã PN", "Mã NV", "Mã NCC", "Tổng Tiền", "Ngày Nhập"};
        CustomTable customTable = new CustomTable(columnNames);
        importTable = customTable.getImportTable();
        tableModel = customTable.getTableModel();

        midPanel.add(customTable, BorderLayout.CENTER);
        CustomScrollPane scrollPane = new CustomScrollPane(importTable);

        // ========== PANEL CHI TIẾT PHIẾU NHẬP ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Hóa Đơn Nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin phiếu nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Phiếu Nhập: "), gbc);
        gbc.gridx = 1;
        JLabel importidLabel = new JLabel("Chọn Hóa Đơn Nhập");
        botPanel.add(importidLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeidLabel = new JLabel("");
        botPanel.add(employeeidLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Mã Nhà Cung Cấp: "), gbc);
        gbc.gridx = 1;
        JLabel supplieridLabel = new JLabel("");
        botPanel.add(supplieridLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Tổng Tiền: "), gbc);
        gbc.gridx = 1;
        JLabel totalmoneyLabel = new JLabel("");
        botPanel.add(totalmoneyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        botPanel.add(new JLabel("Ngày Nhập: "), gbc);
        gbc.gridx = 1;
        JLabel receiptdateLabel = new JLabel("");
        botPanel.add(receiptdateLabel, gbc);

        // ========== PANEL BUTTON ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);


        detailimportButton = new CustomButton("Xem Chi Tiết Hóa Đơn Nhập");
        detailimportButton.setCustomColor(new Color(0, 120, 215));
        buttonPanel.add(detailimportButton, BorderLayout.EAST);
        detailimportButton.addActionListener(e -> {
            if (importChoosing != null) {
                GUI_Import_Detail detailDialog = new GUI_Import_Detail(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        importChoosing
                );
                detailDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xem chi tiết!");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        botPanel.add(buttonPanel, gbc);
        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadImport();


        deleteButton.addActionListener(e -> {
            if (importChoosing != null) {
                int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phiếu nhập này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    importBUS.deleteImport(importChoosing.getimportID());
                    loadImport();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xóa!");
            }
        });


        importTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = importTable.getSelectedRow();
            if (selectedRow != -1) {
                String importID = (String) importTable.getValueAt(selectedRow, 0);
                String employeeID = (String) importTable.getValueAt(selectedRow, 1);
                String supplierID = (String) importTable.getValueAt(selectedRow, 2);
                String totalMoney = (String) importTable.getValueAt(selectedRow, 3);
                String receiptDate = (String) importTable.getValueAt(selectedRow, 4);

                importChoosing = new ImportDTO(importID, employeeID, supplierID, totalMoney, receiptDate);

                importidLabel.setText(importID);
                employeeidLabel.setText(employeeID);
                supplieridLabel.setText(supplierID);
                totalmoneyLabel.setText(totalMoney);
                receiptdateLabel.setText(receiptDate);
            }
        });
    }

    void loadImport() {
        importBUS = new ImportBUS(); // Khởi tạo ImportBUS ở đây để đảm bảo nó được khởi tạo
        List<ImportDTO> importList = importBUS.getAllImport();
        tableModel.setRowCount(0);
        for (ImportDTO importDTO : importList) {
            tableModel.addRow(new Object[]{
                    importDTO.getimportID(),
                    importDTO.getemployeeID(),
                    importDTO.getsupplierID(),
                    Utils.formatCurrency(importDTO.gettotalmoney()),
                    importDTO.getreceiptdate()
            });
        }
    }
}