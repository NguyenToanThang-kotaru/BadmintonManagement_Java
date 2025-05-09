package GUI;

import BUS.ActionBUS;
import BUS.OrderBUS;
import DTO.OrderDTO;
import DTO.AccountDTO;
import BUS.DetailOrderBUS;
import DTO.ActionDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI_Order extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private CustomButton deleteButton, addButton, detailorderButton, excelButton;
    private CustomSearch searchField;
    private OrderBUS orderBUS = new OrderBUS();
    private OrderDTO order = new OrderDTO();
    private GUI_Product product;

    public GUI_Order(AccountDTO cn, GUI_Product product) {
        orderBUS = new OrderBUS();
        this.product = product;

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

        excelButton = new CustomButton("Xuất Excel");
        topPanel.add(excelButton, BorderLayout.WEST);

        addButton = new CustomButton("+ Thêm Hóa Đơn"); // Nút thêm hóa đơn
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH HÓA ĐƠN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã HĐ", "Mã NV", "Mã KH", "Tổng Tiền", "Ngày Xuất", "Tổng Lợi Nhuận", "Trạng Thái"};
        CustomTable customTable = new CustomTable(columnNames);
        orderTable = customTable.getOrderTable();
        tableModel = customTable.getTableModel();

        CustomScrollPane scrollPane = new CustomScrollPane(orderTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT HÓA ĐƠN ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Hóa Đơn"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nhãn hiển thị thông tin hóa đơn
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Hóa Đơn: "), gbc);
        gbc.gridx = 1;
        JLabel orderidLabel = new JLabel("Chọn Hóa Đơn");
        botPanel.add(orderidLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Mã Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeidLabel = new JLabel("");
        botPanel.add(employeeidLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Mã Khách Hàng: "), gbc);
        gbc.gridx = 1;
        JLabel customeridLabel = new JLabel("");
        botPanel.add(customeridLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Tồng Tiền: "), gbc);
        gbc.gridx = 1;
        JLabel totalmoneyLabel = new JLabel("");
        botPanel.add(totalmoneyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        botPanel.add(new JLabel("Ngày Xuất: "), gbc);
        gbc.gridx = 1;
        JLabel issuedateLabel = new JLabel("");
        botPanel.add(issuedateLabel, gbc);

        // ========== PANEL BUTTON ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton);

        detailorderButton = new CustomButton("Xem Chi Tiết Hóa Đơn");
        detailorderButton.setCustomColor(new Color(0, 120, 215));
        buttonPanel.add(detailorderButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {

                String mahd = (String) orderTable.getValueAt(selectedRow, 0);
                String manv = (String) orderTable.getValueAt(selectedRow, 1);
                String makh = (String) orderTable.getValueAt(selectedRow, 2);
                String tongtien = (String) orderTable.getValueAt(selectedRow, 3);
                String ngayxuat = (String) orderTable.getValueAt(selectedRow, 4);

                orderBUS.getOrder(mahd); //; Lấy mã hóa đơn để tham chiếu 

                // Hiển thị dữ liệu trên giao diện
                orderidLabel.setText(mahd);
                employeeidLabel.setText(manv);
                customeridLabel.setText(makh);
                totalmoneyLabel.setText(tongtien);
                issuedateLabel.setText(ngayxuat);
                botPanel.add(buttonPanel, gbc);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                String orderID = (String) orderTable.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn xóa hóa đơn " + orderID + " không?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // 1. Xóa hóa đơn
                    boolean success = orderBUS.deleteOrder(orderID);

                    // 2. Xóa chi tiết hóa đơn
                    DetailOrderBUS detailBUS = new DetailOrderBUS();
                    detailBUS.deleteByOrderID(orderID);
                    orderBUS.rollbackCanceledOrder(orderID);

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn và chi tiết!");
                        product.loadProductData();
                        loadOrder();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        excelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachHoaDon.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                boolean success = orderBUS.exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addButton.addActionListener(e -> {
            Form_Order GFO = new Form_Order(this, null, cn, product);
            GFO.setVisible(true);
        });

        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);

        loadOrder();

        detailorderButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                String orderID = (String) orderTable.getValueAt(selectedRow, 0);
                String customerID = (String) orderTable.getValueAt(selectedRow, 2);
                String totalMoney = (String) orderTable.getValueAt(selectedRow, 3);
                String issueDate = (String) orderTable.getValueAt(selectedRow, 4);

                // Lấy tên khách hàng từ mã khách hàng
                String customerName = orderBUS.getCustomerNameByID(customerID);

                GUI_DetailOrder GDO = new GUI_DetailOrder(orderID, customerName, totalMoney, issueDate);
                GDO.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        excelButton.addActionListener(e -> {

        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                List<OrderDTO> searchResult = orderBUS.searchOrder(keyword);
                updateTable(searchResult);
            } else {
                loadOrder(); // Nếu từ khóa rỗng, tải lại toàn bộ danh sách
            }
        });

        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(cn, "Quản lý đơn hàng");

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
        deleteButton.setVisible(canDelete);
        midPanel.setVisible(canWatch);
    }

    // Thêm phương thức updateTable để cập nhật bảng với kết quả tìm kiếm
    private void updateTable(List<OrderDTO> orders) {
        tableModel.setRowCount(0);
        for (OrderDTO odr : orders) {
            String statusText = odr.getis_deleted()
                    ? "<html><font color='red'>Đã hủy</font></html>"
                    : "<html><font color='green'>Đã hoàn thành</font></html>";
            tableModel.addRow(new Object[]{
                odr.getorderID(),
                odr.getemployeeID(),
                odr.getcustomerID(),
                odr.gettotalmoney(),
                odr.getissuedate(),
                odr.gettotalprofit(),
                statusText
            });
        }
    }

    public void loadOrder() {
        List<OrderDTO> order = orderBUS.getAllOrder();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (OrderDTO odr : order) {
            String statusText = odr.getis_deleted()
                    ? "<html><font color='red'>Đã hủy</font></html>"
                    : "<html><font color='green'>Đã hoàn thành</font></html>";
            tableModel.addRow(new Object[]{
                odr.getorderID(),
                odr.getemployeeID(),
                odr.getcustomerID(),
                odr.gettotalmoney(),
                odr.getissuedate(),
                odr.gettotalprofit(),
                statusText
            });
        }
    }
}
