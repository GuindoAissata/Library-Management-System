package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Magazine extends Document {
    private String ISSN;
    private int numero;
    private String periodicite; // dire si c'est hebdomadaire, mensuel, trimestriel

    /**
     * Default constructor
     */
    public Magazine(String titre, String editeur, int numero, String periodicite, String ISSN) {
        super(titre, editeur);
        this.numero = numero; 
        this.ISSN = ISSN;
        this.periodicite = periodicite;
    }

    /**
     * 
     */
     //////////////getter////////
    @Override
    public String getISBN_ISSN(){return  ISSN;}
    public int getNumero(){return numero;}
    public String getPeriodicite(){return periodicite;}

    
    /////////   Setter////////
    public void setISSN(String n ){  ISSN = n;}
    public void setNumero(int n){ numero = n;}
    public void setPeriodicite(String p){periodicite = p;}
    
    ////////////genre //////////
    @Override
    public String getType() {
        return "Magazine";
    }
    public String toString(){
        return super.toString() + " ; Genre : " + getType() + " ; Numero : " + numero + " ; periodicité : " +periodicite + " ; ISSN : " + ISSN;
    }
    

}
