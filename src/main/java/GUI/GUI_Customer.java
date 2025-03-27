package GUI;

import BUS.CustomerBUS;
import DTO.CustomerDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Customer extends JPanel {
    
    private JPanel topPanel, midPanel, botPanel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton;
    private CustomSearch searchField;
    private CustomScrollPane scroll;
    private CustomerBUS customerBUS;   

    public GUI_Customer() {
        customerBUS = new CustomerBUS();

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

        addButton = new CustomButton("+ Thêm Khách Hàng"); // Nút thêm khách hàng 
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH KHÁCH HÀNG ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã KH", "Họ Tên", "SĐT", "Email"};
        CustomTable customTable = new CustomTable(columnNames);
        customerTable = customTable.getCustomerTable(); 
        tableModel = customTable.getTableModel(); 
        
        midPanel.add(customTable, BorderLayout.CENTER);
        scroll = new CustomScrollPane(midPanel);
        // ========== PANEL CHI TIẾT KHÁCH HÀNG ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Khách Hàng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nhãn hiển thị thông tin khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên Khách Hàng: "), gbc);
        gbc.gridx = 1;
        JLabel customerLabel = new JLabel("Chọn Khách Hàng");
        botPanel.add(customerLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Khách Hàng: "), gbc);
        gbc.gridx = 1;
        JLabel customeridLabel = new JLabel("");
        botPanel.add(customeridLabel, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 2;
        botPanel.add(new JLabel("Số Điện Thoại: "), gbc);
        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        botPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 3;
        botPanel.add(new JLabel("Email: "), gbc);
        gbc.gridx = 1;
        JLabel emailLabel = new JLabel("");
        botPanel.add(emailLabel, gbc);

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
                
                String customerID = (String) customerTable.getValueAt(selectedRow, 0);
                String hoten = (String) customerTable.getValueAt(selectedRow, 1);
                String sdt = (String) customerTable.getValueAt(selectedRow, 2);
                String email = (String) customerTable.getValueAt(selectedRow, 3);

                // Hiển thị dữ liệu trên giao diện
                customerLabel.setText(hoten);
                customeridLabel.setText(customerID);
                phoneLabel.setText(sdt);
                emailLabel.setText(email);
                botPanel.add(buttonPanel, gbc);
            }   
        });
        
        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(scroll);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadCustomer();
        
    }

    private void loadCustomer() {
        List<CustomerDTO> customer = customerBUS.getAllCustomer();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (CustomerDTO ctm : customer) {
            tableModel.addRow(new Object[]{ctm.getcustomerID(), ctm.getFullName(), ctm.getPhone(), ctm.getEmail()});
        }
    }

}
