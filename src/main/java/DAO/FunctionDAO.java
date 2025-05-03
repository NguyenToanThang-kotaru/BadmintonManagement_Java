/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Connection.DatabaseConnection;
import DTO.ActionDTO;
import DTO.FunctionDTO;
import DTO.Permission2DTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Thang Nguyen
 */
public class FunctionDAO {

    public static Boolean deleteFunction(Permission2DTO per) {
        String query = "DELETE From phan_quyen2 where ma_quyen = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, per.getID());
            int rs = stmt.executeUpdate();
            if (rs > 0) {
                return true;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean addFunction(Permission2DTO per) {
        String query = "INSERT INTO phan_quyen2 (ma_quyen, ma_chuc_nang, ma_hanh_dong) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            for (FunctionDTO func : per.getFunction()) {
                for (ActionDTO act : func.getActions()) {
                    stmt.setString(1, per.getID());
                    stmt.setString(2, func.getID());
                    stmt.setString(3, act.getID());
                    stmt.addBatch();
                }
            }

            int[] results = stmt.executeBatch();
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    return false; // Nếu có lỗi khi chèn
                }
            }

            return true; // Tất cả đều chèn thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static FunctionDTO getFunctionByName(String Name) {
        String query = "SELECT * FROM chuc_nang2 WHERE ten_chuc_nang = ?";
        ArrayList<ActionDTO> list = new ArrayList<>();
//        System.out.println(ID); // debug xem ID đúng chưa

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, Name); // Gán tham số vào vị trí ?
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FunctionDTO func = new FunctionDTO(
                        rs.getString("ma_chuc_nang"),
                        rs.getString("ten_chuc_nang"),
                        rs.getString("ten_khong_dau"),
                        list
                );
                return func;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FunctionDTO getFunctionByID(String ID) {
        String query = "SELECT * FROM chuc_nang2 WHERE ma_chuc_nang = ?";
        ArrayList<ActionDTO> list = new ArrayList<>();
//        System.out.println(ID); // debug xem ID đúng chưa

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ID); // Gán tham số vào vị trí ?
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FunctionDTO func = new FunctionDTO(
                        rs.getString("ma_chuc_nang"),
                        rs.getString("ten_chuc_nang"),
                        rs.getString("ten_khong_dau"),
                        list
                );
                return func;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<FunctionDTO> getAllFunctionsByPer(String ID) {
        String query = "Select * FROM phan_quyen2 where ma_quyen = ?";
        ArrayList<FunctionDTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String IDchucNang = rs.getString("ma_chuc_nang");
//                System.out.println(IDchucNang);
                FunctionDTO funcs = getFunctionByID(IDchucNang);
                ArrayList<ActionDTO> acts = ActionDAO.getActionForFunction(ID, IDchucNang);
                FunctionDTO func = new FunctionDTO(
                        funcs.getID(),
                        funcs.getName(),
                        funcs.getUname(),
                        acts
                );
                boolean exists = false;
                for (FunctionDTO f : list) {
                    if (f.getID().equals(func.getID())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    list.add(func);
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Co loi");
            return null;
        }
    }

    public static ArrayList<FunctionDTO> getFunctionsAndActionsByPermission(String permissionID) {
        String query = "SELECT cn.ma_chuc_nang, cn.ten_chuc_nang, cn.ten_khong_dau, "
                + "hd.ma_hanh_dong, hd.ten_hanh_dong, hd.ten_khong_dau "
                + "FROM phan_quyen2 pq "
                + "JOIN chuc_nang2 cn ON pq.ma_chuc_nang = cn.ma_chuc_nang "
                + "JOIN hanh_dong hd ON pq.ma_hanh_dong = hd.ma_hanh_dong "
                + "WHERE pq.ma_quyen = ?";

        ArrayList<FunctionDTO> functionList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, permissionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String funcID = rs.getString("ma_chuc_nang");
                FunctionDTO existingFunc = null;

                // Kiểm tra xem chức năng đã tồn tại trong danh sách chưa
                for (FunctionDTO f : functionList) {
                    if (f.getID().equals(funcID)) {
                        existingFunc = f;
                        break;
                    }
                }

                // Nếu chưa có thì tạo mới
                if (existingFunc == null) {
                    existingFunc = new FunctionDTO(
                            funcID,
                            rs.getString("cn.ten_chuc_nang"),
                            rs.getString("cn.ten_khong_dau"),
                            new ArrayList<>()
                    );
                    functionList.add(existingFunc);
                }

                // Thêm hành động vào chức năng tương ứng
                ActionDTO action = new ActionDTO(
                        rs.getString("hd.ma_hanh_dong"),
                        rs.getString("hd.ten_hanh_dong"),
                        rs.getString("hd.ten_khong_dau")
                );
                existingFunc.getActions().add(action);
            }

            return functionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<FunctionDTO> getAllFunctions() {
        ArrayList<FunctionDTO> list = new ArrayList<>();
        String query = "SELECT * FROM chuc_nang2";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<ActionDTO> temp = new ArrayList<>();
            while (rs.next()) {
                FunctionDTO func = new FunctionDTO(
                        rs.getString("ma_chuc_nang"),
                        rs.getString("ten_chuc_nang"),
                        rs.getString("ten_khong_dau"),
                        temp
                );
                list.add(func);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
