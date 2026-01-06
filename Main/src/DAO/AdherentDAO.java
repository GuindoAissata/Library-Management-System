package DAO;

import Model.Adherent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {

    // Récupérer tous les adhérents
    public List<Adherent> findAll() {
        List<Adherent> list = new ArrayList<>();

        String sql = "SELECT * FROM Adherent";

        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Adherent adherent = new Adherent(
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Mail"),
                    rs.getString("Contact")
                );

                adherent.setID(rs.getString("ID_Adherent"));
                adherent.setID_Bd(rs.getInt("ID"));
                adherent.setPenalite(rs.getDouble("Penalite"));
                adherent.setNb_Emprunt_Encours(rs.getInt("Nb_Emprunt_Encours"));

                list.add(adherent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Insérer un adhérent
    public void insert(Adherent adherent) {
        String sql = "INSERT INTO Adherent ( Nom, Prenom, Mail, Contact, Penalite, Nb_Emprunt_Encours) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //ps.setString(1, adherent.getID_Adherent());
            ps.setString(1, adherent.getNom());
            ps.setString(2, adherent.getPrenom());
            ps.setString(3, adherent.getMail());
            ps.setString(4, adherent.getContact());
            ps.setDouble(5, adherent.getPenalite());
            ps.setInt(6, adherent.getNb_Emprunt_Encours());

            ps.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int idBd = keys.getInt(1);
                    adherent.setID_Bd(idBd);

                    //Générer l'ID de l'Adhérent ici
                String idAdherent = generIdAdherent(adherent.getNom(), adherent.getPrenom(),idBd);
                adherent.setID(idAdherent);
                }
            }

            //Mettre à jour Id_Adherent pour ajoute l'ID généré
            String sql1 = "UPDATE Adherent SET ID_Adherent = ? WHERE ID= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setString(1, adherent.getID_Adherent());
                    ps1.setInt(2, adherent.getID_Bd());
                    ps1.executeUpdate();

                }
            





        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String generIdAdherent(String nom, String prenom, int idBd){
        //les 4 premières lettres du nom 
        String  partNom = nom.length() >= 4 ? nom.substring(0,4).toLowerCase() : nom.toLowerCase();
        // les  2 premières lettres du prenom
        String partPrenom = prenom.length() >=2 ? prenom.substring(0,2).toLowerCase() : prenom.toLowerCase();
        return partNom + partPrenom + idBd ; 
        } 
}
