package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import java.util.ArrayList;
import java.util.List;

public class CustomerBUS {
    private List<CustomerDTO> customerlist;

    public CustomerBUS() {
        this.customerlist = new ArrayList<>();
    }

    public List<CustomerDTO> getAllCustomer() {
        return new ArrayList<>(customerlist);
    }

    public void updateCustomer(CustomerDTO customer) {
        for (int i = 0; i < customerlist.size(); i++) {
            if (customerlist.get(i).getcustomerID().equals(customer.getcustomerID())) {
                customerlist.set(i, customer);
                return;
            }
        }
    }

    public void setEmployees(List<CustomerDTO> customer) {
        this.customerlist = new ArrayList<>(customer);
    }
}
