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
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import Controler.AfficherDocuments;
import Controler.AjouterAdherent;
import Model.*;

public class Interface extends Application {

    private BibliothequeManager bibliothequeManager = new BibliothequeManager();
    //private AfficherDocuments  afficherDocuments= new AfficherDocuments(manager);
    public void TestVue(){ 
    Livre livre1 = new Livre("Alice au pays des merveille", "Lewis Carrol","editeur", "fdfef",192);
    bibliothequeManager.AddDocument(livre1);}


    private TabPane tabPane;

    
    //@SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SGEB - Bibliothèque");
        primaryStage.setMaximized(true);
        
        // Créer le layout principal
        BorderPane layoutPrincipal = new BorderPane(); // BorderPane pour 5 zones : top, bottom, left, right, center
        layoutPrincipal.setTop(creerEntete());
        
        // Créer un tab pane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Add tabs
        tabPane.getTabs().addAll(
            creerPageDocuments(),
            createMembersTab(),
            createBorrowTab(),
            createReturnTab()
            //createAlertsTab()
        );
        
        layoutPrincipal.setCenter(tabPane);
        
        Scene scene = new Scene(layoutPrincipal);
        scene.getStylesheets().add(getStylesheet());
        primaryStage.setScene(scene); // associe scene à primaryStage
        primaryStage.show(); // afficher la fenetre a l'écran
    }
    
    private VBox creerEntete() {
        VBox entete = new VBox(); // Verticale box
        entete.setStyle("-fx-background-color: MIDNIGHTBLUE");
        entete.setPadding(new Insets(20)); // espacement de 20 px
        entete.setSpacing(10);
        
        Label titre = new Label("Système de Gestion des Emprunts de Bibliothèque");
        titre.setFont(Font.font("System", FontWeight.BOLD, 30)); // police, gras, 30
        titre.setTextFill(Color.WHITE);
        
        Label soustitre = new Label("Gérez vos documents, adhérents et emprunts efficacement");
        soustitre.setFont(Font.font("System", 18));
        soustitre.setTextFill(Color.WHITE);
        
        entete.getChildren().addAll(titre, soustitre);
        return entete;
    }
    
    // ==================== Page document ====================
    private Tab creerPageDocuments() {
        Tab tab = new Tab("Gestion des Documents");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Title
        Label titreLabel = new Label("Gestion des Documents");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        // Rechercher un document
        VBox rechercheBox = creerRechercheBox(
            new String[]{"Titre", "Auteur", "ISBN/ISSN", "Type"},
            "Entrez votre recherche..."
        );
        
        // TableVue pour afficher les documents de la base de données
        TableView<Document> table = creerTabVueDocuments();
        
        // Add document form
        VBox ajoutForm = creerFormAjoutDocument();
        
        content.getChildren().addAll(titreLabel, rechercheBox, table, ajoutForm);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        
        return tab;
    }
    
    private TableView<Document> creerTabVueDocuments() {
        TableView<Document> table = new TableView<>();
        
        TableColumn<Document, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getidDoc())));
        idCol.setPrefWidth(150);

        TableColumn<Document, String> isbn_issnCol = new TableColumn<>("ISBN/ISSN");
        isbn_issnCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN_ISSN()));
        isbn_issnCol.setPrefWidth(150);
        

        TableColumn<Document, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        typeCol.setPrefWidth(100);

        
        TableColumn<Document, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getTitre())));
        titreCol.setPrefWidth(250);
        
        TableColumn<Document, String> authorCol = new TableColumn<>("Auteur");
        authorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuteur()));
        authorCol.setPrefWidth(200);

        TableColumn<Document, String> editorCol = new TableColumn<>("Editeur");
        editorCol.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        editorCol.setPrefWidth(200);

        
        TableColumn<Document, String> statusCol = new TableColumn<>("Statut");
        statusCol.setCellValueFactory(cellData -> {
            boolean estDispo = cellData.getValue().getEstDisponible();
            String texte = estDispo ? "Disponible" : "Emprunté";
            return new SimpleStringProperty(texte);
        });
        statusCol.setPrefWidth(120);

        
        TableColumn<Document, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button modifierBtn = new Button("Modifier");
            private final Button supprimerBtn = new Button("Supprimer");
            private final HBox pane = new HBox(5, modifierBtn, supprimerBtn);
            
            {
                modifierBtn.setStyle("-fx-background-color: azure; -fx-text-fill: MIDNIGHTBLUE;");
                supprimerBtn.setStyle("-fx-background-color: pink; -fx-text-fill: RED;");
                pane.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
        
        table.getColumns().addAll(Arrays.asList(idCol,isbn_issnCol,typeCol, titreCol, authorCol, editorCol, statusCol, actionsCol));
        
        // Sample data
    /*   ObservableList<Document> data = FXCollections.observableArrayList(
            new Livre("Le Seigneur des Anneaux", "J.R.R. Tolkien", "Edit", "978-2-253-11235-1", 300),
            new Livre("Les Misérables", "Victor Hugo", "Edit", "978-2-07-036343-8", 700),
            new Magazine("Science & Vie", "Collectif", 5, "Mensuel", "SV-2024-11"),
            new Livre("Python Avancé", "Guido van Rossum", "Edit", "978-2-412-08934-1", 400)
        );
        table.setItems(data);
        */
        //afficherDocuments.chargerDocuments(table);

        //=== Connexion à la liste des documents dans bibliothèquemanager===//
        table.setItems(bibliothequeManager.getList_Document());
        table.setPrefHeight(300);
        return table;
    }
    
    private VBox creerFormAjoutDocument() {
        VBox form = new VBox(10);
        form.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        Label formTitle = new Label("Ajouter un nouveau document");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Livre", "Magazine");
        typeBox.setPromptText("Sélectionner type");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN/ISSN");
        
        TextField titleField = new TextField();
        titleField.setPromptText("Titre");
        
        TextField authorField = new TextField();
        authorField.setPromptText("Auteur");

        TextField editorField = new TextField();
        editorField.setPromptText("Editeur");

        TextField nbpageField = new TextField();
        nbpageField.setPromptText("Nb de pages");

        TextField periodiciteField = new TextField();
        periodiciteField.setPromptText("Periodicité");

        TextField numeroField = new TextField();
        numeroField.setPromptText("Numéro de magazine");
        
        
        grid.add(typeBox, 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(titleField, 2, 0);
        grid.add(editorField,3,0);

        // Les champs qui s'affichent si c'est un document ou un Magazine

    typeBox.setOnAction(e -> {
    String type = typeBox.getValue();

    // Effacer d'abord les champs dynamiques
    grid.getChildren().remove(nbpageField);
    grid.getChildren().remove(periodiciteField);
    grid.getChildren().remove(numeroField);

    if ("Livre".equals(type)) {
        grid.add(authorField, 4, 0);
        grid.add(nbpageField, 5, 0);
    } 
    else if ("Magazine".equals(type)) {
        grid.add(periodiciteField, 4, 0);
        grid.add(numeroField, 5, 0);
    }
});
        
        Button addBtn = new Button("Ajouter le document");
        addBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;");
        
        form.getChildren().addAll(formTitle, grid, addBtn);
        return form;
    }
    
    // ==================== MEMBERS TAB ====================
    private Tab createMembersTab() {
        Tab tab = new Tab("Gestion des Adhérents");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titreLabel = new Label("Gestion des Adhérents");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        VBox searchBox = creerRechercheBox(
            new String[]{"Nom", "ID Adhérent"},
            "Entrez le nom ou l'ID..."
        );
        
        TableView<Adherent> table = createMembersTable();
        VBox ajoutForm = createAddMemberForm();
        
        content.getChildren().addAll(titreLabel, searchBox, table, ajoutForm);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        
        return tab;
    }
    
    private TableView<Adherent> createMembersTable() {
        TableView<Adherent> table = new TableView<>();
        
        TableColumn<Adherent, String> idCol = new TableColumn<>("ID Adhérent");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getID_Adherent()));
        idCol.setPrefWidth(120);
        
        TableColumn<Adherent, String> lastNameCol = new TableColumn<>("Nom");
        lastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        lastNameCol.setPrefWidth(150);
        
        TableColumn<Adherent, String> firstNameCol = new TableColumn<>("Prénom");
        firstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));;
        firstNameCol.setPrefWidth(150);
        
        TableColumn<Adherent, String> mailCol = new TableColumn<>("Adresse mail");
        mailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMail()));
        mailCol.setPrefWidth(150);

        TableColumn<Adherent, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));;
        contactCol.setPrefWidth(200);
        
        TableColumn<Adherent, String> penaltyCol = new TableColumn<>("Pénalité");
        penaltyCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPenalite())));;
        penaltyCol.setPrefWidth(150);
        
        TableColumn<Adherent, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(200);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button historyBtn = new Button("Historique");
            private final Button modifierBtn = new Button("Modifier");
            private final HBox pane = new HBox(5, historyBtn, modifierBtn);
            
            {
                historyBtn.setStyle("-fx-background-color: azure; -fx-text-fill: MIDNIGHTBLUE;");
                modifierBtn.setStyle("-fx-background-color: azure; -fx-text-fill: MIDNIGHTBLUE;");
                pane.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
        
        table.getColumns().addAll(Arrays.asList(idCol, lastNameCol, firstNameCol, mailCol, contactCol, penaltyCol, actionsCol));
        
        /*ObservableList<Adherent> data = FXCollections.observableArrayList(
            new Adherent("Dupont", "Jean", "jean@example.com", "0102030405"),
            new Adherent("Martin", "Marie", "marie@example.com", "0000000000"),
            new Adherent("Bernard", "Pierre", "pierre@example.com", "0101010101")
        );
        table.setItems(data);*/

        //==== Connexion à la liste des Adherents dans bibliothèquemanager===//
        table.setItems(bibliothequeManager.getList_Adherent());
        table.setPrefHeight(300);
        
        return table;
    }
    
    private VBox createAddMemberForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        Label formTitle = new Label("Inscrire un nouvel adhérent");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Nom");
        
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Prénom");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Téléphone");
        
        grid.add(lastNameField, 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(emailField, 2, 0);
        grid.add(phoneField, 3, 0);
        
        Button addBtn = new Button("Inscrire l'adhérent");
        addBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        // ====== Connexion au Controleur pour Ajouter un Adhérent====// 
        new AjouterAdherent(bibliothequeManager, lastNameField,firstNameField, emailField, phoneField, addBtn);
        
        form.getChildren().addAll(formTitle, grid, addBtn);
        return form;
    }
    
    // ==================== BORROW TAB ====================
    private Tab createBorrowTab() {
        Tab tab = new Tab("Nouveaux Emprunts");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titreLabel = new Label("Enregistrer un nouvel emprunt");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        VBox memberSection = createMemberSelectionSection();
        VBox documentSection = createDocumentSelectionSection();
        VBox summarySection = createLoanSummarySection();
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db;");
        Button saveBtn = new Button("Enregistrer l'Emprunt");
        saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(titreLabel, memberSection, documentSection, summarySection, buttons);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        
        return tab;
    }
    
    private VBox createMemberSelectionSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        Label title = new Label("Sélection de l'Adhérent");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        HBox searchBar = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par ID ou Nom...");
        searchField.setPrefWidth(400);
        Button searchBtn = new Button("Rechercher");
        searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white;");
        searchBar.getChildren().addAll(searchField, searchBtn);
        
        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(10);
        info.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        info.add(createInfoLabel("ID :", "-"), 0, 0);
        info.add(createInfoLabel("Nom/Prénom :", "-"), 1, 0);
        info.add(createInfoLabel("Emprunts actuels :", "0/5"), 2, 0);
        info.add(createInfoLabel("Statut Pénalité :", "Aucune"), 3, 0);
        
        section.getChildren().addAll(title, searchBar, info);
        return section;
    }
    
    private VBox createDocumentSelectionSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        Label title = new Label("Sélection du Document");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        HBox searchBar = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par Titre ou ISBN...");
        searchField.setPrefWidth(400);
        Button searchBtn = new Button("Rechercher");
        searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white;");
        searchBar.getChildren().addAll(searchField, searchBtn);
        
        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(10);
        info.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        info.add(createInfoLabel("Titre :", "-"), 0, 0);
        info.add(createInfoLabel("Auteur :", "-"), 1, 0);
        info.add(createInfoLabel("Editeur :", "-"), 2, 0);
        info.add(createInfoLabel("Type :", "-"), 3, 0);
        info.add(createInfoLabel("Statut :", "Disponible"), 4, 0);
        
        section.getChildren().addAll(title, searchBar, info);
        return section;
    }
    
    private VBox createLoanSummarySection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: azure; -fx-padding: 15; -fx-border-color: #60a5fa; -fx-border-radius: 5;");
        
        Label title = new Label("Résumé de l'Emprunt");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(10);
        
        info.add(createInfoLabel("Document :", "-"), 0, 0);
        info.add(createInfoLabel("Adhérent :", "-"), 1, 0);
        info.add(createInfoLabel("Date d'emprunt :", "-"), 2, 0);
        info.add(createInfoLabel("Retour prévu :", "-"), 3, 0);
        
        section.getChildren().addAll(title, info);
        return section;
    }
    
    // ==================== RETURN TAB ====================
    private Tab createReturnTab() {
        Tab tab = new Tab("Enregistrer un retour");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titreLabel = new Label("↩ Enregistrer un retour");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        HBox searchBar = new HBox(10);
        ComboBox<String> searchType = new ComboBox<>();
        searchType.getItems().addAll("ID Emprunt",  "ID Adhérent");

        TextField searchField = new TextField();
        searchField.setPromptText("ID Emprunt ou Adhérent...");
        searchField.setPrefWidth(400);
        Button searchBtn = new Button("Rechercher");
        searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white;");
        searchBar.getChildren().addAll(searchType, searchField, searchBtn);
        
        TableView<Emprunt> table = createBorrowingsTable();
        VBox returnForm = createReturnForm();
        
        VBox penaltyBox = new VBox(5);
        penaltyBox.setStyle("-fx-background-color: #fef2f2; -fx-padding: 15; -fx-border-color: #ef4444; -fx-border-width: 0 0 0 4;");
        Label penaltyLabel = new Label("Pénalité Calculée");
        penaltyLabel.setFont(Font.font("System", 12));
        Label penaltyAmount = new Label("0,00 €");
        penaltyAmount.setFont(Font.font("System", FontWeight.BOLD, 24));
        penaltyAmount.setTextFill(Color.RED);
        Label penaltyNote = new Label("À payer si retard");
        penaltyNote.setFont(Font.font("System", 10));
        penaltyBox.getChildren().addAll(penaltyLabel, penaltyAmount, penaltyNote);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db;");
        Button saveBtn = new Button("Enregistrer le Retour");
        saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(titreLabel, searchBar, table, returnForm, penaltyBox, buttons);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        
        return tab;
    }
    
    private TableView<Emprunt> createBorrowingsTable() {
        TableView<Emprunt> table = new TableView<>();
        
        TableColumn<Emprunt, String> idCol = new TableColumn<>("ID Emprunt");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getID_Emprunt())));
        
        TableColumn<Emprunt, String> docCol = new TableColumn<>("Document");
        docCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().document.getISBN_ISSN()));
        docCol.setPrefWidth(200);
        
        TableColumn<Emprunt, String> memberCol = new TableColumn<>("Adhérent");
        memberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().adherent.getID_Adherent()));
        memberCol.setPrefWidth(150);
        
        TableColumn<Emprunt, String> borrowDateCol = new TableColumn<>("Date Emprunt");
        borrowDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate_Emprunt())));
        
        TableColumn<Emprunt, String> dueDateCol = new TableColumn<>("Date Retour Prévue");
        dueDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate_RetourPrevue())));

        TableColumn<Emprunt, String> realDateCol = new TableColumn<>("Date Retour Réelle");
        realDateCol.setCellValueFactory(cellData -> {
            LocalDate dateRetour = cellData.getValue().getDate_RetourReelle();
            String affichage = (dateRetour != null) ? dateRetour.toString() : "En cours d'emprunt";
            return new SimpleStringProperty(affichage);
        });


        TableColumn<Emprunt, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button detailsBtn = new Button("Détails");
            private final HBox pane = new HBox(5, detailsBtn);
        
        {
            detailsBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15;");
            pane.setAlignment(Pos.CENTER);
            
            // ⭐ Action sur le bouton Détails
            detailsBtn.setOnAction(e -> {
                Emprunt emprunt = getTableView().getItems().get(getIndex());
                afficherPopupDetailEmprunt(emprunt, table);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : pane);
        }
    });

        
        table.getColumns().addAll(Arrays.asList(idCol, docCol, memberCol, borrowDateCol, dueDateCol, realDateCol, actionsCol));
        
        /*ObservableList<Emprunt> data = FXCollections.observableArrayList(
            new Emprunt(new Adherent("PHI", "Nono", "nophi@dauphine.eu", "0202020202"), new Livre("Mon livre", "Moi", "Toi", "ISBN-qqc",200)
        ));*/
        ///////Afficher  la liste des emprunts 
        table.setItems(bibliothequeManager.getList_Emprunt());
        table.setPrefHeight(200);
        
        return table;
    }
    
    private VBox createReturnForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        Label formTitle = new Label("Enregistrer le Retour");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField idField = new TextField();
        idField.setPromptText("Ex: E001");
        Label idLabel = new Label("ID Emprunt :");
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        DatePicker datePicker = new DatePicker();
        Label dateLabel = new Label("Date de retour :");
        dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        grid.add(idLabel, 0, 0);
        grid.add(idField, 0, 1);
        grid.add(dateLabel, 1, 0);
        grid.add(datePicker, 1, 1);
        
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Remarques sur l'état du document...");
        notesArea.setPrefRowCount(3);
        Label notesLabel = new Label("Notes :");
        notesLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        form.getChildren().addAll(formTitle, grid, notesLabel, notesArea);
        return form;
    }
    
    
    
    // ==================== HELPER METHODS ====================
    private VBox creerRechercheBox(String[] options, String placeholder) {
        VBox searchBox = new VBox(10);
        searchBox.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
        
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        
        ComboBox<String> searchType = new ComboBox<>();
        searchType.getItems().addAll(options);
        searchType.setValue(options[0]);
        searchType.setPrefWidth(150);
        
        TextField searchField = new TextField();
        searchField.setPromptText(placeholder);
        searchField.setPrefWidth(300);
        
        Button searchBtn = new Button("Rechercher");
        searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
        
        searchBar.getChildren().addAll(new Label("Rechercher par :"), searchType, new Label("Texte :"), searchField, searchBtn);
        searchBox.getChildren().add(searchBar);
        
        return searchBox;
    }
    
    private VBox createInfoLabel(String label, String value) {
        VBox box = new VBox(5);
        Label labelText = new Label(label);
        labelText.setFont(Font.font("System", 10));
        labelText.setTextFill(Color.GRAY);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        box.getChildren().addAll(labelText, valueText);
        return box;
    }
    
    private String getStylesheet() {
        return "data:text/css," +
            ".tab-pane { -fx-background-color: white; }" +
            ".tab { -fx-background-color: #e5e7eb; -fx-padding: 10 20 10 20; }" +
            ".tab:selected { -fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white; }" +
            ".table-view { -fx-background-color: white; }" +
            ".table-view .column-entete { -fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white; }" +
            ".button { -fx-cursor: hand; -fx-padding: 8 15 8 15; -fx-border-radius: 5; -fx-background-radius: 5; }";
    }
    
    // ==================== DATA CLASSES ====================
    /*
    
    public static class Emprunt {
        private String id;
        private String document;
        private String member;
        private String borrowDate;
        private String dueDate;
        
        public Emprunt(String id, String document, String member, String borrowDate, String dueDate) {
            this.id = id;
            this.document = document;
            this.member = member;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
        }
        
        public String getId() { return id; }
        public String getDocument() { return document; }
        public String getMember() { return member; }
        public String getBorrowDate() { return borrowDate; }
        public String getDueDate() { return dueDate; }
    }
    
    public static class LateItem {
        private String member;
        private String document;
        private String dueDate;
        private int daysLate;
        private String penalty;
        
        public LateItem(String member, String document, String dueDate, int daysLate, String penalty) {
            this.member = member;
            this.document = document;
            this.dueDate = dueDate;
            this.daysLate = daysLate;
            this.penalty = penalty;
        }
        
        public String getMember() { return member; }
        public String getDocument() { return document; }
        public String getDueDate() { return dueDate; }
        public int getDaysLate() { return daysLate; }
        public String getPenalty() { return penalty; }
    }
    
    */
}
