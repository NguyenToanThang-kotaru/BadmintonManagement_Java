package GUI;

import BUS.EmployeeBUS;
import DTO.EmployeeDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Employee extends JPanel {
    private JPanel midPanel, topPanel, botPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private CustomButton deleteButton, addButton, editButton;
    private CustomSearch searchField;
    private EmployeeBUS employeeBUS;
    private JPanel buttonPanel;

    public GUI_Employee() {
        employeeBUS = new EmployeeBUS();
        
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

        addButton = new CustomButton("+ Thêm nhân viên");
        topPanel.add(addButton, BorderLayout.EAST);
        
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã NV", "Tên nhân viên", "Địa chỉ", "Số điện thoại"};
        CustomTable customTable = new CustomTable(columnNames);
        employeeTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();

        midPanel.add(customTable, BorderLayout.CENTER);

        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã nhân viên: "), gbc);
        gbc.gridx = 1;
        JLabel idLabel = new JLabel("");
        botPanel.add(idLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Tên nhân viên: "), gbc);
        gbc.gridx = 1;
        JLabel nameLabel = new JLabel("");
        botPanel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Địa chỉ: "), gbc);
        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        botPanel.add(addressLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Số điện thoại: "), gbc);
        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        botPanel.add(phoneLabel, gbc);

        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xoá");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.EAST);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                Object maNhanVienObj = employeeTable.getValueAt(selectedRow, 0);
                Object tenNhanVienObj = employeeTable.getValueAt(selectedRow, 1);
                Object diaChiObj = employeeTable.getValueAt(selectedRow, 2);
                Object soDienThoaiObj = employeeTable.getValueAt(selectedRow, 3);
                
                String maNhanVien = maNhanVienObj != null ? maNhanVienObj.toString() : "";
                String tenNhanVien = tenNhanVienObj != null ? tenNhanVienObj.toString() : "";
                String diaChi = diaChiObj != null ? diaChiObj.toString() : "";
                String soDienThoai = soDienThoaiObj != null ? soDienThoaiObj.toString() : "";

                idLabel.setText(maNhanVien);
                nameLabel.setText(tenNhanVien);
                addressLabel.setText(diaChi);
                phoneLabel.setText(soDienThoai);
                
                botPanel.remove(buttonPanel);
                gbc.gridx = 1;
                gbc.gridy = 4;
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
        
        loadEmployees();
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = employeeBUS.getAllEmployees();
        tableModel.setRowCount(0);
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeID(), emp.getFullName(), emp.getAddress(), emp.getPhone()});
        }
    }
}
