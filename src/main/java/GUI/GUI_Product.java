package GUI;

import BUS.ActionBUS;
import DAO.ProductDAO;
import DTO.ProductDTO;
import BUS.ProductBUS;
import DTO.AccountDTO;
import DTO.ActionDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableColumnModel;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class GUI_Product extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> roleComboBox;
    private CustomButton fixButton, saveButton, deleteButton, addButton, ShowSEButton, reloadButton;
    private CustomSearch searchField;
    private ProductDTO productChoosing;
    private static GUI_Product instance;
    
    public static GUI_Product getInstance() {
        return instance;
    }

    public GUI_Product(AccountDTO a) {
        instance = this;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========    
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Thanh tìm kiếm
        searchField = new CustomSearch(250, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        // Panel chứa các nút bên trái (Nhập Excel + Xuất Excel)
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtonPanel.setOpaque(false);

        CustomButton importExcelButton = new CustomButton("Nhập Excel");
        importExcelButton.setPreferredSize(new Dimension(120, 30));
        leftButtonPanel.add(importExcelButton);

        CustomButton exportExcelButton = new CustomButton("Xuất Excel");
        exportExcelButton.setPreferredSize(new Dimension(120, 30));
        leftButtonPanel.add(exportExcelButton);

        topPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Panel chứa các nút bên phải (Tải Lại Trang + Thêm sản phẩm)
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonPanel.setOpaque(false);

        reloadButton = new CustomButton("Tải Lại Trang");
        reloadButton.setPreferredSize(new Dimension(120, 30));
        rightButtonPanel.add(reloadButton);

        addButton = new CustomButton("+ Thêm sản phẩm");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(170, 30));
        rightButtonPanel.add(addButton);

        topPanel.add(rightButtonPanel, BorderLayout.EAST);
//        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Giá", "Số lượng"};
        CustomTable customTable = new CustomTable(columnNames);
        productTable = customTable.getEmployeeTable();
        tableModel = customTable.getTableModel();
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
        infoPanel.add(new JLabel("Tên NCC: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel NameNCC = new JLabel("");
        infoPanel.add(NameNCC, gbcInfo);

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

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 7;
        infoPanel.add(new JLabel("Giá nhập: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel priceInto = new JLabel("");
        infoPanel.add(priceInto, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 8;
        infoPanel.add(new JLabel("Khuyến mãi: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel sale = new JLabel("");
        infoPanel.add(sale, gbcInfo);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 9;
        infoPanel.add(new JLabel("Hiệu lực bảo hành: "), gbcInfo);
        gbcInfo.gridx = 1;
        JLabel HLBH = new JLabel("");
        infoPanel.add(HLBH, gbcInfo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton);

        ShowSEButton = new CustomButton("Xem danh sách serial");
        ShowSEButton.setCustomColor(new Color(0, 150, 130));
        buttonPanel.add(ShowSEButton);

        fixButton = new CustomButton("Sửa");
        fixButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(fixButton);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 10;
        gbcInfo.gridwidth = 2;
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;

        righPanel.add(infoPanel);
        botPanel.add(leftPanel, BorderLayout.WEST);
        botPanel.add(righPanel, BorderLayout.CENTER);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String productID = (String) productTable.getValueAt(selectedRow, 0);
                ProductDTO product = ProductBUS.getProduct(productID);
                productChoosing = product;

                // Kiểm tra null trước khi cập nhật
                if (product != null) {
                    productLabel.setText(product.getProductID());
                    namePDLabel.setText(product.getProductName());
                    priceLabel.setText(product.getGia());
                    quantityLabel.setText(product.getSoluong());
                    NameNCC.setText(product.gettenNCC());
                    TSKTLabel.setText(product.getTSKT());
                    TypeName.setText(product.getTL());
                    priceInto.setText(product.getgiaGoc());
                    sale.setText(product.getkhuyenMai());
                    HLBH.setText(product.getTGBH());

                    infoPanel.add(buttonPanel, gbcInfo);

                    // Cập nhật ảnh
//                    if (employeeImg != null && !employeeImg.isEmpty()) {
//                        String tempPath = "images/" + employeeImg;
//                        File imageFile = new File(tempPath);
//                        if (imageFile.exists()) {
//                            imagePath = tempPath;
//                        }
//                    }
//                    ImageIcon employeeIcon = new ImageIcon(imagePath);
//                    Image img = employeeIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
//                    imageLabel.setIcon(new ImageIcon(img));
                    String productImg = productChoosing.getAnh();
                    if (productImg != null && !productImg.isEmpty()) {
                        String imagePath = "images/" + productImg;
                        File imageFile = new File(imagePath);
                        System.out.println(imageFile);

                        if (imageFile.exists()) {
                            ImageIcon productIcon = new ImageIcon(imagePath);
                            Image img = productIcon.getImage().getScaledInstance(240, 220, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(img));
                            System.out.print("co ton tai");
                        } else {
                            imageLabel.setIcon(null);
                            System.out.println("khong ton tai");
                        }
                    } else {
                        imageLabel.setIcon(null);
                    }
                } else {
                    // Xử lý khi không tìm thấy sản phẩm
                    productLabel.setText("Không tìm thấy sản phẩm");
                    namePDLabel.setText("");
                    priceLabel.setText("");
                    quantityLabel.setText("");
                    NameNCC.setText("");
                    TSKTLabel.setText("");
                    TypeName.setText("");
                    priceInto.setText("");
                    sale.setText("");
                    HLBH.setText("");
                    imageLabel.setIcon(null);
                    infoPanel.remove(buttonPanel); // Ẩn nút nếu không có sản phẩm
                    infoPanel.revalidate();
                    infoPanel.repaint();
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
                ProductDTO product = ProductBUS.getProduct(productID);

                // Hiển thị form sửa sản phẩm
                Form_FixProduct fixForm = new Form_FixProduct((JFrame) SwingUtilities.getWindowAncestor(this), this, product);
                fixForm.setVisible(true);

            }
        });

        reloadButton.addActionListener(e -> {
            loadProductData();
            tableModel.fireTableDataChanged();
        });

        // Thêm sự kiện cho nút Nhập Excel
        importExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel để nhập");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".xlsx") || f.isDirectory();
                }
                public String getDescription() {
                    return "Excel Files (*.xlsx)";
                }
            });
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToImport = fileChooser.getSelectedFile();
                ProductBUS bus = new ProductBUS();
                boolean success = bus.importProductsFromExcel(fileToImport);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Nhập file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadProductData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi nhập file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Thêm sự kiện cho nút Xuất Excel
        exportExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachSanPham.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                ProductBUS bus = new ProductBUS();
                boolean success = bus.exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton.addActionListener(e -> {
            GUI_AddFormProduct addForm = new GUI_AddFormProduct((JFrame) SwingUtilities.getWindowAncestor(this), this);
            addForm.setVisible(true);
        });

        loadProductData();

        fixButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            System.out.println("aaaaaaaaa");
        });
//
        deleteButton.addActionListener(e -> {

            ProductBUS bus = new ProductBUS();
            if (!bus.canDeleteProduct(productChoosing)) {
                return; // Dừng xóa nếu không được phép
            }

            if (productChoosing != null && deleteProduct(productChoosing.getProductID(), productChoosing.getAnh())) {
                loadProductData();
                tableModel.fireTableDataChanged();
                productLabel.setText("Chọn sản phẩm");
                namePDLabel.setText("");
                priceLabel.setText("");
                quantityLabel.setText("");
                NameNCC.setText("");
                TSKTLabel.setText("");
                TypeName.setText("");
                priceInto.setText("");
                sale.setText("");
                HLBH.setText("");

                String productImg = productChoosing.getAnh();
                String imagePath = "images/noimage.png"; // Đường dẫn mặc định nếu không có ảnh sản phẩm

                if (productImg != null && !productImg.isEmpty()) {
                    String tempPath = "images/" + productImg;
                    File imageFile = new File(tempPath);
                    if (imageFile.exists()) {
                        imagePath = tempPath;
                    }
                }

                ImageIcon productIcon = new ImageIcon(imagePath);
                Image img = productIcon.getImage().getScaledInstance(240, 220, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));

                productChoosing = null;
            }

        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText();
            ArrayList<ProductDTO> ketQua = ProductBUS.searchProducts(keyword);
            capNhatBangSanPham(ketQua); // Hiển thị kết quả tìm được trên bảng
        });
        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(a, "Quản lý sản phẩm");

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
        fixButton.setVisible(canEdit);
        deleteButton.setVisible(canDelete);
        scrollPane.setVisible(canWatch);
        reloadButton.setVisible(false);
        importExcelButton.setVisible(canAdd);
        exportExcelButton.setVisible(canWatch);
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
    private Boolean deleteProduct(String productID, String productImg) {
        if (ProductBUS.deleteProduct(productID)) {
            // Nếu sản phẩm có ảnh, tiến hành xóa ảnh
            if (productImg != null && !productImg.isEmpty()) {
                String imagePath = "images/" + productImg;
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    if (imageFile.delete()) {
                        System.out.println("Đã xóa ảnh của sản phẩm.");
                    } else {
                        System.out.println("Không thể xóa ảnh của sản phẩm.");
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Xoá sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadProductData();
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    public void loadProductData() {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            List<ProductDTO> products = ProductBUS.getAllProducts();
            for (ProductDTO product : products) {
                model.addRow(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    product.getGia(),
                    product.getSoluong(),});
            }
        });
    }

    private void capNhatBangSanPham(List<ProductDTO> products) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (ProductDTO product : products) {
            tableModel.addRow(new Object[]{
                product.getProductID(),
                product.getProductName(),
                product.getGia(),
                product.getSoluong()
            });
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Quản Lý Sản Phẩm");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(800, 600);
//            frame.setLocationRelativeTo(null);
//            GUI_Product guiProduct = new GUI_Product();
//            frame.setContentPane(guiProduct);
//
//            frame.setVisible(true);
//        });
//    }
}
