package ma.ests.biblio.controller;

import ma.ests.biblio.dao.LivreDAO;
import ma.ests.biblio.model.Livre;

import java.util.List;

public class LivreController {

    private LivreDAO livreDAO;

    public LivreController() {
        this.livreDAO = new LivreDAO();
    }

    public List<Livre> findAll() {
        return livreDAO.findAll();
    }

    public void delete(String isbn) {
        Livre livre = livreDAO.findByIsbn(isbn);
        if (livre != null) {
            livreDAO.updateStock(isbn, 0);
        }
    }
}
