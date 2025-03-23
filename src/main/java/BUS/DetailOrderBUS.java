package BUS;

import DAO.DetailOrderDAO;
import DTO.DetailOrderDTO;
import java.util.List;

public class DetailOrderBUS {

    public List<DetailOrderDTO> getAllDetailOrder() {
        return DetailOrderDAO.getAllDetailOrder();
    }

    public void updateDetailOrder(DetailOrderDTO detailorder) {
        DetailOrderDAO dao = new DetailOrderDAO();
        dao.updateDetailOrder(detailorder);
    }

}