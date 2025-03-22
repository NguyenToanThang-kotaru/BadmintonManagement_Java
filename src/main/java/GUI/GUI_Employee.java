package GUI;

import BUS.EmployeeBUS;
import DTO.EmployeeDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Employee extends JPanel {
    
    private JPanel topPanel, midPanel, botPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton;
    private CustomSearch searchField;
    private EmployeeBUS employeeBUS;   

    public GUI_Employee() {
        employeeBUS = new EmployeeBUS();

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

        addButton = new CustomButton("+ Thêm Nhân Viên"); // Nút thêm nhân viên
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH NHÂN VIÊN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã NV", "Họ Tên", "Địa Chỉ", "SĐT", "Mã Quyền"};
        CustomTable customTable = new CustomTable(columnNames);
        employeeTable = customTable.getEmployeeTable(); 
        tableModel = customTable.getTableModel(); 
        
        midPanel.add(customTable, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT NHÂN VIÊN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nhãn hiển thị thông tin tài khoản
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên nhân viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeLabel = new JLabel("Chọn nhân viên");
        botPanel.add(employeeLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã NV: "), gbc);
        gbc.gridx = 1;
        JLabel employeeidLabel = new JLabel("");
        botPanel.add(employeeidLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 2;
        botPanel.add(new JLabel("Địa Chỉ: "), gbc);
        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        botPanel.add(addressLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 3;
        botPanel.add(new JLabel("SĐT: "), gbc);
        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        botPanel.add(phoneLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 4;
        botPanel.add(new JLabel("Quyền: "), gbc);
        gbc.gridx = 1;
        JLabel quyenLabel = new JLabel("");
        botPanel.add(quyenLabel, gbc);

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
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                
                /*Object value = employeeTable.getValueAt(selectedRow, 0);
                String valueStr = String.valueOf(value);*/
                String manv = (String) employeeTable.getValueAt(selectedRow, 1);
                String hoTen = (String) employeeTable.getValueAt(selectedRow, 0);
                String diaChi = (String) employeeTable.getValueAt(selectedRow, 2);
                String sdt = (String) employeeTable.getValueAt(selectedRow, 3);
                String maQuyen = (String) employeeTable.getValueAt(selectedRow, 4);

                // Hiển thị dữ liệu trên giao diện
                employeeLabel.setText(hoTen);
                employeeidLabel.setText(manv);
                addressLabel.setText(diaChi);
                phoneLabel.setText(sdt);
                quyenLabel.setText(maQuyen);
                botPanel.add(buttonPanel, gbc);
            }   
        });
        
        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadEmployees();
        
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = employeeBUS.getAllEmployees();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{emp.getFullName(), emp.getEmployeeID(), emp.getAddress(), emp.getPhone(), emp.getAccountID()});
        }
    }

}
