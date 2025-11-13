package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Magazine extends Document {
    private String ISSN;
    private Double numero;

    /**
     * Default constructor
     */
    public Magazine(int id, String titre, String auteur, double numero, String ISSN) {
        super(id, titre, auteur);
        this.numero = numero; 
        this.ISSN = ISSN;
    }

    /**
     * 
     */
     //////////////getter////////
    public String getNumero_peridicite(){return  ISSN;}
    public Double getNumero(){return numero;}
    
    /////////   Setter////////
    public void setNumero_periodicite(String n ){  ISSN = n;}
    public void setNumero(Double n){ numero = n;}
    
    ////////////genre //////////
    @Override
    public String getType() {

        return "Magazine";
    }
    public String toString(){
        return super.toString() + " ; Genre : " + getType() + " ; Numero : " + numero + " ; Numero_periodicité : " +numero_periodicite;
    }
    

}