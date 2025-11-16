package Model;
import java.io.*;
import java.util.*;

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
    public Set<Emprunt> List_Emprunt = new HashSet<>();

    /**
     * 
     */
    public  List <Document> List_Document = new ArrayList<>();

    /**
     * 
     */
    public Set<Adherent> List_Adherent = new HashSet<>();


    ///////Methode : Ajouter de nouveaux documents////// 
    public Boolean AddDocument(Document doc){
        List_Document.add(doc);
        return true;
    }

    ///////Methode : Suppression d'un document if not borrowed //////
    public Boolean DeleteDocument(Document doc){
        if(doc.getEstDisponible()==true){// If the document has not been borrowed
          return  List_Document.remove(doc);
        } return false;
        
    }

    ///// Méthode : Enregistrer un nouvel Adherent //////
    public Boolean AddAdherent(Adherent adherent){
        
        return List_Adherent.add(adherent);
        
    }

    ////Méthode : Afficcher la liste des adherents ///////
    public String toString(){
        String result = "";
        for( Adherent a : List_Adherent ){ 
            result+= a.toString() + "; ";

        }
        return result ; 
    }


  /////////////Enregistrer unn Emprunt//////////////
    public LocalDate Enregistrer_Emprunt (Document document,Adherent adherent) throws ArgumentException{
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
        List_Emprunt.add(emprunt); // Ajout dans la liste des emprunts de la Bibliothèque
        adherent.AddEmpruntAdherent(emprunt); //Ajout dans la liste personnelle des emprunts de l'adhérent
        document.AddEmpruntDoc(emprunt); //Ajuot dans la liste des emprunts concernés par le document
        document.setEstDisponible(false);// rendre indisponible le document 
        return emprunt.getDate_RetourPrevue();
    }

    /////Enregistrer un retour//////////////////////
    public boolean Enregistrer_Retour(Emprunt emprunt){
        emprunt.setDateRetourRelle(LocalDate.now());
        if(emprunt.getDate_RetourReelle().isAfter(emprunt.getDate_RetourPrevue())){
            Long retard = ChronoUnit.DAYS.between(emprunt.getDate_RetourReelle(),emprunt.getDate_RetourReelle()) ;
            Double penalite = 0.50 * retard;
            emprunt.setPenalite(penalite);//on met à jour la pénalité de l'emprunt
            emprunt.adherent.setPenalite(penalite);//Ajouter la pénalité à celle existante de l'adherent
        } 
            emprunt.adherent.setNb_Emprunt_Encours();// on enlève 1 à son nbre d'emprunt en cours// il est incrémenté dans la méthode AddEmprunt dans Adherent
            emprunt.document.setEstDisponible(true);// On rend disponible le livre
             emprunt.adherent.decrementeNb_Emprunt_Encours();// on enlève 1 à son nbre d'emprunt en cours// il est incrémenté dans la méthode AddEmprunt dans Adherent
        return true;
    }


}
