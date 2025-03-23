package BUS;

import DAO.SuppliersDAO;
import DTO.SuppliersDTO;
import java.util.List;

public class SuppliersBUS {

    public List<SuppliersDTO> getAllSuppliers() {
        return SuppliersDAO.getAllSuppliers();
    }

    public void updateSuppliers(SuppliersDTO suppliers) {
        SuppliersDAO dao = new SuppliersDAO();
        dao.updateSuppliers(suppliers);
    }

}