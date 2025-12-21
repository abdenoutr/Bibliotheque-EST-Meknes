package ma.ests.biblio.dao;

import ma.ests.biblio.model.Adherent;
import ma.ests.biblio.model.Emprunt;
import ma.ests.biblio.model.Livre;
import ma.ests.biblio.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public void save(Emprunt e) {

        String sql = """
            INSERT INTO emprunt (livre_isbn, adherent_id, date_emprunt, date_retour_prevue)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, e.getLivre().getIsbn());
            ps.setInt(2, e.getAdherent().getId());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, Date.valueOf(e.getDateRetourPrevue()));

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public int countEmpruntsEnCours(int adherentId) {

        String sql = """
            SELECT COUNT(*) 
            FROM emprunt
            WHERE adherent_id = ? AND date_retour_reelle IS NULL
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, adherentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }


    public void updateDateRetour(int empruntId, LocalDate dateRetour) {

        String sql = """
            UPDATE emprunt
            SET date_retour_reelle = ?
            WHERE id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dateRetour));
            ps.setInt(2, empruntId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public Emprunt findById(int id) {

        String sql = """
            SELECT e.*, 
                   l.isbn, l.titre, l.auteur,
                   a.id AS aid, a.nom
            FROM emprunt e
            JOIN livre l ON e.livre_isbn = l.isbn
            JOIN adherent a ON e.adherent_id = a.id
            WHERE e.id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Livre livre = new Livre();
                livre.setIsbn(rs.getString("isbn"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));

                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("aid"));
                adherent.setNom(rs.getString("nom"));

                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setLivre(livre);
                e.setAdherent(adherent);
                e.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());

                Date dr = rs.getDate("date_retour_reelle");
                if (dr != null)
                    e.setDateRetour(dr.toLocalDate());

                return e;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<Emprunt> findAll() {

        List<Emprunt> list = new ArrayList<>();

        String sql = """
            SELECT e.*, 
                   l.isbn, l.titre, l.auteur,
                   a.id AS aid, a.nom
            FROM emprunt e
            JOIN livre l ON e.livre_isbn = l.isbn
            JOIN adherent a ON e.adherent_id = a.id
            ORDER BY e.id DESC
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Livre livre = new Livre();
                livre.setIsbn(rs.getString("isbn"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));

                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("aid"));
                adherent.setNom(rs.getString("nom"));

                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setLivre(livre);
                e.setAdherent(adherent);
                e.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());

                Date dr = rs.getDate("date_retour_reelle");
                if (dr != null)
                    e.setDateRetour(dr.toLocalDate());

                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
