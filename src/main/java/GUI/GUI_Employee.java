package GUI;

import BUS.EmployeeBUS;
import DTO.EmployeeDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI_Employee extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private CustomButton saveButton, addButton;
    private EmployeeBUS employeeBUS;

    public GUI_Employee() {
        employeeBUS = new EmployeeBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ====== TOP PANEL (Search + Add Button) ======
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new JTextField(20);
        topPanel.add(searchField, BorderLayout.CENTER);

        addButton = new CustomButton("+ Thêm nhân viên");
        topPanel.add(addButton, BorderLayout.EAST);

        // ====== MID PANEL (Employee Table) ======
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã NV", "Họ Tên", "Địa Chỉ", "SĐT", "Ngày Vào Làm"};
        CustomTable customTable = new CustomTable(columnNames);
        employeeTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();

        midPanel.add(customTable, BorderLayout.CENTER);

        // ====== BOT PANEL (Employee Details) ======
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        createEditableField("Mã nhân viên:", botPanel, gbc, 0);
        createEditableField("Họ và Tên:", botPanel, gbc, 1);
        createEditableField("Địa chỉ:", botPanel, gbc, 2);
        createEditableField("Số điện thoại:", botPanel, gbc, 3);
        createEditableField("Ngày vào làm:", botPanel, gbc, 4);

        gbc.gridx = 1;
        gbc.gridy = 5;
        saveButton = new CustomButton("💾 Lưu");
        botPanel.add(saveButton, gbc);

        // ====== ADDING PANELS TO MAIN INTERFACE ======
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadEmployees();
    }

    private void createEditableField(String labelText, JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JLabel label = new JLabel("");
        JTextField textField = new JTextField(15);
        textField.setVisible(false);
        panel.add(label, gbc);
        panel.add(textField, gbc);

        gbc.gridx = 2;
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/icon_pencil.png"));
        CustomButton editButton = new CustomButton("");
        Image scaledImage = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        editButton.setIcon(new ImageIcon(scaledImage));
        

        panel.add(editButton, gbc);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(label.getText());
                label.setVisible(false);
                textField.setVisible(true);
                textField.requestFocus();
            }
        });

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                label.setText(textField.getText());
                textField.setVisible(false);
                label.setVisible(true);
            }
        });
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = employeeBUS.getAllEmployees();
        tableModel.setRowCount(0);
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeID(), emp.getFullName(), emp.getAddress(), emp.getPhone(), emp.getStartDate()});
        }
    }
}
