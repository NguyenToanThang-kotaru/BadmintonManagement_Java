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
        setSize(1000, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Tạo panel chính với GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Giảm padding trái/phải để bảng rộng hơn
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Thêm tiêu đề
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("CHI TIẾT PHIẾU NHẬP");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        mainPanel.add(title, gbc);

        // Thêm thông tin cơ bản
        gbc.gridwidth = 1;
        gbc.gridy++;
        addDetailRow(mainPanel, gbc, "Mã phiếu nhập:", importDTO.getimportID());
        gbc.gridy++;
        addDetailRow(mainPanel, gbc, "Ngày nhập:", importDTO.getreceiptdate());
        gbc.gridy++;
        addDetailRow(mainPanel, gbc, "Nhân viên nhập:", bus.getEmployeeInfo(importDTO.getemployeeID()));

        // Tạo bảng sản phẩm
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] columnNames = {"Mã SP", "Tên SP", "Nhà cung cấp", "Số lượng", "Giá gốc", "Giá bán", "Tổng tiền nhập", "Tổng tiền bán"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(model);
        productsTable.setRowHeight(30); // Tăng chiều cao hàng để dễ đọc

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Điều chỉnh độ rộng cột
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã SP
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Tên SP
        productsTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Nhà cung cấp
        productsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Số lượng
        productsTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Giá gốc
        productsTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Giá bán
        productsTable.getColumnModel().getColumn(6).setPreferredWidth(170); // Tổng tiền nhập
        productsTable.getColumnModel().getColumn(7).setPreferredWidth(170); // Tổng tiền bán

        scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(950, 350)); // Tăng chiều rộng và chiều cao bảng
        mainPanel.add(scrollPane, gbc);

        // Tải chi tiết phiếu nhập và tính tổng tiền
        int calculatedTotal = loadImportDetails(importDTO.getimportID(), model);

        // Hiển thị tổng tiền
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        addDetailRow(mainPanel, gbc, "Tổng tiền:", Utils.formatCurrency(calculatedTotal));

        // Thêm nút Đóng
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        CustomButton btnClose = new CustomButton("Đóng");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.addActionListener(e -> dispose());
        mainPanel.add(btnClose, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Thêm một hàng thông tin (label-value) vào panel
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

    // Tải chi tiết phiếu nhập và tính tổng tiền
    private int loadImportDetails(String importID, DefaultTableModel model) {
        List<Object[]> details = bus.loadImportDetails(importID);
        int total = 0;
        model.setRowCount(0);

        for (Object[] detail : details) {
            int quantity = (int) detail[3]; // Số lượng
            int price = Integer.parseInt(((String) detail[4]).replaceAll("[^0-9]", "")); // Giá gốc
            total += quantity * price;
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
        return total;
    }
}