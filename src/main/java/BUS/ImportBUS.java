package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import java.util.List;

public class ImportBUS {

    public List<ImportDTO> getAllImport() {
        return ImportDAO.getAllImport();
    }

    public void updateCustomer(ImportDTO Import) {
        ImportDAO dao = new ImportDAO();
        dao.updateImport(Import);
    }

}