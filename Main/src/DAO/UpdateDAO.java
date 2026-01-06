package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Adherent;
import Model.Document;
import Model.Emprunt;
import Model.Livre;
import Model.Magazine;

public class UpdateDAO {
    // Methode de mise à jour qui enregistre dans la base le Nb lorsqu'il est modifié par création d'un emprunt
    // Methode à mettre dans enregistrer un emprunt et un retour dans bibliothèqueManager
    public void UpdateNb_Emprunt(Emprunt emprunt){
    String sql1 = "UPDATE Adherent SET Nb_Emprunt_Encours = ? WHERE ID= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setInt(1, emprunt.adherent.getNb_Emprunt_Encours());
                    ps1.setInt(2, emprunt.adherent.getID_Bd());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
    
    // Comme la methode précédente
    public void UpdatePenaliteAdherent(Emprunt emprunt ){
        String sql1 = "UPDATE Adherent SET Penalite = ? WHERE ID= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setDouble(1, emprunt.adherent.getPenalite());
                    ps1.setInt(2, emprunt.adherent.getID_Bd());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }

    public void UpdatePenaliteAdherent2(Adherent adherent ){
        String sql1 = "UPDATE Adherent SET Penalite = ? WHERE ID= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setDouble(1, adherent.getPenalite());
                    ps1.setInt(2, adherent.getID_Bd());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }
    public void UpdatePenaliteEmprunt(Emprunt emprunt ){
        String sql1 = "UPDATE Emprunt SET Penalite = ? WHERE ID= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setDouble(1, emprunt.getPenalite());
                    ps1.setLong(2, emprunt.getID_Emprunt());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }
        public void UpdateStatut_Emprunt(Emprunt emprunt ){
        String sql1 = "UPDATE Emprunt SET  Statut_Emprunt = ? WHERE ID_Emprunt= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setBoolean(1, emprunt.getStatut_Emprunt());
                    ps1.setLong(2, emprunt.getID_Emprunt());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }
    public void UpdateEST_Disponieble(Emprunt emprunt ){
        String sql1 = "UPDATE Document SET Est_Disponible = ? WHERE ID_Doc= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setBoolean(1, emprunt.document.getEstDisponible());
                    ps1.setInt(2, emprunt.document.getidDoc());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }

        public void UpdateEST_Disponieble(Document document ){
        String sql1 = "UPDATE Document SET Est_Disponible = ? WHERE ID_Doc= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setBoolean(1, document.getEstDisponible());
                    ps1.setInt(2, document.getidDoc());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }

    public void UpdateDateRetourReelle(Emprunt emprunt ){
        String sql1 = "UPDATE Emprunt SET  Date_RetourReelle = ? WHERE ID_Emprunt= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setString(1, emprunt.getDate_RetourReelle().toString());
                    ps1.setLong(2, emprunt.getID_Emprunt());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }
    }

    public void deleteDocument(Document doc) {
    String deleteMag = "DELETE FROM Magazine WHERE Id_magazine = ?";
    String deleteLivre = "DELETE FROM Livre WHERE Id_livre = ?";
    String deleteDoc = "DELETE FROM Document WHERE ID_Doc = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        int id = doc.getidDoc();

        if (doc instanceof Livre) {
            try (PreparedStatement ps = conn.prepareStatement(deleteLivre)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
        if (doc instanceof Magazine) {
            try (PreparedStatement ps = conn.prepareStatement(deleteMag)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(deleteDoc)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        conn.commit();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    public boolean updateAdherent(Adherent adherent) {
    String sql = "UPDATE Adherent SET ID_Adherent = ?, Nom = ?, Prenom = ?, Mail = ?, Contact = ?, Penalite = ?, Nb_Emprunt_Encours = ? WHERE ID = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, adherent.getID_Adherent());
        ps.setString(2, adherent.getNom());
        ps.setString(3, adherent.getPrenom());
        ps.setString(4, adherent.getMail());
        ps.setString(5, adherent.getContact());
        ps.setDouble(6, adherent.getPenalite());
        ps.setInt(7, adherent.getNb_Emprunt_Encours());
        ps.setInt(8, adherent.getID_Bd());
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public void updateDocument(Document doc) {
    String sqlDoc = "UPDATE Document SET Titre = ?, Editeur = ? WHERE ID_Doc = ?";
    String sqlLivre = "UPDATE Livre SET Auteur = ?, Nombre_page = ? WHERE Id_livre = ?";
    String sqlMag   = "UPDATE Magazine SET ISSN = ?, Numero = ?, Periodicite = ? WHERE Id_magazine = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {

        // Document commun
        try (PreparedStatement ps = conn.prepareStatement(sqlDoc)) {
            ps.setString(1, doc.getTitre());
            ps.setString(2, doc.getEditeur());
            ps.setInt(3, doc.getidDoc());
            ps.executeUpdate();
        }

        if (doc instanceof Livre l) {
            try (PreparedStatement ps = conn.prepareStatement(sqlLivre)) {
                ps.setString(1, l.getAuteur());
                ps.setInt(2, l.getNombre_page());
                ps.setInt(3, doc.getidDoc());
                ps.executeUpdate();
            }
        } else if (doc instanceof Magazine m) {
            try (PreparedStatement ps = conn.prepareStatement(sqlMag)) {
                ps.setString(1, m.getISBN_ISSN());
                ps.setInt(2, m.getNumero());
                ps.setString(3, m.getPeriodicite());
                ps.setInt(4, doc.getidDoc());
                ps.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void UpdateSupprime(Document document ){
        String sql1 = "UPDATE Document SET  Supprime = ? WHERE ID_Doc= ?";
            try(Connection conn2 = DatabaseConnection.getConnection();
                PreparedStatement ps1 = conn2.prepareStatement(sql1)){

                    ps1.setBoolean(1, document.getSupprime());
                    ps1.setInt(2, document.getidDoc());
                    ps1.executeUpdate();

                }catch(SQLException e){
                    e.printStackTrace();
                }



    
}
}