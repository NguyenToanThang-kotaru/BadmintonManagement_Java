package GUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

//LAYOUT BẢNG DANH SÁCH TẤT CẢ SẢN PHẨM CỦA FORM IMPORT
//SỬ DỤNG ĐỂ HIỂN THỊ DANH SÁCH TẤT CẢ SẢN PHẨM TRONG CSDL ĐỂ NGƯỜI DÙNG CHỌN SẢN PHẨM NHẬP

public class AllProductsPanel extends JPanel {
    private JTable allProductsTable;
    private DefaultTableModel productTableModel;

    public AllProductsPanel() {
        setLayout(new BorderLayout());
        setBorder(new CompoundBorder(new TitledBorder("Danh sách sản phẩm"), new EmptyBorder(5, 5, 5, 5)));
        setBackground(Color.WHITE);

        String[] columns = {"Mã SP", "Tên SP", "Giá nhập"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        allProductsTable = new JTable(productTableModel);
        allProductsTable.setRowHeight(30);
        allProductsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = allProductsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(350);
        columnModel.getColumn(2).setPreferredWidth(150);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(allProductsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getAllProductsTable() {
        return allProductsTable;
    }

    public DefaultTableModel getProductTableModel() {
        return productTableModel;
    }
}