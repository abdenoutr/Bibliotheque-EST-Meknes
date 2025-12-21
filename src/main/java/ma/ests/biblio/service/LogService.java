package ma.ests.biblio.service;

import ma.ests.biblio.dao.LogDAO;
import ma.ests.biblio.model.Log;
import java.util.List;

public class LogService {

    private LogDAO dao = new LogDAO();

    public void enregistrer(String action) {
        dao.save(new Log(action));
    }

    public List<Log> getAll() {
        return dao.findAll();
    }
}
