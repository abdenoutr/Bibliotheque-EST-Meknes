package ma.ests.biblio.dao;

import ma.ests.biblio.model.Categorie;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public List<Categorie> findAll() {
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

    public Categorie findById(int id) {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Categorie findByName(String nom) {
        String sql = "SELECT * FROM categorie WHERE nom = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
