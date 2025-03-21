package DAO;

import Connection.DatabaseConnection;
import DTO.CustomerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    public static CustomerDTO getCustomer(String customerID) {
        String query = "SELECT * FROM customer WHERE customerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDTO(
                            rs.getString("CustomerID"),
                            rs.getString("FullName"),
                            rs.getString("Address"),
                            rs.getString("Phone")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<CustomerDTO> getAllCustomer() {
        ArrayList<CustomerDTO> customer = new ArrayList<>();
        String query = "SELECT * FROM customer";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customer.add(new CustomerDTO(
                        rs.getString("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("Phone")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public void updateCustomer(CustomerDTO customer) {
        String sql = "UPDATE customer SET FullName = ?, Address = ?, Phone = ? WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPhone());
            stmt.setString(5, customer.getcustomerID());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
