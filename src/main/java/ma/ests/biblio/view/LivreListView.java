
package ma.ests.biblio.view;

import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import ma.ests.biblio.controller.LivreController; // on garde cette classe
import ma.ests.biblio.dao.LivreDAO; // ajout pour DAO
import ma.ests.biblio.model.Livre;
import java.util.List;

public class LivreListView {

    private LivreController controller; 
    private LivreDAO livreDAO;

    private VBox root;
    private Button btnAjouter;
    private Button btnModifier;
    private Button btnSupprimer;

    public LivreListView() {
        this.controller = new LivreController();
        this.livreDAO = new LivreDAO();

        root = new VBox();

        btnAjouter = new Button("Ajouter");
        btnModifier = new Button("Modifier");
        btnSupprimer = new Button("Supprimer");

        btnAjouter.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnModifier.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
        btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        root.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer);
    }

    public Scene getScene() {
        return new Scene(root, 600, 400);
    }

    public void refreshTable() {
        List<Livre> livres = livreDAO.findAll(); 
    }
}
