package GUI;

import DTO.ProductDTO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import DAO.ProductDAO;
import GUI.GUI_Product;

public class GUI_FixFormProduct extends JDialog {

    private JTextField nameField, priceField, maNCCField, soluongField, tsktField, anhField;
    private CustomCombobox TLField;
    private CustomButton saveButton;
    private ProductDTO product;
    private GUI_Product parentGUI;

    public GUI_FixFormProduct(JFrame parent, GUI_Product parentGUI, ProductDTO product) {
        super(parent, "Sửa sản phẩm", true);
        this.parentGUI = parentGUI;
        this.product = product;
        setSize(400, 310);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(parent);

        getContentPane().setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Mã sản phẩm: "), gbc);
        gbc.gridx = 1;
        JLabel ProductID = new JLabel("");
        ProductID.setText(String.valueOf(product.getProductID()));
        add(ProductID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Tên sản phẩm: "), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setText(product.getProductName());
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Giá: "), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        priceField.setText(String.valueOf(product.getGia()));
        add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Mã NCC: "), gbc);
        gbc.gridx = 1;
        maNCCField = new JTextField(20);
        maNCCField.setText(String.valueOf(product.getMaNCC()));
        add(maNCCField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Số lượng: "), gbc);
        gbc.gridx = 1;
        soluongField = new JTextField(20);
        soluongField.setText(String.valueOf(product.getSoluong()));
        add(soluongField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Thông số kỹ thuật: "), gbc);
        gbc.gridx = 1;
        tsktField = new JTextField(20);
        tsktField.setText(product.getTSKT());
        add(tsktField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Tên loại: "), gbc);
        gbc.gridx = 1;
        ArrayList<String> categoryList = ProductDAO.getAllCategoryNames();
        String[] categoryNames = categoryList.toArray(new String[0]);

// Tạo JComboBox
        TLField = new CustomCombobox(categoryNames);
        TLField.setSelectedItem(product.getTL()); // Set loại hiện tại của sản phẩm
        add(TLField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Ảnh: "), gbc);
        gbc.gridx = 1;
        anhField = new JTextField(20);
        anhField.setText(product.getAnh());
        add(anhField, gbc);

        gbc.gridx = 2;
        JButton chooseImageButton = new JButton("Chọn ảnh");
        chooseImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Chọn ảnh sản phẩm");

            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getName(); // Chỉ lấy tên tệp
                anhField.setText(fileName); // Đặt tên ảnh vào ô nhập liệu
            }
        });

        add(chooseImageButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        saveButton = new CustomButton("Lưu");
        saveButton.addActionListener(e -> {
            // Lấy dữ liệu từ form
            String name = nameField.getText();
            String price = priceField.getText();
            String maNCC = maNCCField.getText();
            String soluong = soluongField.getText();
            String tskt = tsktField.getText();
            String tenLoai = (String) TLField.getSelectedItem(); // Lấy tên loại từ combobox
            String anh = anhField.getText(); // Lấy đường dẫn ảnh từ JLabel

            // Cập nhật vào ProductDTO
            product.setProductName(name);
            product.setGia(price);
            product.setMaNCC(maNCC);
            product.setSoluong(soluong);
            product.setTSKT(tskt);
            product.setTL(tenLoai);
            product.setAnh(anh);

            // Gọi updateProduct để cập nhật sản phẩm với mã loại tương ứng
            ProductDAO.updateProduct(product);

            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
            parentGUI.loadProductData();
            dispose();
            // Đóng form sửa
        });

        add(saveButton, gbc);
    }
}
