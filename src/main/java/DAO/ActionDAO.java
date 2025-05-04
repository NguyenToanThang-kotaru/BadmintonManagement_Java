/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.ActionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class ActionDAO {

    public static ActionDTO getActionByName(String Name) {
        String query = "SELECT * FROM hanh_dong WHERE ten_hanh_dong = ?";
//        System.out.println(ID);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, Name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ActionDTO(
                        rs.getString("ma_hanh_dong"),
                        rs.getString("ten_hanh_dong"),
                        rs.getString("ten_khong_dau")
                );
            }
            System.out.println("khong co cai doe");

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("cccc");
            return null;
        }
    }

    public static ActionDTO getActionByID(String ID) {
        String query = "SELECT * FROM hanh_dong WHERE ma_hanh_dong = ?";
//        System.out.println(ID);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ActionDTO(
                        rs.getString("ma_hanh_dong"),
                        rs.getString("ten_hanh_dong"),
                        rs.getString("ten_khong_dau")
                );
            }
            System.out.println("khong co cai doe");

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("cccc");
            return null;
        }
    }

    public static ArrayList<ActionDTO> getActionForFunction(String perID, String funcID) {
        String query = "Select * FROM phan_quyen2 WHERE ma_quyen = ? AND ma_chuc_nang = ? ";
        ArrayList<ActionDTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareCall(query);
            stmt.setString(1, perID);
            stmt.setString(2, funcID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ma_hanh_dong");
                ActionDTO act = getActionByID(id);
                list.add(act);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("khogn co cc j");
            return null;
        }
    }

    public static ArrayList<ActionDTO> getAllActions() {
        String query = "SELECT * FROM hanh_dong";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<ActionDTO> list = new ArrayList<>();
            while (rs.next()) {
                ActionDTO act = new ActionDTO(
                        rs.getString("ma_hanh_dong"),
                        rs.getString("ten_hanh_dong"),
                        rs.getString("ten_khong_dau")
                );
                list.add(act);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
