package BUS;

import DAO.OrderDAO;
import DTO.OrderDTO;
import java.util.List;

public class OrderBUS {

    public List<OrderDTO> getAllOrder() {
        return OrderDAO.getAllOrder();
    }

    public void updateOrder(OrderDTO order) {
        OrderDAO dao = new OrderDAO();
        dao.updateOrder(order);
    }

}