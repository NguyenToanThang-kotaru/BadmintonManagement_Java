package BUS;

import DAO.DetailImportDAO;
import DTO.DetailImportDTO;
import java.util.List;

public class DetailImportBUS {

    public List<DetailImportDTO> getAllDetailImport() {
        return DetailImportDAO.getAllDetailImport();
    }

    public void updateDetailImport(DetailImportDTO detailimport) {
        DetailImportDAO dao = new DetailImportDAO();
        dao.updateDetailImport(detailimport);
    }

}