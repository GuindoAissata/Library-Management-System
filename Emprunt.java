package sgeb.model;

import java.time.LocalDate;

public class Emprunt {
    private static int id_emprunt; // car un emprunt est unique
    private Adherent adherent; // pour savoir quel adherent a pris le document
    private Document document; // de quel document s'agit-il
    private final int DUREE_MAX_EMPRUNT = 28; // en jours
    private LocalDate dateEmprunt; // quand est-ce qu'il a ete emprunte
    private LocalDate dateRetourPrevue; // quand est-ce qu'il doit etre rendu
    private LocalDate dateRetourReelle; // quand est-ce qu'il a ete rendu et si retard -> penalite


    public Emprunt(Adherent adherent, Document document, LocalDate dateEmprunt, LocalDate dateRetourReelle){
        id_emprunt++;
        this.adherent = adherent;
        this.document = document;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateEmprunt.plusDays(DUREE_MAX_EMPRUNT);
        this.dateRetourReelle = dateRetourReelle;
    }

    // Getters utiles
    public Adherent getAdherent() {
        return adherent;
    }

    public Document getDocument() {
        return document;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public LocalDate getDateRetourReelle() {
        return dateRetourReelle;
    }

    public int getId_emprunt() {
        return id_emprunt;
    }
}
