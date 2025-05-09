package GUI;

import BUS.ActionBUS;
import BUS.ImportBUS;
import DTO.ActionDTO;
import DTO.ImportDTO;
import DTO.AccountDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI_Import extends JPanel {

    private final ImportBUS importBUS;
    private final DefaultTableModel tableModel;
    private final JTable importTable;
    private final CustomSearch searchField;
    private final JLabel importIdLabel;
    private final JLabel employeeIdLabel;
    private final JLabel totalMoneyLabel;
    private final JLabel receiptDateLabel;
    private ImportDTO selectedImport;

    public GUI_Import(AccountDTO a) {
        this.importBUS = new ImportBUS();
        String currentUsername = GUI_Login.getCurrentUsername();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // Panel trên cùng: Thanh tìm kiếm và nút Thêm
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275, 20);
        searchField.setBackground(Color.WHITE);
        searchField.setSearchListener(e -> searchImport());
        topPanel.add(searchField, BorderLayout.CENTER);

        CustomButton exportExcelButton = new CustomButton("Xuất Excel");
        exportExcelButton.setPreferredSize(new Dimension(120, 30));
        topPanel.add(exportExcelButton, BorderLayout.WEST);

        CustomButton addButton = new CustomButton("+ Thêm Phiếu Nhập");
        addButton.addActionListener(e -> {
            Form_Import form = new Form_Import(this, currentUsername);
            form.setVisible(true);
        });
        topPanel.add(addButton, BorderLayout.EAST);

        // Bảng hiển thị danh sách phiếu nhập
        String[] columnNames = {"Mã PN", "Mã NV", "Tổng Tiền", "Ngày Nhập"}; // Xóa "Mã NCC"
        CustomTable customTable = new CustomTable(columnNames);
        importTable = customTable.getImportTable();
        tableModel = customTable.getTableModel();

        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        midPanel.add(new CustomScrollPane(importTable), BorderLayout.CENTER);

        // Panel chi tiết phiếu nhập
        JPanel botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Hóa Đơn Nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Phiếu Nhập: "), gbc);
        gbc.gridx = 1;
        importIdLabel = new JLabel("Chọn Hóa Đơn Nhập");
        botPanel.add(importIdLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhân Viên: "), gbc);
        gbc.gridx = 1;
        employeeIdLabel = new JLabel("");
        botPanel.add(employeeIdLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Tổng Tiền: "), gbc);
        gbc.gridx = 1;
        totalMoneyLabel = new JLabel("");
        botPanel.add(totalMoneyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Ngày Nhập: "), gbc);
        gbc.gridx = 1;
        receiptDateLabel = new JLabel("");
        botPanel.add(receiptDateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        botPanel.add(new JLabel("Tổng Giá Gốc: "), gbc);
        gbc.gridx = 1;
        JLabel totalOriginalPriceLabel = new JLabel("");
        botPanel.add(totalOriginalPriceLabel, gbc);

        // Panel chứa các nút Xóa và Xem Chi Tiết
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        CustomButton deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        deleteButton.addActionListener(e -> deleteImport());
        buttonPanel.add(deleteButton);

        CustomButton detailButton = new CustomButton("Xem Chi Tiết Hóa Đơn Nhập");
        detailButton.setCustomColor(new Color(0, 120, 215));
        detailButton.addActionListener(e -> showImportDetail());
        buttonPanel.add(detailButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        botPanel.add(buttonPanel, gbc);

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        // Tải danh sách phiếu nhập
        loadImport();

        // Thêm sự kiện chọn hàng trong bảng
        importTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = importTable.getSelectedRow();
            if (selectedRow != -1) {
                String importID = (String) importTable.getValueAt(selectedRow, 0);
                String employeeID = (String) importTable.getValueAt(selectedRow, 1);
                String receiptDate = (String) importTable.getValueAt(selectedRow, 3);

                int calculatedTotal = importBUS.calculateImportTotal(importID);
                selectedImport = new ImportDTO(importID, employeeID, String.valueOf(calculatedTotal), receiptDate);

                importIdLabel.setText(importID);
                employeeIdLabel.setText(employeeID);
                totalMoneyLabel.setText(Utils.formatCurrency(calculatedTotal));
                totalOriginalPriceLabel.setText(Utils.formatCurrency(calculatedTotal));
                receiptDateLabel.setText(receiptDate);
            }
        });
        exportExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachPhieuNhap.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);
        
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                boolean success = importBUS.exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(a, "Quản lý hóa đơn nhập");

        boolean canAdd = false, canEdit = false, canDelete = false, canWatch = false;

        if (actions != null) {
            for (ActionDTO action : actions) {
                switch (action.getName()) {
                    case "Thêm" ->
                        canAdd = true;
                    case "Sửa" ->
                        canEdit = true;
                    case "Xóa" ->
                        canDelete = true;
                    case "Xem" ->
                        canWatch = true;
                }
            }
        }

        addButton.setVisible(canAdd);
        deleteButton.setVisible(canDelete);
    }
    

    // Tải danh sách phiếu nhập vào bảng
    public void loadImport() {
        List<ImportDTO> importList = importBUS.getAllImport();
        tableModel.setRowCount(0);
        for (ImportDTO importDTO : importList) {
            int calculatedTotal = importBUS.calculateImportTotal(importDTO.getimportID());
            tableModel.addRow(new Object[]{
                importDTO.getimportID(),
                importDTO.getemployeeID(),
                Utils.formatCurrency(calculatedTotal),
                importDTO.getreceiptdate()
            });
        }
    }

    
    // Tìm kiếm phiếu nhập theo từ khóa
    private void searchImport() {
        String keyword = searchField.getText().trim().toLowerCase();
        List<ImportDTO> importList = importBUS.getAllImport();
        tableModel.setRowCount(0);
    
        for (ImportDTO importDTO : importList) {
            int calculatedTotal = importBUS.calculateImportTotal(importDTO.getimportID());
            String receiptDate = importDTO.getreceiptdate().toLowerCase();
            boolean matchesIdOrEmployee = importDTO.getimportID().toLowerCase().contains(keyword);
            importDTO.getemployeeID().toLowerCase().contains(keyword);
            boolean matchesDate = receiptDate.contains(keyword);
            
            boolean matchesTotal = false;
            try {
                String cleanKeyword = keyword.replaceAll("[^0-9]", "");
                if (!cleanKeyword.isEmpty()) {
                    long keywordTotal = Long.parseLong(cleanKeyword);
                    matchesTotal = String.valueOf(calculatedTotal).contains(cleanKeyword);
                }
            } catch (NumberFormatException e) {
            }
            if (matchesIdOrEmployee || matchesDate || matchesTotal) {
                tableModel.addRow(new Object[]{
                    importDTO.getimportID(),
                    importDTO.getemployeeID(),
                    Utils.formatCurrency(calculatedTotal),
                    importDTO.getreceiptdate()
                });
            }
        }
    }

    private void deleteImport() {
        if (selectedImport == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xóa!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đánh dấu phiếu nhập này là đã xóa?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            importBUS.deleteImport(selectedImport.getimportID());
            loadImport();
            // Reset thông tin chi tiết
            importIdLabel.setText("Chọn Hóa Đơn Nhập");
            employeeIdLabel.setText("");
            totalMoneyLabel.setText("");
            receiptDateLabel.setText("");
            selectedImport = null;
        }
    }

    // Hiển thị chi tiết phiếu nhập
    private void showImportDetail() {
        if (selectedImport == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xem chi tiết!");
            return;
        }

        GUI_Import_Detail detailDialog = new GUI_Import_Detail(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                selectedImport
        );
        detailDialog.setVisible(true);
    }
}
