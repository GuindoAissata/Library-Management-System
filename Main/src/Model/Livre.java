package Model;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Livre extends Document {
    private Double Nombre_page;
    private String ISBN;

    /**
     * Default constructor
     */
    public Livre(int id,String titre, String auteur, String ISBN, double Nombre_page) {
        super(id, titre, auteur);
        this.ISBN = ISBN;
        this.Nombre_page = Nombre_page; 


    }

    /**
     * 
     */
     //////////////getter////////
    public Double getNombre_page(){return  Nombre_page;}
    public String getISBN(){return ISBN;}
    
    /////////   Setter////////
    public void setNombre_page(Double n ){  Nombre_page = n;}
    public void setISBN(String i){ ISBN = i;}
    
    ////////////genre //////////
    @Override
    public String getType() {

        return "Livre";
    }
    public String toString(){
        return super.toString() + " ; Genre : " + getType() + " ; ISBN : " + ISBN + " ; Nombre de page : " +Nombre_page;
    }

}