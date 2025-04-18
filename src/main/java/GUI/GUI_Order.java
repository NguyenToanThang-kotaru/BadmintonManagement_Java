package GUI;

import BUS.OrderBUS;
import DTO.OrderDTO;
import DAO.OrderDAO;
import DTO.AccountDTO;
import BUS.DetailOrderBUS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_Order extends JPanel {
    
    private JPanel topPanel, midPanel, botPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton, detailorderButton;
    private CustomSearch searchField;
    private OrderBUS orderBUS;
    private OrderDTO order;

    public GUI_Order(AccountDTO cn, List<String> t) {
        orderBUS = new OrderBUS();

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

        addButton = new CustomButton("+ Thêm Hóa Đơn"); // Nút thêm hóa đơn
        topPanel.add(addButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH HÓA ĐƠN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"Mã HĐ", "Mã NV", "Mã KH", "Tổng Tiền", "Ngày Xuất"};
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
        if(t.contains("xoa_hd")){
        buttonPanel.add(deleteButton, BorderLayout.WEST);}

        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        if(t.contains("xoa_hd")){
        buttonPanel.add(editButton, BorderLayout.CENTER );}
        
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
                
                order = OrderDAO.getOrder(mahd); //; Lấy mã hóa đơn để tham chiếu 
                
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

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn và chi tiết!");
                        loadOrder();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        addButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng thêm nhân viên chưa được triển khai!");
            Form_Order GFO = new Form_Order(this, null, cn);
            GFO.setVisible(true);
        });
        
        editButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng thêm nhân viên chưa được triển khai!");
            Form_Order GFO = new Form_Order(this, order, cn);
            GFO.setVisible(true);
        });
        
        // Thêm các panel vào giao diện chính
        add(topPanel);
        add(Box.createVerticalStrut(10));
        add(midPanel);
        add(Box.createVerticalStrut(10));
        add(botPanel);
        
        if(t.contains("xoa_hd")){
        loadOrder();}
        
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
        
        searchField.setSearchListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                List<OrderDTO> searchResult = orderBUS.searchOrder(keyword);
                updateTable(searchResult);
            } else {
                loadOrder(); // Nếu từ khóa rỗng, tải lại toàn bộ danh sách
            }
        });
    }
    
    // Thêm phương thức updateTable để cập nhật bảng với kết quả tìm kiếm
    private void updateTable(List<OrderDTO> orders) {
        tableModel.setRowCount(0);
        for (OrderDTO odr : orders) {
            tableModel.addRow(new Object[]{
                odr.getorderID(),
                odr.getemployeeID(),
                odr.getcustomerID(),
                odr.gettotalmoney(),
                odr.getissuedate()
            });
        }
    }
    
    public void loadOrder() {
        List<OrderDTO> order = orderBUS.getAllOrder();
        tableModel.setRowCount(0);
        //int index = 0;
        String no = "";
        for (OrderDTO odr : order ) {
            tableModel.addRow(new Object[]{odr.getorderID(), odr.getemployeeID(), odr.getcustomerID(), odr.gettotalmoney(), odr.getissuedate()});
        }
    }
}
