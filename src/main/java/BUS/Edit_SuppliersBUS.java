package BUS;

import DAO.Edit_SuppliersDAO;
import DTO.SuppliersDTO;

public class Edit_SuppliersBUS {
    
    private Edit_SuppliersDAO editSuppliersDAO;

    public Edit_SuppliersBUS() {
        editSuppliersDAO = new Edit_SuppliersDAO();
    }

    public void updateSupplier(SuppliersDTO supplier) {
        editSuppliersDAO.updateSupplier(supplier);
    }
}