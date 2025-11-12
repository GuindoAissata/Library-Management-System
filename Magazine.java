package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Magazine extends Document {
    private Double numero_periodicite;
    private Double numero;

    /**
     * Default constructor
     */
    public Magazine(int id, String titre, String auteur, double numero, double numero_periodicite) {
        super(id, titre, auteur);
        this.numero = numero; 
        this.numero_periodicite = numero_periodicite;
    }

    /**
     * 
     */
     //////////////getter////////
    public Double getNumero_peridicite(){return  numero_periodicite;}
    public Double getNumero(){return numero;}
    
    /////////   Setter////////
    public void setNumero_periodicite(Double n ){  numero_periodicite = n;}
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