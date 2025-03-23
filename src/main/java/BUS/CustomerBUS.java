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

}