package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Types;
import java.sql.Statement;
import Model.Adherent;
import Model.Document;
import Model.Emprunt;
import Model.Livre;
import Model.Magazine;

public class EmpruntDAO {


public List<Emprunt> findAll() {
    List<Emprunt> liste = new ArrayList<>();

    String sql = """
        SELECT  
            e.ID_Emprunt,
            e.Date_Emprunt,
            e.Date_RetourPrevue,
            e.Date_RetourReelle,
            e.Statut_Emprunt,
            e.ID_Doc,
            e.ID AS ID_Adh_PK,
            e.Penalite
        FROM Emprunt e
        """;

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Emprunt emprunt = new Emprunt();

            emprunt.setID_Emprunt(rs.getLong("ID_Emprunt"));

            String dEmpStr    = rs.getString("Date_Emprunt");
            String dPrevStr   = rs.getString("Date_RetourPrevue");
            String dReelleStr = rs.getString("Date_RetourReelle");

            LocalDate dEmprunt =    (dEmpStr    != null ? LocalDate.parse(dEmpStr)    : null);
            LocalDate dPrev    =    (dPrevStr   != null ? LocalDate.parse(dPrevStr)   : null);
            LocalDate dReelle  =    (dReelleStr != null ? LocalDate.parse(dReelleStr) : null);

            emprunt.setDate_Emprunt(dEmprunt);
            emprunt.setDateRetourPrevu(dPrev);
            emprunt.setDateRetourRelle(dReelle);
            emprunt.setStatut_Emprunt(rs.getInt("Statut_Emprunt") == 1);
            emprunt.setPenalite(rs.getDouble("Penalite"));

            //  on garde juste les ID de la BD
            emprunt.setIdDocumentBD(rs.getInt("ID_Doc"));
            emprunt.setIdAdherentBD(rs.getInt("ID_Adh_PK"));

            liste.add(emprunt);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return liste;
}

    // Insérer un  Emprunt

    public void insert(Emprunt e) {

        String sql = "INSERT INTO Emprunt " +
                    "(ID_Emprunt,Date_Emprunt, Date_RetourPrevue, Date_RetourReelle, Statut_Emprunt, ID_Doc, ID,Penalite) " +
                    "VALUES (?,?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // 0) on recupère l'ID_Emprunt généré dans le constructeur
            Long Id_Emprunt = e.getID_Emprunt();
            ps.setLong(1, Id_Emprunt);
            // 1) Dates : on les stocke en texte ISO "yyyy-MM-dd"
            LocalDate dEmprunt = e.getDate_Emprunt();
            LocalDate dRetourPrev = e.getDate_RetourPrevue();
            LocalDate dRetourReelle = e.getDate_RetourReelle(); // au début souvent null

            ps.setString(2, dEmprunt.toString());
            ps.setString(3, dRetourPrev.toString());
            ps.setDouble(8, e.getPenalite());

            if (dRetourReelle != null) {
                ps.setString(4, dRetourReelle.toString());
            } else {
                // nécessite que Date_RetourReelle soit NULL autorisé dans la table
                ps.setNull(4, Types.VARCHAR);
            }

            // 2) Statut_Emprunt : boolean → 1 / 0
            ps.setInt(5, e.getStatut_Emprunt() ? 1 : 0);

            // 3) Clés étrangères
            ps.setInt(6, e.document.getidDoc()); // ID_Doc
            ps.setInt(7, e.adherent.getID_Bd());    // ID (clé primaire Adherent)

            // 4) Mettre à jour Nb_Emprunt_Encours ou penalité dans la table Adherent 

            ps.executeUpdate();

           /* // 4) Récupérer l'ID_Emprunt généré par SQLite
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    Long dbId = keys.getLong(1);
                    // on met l'ID généré dans l'objet Java (en castant en long si besoin)
                    e.setID_Emprunt(dbId);
                }
            }*/

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
