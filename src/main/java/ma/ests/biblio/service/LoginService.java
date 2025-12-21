package ma.ests.biblio.service;

import ma.ests.biblio.dao.CompteDAO;
import ma.ests.biblio.model.CompteUtilisateur;
import ma.ests.biblio.util.HashUtil;

public class LoginService {

    private CompteDAO compteDAO = new CompteDAO();

    public CompteUtilisateur login(String username, String password) {
        CompteUtilisateur user = compteDAO.findByLogin(username);

        if (user != null && user.isActif()) {
            if (HashUtil.checkPassword(password, user.getMotDePasseHash())) {
                return user; 
            }
        }
        return null; 
    }
}