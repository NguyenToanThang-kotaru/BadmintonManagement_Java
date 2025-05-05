package GUI;

import BUS.ActionBUS;
import BUS.EmployeeBUS;
import DAO.EmployeeDAO;
import DTO.AccountDTO;
import DTO.ActionDTO;
import DTO.EmployeeDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI_Employee extends JPanel {

    private JPanel topPanel, midPanel, botPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private CustomButton editButton, deleteButton, addButton, reloadButton, importExcelButton, exportExcelButton;
    private CustomSearch searchField;
    private EmployeeBUS employeeBUS;
    private EmployeeDTO employeeChoosing;

    public GUI_Employee(AccountDTO a) {
        employeeBUS = new EmployeeBUS();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG (Thanh tìm kiếm & nút) ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Thanh tìm kiếm ở giữa
        searchField = new CustomSearch(250, 30);
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);

        // Panel chứa nút Nhập và Xuất Excel ở WEST
        JPanel westButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        westButtonPanel.setBackground(Color.WHITE);
        importExcelButton = new CustomButton("Nhập Excel");
        importExcelButton.setPreferredSize(new Dimension(120, 30));
        westButtonPanel.add(importExcelButton);
        exportExcelButton = new CustomButton("Xuất Excel");
        exportExcelButton.setPreferredSize(new Dimension(120, 30));
        westButtonPanel.add(exportExcelButton);
        topPanel.add(westButtonPanel, BorderLayout.WEST);

        // Panel chứa nút Tải Lại và Thêm NV ở EAST
        JPanel eastButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        eastButtonPanel.setBackground(Color.WHITE);
        reloadButton = new CustomButton("Tải Lại");
        reloadButton.setPreferredSize(new Dimension(120, 30));
        eastButtonPanel.add(reloadButton);
        addButton = new CustomButton("+ Thêm NV");
        addButton.setPreferredSize(new Dimension(120, 30));
        eastButtonPanel.add(addButton);
        topPanel.add(eastButtonPanel, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ DANH SÁCH NHÂN VIÊN ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        midPanel.setMinimumSize(new Dimension(600, 200));
        midPanel.setPreferredSize(new Dimension(600, 200));
        String[] columnNames = {"STT", "Họ Tên", "Địa Chỉ", "SĐT"};
        CustomTable customTable = new CustomTable(columnNames);
        employeeTable = customTable.getEmployeeTable();
        tableModel = customTable.getTableModel();
        CustomScrollPane scrollPane = new CustomScrollPane(employeeTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        // ========== PANEL CHI TIẾT NHÂN VIÊN ==========
        botPanel = new JPanel(new BorderLayout(20, 0));
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));

        // Phần trái
        JPanel leftPanel = new JPanel(null);
        leftPanel.setPreferredSize(new Dimension(310, 290));
        leftPanel.setBackground(Color.WHITE);
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(30, 10, 210, 190);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        leftPanel.add(imageLabel);

        // Phần phải
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setBackground(Color.WHITE);
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Tên Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeLabel = new JLabel("Chọn Nhân Viên");
        infoPanel.add(employeeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Mã Nhân Viên: "), gbc);
        gbc.gridx = 1;
        JLabel employeeIdLabel = new JLabel("");
        infoPanel.add(employeeIdLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(new JLabel("Địa Chỉ: "), gbc);
        gbc.gridx = 1;
        JLabel addressLabel = new JLabel("");
        infoPanel.add(addressLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        infoPanel.add(new JLabel("Số Điện Thoại: "), gbc);
        gbc.gridx = 1;
        JLabel phoneLabel = new JLabel("");
        infoPanel.add(phoneLabel, gbc);

        // ========== PANEL BUTTON ==========
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        deleteButton = new CustomButton("Xóa");
        deleteButton.setCustomColor(new Color(220, 0, 0));
        buttonPanel.add(deleteButton, BorderLayout.WEST);
        editButton = new CustomButton("Sửa");
        editButton.setCustomColor(new Color(0, 230, 0));
        buttonPanel.add(editButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rightPanel.add(infoPanel);
        botPanel.add(leftPanel, BorderLayout.WEST);
        botPanel.add(rightPanel, BorderLayout.CENTER);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                String hoten = (String) employeeTable.getValueAt(selectedRow, 1);
                String diaChi = (String) employeeTable.getValueAt(selectedRow, 2);
                String sdt = (String) employeeTable.getValueAt(selectedRow, 3);
                employeeChoosing = EmployeeDAO.getEmployee(sdt);
                employeeLabel.setText(hoten);
                employeeIdLabel.setText(employeeChoosing.getEmployeeID());
                addressLabel.setText(diaChi);
                phoneLabel.setText(sdt);
                infoPanel.add(buttonPanel, gbc);

                String employeeImg = employeeChoosing.getImage();
                String imagePath = "images/Avatar.png";
                if (employeeImg != null && !employeeImg.isEmpty()) {
                    String tempPath = "images/" + employeeImg;
                    File imageFile = new File(tempPath);
                    if (imageFile.exists()) {
                        imagePath = tempPath;
                    }
                }
                ImageIcon employeeIcon = new ImageIcon(imagePath);
                Image img = employeeIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            }
        });

        addButton.addActionListener(e -> {
            Form_Employee GFE = new Form_Employee(this, null);
            GFE.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            if (helped.confirmDelete("Bạn có chắc muốn xóa nhân viên này?")) {
                if (deleteEmployee(employeeChoosing.getEmployeeID(), employeeChoosing.getImage())) {
                    loadEmployees();
                    tableModel.fireTableDataChanged();
                    employeeLabel.setText("Chọn nhân viên");
                    employeeIdLabel.setText("");
                    addressLabel.setText("");
                    phoneLabel.setText("");
                    String employeeImg = employeeChoosing.getImage();
                    String imagePath = "images/Avatar.png";
                    if (employeeImg != null && !employeeImg.isEmpty()) {
                        String tempPath = "images/" + employeeImg;
                        File imageFile = new File(tempPath);
                        if (imageFile.exists()) {
                            imagePath = tempPath;
                        }
                    }
                    ImageIcon employeeIcon = new ImageIcon(imagePath);
                    Image img = employeeIcon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                }
            }
        });

        reloadButton.addActionListener(e -> {
            loadEmployees();
            tableModel.fireTableDataChanged();
        });

        editButton.addActionListener(e -> {
            Form_Employee GFE = new Form_Employee(this, employeeChoosing);
            GFE.setVisible(true);
        });

        searchField.setSearchListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                searchEmployee(keyword);
            } else {
                loadEmployees();
            }
        });

        exportExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachNhanVien.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                boolean success = employeeBUS.exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        importExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel để nhập");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx", "xls"));
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToImport = fileChooser.getSelectedFile();
                try {
                    boolean success = employeeBUS.importEmployeesFromExcel(fileToImport);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Nhập dữ liệu từ Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        loadEmployees();
                        tableModel.fireTableDataChanged();
                    } else {
                        JOptionPane.showMessageDialog(this, "Nhập dữ liệu từ Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi nhập file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        add(topPanel);
        add(Box.createVerticalStrut(5));
        add(midPanel);
        add(Box.createVerticalStrut(5));
        add(botPanel);

        loadEmployees();
        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(a, "Quản lý nhân viên");
        boolean canAdd = false, canEdit = false, canDelete = false, canWatch = false;
        if (actions != null) {
            for (ActionDTO action : actions) {
                switch (action.getName()) {
                    case "Thêm" -> canAdd = true;
                    case "Sửa" -> canEdit = true;
                    case "Xóa" -> canDelete = true;
                    case "Xem" -> canWatch = true;
                }
            }
        }

        addButton.setVisible(canAdd);
        editButton.setVisible(canEdit);
        deleteButton.setVisible(canDelete);
        scrollPane.setVisible(canWatch);
        reloadButton.setVisible(true);
    }

    private Boolean deleteEmployee(String employeeID, String employeeImg) {
        if (EmployeeDAO.deleteEmployee(employeeID)) {
            String imagePath = "images/" + employeeImg;
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    System.out.println("Đã xóa ảnh của nhân viên.");
                } else {
                    System.out.println("Không thể xóa ảnh của nhân viên.");
                }
            }
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    private void loadEmployees() {
        List<EmployeeDTO> employees = EmployeeDAO.getAllEmployees();
        tableModel.setRowCount(0);
        int index = 1;
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{
                    index++,
                    emp.getFullName(),
                    emp.getAddress(),
                    emp.getPhone()
            });
        }
    }

    private void searchEmployee(String keyword) {
        List<EmployeeDTO> employees = employeeBUS.searchEmployee(keyword);
        tableModel.setRowCount(0);
        int index = 1;
        for (EmployeeDTO emp : employees) {
            tableModel.addRow(new Object[]{
                    index++,
                    emp.getFullName(),
                    emp.getAddress(),
                    emp.getPhone()
            });
        }
    }
}