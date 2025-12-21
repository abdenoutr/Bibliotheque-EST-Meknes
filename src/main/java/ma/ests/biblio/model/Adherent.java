package ma.ests.biblio.model;

public class Adherent {

    private int id;         
    private String nom;
    private String email;   
    private boolean bloque;

    public Adherent() {
        this.bloque = false;
    }

    public Adherent(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.bloque = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBloque() {
        return bloque;
    }

    public void setBloque(boolean bloque) {
        this.bloque = bloque;
    }
}
