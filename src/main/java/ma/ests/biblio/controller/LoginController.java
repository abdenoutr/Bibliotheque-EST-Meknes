package ma.ests.biblio.controller;

import ma.ests.biblio.model.CompteUtilisateur;
import ma.ests.biblio.service.LoginService;
import ma.ests.biblio.view.DashboardView; // (Sera créé juste après)
import ma.ests.biblio.view.LoginView;
import javafx.stage.Stage;

public class LoginController {

    private LoginView view;
    private LoginService service;

    public LoginController(LoginView view) {
        this.view = view;
        this.service = new LoginService();
    }

    public void handleLogin() {
        String login = view.getLogin();
        String pass = view.getPassword();

        if (login.isEmpty() || pass.isEmpty()) {
            view.setError("Veuillez remplir tous les champs.");
            return;
        }

        CompteUtilisateur user = service.login(login, pass);

        if (user != null) {
            System.out.println("Connexion réussie : " + user.getRole());
            view.close();
            
            new DashboardView(new Stage(), user).show(); 
            
        } else {
            view.setError("Identifiants incorrects.");
        }
    }
}