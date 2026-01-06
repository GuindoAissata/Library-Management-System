package Controler;

import Model.BibliothequeManager;
import Model.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class AfficherDocuments {
    private BibliothequeManager manager;

    public AfficherDocuments(BibliothequeManager manager){
        this.manager = manager; 
    }

    //Remplit la TableView avec les documents du modèle

    public void chargerDocuments(TableView<Document> table){
    ObservableList<Document> data =
            FXCollections.observableArrayList(manager.getList_Document());

        table.setItems(data);
    }
}
