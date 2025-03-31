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
        String[] columnNames = {"Mã NV", "Họ Tên", "Địa Chỉ", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        employeeTable = customTable.getEmployeeTable(); 
        tableModel = customTable.getTableModel(); 
        
        midPanel.add(customTable, BorderLayout.CENTER);
        CustomScrollPane scrollPane = new CustomScrollPane(employeeTable);

        // ========== PANEL CHI TIẾT NHÂN VIÊN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Nhân Viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nhãn hiển thị thông tin nhân viên
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Tên Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeLabel = new JLabel("Chọn Nhân Viên");
        botPanel.add(employeeLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeidLabel = new JLabel("");
        botPanel.add(employeeidLabel, gbc);
        

        gbc.gridx = 0; 
        gbc.gridy = 3;
        botPanel.add(new JLabel("Địa Chỉ: "), gbc);
        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        botPanel.add(addressLabel, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 4;
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
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {

                String manv = (String) employeeTable.getValueAt(selectedRow, 0);
                String hoten = (String) employeeTable.getValueAt(selectedRow, 1);
                String diaChi = (String) employeeTable.getValueAt(selectedRow, 2);
                String sdt = (String) employeeTable.getValueAt(selectedRow, 3);

                // Hiển thị dữ liệu trên giao diện
                employeeLabel.setText(hoten);
//                employeeidLabel.setText(manv);
                addressLabel.setText(diaChi);
                phoneLabel.setText(sdt);
                botPanel.add(buttonPanel, gbc);
            }   
        });
        
        addButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng thêm nhân viên chưa được triển khai!");
            GUI_Form_Employee GFE = new GUI_Form_Employee(this,null);
            GFE.setVisible(true);
        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadEmployees();
        
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = employeeBUS.getAllEmployees();
        tableModel.setRowCount(0);
        //int index = 0;
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeID(), emp.getFullName(), emp.getAddress(), emp.getPhone()});
        }
    }
    
    
    

}
