package GUI;

import BUS.SuppliersBUS;
import Connection.DatabaseConnection;
import DAO.SuppliersDAO;
import DTO.SuppliersDTO;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GUI_Form_Suppliers extends JDialog {
    
    private JLabel lblSupplierID; // Thay JTextField bằng JLabel để đồng bộ với Edit
    private JTextField txtName, txtAddress, txtPhone;
    private CustomButton btnSave, btnCancel;
    private SuppliersBUS suppliersBUS;

    public GUI_Form_Suppliers(JPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Thêm Nhà Cung Cấp", true); // Đồng bộ tiêu đề
        
        suppliersBUS = new SuppliersBUS();

        setSize(500, 350); 
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Thêm Thông Tin Nhà Cung Cấp"); 
        title.setFont(new Font("Arial", Font.BOLD, 18)); 
        title.setForeground(new Color(52, 73, 94)); 
        add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        // Khởi tạo các trường nhập liệu
        lblSupplierID = new JLabel(generateNextSupplierID()); 
        txtName = new JTextField(20);
        txtAddress = new JTextField(20);
        txtPhone = new JTextField(15);

        addComponent("Mã Nhà Cung Cấp:", lblSupplierID, gbc); 
        addComponent("Tên Nhà Cung Cấp:", txtName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton("Lưu");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());
        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> {
            String supplierID = lblSupplierID.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SuppliersDTO supplier = new SuppliersDTO(supplierID, name, address, phone);
            saveSupplier(supplier);
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            dispose();
            ((GUI_Suppliers) parent).loadSuppliers(); // Cập nhật danh sách
        });
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbcNew) {
        gbcNew.gridx = 0;
        gbcNew.gridy++;
        gbcNew.anchor = GridBagConstraints.WEST;
        add(new JLabel(label), gbcNew);

        gbcNew.gridx = 1;
        add(component, gbcNew);
    }

    private String generateNextSupplierID() {
        SuppliersDAO dao = new SuppliersDAO();
        return dao.generateSupplierID();
    }

    private void saveSupplier(SuppliersDTO supplier) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO nha_cung_cap (ma_nha_cung_cap, ten_nha_cung_cap, dia_chi, so_dien_thoai, is_deleted) VALUES (?, ?, ?, ?, 0)")) {
            stmt.setString(1, supplier.getsuppliersID());
            stmt.setString(2, supplier.getfullname());
            stmt.setString(3, supplier.getaddress());
            stmt.setString(4, supplier.getphone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}