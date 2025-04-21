package BUS;

import DAO.DetailOrderDAO;
import DTO.DetailOrderDTO;

import java.util.List;
import java.util.ArrayList;

public class DetailOrderBUS {
    
    private DetailOrderDAO dao = new DetailOrderDAO();
    
    public List<DetailOrderDTO> getAllDetailOrder() {
        return DetailOrderDAO.getAllDetailOrder();
    }

    public void updateDetailOrder(DetailOrderDTO detail) {
        dao.updateDetailOrder(detail);
    }

    public static ArrayList<DetailOrderDTO> getDetailOrderByOrderID(String orderID) {
        return DetailOrderDAO.getDetailOrderByOrderID(orderID);
    }
    
    public void addDetailOrder(DetailOrderDTO detail) {
        DetailOrderDAO.insertDetailOrder(detail);
    }
    
    public void deleteByOrderID(String orderID) {
        DetailOrderDAO.deleteDetailOrder(orderID);
    }
    
    public int getMaxDetailOrderNumber() {
        return DetailOrderDAO.getMaxDetailOrderNumber();
    }
    
    public static boolean deleteDetailOrderByID(String detailOrderID) {
        return DetailOrderDAO.deleteDetailOrderByID(detailOrderID);
    }
}