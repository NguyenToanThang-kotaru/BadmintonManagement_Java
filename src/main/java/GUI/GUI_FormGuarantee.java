package GUI;

import DTO.GuaranteeDTO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import DAO.GuaranteeDAO;
import GUI.GUI_Guarantee;

public class GUI_FormGuarantee extends JDialog {

    private JTextField reasonField;
    private CustomCombobox statusBaohanh;
    private CustomButton saveButton;
    private GuaranteeDTO guarantee;
    private GUI_Guarantee parentGUI;

    public GUI_FormGuarantee(JFrame parent, GUI_Guarantee parentGUI, GuaranteeDTO guarantee) {
        super(parent, "Cập nhật bảo hành", true);
        this.parentGUI = parentGUI;
        this.guarantee = guarantee;
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
        add(new JLabel("Mã serial: "), gbc);
        gbc.gridx = 1;
        JLabel SerialID = new JLabel("");
        SerialID.setText(String.valueOf(guarantee.getSerialID()));
        add(SerialID, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Tình trạng bảo hành: "), gbc);
        gbc.gridx = 1;
        CustomCombobox statusBaohanh = new CustomCombobox(new String[]{"Bảo hành", "Chưa bảo hành","Đã bảo hành"});
        add(statusBaohanh, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        reasonField = new JTextField(20);
        reasonField.setText(String.valueOf(guarantee.getLydo()));
        add(reasonField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        saveButton = new CustomButton("Lưu");
        saveButton.addActionListener(e -> {
//            // Lấy dữ liệu từ form
//            String name = nameField.getText();
//            String price = priceField.getText();
//            String maNCC = maNCCField.getText();
//            String soluong = soluongField.getText();
//            String tskt = tsktField.getText();
//            String tenLoai = (String) TLField.getSelectedItem(); // Lấy tên loại từ combobox
//            String anh = anhField.getText();
//
//            // Cập nhật vào ProductDTO
//            product.setProductName(name);
//            product.setGia(price);
//            product.setMaNCC(maNCC);
//            product.setSoluong(soluong);
//            product.setTSKT(tskt);
//            product.setTL(tenLoai);
//            product.setAnh(anh);
//                
//            // Gọi updateProduct để cập nhật sản phẩm với mã loại tương ứng
//            ProductDAO.updateProduct(product);
//
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
//            parentGUI.loadGuaranteedata();
            dispose();
//             Đóng form sửa
        });

        add(saveButton, gbc);
    }
}
