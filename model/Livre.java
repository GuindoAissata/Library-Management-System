package sgeb.model;

public class Livre extends Document{
    private String ISBN;
    private String auteur;
    private int nbPages;

    public Livre(String titre, String editeur, int anneePublication, Genre genre, String ISBN, String auteur, int nbPages) {
        super(titre, editeur, anneePublication, genre);
        this.ISBN = ISBN;
        this.auteur = auteur;
        this.nbPages = nbPages;
    }


    public String getISBN(){
        return ISBN;
    }

    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }

    public String getAuteur(){
        return auteur;
    }

    public void setAuteur(String auteur){
        this.auteur = auteur;
    }

    public int getNbPages(){
        return nbPages;
    }

    public void setNbPages(int nbPages){
        this.nbPages = nbPages;
    }





    @Override
    public boolean equals(Object livre1) {
        if (this == livre1){
            return true;
        }
        if (livre1 == null || getClass() != livre1.getClass()){
            return false;
        }
        Livre livre2 = (Livre) livre1; // caster pour que ce soit bien une comparaison entre livres
        if(this.ISBN !=null && livre2.ISBN != null){
            return (this.ISBN.equals(livre2.ISBN));
        }
        return false;
    }


    @Override
    public String toString() {
        return "Livre [id_document=" + id_document + ", titre=" + titre + ", editeur=" + editeur
                + ", anneePublication=" + anneePublication + ", genre=" + genre + ", disponible=" + disponible
                + ", ISBN=" + ISBN + ", auteur=" + auteur + ", nbPages=" + nbPages + "]";
    }
    
}
