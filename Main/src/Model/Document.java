package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Document {

  
    /**
     * 
     */
    private String titre;

    /**
     * 
     */
    private String editeur;

    /**
     * 
     */
    private int idDoc;

    /**
     * 
     */

    private Boolean EstDisponible;
    private boolean supprime = false;
    

    /**
     * 
     */
    public Set<Emprunt> List_Emprunt = new HashSet<>();

    /**
     * 
     */
    public BibliothequeManager BibliothequeManager;

    ////////////////Constructeur///////////////
    public Document(String titre, String editeur) {
        this.titre = titre;
        this.editeur = editeur; 
        //this.idDoc = idDoc;
        this.EstDisponible = true; // est disponible par défaut à la création 
        this.supprime = false;    }
    ////////// Getter ////////////////
    
    public String getTitre(){return titre;}
    public String getEditeur(){return editeur;}
    public int getidDoc(){return idDoc;}
    public Boolean getEstDisponible(){return EstDisponible;}
    public boolean getSupprime() {
    return supprime;
}

    
    // A redéfinir dans Livre, pas dans Magazine (getter qui permet d'afficher plus proprement dans l'interface)
    public String getAuteur() {
    return "----------"; // Document normal n’a pas d’auteur, Livre a un auteur, Magazine n'a pas d'auteur
    }

    // A redéfinir dans Livre / Magazine
    public String getISBN_ISSN() {
        return ""; 
    }

    
    /////////Setter ////////////////
    
    public void settitre(String t){ titre = t;}
    public void setediteur(String e){editeur = e;}
    public void setidDoc(int i){ idDoc = i;}
    public void setEstDisponible(Boolean d){EstDisponible = d;}
    public void setSupprime(boolean supprime) {
    this.supprime = supprime;
}
public String getStatutAffichage() {
    if (supprime) return "Supprimé";
    if (!EstDisponible) return "Emprunté";
    return "Disponible";
}


    ///////// abstract method////////////// Sera implémenté dans Livre et Magazine pour trouver le type de livre
    
    
    public abstract String getType() ;
    ////////final method///////////////
    
    public void AddEmpruntDoc(Emprunt e){ List_Emprunt.add(e);}
    public String toString(){ return "ID : " + idDoc + " ; Titre : " +titre + " ; Editeur : " +editeur + " ; Etat" + (EstDisponible? "Disponible":"Emprunté");  }

}
