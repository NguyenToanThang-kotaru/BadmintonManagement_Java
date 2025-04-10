package GUI;

import BUS.SuppliersBUS;
import Connection.DatabaseConnection;
import DTO.SuppliersDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GUI_Suppliers extends JPanel {
    
    private JPanel topPanel, midPanel, botPanel;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, productListButton;
    private CustomButton addButton;
    private CustomSearch searchField;
    private SuppliersBUS suppliersBUS;
    private JLabel suppliersnameLabel, suppliersidLabel, addressLabel, phoneLabel;
    private JPanel buttonPanel;

    public GUI_Suppliers() {
        suppliersBUS = new SuppliersBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));
        
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275, 20);
        searchField.setBackground(Color.WHITE);
        searchField.setSearchListener(e -> searchSuppliers());
        topPanel.add(searchField, BorderLayout.CENTER);

        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        String[] columnNames = {"Mã NCC", "Tên NCC", "Địa chỉ", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        supplierTable = customTable.getSuppliersTable();
        tableModel = customTable.getTableModel();
        
        midPanel.add(customTable, BorderLayout.CENTER);

        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Nhà Cung Cấp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên Nhà Cung Cấp: "), gbc);
        gbc.gridx = 1;
        suppliersnameLabel = new JLabel("Chọn Nhà Cung Cấp");
        botPanel.add(suppliersnameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhà Cung Cấp: "), gbc);
        gbc.gridx = 1;
        suppliersidLabel = new JLabel("");
        botPanel.add(suppliersidLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Địa Chỉ: "), gbc);
        gbc.gridx = 1;
        addressLabel = new JLabel("");
        botPanel.add(addressLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Số Điện Thoại: "), gbc);
        gbc.gridx = 1;
        phoneLabel = new JLabel("");
        botPanel.add(phoneLabel, gbc);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton);

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton);

        productListButton = new CustomButton("Danh sách SP");
        productListButton.setCustomColor(new Color(0, 0, 230));
        buttonPanel.add(productListButton);

        addButton = new CustomButton("+ Thêm Nhà Cung Cấp");
        topPanel.add(addButton, BorderLayout.EAST);

        addButton.addActionListener(e -> new GUI_Form_Suppliers(this).setVisible(true));

        editButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                String supplierID = (String) supplierTable.getValueAt(selectedRow, 0);
                String name = suppliersnameLabel.getText();
                String address = addressLabel.getText();
                String phone = phoneLabel.getText();

                SuppliersDTO supplier = new SuppliersDTO(supplierID, name, address, phone);
                GUI_Edit_Suppliers editDialog = new GUI_Edit_Suppliers(this, supplier);
                editDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                String supplierID = (String) supplierTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteSupplier(supplierID);
                    loadSuppliers();
                    clearDetails();
                    JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        productListButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                String supplierID = (String) supplierTable.getValueAt(selectedRow, 0);
                String name = suppliersnameLabel.getText();
                SuppliersDTO supplier = new SuppliersDTO(supplierID, name, addressLabel.getText(), phoneLabel.getText());
                new GUI_Detail_Suppliers(this, supplier).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xem danh sách sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                String mancc = (String) supplierTable.getValueAt(selectedRow, 0);
                String hoten = (String) supplierTable.getValueAt(selectedRow, 1);
                String diachi = (String) supplierTable.getValueAt(selectedRow, 2);
                String sdt = (String) supplierTable.getValueAt(selectedRow, 3);

                suppliersnameLabel.setText(hoten);
                suppliersidLabel.setText(mancc);
                addressLabel.setText(diachi);
                phoneLabel.setText(sdt);

                botPanel.remove(buttonPanel);
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                botPanel.add(buttonPanel, gbc);

                botPanel.revalidate();
                botPanel.repaint();
            }
        });
        
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadSuppliers();
    }

    public void loadSuppliers() {
        List<SuppliersDTO> suppliers = suppliersBUS.getAllSuppliers();
        tableModel.setRowCount(0);
        for (SuppliersDTO sps : suppliers) {
            if (sps.getsuppliersID() != null && !sps.getsuppliersID().isEmpty()) {
                tableModel.addRow(new Object[]{sps.getsuppliersID(), sps.getfullname(), sps.getaddress(), sps.getphone()});
            }
        }
    }

    private void searchSuppliers() {
        String keyword = searchField.getText().trim().toLowerCase();
        List<SuppliersDTO> suppliers = suppliersBUS.getAllSuppliers();
        tableModel.setRowCount(0);

        for (SuppliersDTO supplier : suppliers) {
            if (supplier.getsuppliersID().toLowerCase().contains(keyword) ||
                supplier.getfullname().toLowerCase().contains(keyword) ||
                supplier.getaddress().toLowerCase().contains(keyword) ||
                supplier.getphone().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    supplier.getsuppliersID(),
                    supplier.getfullname(),
                    supplier.getaddress(),
                    supplier.getphone()
                });
            }
        }
    }

    private void deleteSupplier(String supplierID) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE nha_cung_cap SET is_deleted = 1 WHERE ma_nha_cung_cap = ?")) {
            stmt.setString(1, supplierID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhà cung cấp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearDetails() {
        suppliersnameLabel.setText("Chọn Nhà Cung Cấp");
        suppliersidLabel.setText("");
        addressLabel.setText("");
        phoneLabel.setText("");
        botPanel.remove(buttonPanel);
        botPanel.revalidate();
        botPanel.repaint();
    }
}