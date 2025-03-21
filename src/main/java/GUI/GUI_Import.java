package GUI;

import BUS.ImportBUS;
import DTO.ImportDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Import extends JPanel {

    private JPanel midPanel, topPanel, botPanel, buttonPanel;
    private JTable importTable;
    private DefaultTableModel tableModel;
    private CustomButton deleteButton, addButton, editButton;
    private CustomSearch searchField;
    private ImportBUS importBUS;
    private JLabel importIDLabel, employeeLabel, supplierLabel, totalMoneyLabel, importDateLabel;

    public GUI_Import() {
        importBUS = new ImportBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));
        
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275, 20);
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm phiếu nhập");
        topPanel.add(addButton, BorderLayout.EAST);
        
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"Mã nhập", "Nhân viên", "Nhà cung cấp", "Tổng tiền", "Ngày nhập"};
        CustomTable customTable = new CustomTable(columnNames);
        importTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();

        midPanel.add(customTable, BorderLayout.CENTER);
        
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        importIDLabel = new JLabel("");
        employeeLabel = new JLabel("");
        supplierLabel = new JLabel("");
        totalMoneyLabel = new JLabel("");
        importDateLabel = new JLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã nhập: "), gbc);
        gbc.gridx = 1;
        botPanel.add(importIDLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Nhân viên: "), gbc);
        gbc.gridx = 1;
        botPanel.add(employeeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Nhà cung cấp: "), gbc);
        gbc.gridx = 1;
        botPanel.add(supplierLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Tổng tiền: "), gbc);
        gbc.gridx = 1;
        botPanel.add(totalMoneyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        botPanel.add(new JLabel("Ngày nhập: "), gbc);
        gbc.gridx = 1;
        botPanel.add(importDateLabel, gbc);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xoá");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton);

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        botPanel.add(buttonPanel, gbc);

        importTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = importTable.getSelectedRow();
            if (selectedRow != -1) {
                String importID = String.valueOf(importTable.getValueAt(selectedRow, 0));
                String employeeID = String.valueOf(importTable.getValueAt(selectedRow, 1));
                String supplierID = String.valueOf(importTable.getValueAt(selectedRow, 2));
                double totalMoney = (Double) importTable.getValueAt(selectedRow, 3);
                String importDate = String.valueOf(importTable.getValueAt(selectedRow, 4));

                importIDLabel.setText(importID);
                employeeLabel.setText(employeeID);
                supplierLabel.setText(supplierID);
                totalMoneyLabel.setText(String.format("%,.0f", totalMoney));
                importDateLabel.setText(importDate);
            }
        });
        
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);
        
        loadImports();
    }
    
    private void loadImports() {
        List<ImportDTO> imports = importBUS.getAllImports();
        tableModel.setRowCount(0);
        for (ImportDTO imp : imports) {
            tableModel.addRow(new Object[]{imp.getImportID(), imp.getEmployeeID(), imp.getSupplierID(), imp.getTotalMoney(), imp.getImportDate()});
        }
    }
}
