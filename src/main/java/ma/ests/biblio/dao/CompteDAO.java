package ma.ests.biblio.dao;


import ma.ests.biblio.model.CompteUtilisateur;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;

public class CompteDAO {

    public CompteUtilisateur findByLogin(String login) {
        String sql = "SELECT * FROM compte_utilisateur WHERE login = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CompteUtilisateur c = new CompteUtilisateur();
                c.setLogin(rs.getString("login"));
                c.setMotDePasseHash(rs.getString("mot_de_passe"));
                c.setRole(rs.getString("role"));
                c.setActif(rs.getBoolean("actif"));
                return c;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
