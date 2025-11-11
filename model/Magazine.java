package sgeb.model;

public class Magazine extends Document{
    private int Numero_Magazine;
    private String Periodicite;

    public Magazine(String titre, String editeur, int anneePublication, Genre genre, int numero_Magazine, String periodicite) {
        super(titre, editeur, anneePublication, genre);
        this.Numero_Magazine = numero_Magazine;
        this.Periodicite = periodicite;
    }
    
}