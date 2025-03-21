package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import java.util.ArrayList;

public class ImportBUS {
    private ImportDAO importDAO;

    public ImportBUS() {
        importDAO = new ImportDAO();
    }

    public ImportDTO getImportByID(String importID) {
        return importDAO.getImport(importID);
    }

    public ArrayList<ImportDTO> getAllImports() {
        return importDAO.getAllImport();
    }

    public void updateImport(ImportDTO importDTO) {
        importDAO.updateImport(importDTO);
    }
}