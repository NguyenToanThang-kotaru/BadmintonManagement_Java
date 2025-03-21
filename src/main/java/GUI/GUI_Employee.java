package GUI;

import BUS.EmployeeBUS;
import DTO.EmployeeDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GUI_Employee extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private CustomButton saveButton, addButton;
    private EmployeeBUS employeeBUS;
    private JTextField[] textFields;
    private JLabel[] labels;
    private String[] fieldNames = {"Mã NV", "Họ Tên", "Địa Chỉ", "SĐT", "Mã Quyền"};

    public GUI_Employee() {
        employeeBUS = new EmployeeBUS();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ====== TOP PANEL ======
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        addButton = new CustomButton("+ Thêm nhân viên");
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        // ====== MID PANEL ======
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        CustomTable customTable = new CustomTable(fieldNames);
        employeeTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();
        midPanel.add(customTable, BorderLayout.CENTER);

        // ====== BOT PANEL ======
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        textFields = new JTextField[fieldNames.length];
        labels = new JLabel[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            createEditableField(fieldNames[i] + ":", botPanel, gbc, i, i == 0);
        }
        
        gbc.gridx = 1;
        gbc.gridy = fieldNames.length;
        saveButton = new CustomButton("💾 Lưu");
        botPanel.add(saveButton, gbc);

        // ====== ADD TO PANEL ======
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadEmployees();
        addEventListeners();
    }

    private void createEditableField(String labelText, JPanel panel, GridBagConstraints gbc, int row, boolean isReadOnly) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        labels[row] = new JLabel("");
        textFields[row] = new JTextField(15);
        textFields[row].setVisible(false);
        textFields[row].setEditable(!isReadOnly);
        panel.add(labels[row], gbc);
        panel.add(textFields[row], gbc);

        if (!isReadOnly) {
            gbc.gridx = 2;
            CustomButton editButton = new CustomButton("✎");
            panel.add(editButton, gbc);
            
            int index = row;
            editButton.addActionListener(e -> {
                textFields[index].setText(labels[index].getText());
                labels[index].setVisible(false);
                textFields[index].setVisible(true);
                textFields[index].requestFocus();
            });
            
            textFields[row].addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    labels[index].setText(textFields[index].getText());
                    textFields[index].setVisible(false);
                    labels[index].setVisible(true);
                }
            });
        }
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = employeeBUS.getAllEmployees();
        tableModel.setRowCount(0);
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeID(), emp.getFullName(), emp.getAddress(), emp.getPhone()});
        }
    }

    private void addEventListeners() {
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                int row = employeeTable.getSelectedRow();
                for (int i = 0; i < fieldNames.length; i++) {
                    Object value = tableModel.getValueAt(row, i);
                    labels[i].setText(value != null ? value.toString() : "");

                }
            }
        });
        
        saveButton.addActionListener(e -> {
    int row = employeeTable.getSelectedRow();
    if (row != -1) {
        try {
            int employeeID = Integer.parseInt(labels[0].getText().trim()); // Mã nhân viên
            String fullName = labels[1].getText().trim();
            String address = labels[2].getText().trim();
            String phone = labels[3].getText().trim();
            String startDate = labels[4].getText().trim();

            EmployeeDTO updatedEmployee = new EmployeeDTO(employeeID, fullName, address, phone, 0);
            boolean success = employeeBUS.updateEmployee(updatedEmployee);

            if (success) {
                for (int i = 1; i < fieldNames.length; i++) {
                    tableModel.setValueAt(labels[i].getText(), row, i);
                }
                JOptionPane.showMessageDialog(this, "✔ Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không cập nhật được! Hãy kiểm tra ID nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ ID nhân viên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
});

        
    }
}
