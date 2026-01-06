package DAO;

import java.sql.*;

public class GestionnaireDAO {

    public boolean authenticate(String login, String password) {
        String sql = "SELECT 1 FROM Gestionnaire WHERE Login=? AND MotDePasse=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();  // si une ligne existe → login OK

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
