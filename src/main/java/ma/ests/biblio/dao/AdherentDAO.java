package ma.ests.biblio.dao;
import ma.ests.biblio.model.Adherent;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {

    public Adherent findById(int id) {
        String sql = "SELECT * FROM adherent WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Adherent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Adherent findByEmail(String email) {
        String sql = "SELECT * FROM adherent WHERE email = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Adherent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Adherent> findAll() {
        List<Adherent> liste = new ArrayList<>();
        String sql = "SELECT * FROM adherent";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Adherent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }


    public void save(Adherent a) {
        String sql = "INSERT INTO adherent (nom, email, bloque) VALUES (?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getEmail());
            ps.setBoolean(3, a.isBloque());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) a.setId(rs.getInt(1));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Adherent a) {
        String sql = "UPDATE adherent SET nom = ?, email = ?, bloque = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getEmail());
            ps.setBoolean(3, a.isBloque());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM adherent WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean hasEmpruntsActifs(int id) {
        String sql = "SELECT COUNT(*) FROM emprunt WHERE adherent_id = ? AND date_retour_reelle IS NULL";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public void updateBloque(int id, boolean bloque) {
        String sql = "UPDATE adherent SET bloque = ? WHERE id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, bloque);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
