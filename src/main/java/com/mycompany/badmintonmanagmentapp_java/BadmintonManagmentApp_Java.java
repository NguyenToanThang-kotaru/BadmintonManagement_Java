/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.badmintonmanagmentapp_java;

import com.mycompany.DAO.DatabaseConnection;

/**
 *
 * @author ADMIN
 */
public class BadmintonManagmentApp_Java {

    public static void main(String[] args) {
        // Thử kết nối database
        DatabaseConnection.getConnection();
        
        // Đóng kết nối sau khi chạy xong
        DatabaseConnection.closeConnection();
    }
}
