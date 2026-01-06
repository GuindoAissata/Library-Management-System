package Model;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import DAO.AdherentDAO;
import DAO.DocumentDAO;
import DAO.EmpruntDAO;
import DAO.UpdateDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.chrono.*;
import java.time.temporal.ChronoUnit;

/**
 * 
 */
public class BibliothequeManager {

    /**
     * Default constructor
     */
    public BibliothequeManager() {
    }

    /**
     * 
     */
    //public Set<Emprunt> List_Emprunt = new HashSet<>();
    private  ObservableList<Emprunt> List_Emprunt = FXCollections.observableArrayList();

    /**
     * 
     */
    //public  List <Document> List_Document = new ArrayList<>();
    private ObservableList<Document> List_Document = FXCollections.observableArrayList();

    /**
     * 
     */
    //public Set<Adherent> List_Adherent = new HashSet<>();
    private ObservableList<Adherent> List_Adherent = FXCollections.observableArrayList();

    /// Attributs ajoutés pour la connexion à la base////
    private DocumentDAO documentDAO = new DocumentDAO();
    private AdherentDAO adherentDAO = new AdherentDAO();
    private EmpruntDAO empruntDAO = new EmpruntDAO();
    private UpdateDAO updateDAO = new UpdateDAO();

    //////Getter/////////
    public ObservableList<Document> getList_Document(){ return List_Document;}
    public ObservableList<Adherent> getList_Adherent(){ return List_Adherent;}
    public ObservableList<Emprunt> getList_Emprunt(){ return List_Emprunt;}
    


    // Méthode pour charger la base: à appeler au démarrage de l'application
    public void chargerDocumentsDepuisBD(){ 
    List_Document.setAll(documentDAO.findAll());}

    public void chargerAdherentDepuisBD(){
        List_Adherent.setAll(adherentDAO.findAll());}

    public void chargerEmpruntDepuisBD() {
    // 1) Récupérer tous les emprunts "bruts" (avec idAdherentBD / idDocumentBD)
    List_Emprunt.setAll(empruntDAO.findAll());

    // 2) Vider les listes d'emprunts des adhérents (et éventuellement des documents)
    for (Adherent a : List_Adherent) {
        a.List_Emprunt.clear();
    }
    for (Document d : List_Document) {
        d.List_Emprunt.clear();
    }

    // 3) Pour chaque emprunt, retrouver le vrai Adherent et Document
    for (Emprunt e : List_Emprunt) {

        Adherent adherentReel = trouverAdherentParId(e.getIdAdherentBD());
        Document documentReel = trouverDocumentParId(e.getIdDocumentBD());

        if (adherentReel != null && documentReel != null) {

            // on branche les vraies instances
            e.adherent = adherentReel;
            e.document = documentReel;

            // on met à jour les collections
            adherentReel.AddEmpruntAdherent(e);
            documentReel.AddEmpruntDoc(e);
        }
    }

    System.out.println("Emprunts chargés : " + List_Emprunt.size());
}

    private Adherent trouverAdherentParId(int id){
    for (Adherent a : List_Adherent){
        if (a.getID_Bd() == id) return a;
    }
    return null;
}
private Document trouverDocumentParId(int idDoc) {
    for (Document d : List_Document) {
        if (d.getidDoc() == idDoc) return d;
    }
    return null;
}


    ///////Methode : Ajouter de nouveaux documents////// 
    public Boolean AddDocument(Document doc){
        documentDAO.insert(doc);//Pour inserer dans la base
        List_Document.add(doc);
        return true;
    }

    ///////Methode : Suppression d'un document if not borrowed //////
    public Boolean DeleteDocument(Document doc){
        if(doc.getEstDisponible()==true){// If the document has not been borrowed
        //updateDAO.deleteDocument(doc);// supprimer de la base
        //return List_Document.remove(doc);
        doc.setEstDisponible(false);// retirer le document du catalogue (pas disponible)
        doc.setSupprime(true);
        updateDAO.UpdateEST_Disponieble(doc);// mise à jour dans la base
        updateDAO.UpdateSupprime(doc);
        
        return true;
        } return false;
        
    }

    ////Methode : Modification des informations d'un document /////////
    public void updateDocument(Document document){
        updateDAO.updateDocument(document);

    }

    ///Méthode : Modification des informations d'un Adhérent
    public void updateAdherent(Adherent adherent){
        updateDAO.updateAdherent(adherent);
    }

