package ma.ests.biblio.model;

public class CompteUtilisateur {

    private String login;          
    private String motDePasseHash;    
    private String role;           
    private boolean actif;         

    public CompteUtilisateur() {
        this.actif = true;
    }

    public CompteUtilisateur(String login, String motDePasseHash, String role) {
        this.login = login;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.actif = true;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isUser() {
        return "USER".equalsIgnoreCase(role);
    }
}