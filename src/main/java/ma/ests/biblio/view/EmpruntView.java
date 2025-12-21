package ma.ests.biblio.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ests.biblio.controller.EmpruntController;
import ma.ests.biblio.model.CompteUtilisateur;
import ma.ests.biblio.model.Emprunt;
import ma.ests.biblio.service.LogService;

import java.util.List;

public class EmpruntView {

    private Stage stage;
    private TableView<Emprunt> tableEmprunts;
    private EmpruntController controller;
    private CompteUtilisateur currentUser;
    private LogService logService = new LogService();

    public EmpruntView(Stage stage, CompteUtilisateur currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.controller = new EmpruntController();
    }

    public void refreshTable() {
        List<Emprunt> emprunts = controller.getAllEmprunts();
        tableEmprunts.setItems(FXCollections.observableArrayList(emprunts));
    }

    public void show() {
        BorderPane root = new BorderPane();
        tableEmprunts = new TableView<>();

        TableColumn<Emprunt, String> colLivre = new TableColumn<>("Livre");
        colLivre.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getLivre() != null
                                ? c.getValue().getLivre().getTitre()
                                : ""
                )
        );

        TableColumn<Emprunt, String> colAdherent = new TableColumn<>("Adhérent");
        colAdherent.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getAdherent() != null
                                ? c.getValue().getAdherent().getNom()
                                : ""
                )
        );

        TableColumn<Emprunt, String> colDateEmp = new TableColumn<>("Date Emprunt");
        colDateEmp.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateEmprunt().toString())
        );

        TableColumn<Emprunt, String> colDatePrev = new TableColumn<>("Retour Prévu");
        colDatePrev.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateRetourPrevue().toString())
        );

        TableColumn<Emprunt, String> colDateRet = new TableColumn<>("Retour Réel");
        colDateRet.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDateRetour() != null
                                ? c.getValue().getDateRetour().toString()
                                : "Non retourné"
                )
        );

        TableColumn<Emprunt, Boolean> colRetard = new TableColumn<>("Retard");
        colRetard.setCellValueFactory(c ->
                new SimpleBooleanProperty(c.getValue().isEnRetard())
        );

        TableColumn<Emprunt, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(p -> new TableCell<>() {
            private final Button btn = new Button("Retourner");

            {
                btn.setOnAction(e -> {
                    Emprunt emp = getTableView().getItems().get(getIndex());
                    controller.retournerEmprunt(
                            emp.getId(),
                            emp.getLivre().getIsbn(),
                            emp.getAdherent().getId()
                    );
                    logService.enregistrer("Retour emprunt ID=" + emp.getId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getDateRetour() != null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        tableEmprunts.getColumns().addAll(
                colLivre, colAdherent, colDateEmp,
                colDatePrev, colDateRet, colRetard, colAction
        );

        VBox box = new VBox(10, new Label("Liste des Emprunts"), tableEmprunts);
        box.setPadding(new Insets(15));
        root.setCenter(box);

        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Gestion des Emprunts");
        stage.show();

        refreshTable();
    }
}
