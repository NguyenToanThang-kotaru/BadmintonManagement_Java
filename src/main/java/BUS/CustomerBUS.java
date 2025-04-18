package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;

import java.util.List;

public class CustomerBUS {

    public List<CustomerDTO> getAllCustomer() {
        return CustomerDAO.getAllCustomer();
    }

    public void updateCustomer(CustomerDTO customer) {
        CustomerDAO dao = new CustomerDAO();
        dao.updateCustomer(customer);
    }

    public CustomerDTO getCustomerByPhone(String phone) {
        return CustomerDAO.getCustomerByPhone(phone);
    }
    
    public String getNextCustomerID() {
        return CustomerDAO.getNextCustomerID();
    }
    
    public CustomerDTO getCustomerByID(String id) {
        return CustomerDAO.getCustomer(id);
    }
    
    public void addCustomer(CustomerDTO customer) {
        CustomerDAO.addCustomer(customer);
    }
    
    public boolean deleteCustomer(String orderID) {
        CustomerDAO customer = new CustomerDAO();
        return customer.deleteCustomer(orderID);
    }
    
    public List<CustomerDTO> searchCustomer(String keyword) {
        return CustomerDAO.searchCustomer(keyword);
    }
}