package ma.ests.biblio.service;

import ma.ests.biblio.dao.LivreDAO;
import ma.ests.biblio.model.Livre;
import java.util.List;

public class LivreService {

    private LivreDAO dao = new LivreDAO();

    public List<Livre> findAll() {
        return dao.findAll();
    }

    public Livre findByIsbn(String isbn) {
        return dao.findByIsbn(isbn);
    }
}
