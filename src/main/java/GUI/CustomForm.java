package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CustomForm extends JDialog {

    private final JPanel formPanel;
    private final CustomButton saveButton;

    public CustomForm(JFrame parent, String title, Map<String, Object> fields, JTable table, int selectedRow) {
        super(parent, title, true);
        setSize(400, 250);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        // Add dynamic fields based on the `fields` map
        int rowIndex = 0;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String label = entry.getKey();
            Object value = entry.getValue();

            gbc.gridx = 0;
            gbc.gridy = rowIndex;
            formPanel.add(new JLabel(label + ": "), gbc);

            gbc.gridx = 1;
            JComponent inputField;
            if (value instanceof String) {
                inputField = new JTextField(15);
                ((JTextField) inputField).setText((String) value);
            } else if (value instanceof String[]) {
                inputField = new JComboBox<>((String[]) value);
            } else {
                inputField = new JTextField(15);
            }

            formPanel.add(inputField, gbc);
            rowIndex++;
        }

        gbc.gridx = 0;
        gbc.gridy = rowIndex;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Save button
        saveButton = new CustomButton("Lưu");
        formPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            if (selectedRow != -1) {
                // Update values in the table
                int columnIndex = 0;
                for (Component component : formPanel.getComponents()) {
                    if (component instanceof JTextField) {
                        table.setValueAt(((JTextField) component).getText(), selectedRow, columnIndex++);
                    } else if (component instanceof JComboBox) {
                        table.setValueAt(((JComboBox<?>) component).getSelectedItem(), selectedRow, columnIndex++);
                    }
                }
            }
            dispose();
        });

        add(formPanel, BorderLayout.CENTER);
    }

    // Method to show form
    public void showForm() {
        setVisible(true);
    }

    // Hàm main để chạy thử
    public static void main(String[] args) {
        // Tạo cửa sổ chính JFrame
        JFrame mainFrame = new JFrame("Main Window");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        // Tạo một JTable giả lập để sử dụng với CustomForm
        String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm", "Giá sản phẩm", "Danh mục"};
        Object[][] data = {
                {"SP001", "Sản phẩm A", "100000", "Điện tử"},
                {"SP002", "Sản phẩm B", "200000", "Gia dụng"}
        };
        JTable productTable = new JTable(data, columnNames);

        // Tạo Map với các trường thông tin
        Map<String, Object> fields = Map.of(
                "Mã sản phẩm", "SP003",
                "Tên sản phẩm", "Sản phẩm C",
                "Giá sản phẩm", "300000",
                "Danh mục sản phẩm", new String[]{"Điện tử", "Gia dụng", "Thực phẩm"}
        );

        // Tạo và hiển thị form chỉnh sửa cho sản phẩm
        CustomForm customForm = new CustomForm(mainFrame, "Sửa Sản Phẩm", fields, productTable, 0);
        customForm.showForm();

        // Hiển thị cửa sổ chính
        mainFrame.setVisible(true);
    }
}
