package ma.ests.biblio.dao;

import ma.ests.biblio.model.Log;
import ma.ests.biblio.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    public void save(Log log) {
        String sql = "INSERT INTO log(action, date_action) VALUES (?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, log.getAction());
            ps.setTimestamp(2, Timestamp.valueOf(log.getDateAction()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Log> findAll() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM log ORDER BY date_action DESC";

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setAction(rs.getString("action"));
                log.setDateAction(rs.getTimestamp("date_action").toLocalDateTime());
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
