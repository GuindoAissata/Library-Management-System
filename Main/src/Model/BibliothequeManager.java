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

    /**
     * @param d
     */
    public void Emprunt(Document d) {
        // TODO implement here
    }

    /**
     * @param d
     */
    public void Retour(Document d) {
        // TODO implement here
    }

    ///////Methode : Ajouter de nouveaux documents////// 
    public Boolean AddDocument(Document doc){
        List_Document.add(doc);
        return true;
    }

    ///////Methode : Suppression d'un document if not borrowed //////
    public Boolean DeleteDocument(Document doc){
        if(doc.getEstDisponible()==true){// If the document has not been borrowed
            List_Document.remove(doc);
            return true;
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
    public boolean Enregistrer_Emprunt (Document document,Adherent adherent) throws ArgumentException{
        if (!document.getEstDisponible()) {
            throw new ArgumentException("Le document n'ets pas disponible"); 
        }
        if (adherent.getNb_Emprunt_Encours()>=5){
            throw new ArgumentException("Le nombre d'emprunts autorisé est atteint");
        }
        if(adherent.getPenalite()>0){
            throw new ArgumentException("L'adherent a des pénalités à régler");
        }
        return true;
    }


}