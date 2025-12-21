package ma.ests.biblio.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.ests.biblio.controller.DashboardController;
import ma.ests.biblio.model.CompteUtilisateur;
import ma.ests.biblio.model.Livre;

public class DashboardView {

    private Stage stage;
    private CompteUtilisateur user;
    private TableView<Livre> table;
    private DashboardController controller;

    public DashboardView(Stage stage, CompteUtilisateur user) {
        this.stage = stage;
        this.user = user;
        this.controller = new DashboardController(this);
    }

    public void refreshTable() {
        table.setItems(FXCollections.observableArrayList(controller.chargerLivres()));
    }

    public void show() {
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();

        Tab tabLivres = new Tab("Livres");
        tabLivres.setClosable(false);
        VBox livresBox = new VBox(10);

        table = new TableView<>();
        TableColumn<Livre, String> c1 = new TableColumn<>("ISBN");
        c1.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Livre, String> c2 = new TableColumn<>("Titre");
        c2.setCellValueFactory(new PropertyValueFactory<>("titre"));

        TableColumn<Livre, String> c3 = new TableColumn<>("Auteur");
        c3.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        TableColumn<Livre, Integer> c4 = new TableColumn<>("Disponibles");
        c4.setCellValueFactory(new PropertyValueFactory<>("exemplairesDisponibles"));

        TableColumn<Livre, Void> c5 = new TableColumn<>("Actions");
        c5.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnEmprunt = new Button("Emprunter");

            {
                btnModifier.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                btnEmprunt.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    controller.handleModifierLivre(livre);
                });
                btnSupprimer.setOnAction(event -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    controller.handleSupprimerLivre(livre);
                });
                btnEmprunt.setOnAction(event -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    controller.handleEmprunt(livre);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5);
                    if ("ADMIN".equals(user.getRole())) {
                        box.getChildren().addAll(btnModifier, btnSupprimer);
                    } else {
                        box.getChildren().add(btnEmprunt);
                    }
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(c1, c2, c3, c4, c5);
        refreshTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        livresBox.getChildren().add(table);
        tabLivres.setContent(livresBox);

        Tab tabAdherents = new Tab("Adhérents");
        tabAdherents.setClosable(false);
        tabAdherents.setOnSelectionChanged(e -> {
            if ("ADMIN".equals(user.getRole()) && tabAdherents.isSelected()) {
                Stage stageAdh = new Stage();
                new AdherentView(stageAdh).show();
            } else if ("USER".equals(user.getRole())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Accès réservé aux admins.");
                alert.show();
            }
        });

        Tab tabEmprunts = new Tab("Emprunts");
        tabEmprunts.setClosable(false);
        tabEmprunts.setOnSelectionChanged(e -> {
            if ("ADMIN".equals(user.getRole()) && tabEmprunts.isSelected()) {
                Stage stageEmp = new Stage();
                new EmpruntView(stageEmp, user).show();
            } else if ("USER".equals(user.getRole())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Accès réservé aux admins.");
                alert.show();
            }
        });

        if ("ADMIN".equals(user.getRole())) {
            tabPane.getTabs().addAll(tabLivres, tabAdherents, tabEmprunts);
        } else {
            tabPane.getTabs().add(tabLivres); 
        }

        root.setCenter(tabPane);

        Button btnAjouter = new Button("➕ Ajouter");
        btnAjouter.setStyle("-fx-background-color:#2196F3; -fx-text-fill:white;");
        btnAjouter.setPrefWidth(150);
        btnAjouter.setOnAction(e -> controller.handleAjouterLivre());

        HBox top = new HBox();
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER);
        if ("ADMIN".equals(user.getRole())) top.getChildren().add(btnAjouter);

        root.setTop(top);

        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Dashboard");
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}
