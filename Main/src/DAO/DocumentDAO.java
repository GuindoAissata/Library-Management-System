package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Document;
import Model.Livre;
import Model.Magazine;

public class DocumentDAO {

    // Recupère tous les documents (Livres + Magazines de la base)

    public List<Document> findAll(){
        List<Document> result = new ArrayList<>();

        String sql = """
        SELECT d.ID_DOC, d.Titre, d.Editeur, d.Est_Disponible, d.Type_doc,
            d.Supprime,
            l.ISBN, l.Nombre_page, l.Auteur,
            m.ISSN, m.Numero, m.Periodicite
        FROM Document d
        LEFT JOIN Livre l ON d.ID_Doc = l.Id_livre
        LEFT JOIN Magazine m ON d.ID_Doc = m.Id_magazine
        """;

                try(Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)){

                        while (rs.next()) {
                            // On recupère les dpnnées de la table Type_doc et la table Est_Disponible
                            String typeDoc = rs.getString("Type_doc");
                            boolean estDispo = rs.getInt("Est_Disponible")==1;
                            boolean suppression = rs.getInt("Supprime") ==1;

                            Document doc = null;

                            //// on Crée 1 magazine/Livre avec les données recuperées dans la tables Magzine/Livre, magazine/Livre qui sera ajouté à la liste result
                                //donc lorsqu'on chargera la base dans BibliothèqueManager, au lancement, cette methode 
                                // crée des instances de documents(Livre/Magazine) à partir des données des differentes tables sql
                                //et les stoke dans les listes correspondantes (ici List_Document)

                            // Si le type de document est un livre alors récuperer toutes les données de la table Livre

                            if("Livre".equalsIgnoreCase(typeDoc)){
                                String titre = rs.getString("Titre");
                                String editeur = rs.getString("Editeur");
                                String auteur = rs.getString("Auteur");
                                String isbn = rs.getString("ISBN");
                                int nbpages = rs.getInt("Nombre_page");

                                // On cree un Livre avec ces données recuperées dans la table sql Livre
                                Livre livre = new Livre(isbn,titre,editeur, auteur, nbpages);
                                livre.setidDoc(rs.getInt("ID_Doc"));
                                livre.setEstDisponible(estDispo);
                                livre.setSupprime(suppression);
                                doc = livre;
                            }else{// Si pas livre alors type est magazine alors on recupère les données de la table Magazine 
                                // on Crée 1 magazine avec ces données, magazine qui sera ajouté à la liste result
                                //donc lorsqu"on chargera la base dans BibliothèqueManager, au lancement, cette methode 
                                // crée des instances de documents(Livre/Magazine) à partir des données des differentes tables sql
                                //et les stoke dans les listes correspondantes (ici List_Document)
                                String titre       = rs.getString("Titre");
                                String editeur     = rs.getString("Editeur");
                                String issn        = rs.getString("ISSN");
                                int numero         = rs.getInt("Numero");
                                String periodicite = rs.getString("Periodicite");
                                Magazine magazine = new Magazine(issn,titre, editeur, periodicite,numero);
                                magazine.setidDoc(rs.getInt("ID_Doc"));
                                magazine.setEstDisponible(estDispo);
                                magazine.setSupprime(suppression);

                                doc = magazine;
                            }
                            result.add(doc);
                            }

                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return result;
    }

    // Insère un document (Livre ou Magazine) dans la base
    // - Document dans la table Document 
    // - Puis infos spécifiques dans livre ou Magazine
    public void insert(Document doc){

        String sqlDoc = "INSERT INTO Document (Titre, Editeur, Est_Disponible, Type_doc, Supprime) VALUES (?, ?, ?, ?,?)";
        String sqlLivre = "INSERT INTO Livre (Id_livre, ISBN, Nombre_page, Auteur) VALUES (?, ?, ?, ?)";
        String sqlMagazine = "INSERT INTO Magazine (Id_magazine, ISSN, Numero, Periodicite) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false);//On veut tout ou rien

            int idDocGenere;

            //Inerstion dans la table Document
            try(PreparedStatement psDoc = conn.prepareStatement(sqlDoc,Statement.RETURN_GENERATED_KEYS)){
                psDoc.setString(1, doc.getTitre());
                psDoc.setString(2, doc.getEditeur());
                psDoc.setInt(3, doc.getEstDisponible()? 1 : 0);
                psDoc.setBoolean(5, doc.getSupprime());

                String typeDoc;
                if(doc instanceof Livre){
                    typeDoc = "Livre";
                }else if(doc instanceof Magazine){
                    typeDoc = "Magazine";
                }else{ typeDoc = "Autre";}
                psDoc.setString(4, typeDoc);
                psDoc.executeUpdate();
                // recuperer l'ID_Doc généré automatiquement dans la base 
                try(ResultSet keys = psDoc.getGeneratedKeys()){
                    if(keys.next()){
                        idDocGenere = keys.getInt(1);
                        doc.setidDoc(idDocGenere);
                    }else{
                        throw new SQLException("Impossible de récupérer l'ID_Doc généré");

                    }
                }
            }

            // Insertion dans Livre ou Magazine
            if (doc instanceof Livre livre){
                try(PreparedStatement psLivre = conn.prepareStatement(sqlLivre)){
                    psLivre.setInt(1,doc.getidDoc());// Id_Livre = ID_DOC
                    psLivre.setString(2,livre.getISBN_ISSN());
                    psLivre.setInt(3, livre.getNombre_page());
                    psLivre.setString(4, livre.getAuteur());
                    psLivre.executeUpdate();
                }
            }else if(doc instanceof Magazine magazine){
                System.out.println("Insertion dans Magazine pour ID_Doc=" + doc.getidDoc());
            System.out.println("  ISSN=" + magazine.getISBN_ISSN()
                            + ", Numero=" + magazine.getNumero()
                            + ", Periodicite=" + magazine.getPeriodicite());
                try(PreparedStatement psMag = conn.prepareStatement(sqlMagazine)){
                    psMag.setInt(1, doc.getidDoc());
                    psMag.setString(2, magazine.getISBN_ISSN());
                    psMag.setInt(3, magazine.getNumero());
                    psMag.setString(4, magazine.getPeriodicite());
                    psMag.executeUpdate();
                }
            }
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}
