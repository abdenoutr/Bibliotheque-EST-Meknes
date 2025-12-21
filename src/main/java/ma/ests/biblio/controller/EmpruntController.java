package ma.ests.biblio.controller;

import ma.ests.biblio.model.Emprunt;
import ma.ests.biblio.service.EmpruntService;

import java.util.List;

public class EmpruntController {

    private EmpruntService service = new EmpruntService();

    public List<Emprunt> getAllEmprunts() {
        return service.getAllEmprunts();
    }

    public String retournerEmprunt(int empruntId, String isbn, int adherentId) {
        return service.retourner(empruntId, isbn, adherentId);
    }
}
