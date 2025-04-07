package GUI;

import DAO.ProductDAO;
import DTO.ProductDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
public class GUI_Product extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable productTable;
    private CustomTable tableModel;
    private JComboBox<String> roleComboBox;
    private CustomButton fixButton, saveButton, deleteButton;
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
//        searchField.setPreferredSize(new Dimension(0, 30));d
        topPanel.add(searchField, BorderLayout.CENTER);

//        // Nút "Thêm tài khoản" (30%)
//        addButton = new CustomButton("+ Thêm sản phẩm");
//        addButton.setFont(new Font("Arial", Font.BOLD, 14));
//        addButton.setPreferredSize(new Dimension(170, 30));
//        topPanel.add(addButton, BorderLayout.EAST);
//        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Giá","Mã NCC", "Số lượng"};
        tableModel = new CustomTable(columnNames);
        productTable = tableModel.getAccountTable();
        TableColumnModel columnModel = productTable.getColumnModel();
//        columnModel.getColumn(1).setPreferredWidth(200); 
        columnModel.getColumn(1).setPreferredWidth(200);
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
        infoPanel.add(new JLabel("Mã NCC: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel MaNCC = new JLabel("");
        infoPanel.add(MaNCC, gbcInfo);

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
        JLabel TypeName = new JLabel("");
        infoPanel.add(TypeName, gbcInfo);

// Nút Lưu
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 7;
        gbcInfo.gridwidth = 2;
        fixButton = new CustomButton("Sửa");
        fixButton.setPreferredSize(new Dimension(80, 30));
        fixButton.setCustomColor(Color.RED);
//        infoPanel.add(fixButton, gbcInfo);

//        gbcInfo.gridx = 1;
//        gbcInfo.gridy = 7;
//        gbcInfo.gridwidth = 2;
//        saveButton = new CustomButton("Lưu");
//        saveButton.setPreferredSize(new Dimension(80, 30));
//        saveButton.setCustomColor(Color.RED);
////        infoPanel.add(deleteButton, gbcInfo);
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
                MaNCC.setText(String.valueOf(product.getMaNCC()));
                TSKTLabel.setText(product.getTSKT());
                TypeName.setText(String.valueOf(product.getTL()));

                infoPanel.add(fixButton, gbcInfo);
                // Cập nhật ảnh
                String productImg = product.getAnh();
                if (productImg != null && !productImg.isEmpty()) {
                    String imagePath = System.getProperty("user.dir") + "/images/" + productImg;
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        ImageIcon productIcon = new ImageIcon(new ImageIcon(imageFile.getAbsolutePath()).getImage()
                                .getScaledInstance(240, 220, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(productIcon);
                    } else {
                        imageLabel.setIcon(null); // Ẩn ảnh nếu không tìm thấy
                    }

                } else {
                    imageLabel.setIcon(null);
                }
            }
        });

        add(topPanel);
        add(midPanel);
        add(botPanel);
        fixButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String productID = (String) productTable.getValueAt(selectedRow, 0);
                ProductDTO product = ProductDAO.getProduct(productID);

                // Hiển thị form sửa sản phẩm
                GUI_FixFormProduct fixForm = new GUI_FixFormProduct((JFrame) SwingUtilities.getWindowAncestor(this), this, product);
                fixForm.setVisible(true);

            }
        });
        loadProductData();

        fixButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            System.out.println("Da xoa san pham");
        });
    }

//    private void showEditForm() {
//        JDialog fixForm = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa sản phẩm", true);
//        fixForm.setSize(400, 310);
//        fixForm.setLayout(new GridBagLayout());
//        fixForm.setLocationRelativeTo(this);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        fixForm.add(new JLabel("Tên sản phẩm: "), gbc);
//        gbc.gridx = 1;
//        JTextField NameField = new JTextField(15);
//        fixForm.add(NameField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        fixForm.add(new JLabel("Giá: "), gbc);
//        gbc.gridx = 1;
//        JTextField PriceField = new JTextField(15);
//        fixForm.add(PriceField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        fixForm.add(new JLabel("Mã NCC: "), gbc);
//        gbc.gridx = 1;
//        JTextField MaNCCField = new JTextField(15);
//        fixForm.add(MaNCCField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        fixForm.add(new JLabel("Số lượng: "), gbc);
//        gbc.gridx = 1;
//        JTextField SoluongField = new JTextField(15);
//        fixForm.add(SoluongField, gbc);
//        
//         gbc.gridx = 0;
//        gbc.gridy = 4;
//        fixForm.add(new JLabel("Thông số kĩ thuật: "), gbc);
//        gbc.gridx = 1;
//        JTextField TSKTField = new JTextField(15);
//        fixForm.add(TSKTField, gbc);
//        
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        fixForm.add(new JLabel("Ảnh: "), gbc);
//        gbc.gridx = 1;
//        JTextField TKTField = new JTextField(15);
//        fixForm.add(TKTField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 6;
//        gbc.gridwidth = 2;
//        saveButton = new CustomButton("Lưu");
//
//        saveButton.addActionListener(e -> {
////        int selectedRow = warrantyTable.getAccountTable().getSelectedRow();
////        if (selectedRow != -1) {
////            // Cập nhật giá trị trong bảng
////            warrantyTable.getAccountTable().setValueAt(serialField.getText(), selectedRow, 1);
////            warrantyTable.getAccountTable().setValueAt(statusComboBox.getSelectedItem(), selectedRow, 2);
////            textReasonLabel.setText(reasonField.getText());
////        }
//            fixForm.dispose();
//        });
//
//        fixForm.add(saveButton, gbc);
//
//        fixForm.setVisible(true);
//    }
    public void loadProductData() {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0); // Xóa toàn bộ dữ liệu cũ

        ArrayList<ProductDTO> products = ProductDAO.getAllProducts();
        for (ProductDTO product : products) {
            model.addRow(new Object[]{
                product.getProductID(),
                product.getProductName(),
                product.getGia(),
                product.getMaNCC(),
                product.getSoluong(),
                product.getTSKT(),
                product.getTL(),
                product.getAnh()
            });
        }

        model.fireTableDataChanged(); // Cập nhật lại bảng
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Sản Phẩm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            GUI_Product guiProduct = new GUI_Product();
            frame.setContentPane(guiProduct);

            frame.setVisible(true);
        });
    }
}
