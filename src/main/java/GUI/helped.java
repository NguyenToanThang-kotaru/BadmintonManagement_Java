/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.JOptionPane;

/**
 *
 * @author Thang Nguyen
 */
public class helped {

    public static boolean confirmDelete(String message) {
        int option = JOptionPane.showConfirmDialog(
                null,
                message != null ? message : "Bạn có chắc muốn xóa dữ liệu này?", // Nội dung hỏi
                "Xác nhận xóa", // Tiêu đề
                JOptionPane.YES_NO_OPTION, // Loại nút
                JOptionPane.WARNING_MESSAGE // Biểu tượng cảnh báo
        );
        return option == JOptionPane.YES_OPTION;
    }
}
