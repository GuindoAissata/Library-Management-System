import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import Model.*;

public class FenetrePopUp{


    public static <T> void afficherPopupSuppression(T element, TableView<T> table) {

        // Créer une nouvelle fenêtre (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Confirmation de suppression");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Bloque l'interaction avec la fenêtre principale
        
        // Créer le layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");
        
        
        // Message de confirmation
        Label messageLabel = new Label("Voulez-vous vraiment supprimer cet élément ?");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        
        // Message d'avertissement
        Label warningLabel = new Label("Cette action est irréversible !");
        warningLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        warningLabel.setTextFill(Color.RED);
        
        // Boutons
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button annulerBtn = new Button("Annuler");
        annulerBtn.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-padding: 10 30; -fx-font-size: 14;");
        annulerBtn.setOnAction(e -> popupStage.close()); // Pour fermer la fenêtre
        
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
        supprimerBtn.setOnAction(e -> {
            // Supprimer l'élément de la table
            //table.getItems().remove(element);
            //////////// RAJOUTER LES MANINUP COTE BDD JE CROIS
            /// ////////////////////
            /// ///////////////////////
            
            // Afficher un message de confirmation
            afficherMessageSucces("Suppression effectuée avec succès !");
            
            // Fermer la popup
            popupStage.close();
        });
        
        buttonsBox.getChildren().addAll(annulerBtn, supprimerBtn);
        
        // Ajouter tous les éléments au layout
        layout.getChildren().addAll(messageLabel, warningLabel, buttonsBox);
        
        // Créer la scène
        Scene scene = new Scene(layout, 400, 400);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        
        // Afficher la popup
        popupStage.showAndWait();
    }


