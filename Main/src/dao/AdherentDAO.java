package sgeb.dao;


import sgeb.model.Adherent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {
    
    private List<Adherent> listeAdherents = new ArrayList<>();

    // Créer la table Adherent
    public void creerTableSiNonExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Adherent (
                id_adherent INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prenom TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                telephone TEXT
            );
        """;

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'Adherent' prête !");
        } catch (SQLException e) {
            System.err.println("Erreur création table : " + e.getMessage());
        }
    }


    // Ajouter un adhérent dans la base de données
    public void ajouterAdherent(Adherent adh) {
        String sql = "INSERT INTO Adherent (nom, prenom, email, telephone) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, adh.getNom());
            pstmt.setString(2, adh.getPrenom());
            pstmt.setString(3, adh.getEmail());
            pstmt.setString(4, adh.getTelephone());
            pstmt.executeUpdate();

            System.out.println("Adhérent ajouté : " + adh.getNom() + " " + adh.getPrenom());
        } catch (SQLException e) {
            System.err.println("Erreur, adhérent non ajouté à la base de données : " + e.getMessage());
        }
    }

    public List<Adherent> getlisteAdherents() {
        return listeAdherents;
    }



    public void afficherTousAdherents() {
        String sql = "SELECT * FROM adherent";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id_adherent") + " | " +
                    rs.getString("nom") + " " +
                    rs.getString("prenom") + " | " +
                    rs.getString("email")
                );
            }

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }





}

