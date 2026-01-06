package Controler;

import Model.Adherent;
import Model.ArgumentException;
import Model.BibliothequeManager;
import Model.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AjouterAdherent {
    private BibliothequeManager manager;
    private TextField nom1;
    private TextField prenom1;
    private  TextField mail1;
    private  TextField contact1;
    private Button addBtn;
    private TableView <Adherent> adherentsTable;


    public AjouterAdherent(BibliothequeManager manager,TextField n , TextField p , TextField m , TextField c, Button a,TableView<Adherent> adherentsTable){
        this.nom1 = n;
        this.prenom1 = p;
        this.mail1 = m;
        this.contact1 = c;
        this.manager = manager; 
        this.addBtn = a;
        this.adherentsTable = adherentsTable;

        CreerAdherent();
    }
    public void CreerAdherent(){
        addBtn.setOnAction(e -> { 
            // recupérer les données saisies sur l'interface
        String nom = nom1.getText();
        String prenom = prenom1.getText();
        String mail =  mail1.getText();
        String contact = contact1.getText();

        //Vérification de champs vides 
        if(nom == null || nom.isBlank() || prenom == null || prenom.isBlank() || mail == null || mail.isBlank()|| contact == null || contact.isBlank()){
            showError("Champs manquants",
        "Veuillez remplir tous les champs (nom, prénom, email, téléphone).") ;
        return ;
        }
        try{
            // utiliser le constructeur pour créer un adherrent 
        Adherent adherent = new Adherent(nom, prenom, mail, contact);
        // Enregistrer l'Adhérent dans la liste des Adherents (BD + List)
            try {
                manager.AddAdherent(adherent);
                showInfo("Adhérent ajouté", "L'Adhérent "+ adherent.getID_Adherent()+" été enregistré avec succès !");
                // refresh
                adherentsTable.refresh();
                nom1.clear();
                prenom1.clear();
                mail1.clear();
                contact1.clear();
                
                } catch (ArgumentException execpt) {
                showError("Adresse mail déjà enregistré: ", execpt.getMessage());
                }
        }catch(IllegalArgumentException ex){
            showError("Données invalides", ex.getMessage());
        }

        });
    }

    private void showError(String titre, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }



