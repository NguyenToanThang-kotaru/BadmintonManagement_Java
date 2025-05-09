package GUI;

import BUS.DetailOrderBUS;
import DTO.DetailOrderDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GUI_DetailOrder extends JFrame {
    private JTable detailTable;
    private DefaultTableModel tableModel;
    private DetailOrderBUS detailOrderBUS;
    private String orderID;
    private String totalMoney;
    private String issueDate;
    private String customerName;

    public GUI_DetailOrder(String orderID, String customerName, String totalMoney, String issueDate) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.totalMoney = totalMoney;
        this.issueDate = issueDate;
        detailOrderBUS = new DetailOrderBUS();

        setTitle("Chi Tiết Hóa Đơn");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Chi Tiết Hóa Đơn: " + orderID, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa thông tin hóa đơn + bảng chi tiết
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        // Panel thông tin hóa đơn
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Hóa Đơn"));
        
        JLabel customerLabel = new JLabel("Tên Khách Hàng:");
        JLabel customerValue = new JLabel(customerName);
        JLabel totalLabel = new JLabel("Tổng Tiền:");
        JLabel totalValue = new JLabel(totalMoney);
        JLabel dateLabel = new JLabel("Ngày Xuất:");
        JLabel dateValue = new JLabel(issueDate);

        customerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        customerValue.setFont(new Font("Arial", Font.PLAIN, 14));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalValue.setFont(new Font("Arial", Font.PLAIN, 14));
        dateValue.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(createInfoRow("Tên Khách Hàng:", customerName));
        infoPanel.add(createInfoRow("Tổng Tiền:", totalMoney));
        infoPanel.add(createInfoRow("Ngày Xuất:", issueDate));


        centerPanel.add(infoPanel, BorderLayout.NORTH);

        // Bảng chi tiết hóa đơn
        String[] columnNames = {"Mã CTHĐ", "Mã SP", "Mã Serial", "Giá", "Lợi Nhuận"};
        tableModel = new DefaultTableModel(columnNames, 0);
        detailTable = new JTable(tableModel);
        detailTable.setFont(new Font("Arial", Font.PLAIN, 14));
        detailTable.setRowHeight(25);
        detailTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        detailTable.getTableHeader().setBackground(new Color(30, 144, 255));
        detailTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(detailTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel chứa nút đóng
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(255, 69, 0));
        closeButton.setForeground(Color.WHITE);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setUndecorated(true);
        loadDetailOrder();
    }

    private void loadDetailOrder() {
        List<DetailOrderDTO> details = detailOrderBUS.getDetailOrderByOrderID(orderID);
        tableModel.setRowCount(0);
        for (DetailOrderDTO detail : details) {
            tableModel.addRow(new Object[]{
                detail.getdetailorderID(),
                detail.getproductID(),
                detail.getserialID(),
                detail.getprice(),
                detail.getprofit()
            });
        }
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.setBackground(Color.WHITE);

        JLabel rowLabel = new JLabel(label);
        rowLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel rowValue = new JLabel(value);
        rowValue.setFont(new Font("Arial", Font.PLAIN, 14));

        rowPanel.add(rowLabel);
        rowPanel.add(rowValue);

        return rowPanel;
    }
}
