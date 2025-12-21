package ma.ests.biblio.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.ests.biblio.controller.LoginController;

public class LoginView {

    private Stage stage;
    private TextField txtLogin;
    private PasswordField txtPassword;
    private Label lblError;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label lblTitle = new Label("Bibliothèque EST Meknès");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        txtLogin = new TextField();
        txtLogin.setPromptText("Nom d'utilisateur");
        txtLogin.setMaxWidth(300);

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Mot de passe");
        txtPassword.setMaxWidth(300);

        Button btnConnect = new Button("Se connecter");
        btnConnect.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConnect.setMaxWidth(300);

        lblError = new Label();
        lblError.setTextFill(Color.RED);

        VBox root = new VBox(15); 
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(lblTitle, txtLogin, txtPassword, btnConnect, lblError);

        LoginController controller = new LoginController(this);
        btnConnect.setOnAction(event -> controller.handleLogin());

        Scene scene = new Scene(root, 400, 350);
        stage.setTitle("Authentification");
        stage.setScene(scene);
        stage.show();
    }

    public String getLogin() { return txtLogin.getText(); }
    public String getPassword() { return txtPassword.getText(); }
    public void setError(String msg) { lblError.setText(msg); }
    public void close() { stage.close(); }
}