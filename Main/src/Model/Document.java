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
    private String auteur;

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
    public Document(int idDoc,String titre, String auteur) {
        this.titre = titre;
        this.auteur = auteur; 
        this.idDoc = idDoc;
        this.EstDisponible = true; // est disponible par défaut à la création 
        }
    ////////// Getter ////////////////
    
    public String getitre(){return titre;}
    public String getauteur(){return auteur;}
    public int getidDoc(){return idDoc;}
    public Boolean getEstDisponible(){return EstDisponible;}
    
    /////////Setter ////////////////
    
    public void settitre(String t){ titre = t;}
    public void setauteur(String a){auteur = a;}
    public void setidDoc(int i){ idDoc = i;}
    public void setEstDisponible(Boolean d){EstDisponible = d;}

    ///////// abstract method////////////// Sera implémenté dans Livre et Magazine pour trouver le type de livre
    
    public abstract String getType() ;
    ////////final method///////////////
    
    public void AddEmpruntDoc(Emprunt e){ List_Emprunt.add(e);}
    public String toString(){ return "ID : " + idDoc + " ; Titre : " +titre + " ; Auteur : " +auteur + " ; Etat" + (EstDisponible? "Disponible":"Emprunté");  }

}