    public static void afficherPopupModifierDocument(Document document, TableView<Document> table) {

        // Créer une nouvelle fenêtre (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Détails du document");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Bloque l'interaction avec la fenêtre principale
        
        // Créer le layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");
        
        
        // Message
        Label messageLabel = new Label("Modifier les informations document");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        
        // Formulaire avec champs pré-remplis
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // ID (non modifiable)
        Label idLabel = new Label("ID du document : ");
        Label idDetail = new Label(String.valueOf(document.getidDoc()));
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Type (non modifiable)
        Label typeLabel = new Label("Type : ");
        Label typeDetail = new Label(document.getType());
        typeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));


        // Titre
        Label titreLabel = new Label("Titre : ");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField titreField = new TextField();
        titreField.setPrefWidth(200);
        titreField.setText(document.getTitre());

        // Ajouter dans la grid (attention : colonne, ligne)
        grid.add(idLabel, 0, 0);
        grid.add(idDetail, 1,0);
        grid.add(typeLabel, 0, 1);
        grid.add(typeDetail, 1, 1);
        grid.add(titreLabel, 0, 2);
        grid.add(titreField, 1, 2);

        if (document instanceof Livre){
            Livre livre = (Livre) document;

            // Auteur (modifiable)
            Label auteurLabel = new Label("Auteur : ");
            auteurLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField auteurField = new TextField();
            auteurField.setText(livre.getAuteur());

            // Editeur (modifiable)
            Label editeurLabel = new Label("Editeur : ");
            editeurLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField editeurField = new TextField();
            editeurField.setText(livre.getEditeur());

            // ISBN (modifiable)
            Label isbnLabel = new Label("ISBN : ");
            isbnLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField isbnField = new TextField();
            isbnField.setText(livre.getISBN_ISSN());

            // Nombre de pages (modifiable)
            Label nbPageLabel = new Label("Nombre de pages : ");
            nbPageLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField nbPageField = new TextField();
            nbPageField.setText(String.valueOf(livre.getNombre_page()));

            // Mise en page du reste (colonne, ligne)
            grid.add(auteurLabel, 0, 3);
            grid.add(auteurField, 1, 3);
            grid.add(editeurLabel, 0, 4);
            grid.add(editeurField, 1, 4);
            grid.add(isbnLabel, 0, 5);
            grid.add(isbnField, 1, 5);
            grid.add(nbPageLabel, 0, 6);
            grid.add(nbPageField, 1, 6);
        }

        else if (document instanceof Magazine){
            Magazine mag = (Magazine) document;

            // Editeur (modifiable)
            Label editeurLabel = new Label("Editeur : ");
            editeurLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField editeurField = new TextField();
            editeurField.setText(mag.getEditeur());

            // Numéro (modifiable)
            Label numLabel = new Label("Numéro : ");
            numLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField numField = new TextField();
            numField.setText(String.valueOf(mag.getNumero()));

            // Périodicité (modifiable)
            Label periodiciteLabel = new Label("Périodicité : ");
            periodiciteLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField periodiciteField = new TextField();
            periodiciteField.setText(String.valueOf(mag.getPeriodicite()));

            // ISSN (modifiable)
            Label issnLabel = new Label("ISSN : ");
            issnLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            TextField issnField = new TextField();
            issnField.setText(String.valueOf(mag.getISBN_ISSN()));

            // Mise en page du reste (colonne, ligne)
            grid.add(editeurLabel, 0, 3);
            grid.add(editeurField, 1, 3);
            grid.add(numLabel, 0, 4);
            grid.add(numField, 1, 4);
            grid.add(periodiciteLabel, 0, 5);
            grid.add(periodiciteField, 1 ,5);
            grid.add(issnLabel, 0, 6);
            grid.add(issnField, 1, 6);
        } 
          

        Label statutLabel = new Label("Statut : ");
        statutLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        Label statutDetail = new Label((document.getEstDisponible()) ? "Disponible" : "Emprunté");
        grid.add(statutLabel, 0, 7);
        grid.add(statutDetail, 1, 7);
        
        // Message d'avertissement
        Label warningLabel = new Label("Attention : vous allez modifier \nles informations du document !");
        warningLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        warningLabel.setTextFill(Color.RED);
        
        // Boutons
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button annulerBtn = new Button("Annuler");
        annulerBtn.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-padding: 10 30; -fx-font-size: 14;");
        annulerBtn.setOnAction(e -> popupStage.close()); // Pour fermer la fenêtre
        
        Button modifierBtn = new Button("Modifier");
        modifierBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
        modifierBtn.setOnAction(e -> {
            // Modifier le document de la table
            //table.getItems().remove(document);
            //////////// RAJOUTER LES MANINUP COTE BDD JE CROIS
            /// ////////////////////
            /// ///////////////////////
            
            // Afficher un message de confirmation
            afficherMessageSucces("Informations modifiées avec succès !");
            
            // Fermer la popup
            popupStage.close();
        });
        
        buttonsBox.getChildren().addAll(annulerBtn, modifierBtn);
        
        // Ajouter tous les éléments au layout
        layout.getChildren().addAll(messageLabel, grid, warningLabel, buttonsBox);
        
        // Créer la scène
        Scene scene = new Scene(layout, 600, 600);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        
        // Afficher la popup
        popupStage.showAndWait();
    }

    public static void afficherPopupModifierAdherent(Adherent adherent, TableView<Adherent> table) {

        // Créer une nouvelle fenêtre (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Détails de l'adhérent");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Bloque l'interaction avec la fenêtre principale
        
        // Créer le layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");
        
        
        // Message
        Label messageLabel = new Label("Modifier les informations de l'adhérent");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        
        // Formulaire avec champs pré-remplis
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // ID (non modifiable)
        Label idLabel = new Label("ID de l'adhérent : ");
        Label idDetail = new Label(String.valueOf(adherent.getID()));
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    
        // Nom (modifiable)
        Label nomLabel = new Label("Nom : ");
        nomLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField nomField = new TextField();
        nomField.setPrefWidth(200);
        nomField.setText(adherent.getNom());

        // Prénom (modifiable)
        Label prenomLabel = new Label("Prenom : ");
        prenomLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField prenomField = new TextField();
        prenomField.setPrefWidth(200);
        prenomField.setText(adherent.getPrenom());

        // Mail (modifiable)
        Label mailLabel = new Label("Adresse mail : ");
        mailLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField mailField = new TextField();
        mailField.setPrefWidth(200);
        mailField.setText(adherent.getMail());

        // Contact (modifiable)
        Label contactLabel = new Label("Contact : ");
        contactLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField contactField = new TextField();
        contactField.setPrefWidth(200);
        contactField.setText(adherent.getContact());

        // Penalite
        Label penaliteLabel = new Label("Pénalité : ");
        penaliteLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        TextField penaliteField = new TextField();
        penaliteField.setPrefWidth(200);
        penaliteField.setText(String.valueOf(adherent.getPenalite()));

        // Ajouter toutes ces infos dans la grid (attention : colonne, ligne)
        grid.add(idLabel, 0, 0);
        grid.add(idDetail, 1, 0);
        grid.add(nomLabel, 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(prenomLabel, 0, 2);
        grid.add(prenomField, 1, 2);
        grid.add(mailLabel, 0, 3);
        grid.add(mailField, 1, 3);
        grid.add(contactLabel, 0, 4);
        grid.add(contactField, 1, 4);
        grid.add(penaliteLabel, 0, 5);
        grid.add(penaliteField, 1, 5);

        
        // Message d'avertissement
        Label warningLabel = new Label("Attention : vous allez modifier \nles informations de l'adhérent !");
        warningLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        warningLabel.setTextFill(Color.RED);
        
        // Boutons
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button annulerBtn = new Button("Annuler");
        annulerBtn.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-padding: 10 30; -fx-font-size: 14;");
        annulerBtn.setOnAction(e -> popupStage.close()); // Pour fermer la fenêtre
        
        Button modifierBtn = new Button("Modifier");
        modifierBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
        modifierBtn.setOnAction(e -> {
            // Modifier l'adhérent de la table
            //////////// RAJOUTER LES MANINUP COTE BDD JE CROIS
            /// ////////////////////
            /// ///////////////////////
            
            // Afficher un message de confirmation
            afficherMessageSucces("Informations modifiées avec succès !");
            
            // Fermer la popup
            popupStage.close();
        });
        
        buttonsBox.getChildren().addAll(annulerBtn, modifierBtn);
        
        // Ajouter tous les éléments au layout
        layout.getChildren().addAll(messageLabel, grid, warningLabel, buttonsBox);
        
        // Créer la scène
        Scene scene = new Scene(layout, 600, 600);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        
        // Afficher la popup
        popupStage.showAndWait();
    }


    public static void afficherPopupDetailEmprunt(Emprunt emprunt, TableView<Emprunt> table) {
    
    // Récupérer le document et l'adhérent depuis l'emprunt
    Document doc = emprunt.getDocument();
    Adherent adherent = emprunt.getAdherent();
    
    // Créer une nouvelle fenêtre (Stage)
    Stage popupStage = new Stage();
    popupStage.setTitle("Détails de l'emprunt");
    popupStage.initModality(Modality.APPLICATION_MODAL);
    
    // Créer le layout principal avec ScrollPane
    ScrollPane scrollPane = new ScrollPane();
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(30));
    layout.setAlignment(Pos.TOP_CENTER);
    layout.setStyle("-fx-background-color: white;");
    
    // Titre principal
    Label titleLabel = new Label("Détails de l'emprunt");
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
    
    // ========== SECTION ADHÉRENT ==========
    VBox adherentSection = new VBox(10);
    adherentSection.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");
    
    Label adherentTitle = new Label("Informations de l'adhérent");
    adherentTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
    adherentTitle.setTextFill(Color.DARKBLUE);
    
    GridPane adherentGrid = new GridPane();
    adherentGrid.setHgap(15);
    adherentGrid.setVgap(10);
    adherentGrid.setPadding(new Insets(10, 0, 0, 0));
    
    // Informations adhérent
    ajouterLigneInfo(adherentGrid, 0, "ID :", String.valueOf(adherent.getID()));
    ajouterLigneInfo(adherentGrid, 1, "Nom :", adherent.getNom());
    ajouterLigneInfo(adherentGrid, 2, "Prénom :", adherent.getPrenom());
    ajouterLigneInfo(adherentGrid, 3, "Email :", adherent.getMail());
    ajouterLigneInfo(adherentGrid, 4, "Contact :", adherent.getContact());
    ajouterLigneInfo(adherentGrid, 5, "Pénalité :", adherent.getPenalite() + " €");
    ajouterLigneInfo(adherentGrid, 6, "Emprunts en cours :", String.valueOf(adherent.getNb_Emprunt_Encours()) + "/5");
    
    adherentSection.getChildren().addAll(adherentTitle, adherentGrid);
    
    // ========== SECTION DOCUMENT ==========
    VBox documentSection = new VBox(10);
    documentSection.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");
    
    Label documentTitle = new Label("Informations du document");
    documentTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
    documentTitle.setTextFill(Color.DARKGOLDENROD);
    
    GridPane documentGrid = new GridPane();
    documentGrid.setHgap(15);
    documentGrid.setVgap(10);
    documentGrid.setPadding(new Insets(10, 0, 0, 0));
    
    int row = 0;
    ajouterLigneInfo(documentGrid, row++, "ID :", doc.getidDoc());
    ajouterLigneInfo(documentGrid, row++, "Titre :", doc.getTitre());
    
    // Informations spécifiques selon le type de document
    if (doc instanceof Livre) {
        Livre livre = (Livre) doc;
        ajouterLigneInfo(documentGrid, row++, "Type :", "Livre");
        ajouterLigneInfo(documentGrid, row++, "Auteur :", livre.getAuteur());
        ajouterLigneInfo(documentGrid, row++, "Éditeur :", livre.getEditeur());
        ajouterLigneInfo(documentGrid, row++, "ISBN :", livre.getISBN_ISSN());
        ajouterLigneInfo(documentGrid, row++, "Nombre de pages :", String.valueOf(livre.getNombre_page()));
        
    } else if (doc instanceof Magazine) {
        Magazine magazine = (Magazine) doc;
        ajouterLigneInfo(documentGrid, row++, "Type :", "Magazine");
        ajouterLigneInfo(documentGrid, row++, "Éditeur :", magazine.getEditeur());
        ajouterLigneInfo(documentGrid, row++, "ISSN :", magazine.getISBN_ISSN());
        ajouterLigneInfo(documentGrid, row++, "Numéro :", String.valueOf(magazine.getNumero()));
        ajouterLigneInfo(documentGrid, row++, "Périodicité :", magazine.getPeriodicite());
    }
    
    ajouterLigneInfo(documentGrid, row++, "Statut :", doc.getEstDisponible() ? "Disponible" : "Emprunté");
    
    documentSection.getChildren().addAll(documentTitle, documentGrid);
    
    // ========== SECTION EMPRUNT ==========
    VBox empruntSection = new VBox(10);
    empruntSection.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");
    
    Label empruntTitle = new Label("Informations de l'emprunt");
    empruntTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
    empruntTitle.setTextFill(Color.DARKGREEN);
    
    GridPane empruntGrid = new GridPane();
    empruntGrid.setHgap(15);
    empruntGrid.setVgap(10);
    empruntGrid.setPadding(new Insets(10, 0, 0, 0));
    
    ajouterLigneInfo(empruntGrid, 0, "ID Emprunt :", String.valueOf(emprunt.getIdEmprunt()));
    ajouterLigneInfo(empruntGrid, 1, "Date d'emprunt :", emprunt.getDate_Emprunt().toString());
    ajouterLigneInfo(empruntGrid, 2, "Date de retour prévue :", emprunt.getDate_Retour_Prevue().toString());
    
    if (emprunt.getDate_Retour_Effective() != null) {
        ajouterLigneInfo(empruntGrid, 3, "Date de retour effective :", emprunt.getDate_Retour_Effective().toString());
    } else {
        ajouterLigneInfo(empruntGrid, 3, "Date de retour effective :", "En cours d'emprunt");
    }
    
    // Calculer les jours de retard si applicable
    LocalDate aujourdhui = LocalDate.now();
    LocalDate dateRetourPrevue = emprunt.getDate_Retour_Prevue();
    
    if (emprunt.getDate_Retour_Effective() == null && aujourdhui.isAfter(dateRetourPrevue)) {
        long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, aujourdhui);
        Label retardLabel = new Label("RETARD : " + joursRetard + " jour(s)");
        retardLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        retardLabel.setTextFill(Color.RED);
        empruntGrid.add(retardLabel, 0, 4, 2, 1);
    }
    
    empruntSection.getChildren().addAll(empruntTitle, empruntGrid);
    
    // ========== BOUTONS ==========
    HBox buttonsBox = new HBox(15);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.setPadding(new Insets(20, 0, 0, 0));
    
    Button fermerBtn = new Button("Fermer");
    fermerBtn.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
    fermerBtn.setOnAction(e -> popupStage.close());
    
    Button modifierBtn = new Button("Modifier");
    modifierBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
    modifierBtn.setOnAction(e -> {
        popupStage.close();
        afficherPopupModificationEmprunt(emprunt, table);
    });
    
    Button supprimerBtn = new Button("Supprimer");
    supprimerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
    supprimerBtn.setOnAction(e -> {
        popupStage.close();
        afficherPopupSuppression(
            emprunt,
            table,
            "Voulez-vous vraiment supprimer cet emprunt ?",
            (emp) -> EmpruntDAO.supprimerEmprunt(emp.getIdEmprunt())
        );
    });
    
    buttonsBox.getChildren().addAll(fermerBtn, modifierBtn, supprimerBtn);
    
    // Ajouter toutes les sections au layout
    layout.getChildren().addAll(
        titleLabel,
        empruntSection,
        adherentSection,
        documentSection,
        buttonsBox
    );
    
    scrollPane.setContent(layout);
    scrollPane.setFitToWidth(true);
    
    // Créer la scène
    Scene scene = new Scene(scrollPane, 700, 800);
    popupStage.setScene(scene);
    popupStage.setResizable(true);
    
    // Afficher la popup
    popupStage.showAndWait();
}

// ========== MÉTHODE HELPER POUR AJOUTER UNE LIGNE D'INFO ==========
private static void ajouterLigneInfo(GridPane grid, int row, String label, String value) {
    Label labelNode = new Label(label);
    labelNode.setFont(Font.font("System", FontWeight.BOLD, 14));
    
    Label valueNode = new Label(value);
    valueNode.setFont(Font.font("System", 14));
    valueNode.setWrapText(true);
    valueNode.setMaxWidth(400);
    
    grid.add(labelNode, 0, row);
    grid.add(valueNode, 1, row);
}














    

// Méthode pour afficher un message de succès
private static void afficherMessageSucces(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Succès");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    public static void main(String[] args){
        //
    }
    
}
