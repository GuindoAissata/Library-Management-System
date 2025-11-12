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
    public Set<Emprunt> List_Emprunt;

    /**
     * 
     */
    public  List <Document> List_Document;

    /**
     * 
     */
    public Set<Adherent> List_Adherent;

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

    /////Enregistrer un nouvel Adherent //////
    public Boolean AddAdherent(String Nom, String Prenom,String Mail, String contact){
        Adherent adherent = new Adherent(Nom, Prenom, Mail, contact);
        return List_Adherent.add(adherent);
        
    }

}