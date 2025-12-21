package ma.ests.biblio.service;

import ma.ests.biblio.dao.AdherentDAO;
import ma.ests.biblio.model.Adherent;
import java.util.List;

public class AdherentService {

    private AdherentDAO dao = new AdherentDAO();

    public List<Adherent> findAll() {
        return dao.findAll();
    }

    public void save(Adherent adherent) {
        dao.save(adherent);
    }

    public void update(Adherent adherent) {
        dao.update(adherent);
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
