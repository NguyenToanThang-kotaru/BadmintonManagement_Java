package GUI;

import BUS.SuppliersBUS;
import DTO.SuppliersDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Suppliers extends JPanel {
    
    private JPanel topPanel, midPanel, botPanel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton;
    private CustomSearch searchField;
    private SuppliersBUS suppliersBUS;   

    public GUI_Suppliers() {
        suppliersBUS = new SuppliersBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));
        
        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút thêm) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275,20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm Nhà Cung Cấp"); // Nút thêm nhà cung cấp
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH NHÀ CUNG CẤP ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã NCC", "Tên NCC", "Địa chỉ", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        customerTable = customTable.getCustomerTable(); 
        tableModel = customTable.getTableModel(); 
        
        midPanel.add(customTable, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT NHÀ CUNG CẤP ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Nhà Cung Cấp"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nhãn hiển thị thông tin nhà cung cấp
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên Nhà Cung Cấp: "), gbc);
        gbc.gridx = 1;
        JLabel suppliersnameLabel = new JLabel("Chọn Nhà Cung Cấp");
        botPanel.add(suppliersnameLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhà Cung Cấp: "), gbc);
        gbc.gridx = 1;
        JLabel suppliersidLabel = new JLabel("");
        botPanel.add(suppliersidLabel, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 2;
        botPanel.add(new JLabel("Địa Chỉ: "), gbc);
        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        botPanel.add(addressLabel, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 3;
        botPanel.add(new JLabel("Số Điện Thoại: "), gbc);
        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        botPanel.add(phoneLabel, gbc);

        // ========== PANEL BUTTON ==========
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {

                String hoten = (String) customerTable.getValueAt(selectedRow, 0);
                String mancc = (String) customerTable.getValueAt(selectedRow, 1);
                String diachi = (String) customerTable.getValueAt(selectedRow, 2);
                String sdt = (String) customerTable.getValueAt(selectedRow, 3);

                // Hiển thị dữ liệu trên giao diện
                suppliersnameLabel.setText(hoten);
                suppliersidLabel.setText(mancc);
                addressLabel.setText(diachi);
                phoneLabel.setText(sdt);
                botPanel.add(buttonPanel, gbc);
            }   
        });
        
        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadSuppliers();
        
    }

    private void loadSuppliers() {
        List<SuppliersDTO> suppliers = suppliersBUS.getAllSuppliers();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (SuppliersDTO sps : suppliers) {
            tableModel.addRow(new Object[]{sps.getsuppliersID(), sps.getfullname(), sps.getaddress(), sps.getphone()});
        }
    }
}
