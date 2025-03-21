package GUI;

import BUS.CustomerBUS;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI_Customer extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private CustomButton saveButton, editButton, addButton, deleteButton;
    private CustomerBUS customerBUS;

    public GUI_Customer() {
        customerBUS = new CustomerBUS();

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

        addButton = new CustomButton("+ Thêm Khách Hàng");
        topPanel.add(addButton, BorderLayout.EAST);

        // ====== MID PANEL (Customer Table) ======
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã KH", "Họ Tên", "Địa Chỉ", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        customerTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();

        midPanel.add(customTable, BorderLayout.CENTER);

        // ====== BOT PANEL (Customer Details) ======
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết khách hàng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        createEditableField("Mã khách hàng:", botPanel, gbc, 0);
        createEditableField("Họ và Tên:", botPanel, gbc, 1);
        createEditableField("Địa chỉ:", botPanel, gbc, 2);
        createEditableField("Số điện thoại:", botPanel, gbc, 3);

        // ====== BUTTONS PANEL ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        editButton = new CustomButton("✏️ Chỉnh sửa");
        saveButton = new CustomButton("💾 Lưu");
        deleteButton = new CustomButton("🗑️ Xóa");

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        botPanel.add(buttonPanel, gbc);

        // ====== ADDING PANELS TO MAIN INTERFACE ======
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadCustomer();
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

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                label.setText(textField.getText());
                textField.setVisible(false);
                label.setVisible(true);
            }
        });
    }

    private void loadCustomer() {
        List<CustomerDTO> customer = customerBUS.getAllCustomer();
        tableModel.setRowCount(0);
        for (CustomerDTO ctm : customer) {
            tableModel.addRow(new Object[]{ctm.getcustomerID(), ctm.getFullName(), ctm.getAddress(), ctm.getPhone()});
        }
    }
}
