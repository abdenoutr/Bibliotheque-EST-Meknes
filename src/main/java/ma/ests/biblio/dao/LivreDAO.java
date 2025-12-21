


package ma.ests.biblio.dao;

import ma.ests.biblio.model.Categorie;
import ma.ests.biblio.model.Livre;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    public List<Livre> findAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre";

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(new Livre(
                        rs.getString("isbn"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("exemplaires_disponibles"),
                        new Categorie(rs.getInt("categorie_id"), "")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }

    public Livre findByIsbn(String isbn) {
        String sql = "SELECT * FROM livre WHERE isbn = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Livre(
                        rs.getString("isbn"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("exemplaires_disponibles"),
                        new Categorie(rs.getInt("categorie_id"), "")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean save(Livre livre) {
        String sql = "INSERT INTO livre (isbn, titre, auteur, exemplaires_disponibles, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, livre.getIsbn());
            ps.setString(2, livre.getTitre());
            ps.setString(3, livre.getAuteur());
            ps.setInt(4, livre.getExemplairesDisponibles());
            ps.setInt(5, livre.getCategorie().getId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Livre livre) {
        String sql = "UPDATE livre SET titre = ?, auteur = ?, exemplaires_disponibles = ?, categorie_id = ? WHERE isbn = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setInt(3, livre.getExemplairesDisponibles());
            ps.setInt(4, livre.getCategorie().getId());
            ps.setString(5, livre.getIsbn());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateStock(String isbn, int nouveauStock) {
        String sql = "UPDATE livre SET exemplaires_disponibles = ? WHERE isbn = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, nouveauStock);
            ps.setString(2, isbn);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String isbn) {
        String sql = "DELETE FROM livre WHERE isbn = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Categorie(rs.getInt("id"), rs.getString("nom")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public String generateIsbn() {
        return "ISBN" + System.currentTimeMillis();
    }
}
