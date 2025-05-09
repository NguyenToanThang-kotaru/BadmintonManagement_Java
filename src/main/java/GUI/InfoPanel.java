package GUI;

import BUS.Form_ImportBUS;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;

//LAYOUT THÔNG TIN PHIẾU NHẬP HÀNG CỦA FORM IMPORT
//SỬ DỤNG ĐỂ HIỂN THỊ THÔNG TIN PHIẾU NHẬP HÀNG TRONG FORM IMPORT

public class InfoPanel extends JPanel {
    private JLabel lblMaNhapHang, lblTongTien;

    public InfoPanel(String importId, String currentUser, Form_ImportBUS bus) {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(new CompoundBorder(new TitledBorder("Thông tin nhập hàng"), new EmptyBorder(10, 10, 10, 10)));
        setBackground(Color.WHITE);

        // Dòng 1
        JPanel maNhapHangPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maNhapHangPanel.setBackground(Color.WHITE);
        maNhapHangPanel.add(new JLabel("Mã nhập hàng:"));
        lblMaNhapHang = new JLabel(importId);
        lblMaNhapHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        maNhapHangPanel.add(lblMaNhapHang);
        add(maNhapHangPanel);

        JPanel nhanVienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nhanVienPanel.setBackground(Color.WHITE);
        nhanVienPanel.add(new JLabel("Nhân viên:"));
        JLabel lblNhanVien = new JLabel(currentUser + " - " + bus.getEmployeeName(currentUser));
        lblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nhanVienPanel.add(lblNhanVien);
        add(nhanVienPanel);

        // Dòng 2
        JPanel ngayNhapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ngayNhapPanel.setBackground(Color.WHITE);
        ngayNhapPanel.add(new JLabel("Ngày nhập:"));
        JLabel lblNgayNhap = new JLabel(LocalDate.now().toString());
        lblNgayNhap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ngayNhapPanel.add(lblNgayNhap);
        add(ngayNhapPanel);

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tongTienPanel.setBackground(Color.WHITE);
        tongTienPanel.add(new JLabel("Tổng tiền:"));
        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(0, 100, 0));
        tongTienPanel.add(lblTongTien);
        add(tongTienPanel);
    }

    // Getter cho lblTongTien
    public JLabel getLblTongTien() {
        return lblTongTien;
    }

    // Thêm getter cho lblMaNhapHang
    public JLabel getLblMaNhapHang() {
        return lblMaNhapHang;
    }
}