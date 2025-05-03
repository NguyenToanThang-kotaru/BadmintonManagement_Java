package GUI;

import BUS.ActionBUS;
import BUS.CustomerBUS;
import DTO.AccountDTO;
import DTO.ActionDTO;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI_Customer extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton;
    private CustomSearch searchField;
    private CustomScrollPane scroll;
    private CustomerBUS customerBUS;
    private CustomerDTO customerChoosing;

    public GUI_Customer(AccountDTO a) {
        customerBUS = new CustomerBUS();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút thêm) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        searchField = new CustomSearch(275, 20); // Ô nhập tìm kiếm
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        CustomButton exportExcelButton = new CustomButton("Xuất Excel");
        exportExcelButton.setPreferredSize(new Dimension(120, 30));
        topPanel.add(exportExcelButton, BorderLayout.WEST);

        addButton = new CustomButton("+ Thêm Khách Hàng"); // Nút thêm khách hàng 
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH KHÁCH HÀNG ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = { "STT", "Mã KH", "Họ Tên", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        customerTable = customTable.getCustomerTable();
        tableModel = customTable.getTableModel();

        scroll = new CustomScrollPane(customerTable);
        midPanel.add(scroll, BorderLayout.CENTER);

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
                String customerID = (String) customerTable.getValueAt(selectedRow, 1);
                String hoten = (String) customerTable.getValueAt(selectedRow, 2);
                String sdt = (String) customerTable.getValueAt(selectedRow, 3);
                customerChoosing = new CustomerDTO(customerID, hoten, sdt);
                // Hiển thị dữ liệu trên giao diện
                customerLabel.setText(hoten);
                customeridLabel.setText(customerID);
                phoneLabel.setText(sdt);
                botPanel.add(buttonPanel, gbc);
            }
        });
        
        exportExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachKhachHang.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);
        
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                boolean success = customerBUS.exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadCustomer();

        addButton.addActionListener(e -> {
//            
            Form_Customer GFC = new Form_Customer(this, null);
            GFC.setVisible(true);
        });

        editButton.addActionListener(e -> {
            Form_Customer GFC = new Form_Customer(this, customerChoosing);
            GFC.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();

            String customerID = (String) customerTable.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (customerBUS.deleteCustomer(customerID)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadCustomer();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại, vui lòng thử lại!");
                }
                System.out.println("Da xo customer");
            }
        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                searchCustomer(keyword);
            } else {
                loadCustomer(); // Nếu ô tìm kiếm trống, load lại toàn bộ khách hàng
            }
        });
        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(a, "Quản lý khách hàng");

        boolean canAdd = false, canEdit = false, canDelete = false, canWatch = false;

        if (actions != null) {
            for (ActionDTO action : actions) {
                switch (action.getName()) {
                    case "Thêm" ->
                        canAdd = true;
                    case "Sửa" ->
                        canEdit = true;
                    case "Xóa" ->
                        canDelete = true;
                    case "Xem" ->
                        canWatch = true;
                }
            }
        }

        addButton.setVisible(canAdd);
        editButton.setVisible(canEdit);
        deleteButton.setVisible(canDelete);
        scroll.setVisible(canWatch);
    }

    private void loadCustomer() {
        List<CustomerDTO> customer = customerBUS.getAllCustomer();
        tableModel.setRowCount(0);
        int index = 1;
        for (CustomerDTO ctm : customer) {
            tableModel.addRow(new Object[]{index++, ctm.getcustomerID(), ctm.getFullName(), ctm.getPhone()});
        }
    }

    private void searchCustomer(String keyword) {
        List<CustomerDTO> customers = customerBUS.searchCustomer(keyword);
        tableModel.setRowCount(0);
        for (CustomerDTO ctm : customers) {
            tableModel.addRow(new Object[]{ctm.getcustomerID(), ctm.getFullName(), ctm.getPhone()});
        }
    }
}
