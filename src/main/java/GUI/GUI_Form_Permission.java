package GUI;

import DAO.PermissionDAO;
import DTO.PermissionDTO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI_Form_Permission extends JDialog {

    private JTextField txtPermissionName;
    private JLabel title;
    private JList<String> functionList;
    private DefaultListModel<String> listModel;
    private CustomButton btnSave, btnCancel;

    public GUI_Form_Permission(JPanel parent, PermissionDTO permission) {
        super((Frame) SwingUtilities.getWindowAncestor(parent),
                permission == null ? "Thêm Quyền" : "Sửa Quyền", true);

        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Tiêu đề form
        title = new JLabel(permission == null ? "THÊM QUYỀN MỚI" : "CHỈNH SỬA QUYỀN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Tên quyền
        txtPermissionName = new JTextField(20);
        addComponent("Tên Quyền:", txtPermissionName, gbc);

        // Danh sách chức năng
        listModel = new DefaultListModel<>();
        functionList = new JList<>(listModel);
        functionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Thêm tất cả chức năng có sẵn (CN001-CN032)
        for (int i = 1; i <= 32; i++) {
            String code = String.format("CN%03d", i);
            listModel.addElement(code);
        }

        JScrollPane scrollPane = new JScrollPane(functionList);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Danh Sách Chức Năng:"), gbc);

        gbc.gridy++;
        add(scrollPane, gbc);

        // Nút lưu và hủy
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new CustomButton(permission == null ? "Thêm" : "Cập Nhật");
        btnCancel = new CustomButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Nếu là chế độ sửa, load dữ liệu hiện có
        if (permission != null) {
            txtPermissionName.setText(permission.getName());

            // Chọn các chức năng hiện có
            List<String> selectedFunctions = permission.getChucNang();
            int[] selectedIndices = new int[selectedFunctions.size()];

            for (int i = 0; i < selectedFunctions.size(); i++) {
                selectedIndices[i] = listModel.indexOf(selectedFunctions.get(i));
            }

            functionList.setSelectedIndices(selectedIndices);
        }

        btnCancel.addActionListener(e -> dispose());
    }

    private void addComponent(String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(component, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }
}
