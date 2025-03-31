package GUI;

import DTO.EmployeeDTO;
import DAO.EmployeeDAO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class GUI_Form_Employee extends JDialog {

    private JTextField txtEmployeeName, txtAddress, txtPhone, txtImagePath;
    private JLabel title;
    private CustomButton btnSave, btnCancel, btnChooseImage;
    private JLabel lblImagePreview;

    public GUI_Form_Employee(JPanel parent, EmployeeDTO employee) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), employee == null ? "Thêm Nhân Viên" : "Sửa Nhân Viên", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Tiêu đề
        title = new JLabel(employee == null ? "THÊM NHÂN VIÊN" : "SỬA NHÂN VIÊN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Form nhập thông tin
        txtEmployeeName = new JTextField(20);
        txtAddress = new JTextField(20);
        txtPhone = new JTextField(20);
        txtImagePath = new JTextField(20);
        btnChooseImage = new CustomButton("Chọn Ảnh");
        lblImagePreview = new JLabel(); // Hiển thị ảnh

        // Nếu đang sửa nhân viên, điền thông tin vào form
        if (employee != null) {
            txtEmployeeName.setText(employee.getFullName());
            txtAddress.setText(employee.getAddress());
            txtPhone.setText(employee.getPhone());
            txtImagePath.setText(employee.getImage());

            // Hiển thị ảnh cũ
            displayImage(employee.getImage());
        }

        addComponent("Tên Nhân Viên:", txtEmployeeName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

        // Thêm phần chọn ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel imagePanel = new JPanel(new FlowLayout());
        imagePanel.add(txtImagePath);
        imagePanel.add(btnChooseImage);
        add(imagePanel, gbc);

        // Thêm ảnh preview
        gbc.gridy++;
        add(lblImagePreview, gbc);

        // Nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton(employee == null ? "Thêm" : "Cập Nhật");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridy++;
        add(buttonPanel, gbc);

        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());

        // Sự kiện nút Chọn Ảnh
        btnChooseImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn Ảnh Nhân Viên");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                txtImagePath.setText(selectedFilePath); // Hiển thị đường dẫn ảnh
                displayImage(selectedFilePath); // Hiển thị ảnh preview
            }
        });

        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> {
            String name = txtEmployeeName.getText().trim();
            String address = txtAddress.getText().trim();
            String phone = txtPhone.getText().trim();
            String imagePath = txtImagePath.getText().trim();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (employee == null) { // Thêm nhân viên mới
                EmployeeDTO newEmployee = new EmployeeDTO(null, name, address, phone, imagePath);
                EmployeeDAO.addEmployee(newEmployee);
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else { // Cập nhật nhân viên
                employee.setFullName(name);
                employee.setAddress(address);
                employee.setPhone(phone);
                employee.setImage(imagePath);
                EmployeeDAO.updateEmployee(employee);
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();
        });
    }

    // Phương thức để hiển thị ảnh preview
    private void displayImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            lblImagePreview.setIcon(null);
            return;
        }

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
            lblImagePreview.setIcon(imageIcon);
        } else {
            lblImagePreview.setIcon(null);
        }
    }

    // Thêm component vào form
    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }
}
