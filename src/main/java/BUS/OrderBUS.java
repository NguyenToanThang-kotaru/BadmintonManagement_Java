package BUS;

import DAO.OrderDAO;
import DAO.CustomerDAO;
import DTO.OrderDTO;

import java.util.List;

public class OrderBUS {

    private OrderDAO dao = new OrderDAO();
    private CustomerDAO customer = new CustomerDAO();
    
    public List<OrderDTO> getAllOrder() {
        return dao.getAllOrder();
    }

    public void updateOrder(OrderDTO order) {
        dao.updateOrder(order);
        dao.updateTotalProfit(order.getorderID());
    }

    public String getCustomerNameByID(String customerID) {
        return customer.getCustomerNameByID(customerID);
    }
    
    public boolean deleteOrder(String orderID) {
        return dao.deleteOrder(orderID);
    }
    
    public void addOrder(OrderDTO order) {
        if (order.gettotalprofit() == null || order.gettotalprofit().trim().isEmpty()) {
            order.settotalprofit("0");
        }
        dao.insertOrder(order);
    }
    
    public String getNextOrderID() {
        return new OrderDAO().getNextOrderID();
    }
    
    public List<OrderDTO> searchOrder(String keyword) {
        return OrderDAO.searchOrder(keyword);
    }
    
    public void getOrder(String mahd) {
        dao.getOrder(mahd);
    }
}