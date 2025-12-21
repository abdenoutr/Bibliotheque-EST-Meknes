package ma.ests.biblio;

import javafx.application.Application;
import javafx.stage.Stage;
import ma.ests.biblio.view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginScreen = new LoginView(primaryStage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
