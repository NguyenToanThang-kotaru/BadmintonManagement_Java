package BUS;

import DAO.OrderDAO;
import DAO.CustomerDAO;
import DTO.OrderDTO;

import java.util.List;

public class OrderBUS {

    public List<OrderDTO> getAllOrder() {
        return OrderDAO.getAllOrder();
    }

    public void updateOrder(OrderDTO order) {
        OrderDAO dao = new OrderDAO();
    }
    
    private CustomerDAO customerDAO;

    public OrderBUS() {
        customerDAO = new CustomerDAO();
    }

    public String getCustomerNameByID(String customerID) {
        return customerDAO.getCustomerNameByID(customerID);
    }
    
    public boolean deleteOrder(String orderID) {
        OrderDAO dao = new OrderDAO();
        return dao.deleteOrder(orderID);
    }
    
    public void addOrder(OrderDTO order) {
        OrderDAO dao = new OrderDAO();
        dao.insertOrder(order); // bạn tự tạo insertOrder trong OrderDAO
    }
    
    public String getNextOrderID() {
        return new OrderDAO().getNextOrderID();
    }
    
    public List<OrderDTO> searchOrder(String keyword) {
        return OrderDAO.searchOrder(keyword);
    }
}