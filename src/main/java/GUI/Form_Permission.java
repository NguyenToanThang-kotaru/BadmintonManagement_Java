package GUI;

import BUS.ActionBUS;
import BUS.FunctionBUS;
import BUS.PermissionBUS;
import DTO.ActionDTO;
import DTO.FunctionDTO;
import DTO.Permission2DTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Form_Permission extends JDialog {

    private JTextField txtPermissionName;
    private JLabel title;
    private JPanel checkBoxPanel;
    private List<JCheckBox> allCheckBoxes = new ArrayList<>();
    private CustomButton btnSave, btnCancel;

    public Form_Permission(GUI_Permission parent, Permission2DTO permission) {
        super((Frame) SwingUtilities.getWindowAncestor(parent),
                permission == null ? "Thêm Quyền" : "Sửa Quyền", true);

        setSize(600, 500); // Tăng chiều cao để hiển thị rõ hơn
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

        // Danh sách chức năng với checkbox
        checkBoxPanel = CheckBox(permission);
//        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
//        CustomScrollPane scrollPane = new CustomScrollPane(checkBoxPanel);
        // Thêm checkbox cho tất cả chức năng
//        PermissionDTO allFunctions = new PermissionDTO(PermissionDAO.getPermission("1"));
//        for (String chucNang : PermissionDAO.getAllFunctionByName()) {
//            JCheckBox checkBox = new JCheckBox(PermissionBUS.decodeFunctionName(chucNang));
//            checkBox.setName(chucNang); // Lưu mã gốc vào thuộc tính name
//            allCheckBoxes.add(checkBox);
//            checkBoxPanel.add(checkBox);
//        }
        // Thêm vào ScrollPane
        CustomScrollPane scrollPane = new CustomScrollPane(checkBoxPanel);
        scrollPane.setPreferredSize(new Dimension(350, 250));
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
        if (permission != null) {
            txtPermissionName.setText(permission.getName());
        }
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            Permission2DTO NewPermission = new Permission2DTO(PermissionBUS.generateID(),
                    txtPermissionName.getText(), "", getSelectedFunctions());
            if (permission == null) {
                if (PermissionBUS.addPermission(NewPermission) == true) {
                    System.out.println(txtPermissionName.getText());
                    System.out.println(NewPermission.getName());
                    JOptionPane.showMessageDialog(null, "Thêm quyền thành công.");
                    dispose();
                } 
            } else {
                permission.setName(txtPermissionName.getText());
                permission.setFunction(getSelectedFunctions());
                if (PermissionBUS.updatePermission(permission)) {
                    parent.loadPermissions();
                    dispose();
                } else {
                    System.out.println("Sua that bai...");
                }

            }
        });
//            } else if (PermissionDAO.editPermission(permission, txtPermissionName.getText(), getSelectedFunctions()) == true) {
//                System.out.println(txtPermissionName.getText());
//
//                System.out.println("Sua thanh cong");
//                dispose();
//            } else {
//                System.out.println("Sua that bai...");
//            }
//        });
    }

    public JPanel CheckBox(Permission2DTO per) {
        ArrayList<ActionDTO> actions = new ArrayList<>();
        for (var action : ActionBUS.getAllActions()) {
            actions.add(action); // Add, Edit, Delete
        }

        ArrayList<FunctionDTO> functions = new ArrayList<>();
        for (var function : FunctionBUS.getAllFunctions()) {
            functions.add(function); // Quản lý SP, Khuyến mãi...
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng tiêu đề (Xem, Thêm, Sửa, Xóa)
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Tên chức năng"), gbc);

        for (int i = 0; i < actions.size(); i++) {
            gbc.gridx = i + 1;
            JLabel actionLabel = new JLabel(actions.get(i).getName(), JLabel.CENTER);
            actionLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(actionLabel, gbc);
        }
        // Các dòng chức năng
        for (int row = 0; row < functions.size(); row++) {
            FunctionDTO function = functions.get(row);
            gbc.gridy = row + 1;
            gbc.gridx = 0;
            // Label chức năng
            panel.add(new JLabel(function.getName()), gbc);
            for (int col = 0; col < actions.size(); col++) {
                ActionDTO action = actions.get(col);
                gbc.gridx = col + 1;

                JCheckBox cb = new JCheckBox();
                cb.setName(function.getName() + "_" + action.getName()); // Ví dụ: "Quản lý SP_Add"
                if (function.getUname().equals("Quan ly thong ke") && (col == 1 || col == 2 || col == 3)) {
                    continue;
                }
//                System.out.println(cb.getName());
                allCheckBoxes.add(cb);
                panel.add(cb, gbc);
            }
        }
        if (per != null) {
            for (FunctionDTO func : per.getFunction()) {
                for (ActionDTO act : func.getActions()) {
                    String key = func.getName() + "_" + act.getName();
                    boolean found = false;
                    for (JCheckBox cb : allCheckBoxes) {
                        if (cb.getName().equals(key)) {
                            cb.setSelected(true);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Không tìm thấy checkbox với key: " + key);
                    }
                }
            }
        }
        return panel;
    }

    public ArrayList<FunctionDTO> getSelectedFunctions() {
        ArrayList<FunctionDTO> selectedFunctions = new ArrayList<>();

        for (JCheckBox checkBox : allCheckBoxes) {
            if (checkBox.isSelected()) {
                String[] parts = checkBox.getName().split("_");
                String functionName = parts[0];
                String actionName = parts[1];

                // Kiểm tra xem FunctionDTO đã tồn tại trong selectedFunctions chưa
                FunctionDTO existingFunction = null;
                for (FunctionDTO func : selectedFunctions) {
                    if (func.getName().equals(functionName)) {
                        existingFunction = func;
                        break;
                    }
                }

                // Nếu chưa tồn tại, tạo mới và thêm vào danh sách
                if (existingFunction == null) {
                    existingFunction = FunctionBUS.getFunctionByName(functionName);
                    existingFunction.setActions(new ArrayList<>()); // Khởi tạo danh sách action
                    selectedFunctions.add(existingFunction);
                }

                // Thêm ActionDTO vào FunctionDTO (nếu chưa có)
                ActionDTO action = ActionBUS.getActionByName(actionName);
                if (action != null) {
                    boolean actionExists = false;
                    for (ActionDTO existingAction : existingFunction.getActions()) {
                        if (existingAction.getName().equals(actionName)) {
                            actionExists = true;
                            break;
                        }
                    }
                    if (!actionExists) {
                        existingFunction.getActions().add(action);
                    }
                }
            }
        }

        return selectedFunctions;
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
