package GUI;

import DAO.ProductDAO;
import DTO.ProductDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableColumnModel;
import java.util.List;

public class GUI_Product extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable productTable;
    private CustomTable tableModel;
    private JComboBox<String> roleComboBox;
    private CustomButton saveButton, addButton;
    private CustomSearch searchField;

    public GUI_Product() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding trên-dưới 10px
        topPanel.setBackground(Color.WHITE);
        // Thanh tìm kiếm (70%)
        searchField = new CustomSearch(250, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
//        searchField.setPreferredSize(new Dimension(0, 30));
        topPanel.add(searchField, BorderLayout.CENTER);

        // Nút "Thêm tài khoản" (30%)
        addButton = new CustomButton("+ Thêm sản phẩm");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(170, 30));
        topPanel.add(addButton, BorderLayout.EAST);

//        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Giá", "Số lượng", "Mã Thương Hiệu", "Thông Số Kĩ Thuật", "Mã Loại"};
        tableModel = new CustomTable(columnNames);
        productTable = tableModel.getAccountTable();
        TableColumnModel columnModel = productTable.getColumnModel();
//        columnModel.getColumn(1).setPreferredWidth(200); 
        columnModel.getColumn(2).setPreferredWidth(15);
//        columnModel.getColumn(3).setPreferredWidth(15); 
//        columnModel.getColumn(4).setPreferredWidth(100); 

        // ScrollPane để bảng có thanh cuộn
        CustomScrollPane scrollPane = new CustomScrollPane(productTable);

        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT TÀI KHOẢN ==========  s
        botPanel = new JPanel(new BorderLayout(20, 0)); // Khoảng cách giữa 2 phần
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

        JPanel leftPanel = new JPanel(null);

        leftPanel.setPreferredSize(new Dimension(310, 290));
        leftPanel.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(30, 10, 230, 220);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        leftPanel.add(imageLabel);

// Phần phải
        JPanel righPanel = new JPanel();
        righPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        righPanel.setBackground(Color.WHITE);
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 5, 5, 5);
        gbcInfo.anchor = GridBagConstraints.WEST;

// Thêm vào infoPanel
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 0;
        infoPanel.add(new JLabel("Mã sản phẩm: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel productLabel = new JLabel("Chọn sản phẩm");
        infoPanel.add(productLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;
        infoPanel.add(new JLabel("Tên sản phẩm: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel namePDLabel = new JLabel("");
        namePDLabel.setPreferredSize(new Dimension(200, 20));
        infoPanel.add(namePDLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 2;
        infoPanel.add(new JLabel("Giá: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel priceLabel = new JLabel("");
        infoPanel.add(priceLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 3;
        infoPanel.add(new JLabel("Số lượng: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel quantityLabel = new JLabel("");
        infoPanel.add(quantityLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 4;
        infoPanel.add(new JLabel("Mã thương hiệu: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel MaThHieuLabel = new JLabel("");
        infoPanel.add(MaThHieuLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 5;
        infoPanel.add(new JLabel("Thông số kỹ thuật: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel TSKTLabel = new JLabel("");
        infoPanel.add(TSKTLabel, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 6;
        infoPanel.add(new JLabel("Tên loại: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel TypeidLabel = new JLabel("");
        infoPanel.add(TypeidLabel, gbcInfo);

// Nút Lưu
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 7;
        gbcInfo.gridwidth = 2;
        saveButton = new CustomButton("💾 Lưu");
        saveButton.setPreferredSize(new Dimension(80, 30));
        infoPanel.add(saveButton, gbcInfo);

// Thêm infoPanel vào righPanel
        righPanel.add(infoPanel);

// Thêm leftPanel và righPanel vào botPanel
        botPanel.add(leftPanel, BorderLayout.WEST);
        botPanel.add(righPanel, BorderLayout.CENTER);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String productID = (String) productTable.getValueAt(selectedRow, 0);
                ProductDTO product = ProductDAO.getProduct(productID);

                // Cập nhật thông tin sản phẩm
                productLabel.setText(String.valueOf(product.getProductID()));
                namePDLabel.setText(product.getProductName());
                priceLabel.setText(String.valueOf(product.getGia()));
                quantityLabel.setText(String.valueOf(product.getSoluong()));
                MaThHieuLabel.setText(String.valueOf(product.getMaThuongHieu()));
                TSKTLabel.setText(product.getTSKT());
                TypeidLabel.setText(product.getML());

                // Cập nhật ảnh
                String productImg = product.getAnh();
                if (productImg != null && !productImg.isEmpty()) {
                    String imagePath = "/images/" + productImg;
                    java.net.URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl != null) {
                        ImageIcon productIcon = new ImageIcon(imageUrl);
                        Image img = productIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                    } else {
                        imageLabel.setIcon(null);
                    }
                } else {
                    imageLabel.setIcon(null);
                }
            }
        });

        add(topPanel);
        add(midPanel);
        add(botPanel);
        loadProductData();
    }

    private void loadProductData() {
        List<ProductDTO> productList = ProductDAO.getAllProducts();
        for (ProductDTO product : productList) {
            tableModel.addRow(new Object[]{
                product.getProductID(), product.getProductName(), product.getGia(),
                product.getSoluong(), product.getMaThuongHieu(), product.getTSKT(), product.getML()
            });
        }
    }
}
