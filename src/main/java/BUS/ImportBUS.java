package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import java.util.List;

public class ImportBUS {

    
    public List<ImportDTO> getAllImport() {
        return ImportDAO.getAllImport(); 
    }
   
    public void updateImport(ImportDTO importDTO) {
        ImportDAO dao = new ImportDAO(); 
               dao.updateImport(importDTO); 
    }


    public boolean deleteImport(String importID) {
        ImportDAO dao = new ImportDAO(); 
        return dao.deleteImport(importID);
    }
}