    ///// Méthode : Enregistrer un nouvel Adherent //////
    public Boolean AddAdherent(Adherent adherent) throws ArgumentException{
        if(!SameMail(adherent)){ //vérifier si un adherent n'existe pas déjà avec le mail donné
        adherentDAO.insert(adherent); // insérer dans la base
        return List_Adherent.add(adherent);
        // faire connexion à nouveau à la base à chaque ajout
        } 
        throw new ArgumentException("Un compte existe déjà avec cette adresse mail"); 
        
    }

    ////Méthode : Afficcher la liste des adherents ///////
    public String toString(){
        String result = "";
        for( Adherent a : List_Adherent ){ 
            result+= a.toString() + "; ";

        }
        return result ; 
    }

   /*public void supprimerDocumentLogique(Document doc) throws Exception {
    if (doc == null) return;

    try {
        documentDAO.marquerSupprime(doc);
        doc.setSupprime(true);
        doc.setEstDisponible(false);
    } catch (Exception e) {
        throw new Exception("Erreur lors de la suppression dans la base.", e);
    }
}*/




    /////////////Enregistrer un Emprunt//////////////
    public Emprunt Enregistrer_Emprunt (Document document,Adherent adherent) throws ArgumentException{
        if (!document.getEstDisponible()) {
            throw new ArgumentException("Le document n'ets pas disponible"); 
        }
        if (adherent.getNb_Emprunt_Encours()>=5){
            throw new ArgumentException("Le nombre d'emprunts autorisé est atteint");
        }
        if(adherent.getPenalite()>=10){// s'il a des  penalités majeures (j'ai choisi 10) 
            throw new ArgumentException("L'adherent a des pénalités à régler");
        }
        Emprunt emprunt = new Emprunt(adherent, document);
        empruntDAO.insert(emprunt);// insérer dans la base 
        List_Emprunt.add(emprunt); // Ajout dans la liste des emprunts de la Bibliothèque
        adherent.AddEmpruntAdherent(emprunt); //Ajout dans la liste personnelle des emprunts de l'adhérent
        int n = adherent.getNb_Emprunt_Encours();// on recupère son nombre d'emprunt en cours 
        adherent.setNb_Emprunt_Encours(n+1);// ensuite on l'incremente de 1 
        document.AddEmpruntDoc(emprunt); //Ajuot dans la liste des emprunts concernés par le document
        document.setEstDisponible(false);// rendre indisponible le document
        //Base de données 
        updateDAO.UpdateNb_Emprunt(emprunt);// on enregistre le nouveau nb d'emprunt dans la base 
        updateDAO.UpdateEST_Disponieble(emprunt);
        return emprunt;
    }

    /////Enregistrer un retour//////////////////////
    public boolean Enregistrer_Retour(Emprunt emprunt)throws ArgumentException{
        if(emprunt.getStatut_Emprunt()){ 
        emprunt.setDateRetourRelle(LocalDate.now());
        if(emprunt.getDate_RetourReelle().isAfter(emprunt.getDate_RetourPrevue())){
            Long retard = ChronoUnit.DAYS.between(emprunt.getDate_RetourPrevue(),emprunt.getDate_RetourReelle()) ;
            Double penalite = 0.50 * retard;
            Double anciennePenalite = emprunt.adherent.getPenalite();
            emprunt.setPenalite(penalite);//on met à jour la pénalité de l'emprunt
            emprunt.adherent.setPenalite(penalite + anciennePenalite);//Ajouter la pénalité à celle existante de l'adherent
            //Base de données
            updateDAO.UpdatePenaliteAdherent(emprunt);
            updateDAO.UpdatePenaliteEmprunt(emprunt);
        } 
            emprunt.adherent.decrementeNb_Emprunt_Encours();// on enlève 1 à son nbre d'emprunt en cours// il est incrémenté dans la méthode AddEmprunt dans Adherent
            emprunt.document.setEstDisponible(true);// On rend disponible le livre
            emprunt.setStatut_Emprunt(false);// on met à false pour dire que l'emprunt n'est plus en cours // il est à true par défaut à la création d'un emprunt
            
            //Base de donnees
            updateDAO.UpdateNb_Emprunt(emprunt);
            updateDAO.UpdateEST_Disponieble(emprunt);
            updateDAO.UpdateStatut_Emprunt(emprunt);
            updateDAO.UpdateDateRetourReelle(emprunt);
            // pour Statut_Emprunt
            

        return true;
    }else{
        throw new ArgumentException("Retour déjà effectué");
    }
    }

    //////Une methode pour vérifier qu'un adherent n'a pas le même mail que les autres
    public Boolean SameMail(Adherent adherent){
        for (Adherent a : List_Adherent) {
            if(adherent.getMail().equalsIgnoreCase(a.getMail())){
                return true;
            } 
        } return false;
    } 


}
