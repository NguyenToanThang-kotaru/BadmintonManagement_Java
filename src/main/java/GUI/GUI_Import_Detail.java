package GUI;

import BUS.DetailImportBUS;
import DTO.ImportDTO;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GUI_Import_Detail extends JDialog {
    private JTable productsTable;
    private JScrollPane scrollPane;
    private DetailImportBUS bus;

    public GUI_Import_Detail(JFrame parent, ImportDTO importDTO) {
        super(parent, "Chi Tiết Phiếu Nhập", true);
        bus = new DetailImportBUS();
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Header panel for title and details
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("CHI TIẾT PHIẾU NHẬP");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        headerPanel.add(title, gbc);

        // Details
        gbc.gridwidth = 1;
        gbc.gridy++;
        addDetailRow(headerPanel, gbc, "Mã phiếu nhập:", importDTO.getimportID());
        gbc.gridy++;
        addDetailRow(headerPanel, gbc, "Ngày nhập:", importDTO.getreceiptdate());
        gbc.gridy++;
        addDetailRow(headerPanel, gbc, "Nhân viên nhập:", bus.getEmployeeInfo(importDTO.getemployeeID()));

        // Table panel
        String[] columnNames = {"Mã SP", "Tên SP", "Nhà cung cấp", "Số lượng", "Giá gốc", "Giá bán", "Tổng tiền nhập", "Tổng tiền bán"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(model);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Center-align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Adjust column widths
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã SP
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Tên SP
        productsTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Nhà cung cấp
        productsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Số lượng
        productsTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Giá gốc
        productsTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Giá bán
        productsTable.getColumnModel().getColumn(6).setPreferredWidth(170); // Tổng tiền nhập
        productsTable.getColumnModel().getColumn(7).setPreferredWidth(170); // Tổng tiền bán

        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Footer panel for total and close button
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        int[] totals = loadImportDetails(importDTO.getimportID(), model);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addDetailRow(footerPanel, gbc, "Tổng tiền nhập:", Utils.formatCurrency(totals[0]));
        
        gbc.gridy++;
        addDetailRow(footerPanel, gbc, "Tổng tiền bán:", Utils.formatCurrency(totals[1]));
        
        gbc.gridy++;
        CustomButton btnClose = new CustomButton("Đóng");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.addActionListener(e -> dispose());
        footerPanel.add(btnClose, gbc);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(labelLbl, gbc);

        gbc.gridx = 1;
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(valueLbl, gbc);
    }
    private int[] loadImportDetails(String importID, DefaultTableModel model) {
        List<Object[]> details = bus.loadImportDetails(importID);
        int totalImport = 0;
        int totalSale = 0;
        model.setRowCount(0);
    
        for (Object[] detail : details) {
            int quantity = (int) detail[3];
            int originalPrice = Integer.parseInt(((String) detail[4]).replaceAll("[^0-9]", ""));
            int salePrice = Integer.parseInt(((String) detail[5]).replaceAll("[^0-9]", ""));
            totalImport += quantity * originalPrice;
            totalSale += quantity * salePrice;
            model.addRow(detail);
        }
    
        if (details.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào cho phiếu nhập " + importID,
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
        } else {
            SwingUtilities.invokeLater(() -> {
                model.fireTableDataChanged();
                productsTable.revalidate();
                productsTable.repaint();
                scrollPane.revalidate();
                scrollPane.repaint();
            });
        }
        return new int[]{totalImport, totalSale};
    }
}