
package ma.ests.biblio.dao;

import ma.ests.biblio.model.Livre;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    private Connection connection;

    public LivreDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean save(Livre livre) {
        String sql = "INSERT INTO livre (isbn, titre, auteur, exemplaires_disponibles, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getIsbn());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getExemplairesDisponibles());
            stmt.setInt(5, livre.getCategorie().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Livre livre) {
        String sql = "UPDATE livre SET titre=?, auteur=?, exemplaires_disponibles=?, categorie_id=? WHERE isbn=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getExemplairesDisponibles());
            stmt.setInt(4, livre.getCategorie().getId());
            stmt.setString(5, livre.getIsbn());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String isbn) {
        String sql = "DELETE FROM livre WHERE isbn=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Livre> findAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre l = new Livre(
                        rs.getString("isbn"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("exemplaires_disponibles"),
                        null
                );
                livres.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    public Livre findByIsbn(String isbn) {
        String sql = "SELECT * FROM livre WHERE isbn=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Livre(
                        rs.getString("isbn"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("exemplaires_disponibles"),
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStock(String isbn, int stock) {
        String sql = "UPDATE livre SET exemplaires_disponibles=? WHERE isbn=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stock);
            stmt.setString(2, isbn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateIsbn() {
        String sql = "SELECT COUNT(*) AS count FROM livre";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt("count") + 1;
                return "ISBN" + String.format("%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ISBN0001";
    }


    public boolean isLivreEmprunte(String isbn) {
        String sql = "SELECT COUNT(*) AS count FROM emprunt WHERE livre_isbn=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
