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
    private String nom;

    /**
     * 
     */
    private String prenom;

    /**
     * 
     */
    private String mail;

    /**
     * 
     */
    private String contact ;
    /**
     * 
     */
    private Double penalite; // Si false alors pas de penalité si true alors des penalités à payer

    /**
     * 
     */
    private int Nb_Emprunt_Encours = 0; // pour compter le nombre d'emprunt en cours , initialisé à 0 à la création

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
                        
    public Adherent(String nom,String prenom, String mail, String contact){
        if ( contact!=null && !contact.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Numero de téléphone invalide");
        }
        if(mail !=null && !mail.matches("^[a-zA-Z][a-zA-Z0-9]*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException("Adresse mail invalide");
        }
            verificationAdherent(nom, prenom);
        this.List_Emprunt = new HashSet<>();
        this.nom = nom.trim().replaceAll("\\s+", "") ; 
        this.prenom = prenom.trim().replaceAll("\\s+", "") ;; 
        this.mail = mail.trim().replaceAll("\\s+", "") ;;
        this.penalite= 0.0; // pas de pénalité à la création
        this.contact  = contact.trim();
        this.ID_adherent = this.getIdAdherent(); 
        System.out.println("Votre ID est : " + ID_adherent);
    }

    // Methode pour générer l'ID automatiquement
    private String getIdAdherent(){
    //les 4 premières lettres du nom 
    String  partNom = nom.length() >= 4 ? nom.substring(0,4).toLowerCase() : nom.toLowerCase();
    // les  2 premières lettres du prenom
    String partPrenom = prenom.length() >=2 ? prenom.substring(0,2).toLowerCase() : prenom.toLowerCase();
    return partNom + partPrenom + compteur ;
        compteur++;
    } 

     // Méthodes de validation
    private static boolean verificationNom(String nom) {
        // Uniquement des lettres (incluant les accents et tirets)
        return nom != null && !nom.trim().isEmpty() && nom.matches("^[a-zA-ZÀ-ÿ\\s.-]+$");
    }

    private static boolean verificationPrenom(String prenom) {
        // Uniquement des lettres (incluant les accents et tirets)
        return prenom != null && !prenom.trim().isEmpty() && prenom.matches("^[a-zA-ZÀ-ÿ\\s.-]+$");
    }
    public static void verificationAdherent(String nom, String prenom){
        if (!verificationNom(nom)) {
            throw new IllegalArgumentException("Format du nom invalide (doit contenir uniquement des lettres, des espaces, des tirets ou des points)");
        }
        if (!verificationPrenom(prenom)) {
            throw new IllegalArgumentException("Format du prénom invalide (doit contenir uniquement des lettres, des espaces, des tirets ou des points)");
        }
    }
                            //////////////Méthodes//////////////////
    /// Getter ///////

    public String getNom(){ return nom;}
    public String getPrenom(){ return prenom;}
    public String getMail(){ return mail;}
    public String getID(){ return ID_adherent;}
    public String getContact(){ return contact;}
    public Double getPenalite(){ return penalite;}
    public int getNb_Emprunt_Encours(){ return Nb_Emprunt_Encours;}

    //// Setter //////
    
    public void setNom(String n ){ nom = n ; }
    public void setPrenom(String p){prenom = p;}
    public void setMail(String m){mail = m;}
    public void setContact(String c){contact = c;}
    //private void setID(String id){ ID_adherent = id;}
    public void setPenalite( Double s){ penalite = s;}

    ////////////ToString///////////////
    public String toString(){
        return "ID :"+ ID_adherent +" Nom : " +nom + " Prenom : " + prenom + " Contact: " + contact + " Mail : "+ mail; 
    }

    ////Ajout d'un emprunt /////////// 
    //A chaque emprunt on rajoute dans la liste des emprunts de l'adhérent en question
    public void AddEmpruntAdherent(Emprunt e ){ 
        List_Emprunt.add(e); Nb_Emprunt_Encours +=1;
    }

    //// A chaque retour on decremente le Nb_Emprunt_Encours 
    public void setNb_Emprunt_Encours(){Nb_Emprunt_Encours -=1 ; }
    @Override
    // redefinir la methode equals du set , pour lui dire que deux ensembles sont égales que si leur Mail sont pareil
    public boolean equals(Object o){
        if(this == o){ return true;}
        if(o == null || getClass() != o.getClass()){return false;}

        Adherent other = (Adherent)o;
        boolean sameMail =  this.mail != null && mail.equalsIgnoreCase(other.mail);
        boolean sameContact = this.contact != null && contact.equalsIgnoreCase(other.contact);

        return sameMail || sameContact ; 
    }
    @Override
    public int hashCode() {
        // hash basé sur le mail et le contact
        int result = 0;
        if (mail != null) result += mail.toLowerCase().hashCode();
        if (contact != null) result += contact.hashCode();
        return result;
    }
}
