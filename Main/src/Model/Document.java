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

    /**
     * 
     */
    public Set<Emprunt> List_Emprunt;

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
        }
    ////////// Getter ////////////////
    
    public String getitre(){return titre;}
    public String getEditeur(){return editeur;}
    public int getidDoc(){return idDoc;}
    public Boolean getEstDisponible(){return EstDisponible;}
    
    /////////Setter ////////////////
    
    public void settitre(String t){ titre = t;}
    public void setediteur(String e){editeur = e;}
    public void setidDoc(int i){ idDoc = i;}
    public void setEstDisponible(Boolean d){EstDisponible = d;}

    ///////// abstract method////////////// Sera implémenté dans Livre et Magazine pour trouver le type de livre
    
    public abstract String getType() ;
    ////////final method///////////////
    
    public void AddEmpruntDoc(Emprunt e){ List_Emprunt.add(e);}
    public String toString(){ return "ID : " + idDoc + " ; Titre : " +titre + " ; Editeur : " +editeur + " ; Etat" + (EstDisponible? "Disponible":"Emprunté");  }

}
