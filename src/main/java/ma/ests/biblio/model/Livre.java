package ma.ests.biblio.model;

public class Livre {

    private String isbn;               
    private String titre;
    private String auteur;
    private int exemplairesDisponibles;
    private Categorie categorie;

    public Livre() {
    }

    public Livre(String isbn, String titre, String auteur,
                 int exemplairesDisponibles, Categorie categorie) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.exemplairesDisponibles = exemplairesDisponibles;
        this.categorie = categorie;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }

    public void setExemplairesDisponibles(int exemplairesDisponibles) {
        this.exemplairesDisponibles = exemplairesDisponibles;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}