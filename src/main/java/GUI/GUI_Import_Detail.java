package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Connection.DatabaseConnection;
import DTO.ImportDTO;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GUI_Import_Detail extends JDialog {
    private JTable productsTable;
    private JScrollPane scrollPane;

    public GUI_Import_Detail(JFrame parent, ImportDTO importDTO) {
        super(parent, "Chi Tiết Phiếu Nhập", true);
        setSize(700, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Tạo panel chính với GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
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
        addDetailRow(mainPanel, gbc, "Nhân viên nhập:", getEmployeeInfo(importDTO.getemployeeID()));
        gbc.gridy++;
        addDetailRow(mainPanel, gbc, "Nhà cung cấp:", getSupplierInfo(importDTO.getsupplierID()));

        // Tạo bảng sản phẩm
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] columnNames = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productsTable = new JTable(model);
        productsTable.setRowHeight(25);

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productsTable.getColumnCount(); i++) {
            productsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
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

    // Lấy thông tin nhân viên từ cơ sở dữ liệu
    private String getEmployeeInfo(String employeeID) {
        String query = "SELECT ten_nhan_vien, so_dien_thoai, dia_chi FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.format("%s - %s - %s - %s",
                        employeeID,
                        rs.getString("ten_nhan_vien"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("dia_chi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeID;
    }

    // Lấy thông tin nhà cung cấp từ cơ sở dữ liệu
    private String getSupplierInfo(String supplierID) {
        String query = "SELECT ten_nha_cung_cap, dia_chi, so_dien_thoai FROM nha_cung_cap WHERE ma_nha_cung_cap = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.format("%s - %s - %s",
                        rs.getString("ten_nha_cung_cap"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierID;
    }

    // Tải chi tiết phiếu nhập và tính tổng tiền
    private int loadImportDetails(String importID, DefaultTableModel model) {
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, ctnh.so_luong, ctnh.gia " +
                "FROM chi_tiet_nhap_hang ctnh " +
                "JOIN san_pham sp ON ctnh.ma_san_pham = sp.ma_san_pham " +
                "WHERE ctnh.ma_nhap_hang = ?";

        int total = 0;
        model.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            ResultSet rs = stmt.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int quantity = rs.getInt("so_luong");
                int price = rs.getInt("gia");
                int rowTotal = quantity * price;
                total += rowTotal;

                model.addRow(new Object[]{
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        quantity,
                        Utils.formatCurrency(price),
                        Utils.formatCurrency(rowTotal)
                });
            }

            if (!hasData) {
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

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return total;
    }
}