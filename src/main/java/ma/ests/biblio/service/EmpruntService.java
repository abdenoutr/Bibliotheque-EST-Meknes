package ma.ests.biblio.service;

import ma.ests.biblio.dao.AdherentDAO;
import ma.ests.biblio.dao.EmpruntDAO;
import ma.ests.biblio.dao.LivreDAO;
import ma.ests.biblio.model.Adherent;
import ma.ests.biblio.model.Emprunt;
import ma.ests.biblio.model.Livre;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmpruntService {

    private EmpruntDAO empruntDAO = new EmpruntDAO();
    private LivreDAO livreDAO = new LivreDAO();
    private AdherentDAO adherentDAO = new AdherentDAO();
    private LogService logService = new LogService();

    public String emprunter(int adherentId, String isbn) {
        Adherent a = adherentDAO.findById(adherentId);
        if (a == null) return "Adhérent inexistant.";
        if (a.isBloque()) return "Adhérent bloqué.";

        if (empruntDAO.countEmpruntsEnCours(adherentId) >= 3)
            return "Quota max atteint.";

        Livre l = livreDAO.findByIsbn(isbn);
        if (l == null) return "Livre inexistant.";
        if (l.getExemplairesDisponibles() <= 0) return "Livre indisponible.";

        Emprunt e = new Emprunt(0, l, a, LocalDate.now());
        empruntDAO.save(e);
        livreDAO.updateStock(isbn, l.getExemplairesDisponibles() - 1);

        logService.enregistrer("Emprunt réussi : Livre " + l.getTitre() + " (ISBN: " + isbn + ") par " + a.getNom() + " (ID: " + adherentId + ")");

        return "Emprunt réussi jusqu'au " + e.getDateRetourPrevue();
    }

    public String retourner(int empruntId, String isbn, int adherentId) {
        LocalDate dateRetour = LocalDate.now();
        empruntDAO.updateDateRetour(empruntId, dateRetour);

        Livre livre = livreDAO.findByIsbn(isbn);
        if (livre != null)
            livreDAO.updateStock(isbn, livre.getExemplairesDisponibles() + 1);

        Emprunt e = empruntDAO.findById(empruntId);
        if (e != null) {
            long retard = ChronoUnit.DAYS.between(e.getDateRetourPrevue(), dateRetour);
            logService.enregistrer("Retour emprunt ID=" + empruntId + " - Livre ISBN=" + isbn + (retard > 0 ? " avec " + retard + " jours de retard" : ""));
            if (retard > 10) {
                adherentDAO.updateBloque(adherentId, true);
                return "Retour avec " + retard + " jours de retard. Adhérent bloqué !";
            } else if (retard > 0) return "Retour avec retard : " + retard + " jours.";
        }

        return "Retour effectué avec succès.";
    }

    public List<Emprunt> getAllEmprunts() {
        return empruntDAO.findAll();
    }
}