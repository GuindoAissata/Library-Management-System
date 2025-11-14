package Model;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 
 */
public class Emprunt {

    /**
     * 
     */
    private Long ID_Emprunt;

    /**
     * 
     */
    private LocalDate Date_Emprunt;

    /**
     * 
     */
    private LocalDate Date_RetourPrevue;

    /**
     * 
     */
    private LocalDate Date_RetourReelle;
    /*
     * 
     */
    private Boolean Statut_Emprunt; // ajouter pour savoir si emprunt en cours ou non 
    /**
     * 
     */
    public Document Document;

    /**
     * 
     */
    public Adherent Adherent;

    /**
     * 
     */
    public BibliothequeManager BibliothequeManager;

    /**
     * Default constructor
     */
    // Pour créer un emprunt j'ai besoin de renseigner l'adherent qui prête et le document concerné par le prêt
    public Emprunt(Adherent Adherent, Document Document) {
        this.Date_Emprunt = LocalDate.now();// Annee/mois/jour au moment du prêt
        this.Date_RetourPrevue = Date_Emprunt.plusMonths(1);// retour 1 mois apèrs l'emprunt
        this.Adherent= Adherent;
        this.Document = Document;
        this.Statut_Emprunt = true;
        // L'id sera automatiquement renseigné à la création d'un emprunt
        // l'id sera l'annee+mois+jour+minute+seconde du prêt 
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.ID_Emprunt = Long.parseLong(time.format(formatter)) ;
    }

///////////////getter////////////////
    public Long getID_Emprunt(){  return ID_Emprunt;}
    public Document getDocument(){ return Document;}
    public Adherent getAdherent(){ return Adherent;}
    public LocalDate getDate_Emprunt(){ return Date_Emprunt;}
    public LocalDate getDate_RetourPrevue(){return Date_RetourPrevue; }
    public LocalDate getDate_RetourReelle(){return Date_RetourReelle;}
    public Boolean getStatut_Emprunt(){return Statut_Emprunt;}

    ////////////////setter////////////////
    public void setID_Emprunt( Long id){ ID_Emprunt = id;}
    public void setDateRetourPrevu( LocalDate d){ Date_RetourPrevue = d;}
    public void setDateRetourRelle(LocalDate d){ Date_RetourReelle = d;}

}
