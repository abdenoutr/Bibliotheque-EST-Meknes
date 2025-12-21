
package ma.ests.biblio.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ma.ests.biblio.dao.CategorieDAO;
import ma.ests.biblio.dao.LivreDAO;
import ma.ests.biblio.model.Categorie;
import ma.ests.biblio.model.Livre;

import java.util.List;

public class LivreForm {

    private Stage stage;
    private LivreDAO livreDAO = new LivreDAO();
    private CategorieDAO categorieDAO = new CategorieDAO();

    public LivreForm(Stage stage, Livre livre) {
        this.stage = stage;

        TextField tfIsbn = new TextField();
        tfIsbn.setDisable(true); 

        TextField tfTitre = new TextField();
        TextField tfAuteur = new TextField();
        TextField tfStock = new TextField();

        List<Categorie> categories = categorieDAO.findAll();
        ComboBox<Categorie> cbCategorie = new ComboBox<>();
        cbCategorie.setItems(FXCollections.observableArrayList(categories));
        cbCategorie.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Categorie object) {
                return object == null ? "" : object.getNom();
            }

            @Override
            public Categorie fromString(String string) {
                return categories.stream()
                        .filter(c -> c.getNom().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        if (livre != null) {
            tfIsbn.setText(livre.getIsbn());
            tfTitre.setText(livre.getTitre());
            tfAuteur.setText(livre.getAuteur());
            tfStock.setText(String.valueOf(livre.getExemplairesDisponibles()));

            for (Categorie c : categories) {
                if (livre.getCategorie() != null && c.getId() == livre.getCategorie().getId()) {
                    cbCategorie.setValue(c);
                    break;
                }
            }
        }

        Button btnSave = new Button("Enregistrer");
        btnSave.setStyle("-fx-background-color:#4CAF50; -fx-text-fill:white;");

        btnSave.setOnAction(e -> {
            try {
                String isbn = (livre == null) ? livreDAO.generateIsbn() : tfIsbn.getText();

                Categorie selectedCategorie = cbCategorie.getValue();
                if (selectedCategorie == null) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Veuillez sélectionner une catégorie !");
                    a.show();
                    return;
                }

                Livre l = new Livre(
                        isbn,
                        tfTitre.getText(),
                        tfAuteur.getText(),
                        Integer.parseInt(tfStock.getText()),
                        selectedCategorie
                );

                if (livre == null) livreDAO.save(l);
                else livreDAO.update(l);

                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Données invalides !");
                a.show();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        int row = 0;
        if (livre != null) {
            grid.addRow(row++, new Label("ISBN"), tfIsbn);
        }
        grid.addRow(row++, new Label("Titre"), tfTitre);
        grid.addRow(row++, new Label("Auteur"), tfAuteur);
        grid.addRow(row++, new Label("Disponibles"), tfStock);
        grid.addRow(row++, new Label("Catégorie"), cbCategorie);
        grid.add(btnSave, 1, row);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(grid, 400, 300));
        stage.setTitle(livre == null ? "Ajouter Livre" : "Modifier Livre");
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
