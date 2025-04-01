package GUI;

import DTO.EmployeeDTO;
import DAO.EmployeeDAO;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GUI_Form_Employee extends JDialog {

    private JTextField txtEmployeeName, txtAddress, txtPhone;
    private JLabel title;
    private CustomButton btnSave, btnCancel, btnChooseImage;
    private JLabel lblImagePreview;
    private File selectedImageFile = null; // Chỉ lưu ảnh sau khi lưu database thành công

    public GUI_Form_Employee(JPanel parent, EmployeeDTO employee) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), employee == null ? "Thêm Nhân Viên" : "Sửa Nhân Viên", true);
        setSize(600, 500);
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
        btnChooseImage = new CustomButton("Chọn Ảnh");

        lblImagePreview = new JLabel();
        lblImagePreview.setPreferredSize(new Dimension(150, 150));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Nếu đang sửa nhân viên, điền thông tin vào form
        if (employee != null) {
            txtEmployeeName.setText(employee.getFullName());
            txtAddress.setText(employee.getAddress());
            txtPhone.setText(employee.getPhone());
            displayImage(employee.getImage());
        }

        addComponent("Tên Nhân Viên:", txtEmployeeName, gbc);
        addComponent("Địa Chỉ:", txtAddress, gbc);
        addComponent("Số Điện Thoại:", txtPhone, gbc);

        // Thêm nút chọn ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(btnChooseImage, gbc);

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
        btnChooseImage.addActionListener(e -> chooseImage());

        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> saveEmployee(employee));
    }

    // Chọn ảnh nhưng chưa lưu ngay
    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn Ảnh Nhân Viên");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            displayImage(selectedImageFile.getAbsolutePath());
        }
    }

    // Lưu nhân viên vào database, sau đó mới lưu ảnh
    private void saveEmployee(EmployeeDTO employee) {
        String name = txtEmployeeName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Nếu là thêm nhân viên
        if (employee == null) {
            String imagePath = null; // Chưa có ảnh

            // Nếu đã chọn ảnh thì lưu ảnh trước
            if (selectedImageFile != null) {
                imagePath = saveImage(selectedImageFile); // Lưu ảnh vào thư mục
                System.out.println("da la anh" + imagePath);
            }

            // Tạo Employee mới
            EmployeeDTO newEmployee = new EmployeeDTO(null, name, address, phone, imagePath);
            System.out.println("duong dan anh" + newEmployee.getImage());
            boolean success = EmployeeDAO.addEmployee(newEmployee);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } // Nếu là cập nhật nhân viên
        else {
            String imagePath = employee.getImage(); // Mặc định giữ ảnh cũ

            // Nếu đã chọn ảnh mới thì lưu ảnh trước
            if (selectedImageFile != null) {
                imagePath = saveImage(selectedImageFile); // Lưu ảnh vào thư mục
                employee.setImage(imagePath); // Cập nhật ảnh mới
            }

            // Cập nhật thông tin nhân viên
            employee.setFullName(name);
            employee.setAddress(address);
            employee.setPhone(phone);

            boolean success = EmployeeDAO.updateEmployee(employee);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
    }
    

    // Lưu ảnh vào thư mục resources/images và trả về đường dẫn
    private String saveImage(File imageFile) {
        // Lấy đường dẫn thư mục images trong project (ngoài thư mục resources)
        String imageDir = System.getProperty("user.dir") + "/images";
        File dir = new File(imageDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Đảm bảo thư mục tồn tại
        }
        // Tạo tên file mới với timestamp
        String newImageName = System.currentTimeMillis() + "_" + imageFile.getName();
        File newImageFile = new File(imageDir, newImageName);

        try {
            Files.copy(imageFile.toPath(), newImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Ảnh đã được lưu vào: " + newImageFile.getAbsolutePath());
            return newImageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu lỗi, giữ nguyên ảnh cũ
        }
    }

    // Hiển thị ảnh từ thư mục images
    private void displayImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            lblImagePreview.setIcon(null);
            return;
        }

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
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
