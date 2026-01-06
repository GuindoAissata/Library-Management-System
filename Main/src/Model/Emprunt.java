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
    private Boolean statut_Emprunt; // ajouter pour savoir si emprunt en cours ou non 
    private Double penalite = 0.0;
    /**
     * 
     */
    public Document document;

    /**
     * 
     */
    public Adherent adherent;

    /**
     * 
     */
    public BibliothequeManager BibliothequeManager;

    //Construcetur ajouté pour récuperer les clés étrangères dans la base ///

    private int idAdherentBD;
    private int idDocumentBD;

    
    /*Default constructor
      * 
     */
    public Emprunt(){}; // constructeur par défaut pour permettre le chargement depuis la base
    
    // Pour créer un emprunt j'ai besoin de renseigner l'adherent qui prête et le document concerné par le prêt
    public Emprunt(Adherent adherent, Document document) {
        this.Date_Emprunt = LocalDate.now();// Annee/mois/jour au moment du prêt
        this.Date_RetourPrevue = Date_Emprunt.plusMonths(1);// retour 1 mois apèrs l'emprunt
        this.adherent= adherent;
        this.document = document;
        this.statut_Emprunt = true;
        // L'id sera automatiquement renseigné à la création d'un emprunt
        // l'id sera l'annee+mois+jour+minute+seconde du prêt 
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.ID_Emprunt = Long.parseLong(time.format(formatter)) ;
    }

    ///////////////getter////////////////
    public Long getID_Emprunt(){  return ID_Emprunt;}
    public LocalDate getDate_Emprunt(){ return Date_Emprunt;}
    public LocalDate getDate_RetourPrevue(){return Date_RetourPrevue; }
    public LocalDate getDate_RetourReelle(){return Date_RetourReelle;}
    public Boolean getStatut_Emprunt(){return statut_Emprunt;}
    public Double getPenalite(){return penalite;}
    public int getIdAdherentBD() { return idAdherentBD; }
    public int getIdDocumentBD() { return idDocumentBD; }

    ////////////////setter////////////////
    public void setID_Emprunt( Long id){ ID_Emprunt = id;}
    public void setDate_Emprunt( LocalDate d){ Date_Emprunt = d;}
    public void setDateRetourPrevu( LocalDate d){ Date_RetourPrevue = d;}
    public void setDateRetourRelle(LocalDate d){ Date_RetourReelle = d;}
    public void setStatut_Emprunt(Boolean s){ statut_Emprunt = s;}
    public void setPenalite(Double p){penalite = p;}
    public void setIdAdherentBD(int idAdherentBD) { this.idAdherentBD = idAdherentBD; }
    public void setIdDocumentBD(int idDocumentBD) { this.idDocumentBD = idDocumentBD; }

}
