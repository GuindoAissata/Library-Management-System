package Vue;

import java.time.LocalDate;
import java.util.Arrays;

import Model.Adherent;
import Model.BibliothequeManager;
import Model.Emprunt;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupHistorique {

    /**
     * Affiche une fenêtre modale avec l'historique des emprunts d'un adhérent.
     */
    public static void afficher(Adherent adherent) {

        // Fenêtre popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Historique des emprunts");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Layout principal
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: white;");

        // ========== EN-TÊTE ==========
        Label titleLabel = new Label(
                "Historique des emprunts de " + adherent.getPrenom() + " " + adherent.getNom()
        );
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        // ========== TABLEAU DES EMPRUNTS ==========
        TableView<Emprunt> tableEmprunts = new TableView<>();

        // Colonne ID de l'emprunt
        TableColumn<Emprunt, String> idCol = new TableColumn<>("ID de l'Emprunt");
        idCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getID_Emprunt()))
        );
        // colonne ID  document
        idCol.setPrefWidth(120);
        TableColumn<Emprunt, String> idDocCol = new TableColumn<>("ID du document");
        idDocCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().document.getidDoc()))
        );
        idDocCol.setPrefWidth(120);

        // Colonne ISBN/ISSN (du document)
        TableColumn<Emprunt, String> isbn_issnCol = new TableColumn<>("ISBN/ISSN");
        isbn_issnCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().document.getISBN_ISSN())
                // ou cellData.getValue().getDocument().getISBN_ISSN() si tu as un getter
        );
        isbn_issnCol.setPrefWidth(130);

        // Colonne Type
        TableColumn<Emprunt, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().document.getType())
        );
        typeCol.setPrefWidth(100);

        // Colonne Titre
        TableColumn<Emprunt, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().document.getTitre())
        );
        titreCol.setPrefWidth(200);

        // Colonne Auteur
        TableColumn<Emprunt, String> authorCol = new TableColumn<>("Auteur");
        authorCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().document.getAuteur())
        );
        authorCol.setPrefWidth(150);

        // Colonne Editeur
        TableColumn<Emprunt, String> editorCol = new TableColumn<>("Editeur");
        editorCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().document.getEditeur())
        );
        editorCol.setPrefWidth(150);

        // Colonne Date Emprunt
        TableColumn<Emprunt, String> dateEmpruntCol = new TableColumn<>("Date Emprunt");
        dateEmpruntCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_Emprunt().toString())
        );
        dateEmpruntCol.setPrefWidth(120);

        // Colonne Date Retour Prévue
        TableColumn<Emprunt, String> dateRetourPrevueCol = new TableColumn<>("Retour Prévu");
        dateRetourPrevueCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_RetourPrevue().toString())
        );
        dateRetourPrevueCol.setPrefWidth(120);

        // Colonne Date Retour Réelle
        TableColumn<Emprunt, String> dateRetourReelleCol = new TableColumn<>("Retour Réel");
        dateRetourReelleCol.setCellValueFactory(cellData -> {
        LocalDate dateRetour = cellData.getValue().getDate_RetourReelle();
        String affichage = (dateRetour != null) ? dateRetour.toString() : "En cours";
        return new SimpleStringProperty(affichage);
        });
        dateRetourReelleCol.setPrefWidth(120);
        // Colonne Editeur
        TableColumn<Emprunt, String> penaliteCol = new TableColumn<>("Penalité");
        penaliteCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPenalite()))
        );
        penaliteCol.setPrefWidth(150);

        // Ajouter les colonnes
        tableEmprunts.getColumns().addAll(Arrays.asList(
                idCol,
                idDocCol,
                isbn_issnCol,
                typeCol,
                titreCol,
                //authorCol,
                //editorCol,
                dateEmpruntCol,
                dateRetourPrevueCol,
                dateRetourReelleCol,
                penaliteCol
        ));

        // Charger les données : la liste des emprunts de cet adhérent
        // Si List_Emprunt est un Set ou List dans Adherent :
        ObservableList<Emprunt> historique =
                FXCollections.observableArrayList(adherent.List_Emprunt);
        tableEmprunts.setPrefHeight(400);
        tableEmprunts.setItems(historique);
        tableEmprunts.setPrefHeight(300);

        // ========== BOUTON FERMER ==========
        Button fermerBtn = new Button("Fermer");
        fermerBtn.setStyle(
                "-fx-background-color: gray; -fx-text-fill: white; " +
                "-fx-padding: 10 30; -fx-font-size: 14;"
        );


        fermerBtn.setOnAction(e -> popupStage.close());

        //============Label nombre d'emprunt encours========
        Label labelNombre = new Label("Nombre d'emprunt en cours : " + adherent.getNb_Emprunt_Encours());
        labelNombre.setFont(Font.font(14));


        // Ajouter au layout
        layout.getChildren().addAll(titleLabel, tableEmprunts, fermerBtn,labelNombre);

        // Créer la scène
        Scene scene = new Scene(layout, 1200, 600);
        popupStage.setScene(scene);
        popupStage.setResizable(true);

        popupStage.showAndWait();
}
}
