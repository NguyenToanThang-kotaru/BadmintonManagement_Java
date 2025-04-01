package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI_FormSuppliers extends JDialog {
    
    private final JLabel lblSupplierID;
    private JTextField txtName, txtAddress, txtPhone;
    private CustomButton btnSave, btnCancel;

    public GUI_FormSuppliers(JPanel parent) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Nhà Cung Cấp", true);

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

        JLabel title = new JLabel("Thông Tin Nhà Cung Cấp");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        txtName = new JTextField(20);
        txtAddress = new JTextField(20);
        txtPhone = new JTextField(15);
        lblSupplierID = new JLabel(generateNextSupplierID());

        addComponent("Mã Nhà Cung Cấp:", lblSupplierID, gbc);
        addComponent("Tên Nhà Cung Cấp:", txtName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton("Thêm");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        btnCancel.addActionListener(e -> dispose());
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
        return "NCC009";
    }
}