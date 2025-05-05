package GUI;

import BUS.ActionBUS;
import DAO.GuaranteeDAO;
import DTO.GuaranteeDTO;
import BUS.GuaranteeBUS;

import BUS.GuaranteeBUS;
import DTO.AccountDTO;
import DTO.ActionDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class GUI_Guarantee extends JPanel {

    private JPanel midPanel, topPanel, botPanel;
    private JTable warrantyTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> warrantyStatusComboBox;
    private CustomButton saveButton, fixButton, reloadButton;
    private CustomSearch searchField;

    public GUI_Guarantee(AccountDTO a) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 200, 200));

        // ========== PANEL TRÊN CÙNG ==========
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Thanh tìm kiếm
        searchField = new CustomSearch(250, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
        topPanel.add(searchField, BorderLayout.CENTER);
        reloadButton = new CustomButton("Tải Lại Trang");
        topPanel.add(reloadButton, BorderLayout.WEST);
        
        // Nút xuất Excel
        CustomButton exportExcelButton = new CustomButton("Xuất Excel");
        exportExcelButton.setPreferredSize(new Dimension(120, 30));
        topPanel.add(exportExcelButton, BorderLayout.EAST);

        // ========== BẢNG HIỂN THỊ ==========
        midPanel = new JPanel(new BorderLayout());
        midPanel.setBackground(Color.WHITE);
        String[] columnNames = {"Mã BH", "Mã Serial", "Lý do bảo hành", "Thời gian bảo hành", "Trạng thái bảo hành"};
        CustomTable customTable = new CustomTable(columnNames);

        warrantyTable = customTable.getAccountTable();
        tableModel = customTable.getTableModel();

        CustomScrollPane scrollPane = new CustomScrollPane(warrantyTable);
        midPanel.add(scrollPane, BorderLayout.CENTER);
        midPanel.setMinimumSize(new Dimension(600, 200));
        midPanel.setPreferredSize(new Dimension(600, 200));

        // ========== PANEL CHI TIẾT ==========
        botPanel = new JPanel(new GridBagLayout());
        botPanel.setBackground(Color.WHITE);
        botPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết bảo hành"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Mã Serial
        gbc.gridx = 0;
        gbc.gridy = 0;
        botPanel.add(new JLabel("Mã Serial: "), gbc);
        gbc.gridx = 1;
        JLabel serialLabel = new JLabel("Chưa chọn");
        botPanel.add(serialLabel, gbc);

        // Ngày mua
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        botPanel.add(new JLabel("Ngày mua: "), gbc);
//        gbc.gridx = 1;
//        JLabel purchaseDateLabel = new JLabel("Chưa chọn");
//        botPanel.add(purchaseDateLabel, gbc);
        // Trạng thái bảo hành
        gbc.gridx = 0;
        gbc.gridy = 1;
        botPanel.add(new JLabel("Trạng thái bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel StatusLabel = new JLabel("");
        botPanel.add(StatusLabel, gbc);

        // Lý do bảo hành
        gbc.gridx = 0;
        gbc.gridy = 2;
        botPanel.add(new JLabel("Lý do bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel textReasonLabel = new JLabel("");
        botPanel.add(textReasonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        botPanel.add(new JLabel("Thời gian bảo hành: "), gbc);
        gbc.gridx = 1;
        JLabel StatusTime = new JLabel("");
        botPanel.add(StatusTime, gbc);

        // Nút sửa
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fixButton = new CustomButton("Sửa");
        fixButton.setCustomColor(Color.RED);

        warrantyTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = warrantyTable.getSelectedRow();

            if (selectedRow != -1) {
                String guaranteeID = (String) warrantyTable.getValueAt(selectedRow, 0);
                GuaranteeDTO guarantee = GuaranteeBUS.getGuarantee(guaranteeID);
//                System.out.println("Dang chon ");
                serialLabel.setText(guarantee.getSerialID());
                textReasonLabel.setText(guarantee.getLydo());
                StatusTime.setText(guarantee.getTGBH());
                StatusLabel.setText(guarantee.gettrangthai());
                botPanel.add(fixButton, gbc);
                System.out.println("Co sua ");

            }
        }
        );
        // ========== THÊM MỌI THỨ VÀO MAIN PANEL ==========
        add(topPanel);
        add(midPanel);
        add(botPanel);

        fixButton.addActionListener(e -> {
            int selectedRow = warrantyTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một bảo hành để sửa!");
                return;
            }

            // Lấy mã bảo hành từ bảng
            String guaranteeID = (String) warrantyTable.getValueAt(selectedRow, 0);

            // Lấy dữ liệu bảo hành từ database
            GuaranteeDTO guarantee = GuaranteeBUS.getGuarantee(guaranteeID);

            Form_Guarantee fixForm = new Form_Guarantee((JFrame) SwingUtilities.getWindowAncestor(this), this, guarantee);
            fixForm.setVisible(true);
        });

        exportExcelButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setSelectedFile(new File("DanhSachBaoHanh.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);
        
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                boolean success = new GuaranteeBUS().exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        reloadButton.addActionListener(e -> {
            loadGuaranteeData();
            tableModel.fireTableDataChanged();
        });

        loadGuaranteeData();
        ArrayList<ActionDTO> actions = ActionBUS.getPermissionActions(a, "Quản lý bảo hành");

        Boolean canEdit = false, canWatch = false;

        if (actions != null) {
            for (ActionDTO action : actions) {
                switch (action.getName()) {
                    case "Sửa" ->
                        canEdit = true;
                    case "Xem" ->
                        canWatch = true;
                }
            }
        }

        fixButton.setVisible(canEdit);
        scrollPane.setVisible(canWatch);
//        reloadButton.setVisible(false);

        // Xử lý sự kiện chọn dòng trong bảng
        // Xử lý sự kiện chọn dòng trong bảng
    }

    public void loadGuaranteeData() {
        ArrayList<GuaranteeDTO> guaranteeList = GuaranteeBUS.getAllGuarantee();
        tableModel.setRowCount(0);
        for (GuaranteeDTO guarantee : guaranteeList) {
            tableModel.addRow(new Object[]{
                guarantee.getBaohanhID(), guarantee.getSerialID(), guarantee.getLydo(), guarantee.getTGBH(), guarantee.gettrangthai()
            });
        }

    }

    private void capNhatBangBaoHanh(ArrayList<GuaranteeDTO> guarantees) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (GuaranteeDTO guarantee : guarantees) {
            tableModel.addRow(new Object[]{
                guarantee.getBaohanhID(),
                guarantee.getSerialID(),
                guarantee.getLydo(),
                guarantee.getTGBH(),
                guarantee.gettrangthai()
            });
        }
    }
//

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Quản lý bảo hành");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(900, 600);
//            frame.setLocationRelativeTo(null);
//            frame.setContentPane(new GUI_Guarantee());
//            frame.setVisible(true);
//        });
//    }
}
