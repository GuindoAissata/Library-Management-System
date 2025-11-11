package sgeb.model;

import sgeb.exception.DocumentException;

public abstract class Document{
    protected static int cpt_id = 0; // compteur pour générer des ids uniques
    protected int id_document; // car un document est unique
    protected String titre;
    protected String editeur;
    protected int anneePublication;
    protected Genre genre;
    protected boolean disponible;


    // Constructeur

    public Document(String titre, String editeur, int anneePublication, Genre genre) {
        cpt_id++;
        this.id_document = cpt_id;
        this.titre = titre;
        this.editeur = editeur;
        this.anneePublication = anneePublication;
        this.genre = genre;
        this.disponible = true;
    }

    // Getters et Setters
    
    public int getId_document() {
        return id_document;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) throws DocumentException {
        if (!verificationTitre(titre)) {
            throw new DocumentException("Format du titre invalide (ne peut pas être vide)");
        }
        this.titre = titre.trim().replaceAll("\\s+", " ");
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) throws DocumentException {
        if (!verificationEditeur(editeur)) {
            throw new DocumentException("Format de l'éditeur invalide (ne peut pas être vide)");
        }
        this.editeur = editeur.trim().replaceAll("\\s+", " ");
    } 

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) throws DocumentException {
        if( !verificationAnneePublication(anneePublication)) {
            throw new DocumentException("Année de publication invalide (doit être entre 1900 et l'année actuelle)");
        }
        this.anneePublication = anneePublication;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (!verificationGenre(genre)) {
            throw new IllegalArgumentException("Genre invalide");
        }
        this.genre = Genre.valueOf(genre); // Conversion de String à Genre
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Document [id_document=" + id_document + ", titre=" + titre + ", editeur=" + editeur
                + ", anneePublication=" + anneePublication + ", genre=" + genre + ", disponible=" + disponible + "]";
    }




    // METHODES DE VERIFICATION DES FORMATS DES CHAMPS

    // TITRE
    protected boolean verificationTitre(String titre) {
        return titre != null && !titre.trim().isEmpty();
    }

    // EDITEUR
    protected boolean verificationEditeur(String editeur) {
        return editeur != null && !editeur.trim().isEmpty();
    }

    // ANNEE PUBLICATION
    protected boolean verificationAnneePublication(int anneePublication) {
        int anneeActuelle = 2025;
        return anneePublication >= 1900 && anneePublication <= anneeActuelle;
    }

    // GENRE
    protected boolean verificationGenre(String genre) {
        if (genre == null) {
            return false;
        }
        for (Genre g : Genre.values()) {
            if (g.name().equalsIgnoreCase(genre)) {
                return true;
            }
        }
        return false;
    }




    
}
