package ma.ests.biblio.controller;

import javafx.scene.control.*;
import javafx.stage.Stage;
import ma.ests.biblio.dao.LivreDAO;
import ma.ests.biblio.model.Livre;
import ma.ests.biblio.service.EmpruntService;
import ma.ests.biblio.view.DashboardView;
import ma.ests.biblio.view.EmpruntFormView;
import ma.ests.biblio.view.LivreForm;

import java.util.List;

public class DashboardController {

    private DashboardView view;
    private LivreDAO livreDAO = new LivreDAO();
    private EmpruntService empruntService = new EmpruntService();

    public DashboardController(DashboardView view) {
        this.view = view;
    }

    public List<Livre> chargerLivres() {
        return livreDAO.findAll();
    }

    public void handleAjouterLivre() {
        Stage s = new Stage();
        new LivreForm(s, null).showAndWait();
        view.refreshTable();
    }

    public void handleModifierLivre(Livre livre) {
        Stage s = new Stage();
        new LivreForm(s, livre).showAndWait();
        view.refreshTable();
    }
    public void handleSupprimerLivre(Livre livre) {

        if (livreDAO.isLivreEmprunte(livre.getIsbn())) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Impossible de supprimer un livre déjà emprunté.");
            a.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer ce livre ?", ButtonType.OK, ButtonType.CANCEL);

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            livreDAO.delete(livre.getIsbn());
            view.refreshTable();
        }
    }

    public void handleEmprunt(Livre livre) {
        EmpruntFormView form = new EmpruntFormView(view.getStage());
        Integer adherentId = form.showAndGetAdherentId();

        if (adherentId != null) {
            String res = empruntService.emprunter(adherentId, livre.getIsbn());
            alert(res);
            view.refreshTable();
        } else {
            alert("Emprunt annulé.");
        }
    }
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }
}
