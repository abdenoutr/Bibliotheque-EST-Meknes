package ma.ests.biblio.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ma.ests.biblio.model.Adherent;
import ma.ests.biblio.service.AdherentService;

public class EmpruntFormView {

    private Stage stage;
    private Integer adherentId = null; 
    private AdherentService adherentService = new AdherentService();

    public EmpruntFormView(Stage owner) {
        this.stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Confirmer l'Emprunt - Identité de l'Adhérent");
    }

    public Integer showAndGetAdherentId() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);

        Label lblTitle = new Label("Veuillez entrer vos informations pour confirmer l'emprunt :");
        lblTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblNom = new Label("Nom complet :");
        TextField tfNom = new TextField();
        tfNom.setPromptText("Ex: Ahmed Benali");

        Label lblEmail = new Label("Email :");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Ex: ahmed@example.com");

        Label lblResult = new Label("");
        lblResult.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        Button btnVerifier = new Button("Vérifier et Emprunter");
        Button btnAnnuler = new Button("Annuler");

        btnVerifier.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        btnAnnuler.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        btnVerifier.setOnAction(e -> {
            String nom = tfNom.getText().trim();
            String email = tfEmail.getText().trim().toLowerCase();

            if (nom.isEmpty() || email.isEmpty()) {
                showAlert("Veuillez remplir le nom et l'email.");
                return;
            }

            Adherent adherent = adherentService.findAll().stream()
                    .filter(a -> a.getNom().equalsIgnoreCase(nom) && a.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);

            if (adherent == null) {
                lblResult.setText("Adhérent introuvable !");
                lblResult.setStyle("-fx-text-fill: red;");
                return;
            }

            if (adherent.isBloque()) {
                lblResult.setText("Adhérent bloqué ! Emprunt impossible.");
                lblResult.setStyle("-fx-text-fill: red;");
                return;
            }

            lblResult.setText("Adhérent trouvé : " + adherent.getNom() + " (ID: " + adherent.getId() + ")");
            lblResult.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

            adherentId = adherent.getId();

            PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(1));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        });

        btnAnnuler.setOnAction(e -> stage.close());

        grid.add(lblTitle, 0, 0, 2, 1);
        grid.add(lblNom, 0, 1);
        grid.add(tfNom, 1, 1);
        grid.add(lblEmail, 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(lblResult, 0, 3, 2, 1);

        HBox buttons = new HBox(15, btnVerifier, btnAnnuler);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        grid.add(buttons, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 500, 300);
        stage.setScene(scene);
        stage.showAndWait();

        return adherentId; 
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}