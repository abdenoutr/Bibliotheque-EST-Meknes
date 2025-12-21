package ma.ests.biblio.controller;

import ma.ests.biblio.dao.AdherentDAO;
import ma.ests.biblio.model.Adherent;

import java.util.List;

public class AdherentController {

    private AdherentDAO adherentDAO;

    public AdherentController() {
        this.adherentDAO = new AdherentDAO();
    }

    public List<Adherent> findAll() {
        return adherentDAO.findAll();
    }

    public String save(String nom, String email) {
        if (adherentDAO.findByEmail(email) != null) {
            return "Erreur : Email déjà utilisé !";
        }
        Adherent a = new Adherent();
        a.setNom(nom);
        a.setEmail(email);
        adherentDAO.save(a);
        return "Adhérent ajouté avec succès !";
    }

    public Adherent findById(int id) {
        return adherentDAO.findById(id);
    }

    public String update(int id, String nom, String email, boolean bloque) {
        Adherent a = adherentDAO.findById(id);
        if (a == null) return "Erreur : Adhérent introuvable !";

        Adherent exist = adherentDAO.findByEmail(email);
        if (exist != null && exist.getId() != id) {
            return "Erreur : Email déjà utilisé par un autre adhérent !";
        }

        a.setNom(nom);
        a.setEmail(email);
        a.setBloque(bloque);
        adherentDAO.update(a);
        return "Adhérent mis à jour avec succès !";
    }

    public boolean hasEmpruntsActifs(int id) {
        return adherentDAO.hasEmpruntsActifs(id);
    }

    public String delete(int id) {
        if (hasEmpruntsActifs(id)) {
            return "Impossible de supprimer : emprunts actifs !";
        }
        adherentDAO.delete(id);
        return "Adhérent supprimé avec succès !";
    }
}
