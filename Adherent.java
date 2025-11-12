package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Adherent {

    private String ID_adherent;

    /**
     * 
     */
    private String Nom;

    /**
     * 
     */
    private String Prenom;

    /**
     * 
     */
    private String Mail;

    /**
     * 
     */
    private String Contact ;
    /**
     * 
     */
    private Boolean Statut_Penalite; // Si false alors pas de penalité si true alors des penalités à payer

    /**
     * 
     */
    public Set<Emprunt> List_Emprunt;

    /**
     * 
     */
    public BibliothequeManager BibliothequeManager;
    /**
     * 
     */
    private static int compteur = 0; // compteur partagé poour trouver l'ordre d'inscription de l'instance courant
    /**
     * Default constructor
     */
                        //////////Constructeur/////////////////
                        
    public Adherent(String Nom,String Prenom, String Mail, String Contact) {
        if ( Contact!=null && !Contact.matches("^\\+?[0-9\\s-]+$")) {
            throw new IllegalArgumentException("Numero de téléphone invalide");
        }
        /*if(Mail !=null && !Mail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$")){
            throw new IllegalArgumentException("Adresse mail invalide");
        }*/
        this.Nom = Nom ; 
        this.Prenom = Prenom; 
        this.Mail = Mail;
        this.Statut_Penalite = false;
        this.Contact = Contact;
        this.ID_adherent = this.getIdAdherent(); 
        System.out.println("Votre ID est : " + ID_adherent);
    }

    // Methode pour générer l'ID automatiquement
    private String getIdAdherent(){
    //les 4 premières lettres du nom 
    String  partNom = Nom.length() >= 4 ? Nom.substring(0,4).toLowerCase() : Nom.toLowerCase();
    // les  2 premières lettres du prenom
    String partPrenom = Prenom.length() >=2 ? Prenom.substring(0,2).toLowerCase() : Prenom.toLowerCase();
    return partNom + partPrenom + compteur ; 
    } 
                            //////////////Méthodes//////////////////
    /// Getter ///////

    public String getNom(){ return Nom;}
    public String getPrenom(){ return Prenom;}
    public String getMail(){ return Mail;}
    public String getID(){ return ID_adherent;}
    public String getContact(){ return Contact;}
    public Boolean getPenalite(){ return Statut_Penalite;}

    //// Setter //////
    
    public void setNom(String n ){ Nom = n ; }
    public void setPrenom(String p){Prenom = p;}
    public void setMail(String m){Mail = m;}
    public void setContact(String c){Contact = c;}
    //private void setID(String id){ ID_adherent = id;}
    public void setPenalite(Boolean s){ Statut_Penalite = s;}

    ////Ajout d'un emprunt /////////// 
    //A chaque emprunt on rajoute dans la liste des emprunts de l'adhérent en question
    public void AddEmpruntAd(Emprunt e ){ List_Emprunt.add(e);}
}