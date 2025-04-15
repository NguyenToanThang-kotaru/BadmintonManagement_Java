package BUS;

import DAO.DetailOrderDAO;
import DTO.DetailOrderDTO;

import java.util.List;
import java.util.ArrayList;

public class DetailOrderBUS {

    public List<DetailOrderDTO> getAllDetailOrder() {
        return DetailOrderDAO.getAllDetailOrder();
    }

    public void updateDetailOrder(DetailOrderDTO detailorder) {
        DetailOrderDAO dao = new DetailOrderDAO();
        dao.updateDetailOrder(detailorder);
    }

    public static ArrayList<DetailOrderDTO> getDetailOrderByOrderID(String orderID) {
        return DetailOrderDAO.getDetailOrderByOrderID(orderID);
    }
    
    public void addDetailOrder(DetailOrderDTO detail) {
        DetailOrderDAO.insertDetailOrder(detail); // gọi hàm insert
    }
    
    public void deleteByOrderID(String orderID) {
        DetailOrderDAO.deleteDetailOrder(orderID);
    }
}