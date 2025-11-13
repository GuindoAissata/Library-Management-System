package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Livre extends Document {
    private String auteur;
    private int Nombre_page;
    private String ISBN;

    /**
     * Default constructor
     */
    public Livre(String titre, String auteur, String editeur, String ISBN, int Nombre_page) {
        super(titre, editeur);
        this.auteur = auteur;
        this.ISBN = ISBN;
        this.Nombre_page = Nombre_page; 


    }

    /**
     * 
     */
     //////////////getter////////
    public String getAuteur(){return auteur;}
    public int getNombre_page(){return  Nombre_page;}
    public String getISBN(){return ISBN;}
    
    /////////   Setter////////
    public void setAuteur(String a){auteur = a;}
    public void setNombre_page(int n ){  Nombre_page = n;}
    public void setISBN(String i){ ISBN = i;}
    
    ////////////genre //////////
    @Override
    public String getType() {
        return "Livre";
    }
    public String toString(){
        return super.toString() + " ; Auteur : " + auteur + " ; Genre : " + getType() + " ; ISBN : " + ISBN + " ; Nombre de page : " +Nombre_page;
    }

}
