package ma.ests.biblio.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ma.ests.biblio.controller.AdherentController;
import ma.ests.biblio.model.Adherent;
import ma.ests.biblio.service.LogService;

import java.util.Optional;

public class AdherentView {

    private Stage stage;
    private TableView<Adherent> table;
    private AdherentController controller = new AdherentController();
    private LogService logService = new LogService();

    public AdherentView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        table = new TableView<>();
        TableColumn<Adherent, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Adherent, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Adherent, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Adherent, Boolean> colBloque = new TableColumn<>("Bloqué");
        colBloque.setCellValueFactory(new PropertyValueFactory<>("bloque"));

        table.getColumns().addAll(colId, colNom, colEmail, colBloque);
        root.setCenter(table);

        // Boutons
        Button btnAjouter = new Button("Ajouter");
        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRafraichir = new Button("Rafraîchir");

        btnAjouter.setOnAction(e -> ajouterAdherent());
        btnModifier.setOnAction(e -> modifierAdherent());
        btnSupprimer.setOnAction(e -> supprimerAdherent());
        btnRafraichir.setOnAction(e -> refreshTable());

        HBox buttons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRafraichir);
        buttons.setPadding(new Insets(10));
        root.setBottom(buttons);
        refreshTable();
        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Gestion des Adhérents");
        stage.show();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(controller.findAll()));
    }

    private void ajouterAdherent() {
        Dialog<Adherent> dialog = createAdherentDialog(null);
        Optional<Adherent> result = dialog.showAndWait();
        result.ifPresent(a -> {
            String msg = controller.save(a.getNom(), a.getEmail());
            showAlert(msg);
            logService.enregistrer("Ajout adhérent : " + a.getNom() + " (" + a.getEmail() + ")");
            refreshTable();
        });
    }
    private void modifierAdherent() {
        Adherent selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Veuillez sélectionner un adhérent.");
            return;
        }
        Dialog<Adherent> dialog = createAdherentDialog(selected);
        Optional<Adherent> result = dialog.showAndWait();
        result.ifPresent(a -> {
            String msg = controller.update(selected.getId(), a.getNom(), a.getEmail(), a.isBloque());
            showAlert(msg);
            logService.enregistrer("Modification adhérent ID=" + selected.getId() + " : " + a.getNom());
            refreshTable();
        });
    }

    private void supprimerAdherent() {
        Adherent selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Veuillez sélectionner un adhérent.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + selected.getNom() + " ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String msg = controller.delete(selected.getId());
                showAlert(msg);
                logService.enregistrer("Suppression adhérent ID=" + selected.getId() + " : " + selected.getNom());
                refreshTable();
            }
        });
    }

    private Dialog<Adherent> createAdherentDialog(Adherent adherent) {
        Dialog<Adherent> dialog = new Dialog<>();
        dialog.setTitle(adherent == null ? "Ajouter Adhérent" : "Modifier Adhérent");
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);

        TextField tfNom = new TextField(adherent != null ? adherent.getNom() : "");
        TextField tfEmail = new TextField(adherent != null ? adherent.getEmail() : "");
        CheckBox cbBloque = new CheckBox("Bloqué");
        cbBloque.setSelected(adherent != null && adherent.isBloque());

        VBox vbox = new VBox(10,
                new Label("Nom:"), tfNom,
                new Label("Email:"), tfEmail,
                cbBloque);
        vbox.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == okButton) {
                Adherent a = new Adherent();
                a.setNom(tfNom.getText());
                a.setEmail(tfEmail.getText());
                a.setBloque(cbBloque.isSelected());
                if (adherent != null) a.setId(adherent.getId());
                return a;
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}