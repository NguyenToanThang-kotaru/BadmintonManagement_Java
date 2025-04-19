package BUS;

import DAO.DetailImportDAO;
import DTO.DetailImportDTO;
import GUI.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Connection.DatabaseConnection;

public class DetailImportBUS {
    private DetailImportDAO dao;

    public DetailImportBUS() {
        this.dao = new DetailImportDAO();
    }

    public List<DetailImportDTO> getAllDetailImport() {
        return dao.getAllDetailImport();
    }

    public void updateDetailImport(DetailImportDTO detailImport) {
        dao.updateDetailImport(detailImport);
    }

    public String getEmployeeInfo(String employeeID) {
        String query = "SELECT ten_nhan_vien, so_dien_thoai, dia_chi FROM nhan_vien WHERE ma_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.format("%s - %s - %s - %s",
                        employeeID,
                        rs.getString("ten_nhan_vien"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("dia_chi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeID;
    }

    public String getSupplierInfo(String supplierID) {
        String query = "SELECT ten_nha_cung_cap, dia_chi, so_dien_thoai FROM nha_cung_cap WHERE ma_nha_cung_cap = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.format("%s - %s - %s",
                        rs.getString("ten_nha_cung_cap"),
                        rs.getString("dia_chi"),
                        rs.getString("so_dien_thoai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierID;
    }

    public List<Object[]> loadImportDetails(String importID) {
        List<Object[]> details = new ArrayList<>();
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, ncc.ten_nha_cung_cap, ctnh.so_luong, ctnh.gia, sp.gia as gia_ban " +
                      "FROM chi_tiet_nhap_hang ctnh " +
                      "JOIN san_pham sp ON ctnh.ma_san_pham = sp.ma_san_pham " +
                      "JOIN nha_cung_cap ncc ON ctnh.ma_nha_cung_cap = ncc.ma_nha_cung_cap " +
                      "WHERE ctnh.ma_nhap_hang = ? AND ctnh.is_deleted = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int quantity = rs.getInt("so_luong");
                int price = rs.getInt("gia"); // Giá gốc từ chi_tiet_nhap_hang
                int sellingPrice = rs.getInt("gia_ban"); // Giá bán từ san_pham
                int importTotal = quantity * price; // Tổng tiền nhập
                int sellingTotal = quantity * sellingPrice; // Tổng tiền bán
                details.add(new Object[]{
                        rs.getString("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        rs.getString("ten_nha_cung_cap"),
                        quantity,
                        Utils.formatCurrency(price),
                        Utils.formatCurrency(sellingPrice),
                        Utils.formatCurrency(importTotal),
                        Utils.formatCurrency(sellingTotal)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}