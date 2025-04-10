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
        String query = "SELECT sp.ma_san_pham, sp.ten_san_pham, ctnh.so_luong, ctnh.gia " +
                       "FROM chi_tiet_nhap_hang ctnh " +
                       "JOIN san_pham sp ON ctnh.ma_san_pham = sp.ma_san_pham " +
                       "WHERE ctnh.ma_nhap_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, importID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int quantity = rs.getInt("so_luong");
                int price = rs.getInt("gia");
                int rowTotal = quantity * price;
                details.add(new Object[]{
                    rs.getString("ma_san_pham"),
                    rs.getString("ten_san_pham"),
                    quantity,
                    Utils.formatCurrency(price),
                    Utils.formatCurrency(rowTotal)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}