
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
// ajouté pour que les recherches fonctionnent 
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

// les imports pour avoir une barre de recherche type combobox 
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
import Controler.AjouterDocumentController;
import Controler.AjouterEmprunt;
import Controler.RetourController;
import Model.*;
import Vue.PopupHistorique;
import javafx.geometry.Side;





public class Interface extends Application {

    private BibliothequeManager bibliothequeManager = new BibliothequeManager();
    private TabPane tabPane;
    // l'objet Retour controler 
    private RetourController retourController;
    // Variable globale à la table des documents
    // rendu global pour permettre le rafraichissement des tablesview 
    private TableView<Document> documentsTable;
    private TableView<Adherent> adherentsTable;

    
    //@SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        bibliothequeManager.chargerDocumentsDepuisBD();
        bibliothequeManager.chargerAdherentDepuisBD();
        bibliothequeManager.chargerEmpruntDepuisBD();



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
// ==================== Page document ====================
private Tab creerPageDocuments() {
    Tab tab = new Tab("Gestion des Documents");
    
    VBox content = new VBox(15);
    content.setPadding(new Insets(20));
    
    // Title
    Label titreLabel = new Label("Gestion des Documents");
    titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
    
    // TableVue pour afficher les documents de la base de données
    TableView<Document> table = creerTabVueDocuments();
    
    // 🔍 Nouvelle barre de recherche spécifique pour les documents
    DocumentSearchComponents searchComponents = createDocumentSearchBox(table);

    // Formulaire d'ajout de document
    VBox ajoutForm = creerFormAjoutDocument();
    
    content.getChildren().addAll(
        titreLabel,
        searchComponents.root,  // barre recherche documents
        table,
        ajoutForm
    );
    
    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    tab.setContent(scrollPane);
    
    return tab;
}

    
    private TableView<Document> creerTabVueDocuments() {
        documentsTable = new TableView<>();// fait de cette manière pour permetter un refresh de la tableview
        //TableView<Document> table = new TableView<>();
        TableView<Document> table = documentsTable;

        
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
    grid.getChildren().remove(authorField);

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

        // ===============Controler pour ajouter un document=============
        new AjouterDocumentController(
    bibliothequeManager,
    typeBox,
    isbnField,
    titleField,
    authorField,
    editorField,
    nbpageField,
    periodiciteField,
    numeroField,
    addBtn);

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
    
    TableView<Adherent> table = createMembersTable();

    // Nouvelle barre de recherche spécifique aux adhérents
    AdherentSearchComponents searchComponents = createAdherentSearchBox(table);

    VBox ajoutForm = createAddMemberForm();
    
    content.getChildren().addAll(
        titreLabel,
        searchComponents.root,   // la barre de recherche
        table,
        ajoutForm
    );
    
    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    tab.setContent(scrollPane);
    
    return tab;
}

    
    private TableView<Adherent> createMembersTable() {
        //Modification pour permettre un refresh de la table après un retour ou emprunt
        adherentsTable = new TableView<>();
        //TableView<Adherent> table = new TableView<>();
        TableView<Adherent> table = adherentsTable;
        
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

            //========Controler qui Connecte le bouton à la fenêtre PopupHistorique =======//
            historyBtn.setOnAction(e -> {
            Adherent adherent = getTableView().getItems().get(getIndex());
            PopupHistorique.afficher(adherent);});
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

        new AjouterAdherent(bibliothequeManager, lastNameField,firstNameField, emailField, phoneField, addBtn,adherentsTable);
        
        form.getChildren().addAll(formTitle, grid, addBtn);
        return form;
    }
    
    // ==================== BORROW TAB ====================//
    private Tab createBorrowTab() {
        Tab tab = new Tab("Nouveaux Emprunts");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titreLabel = new Label("Enregistrer un nouvel emprunt");
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        MemberSelectionComponents memberSection = createMemberSelectionSection();
        DocumentSelectionComponents documentSection = createDocumentSelectionSection();
        LoanSummaryComponents summarySection = createLoanSummarySection();
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db;");
        Button saveBtn = new Button("Enregistrer l'Emprunt");
        saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        // on crée le contrôleur qui gère le bouton
        // En créant le controleur on lance la méthode qui ajoute car la méthode est appelée dans le constructeur

        AjouterEmprunt empruntController = new  AjouterEmprunt(
            bibliothequeManager,
            summarySection.docValueLabel,
            summarySection.adherentValueLabel,
            summarySection.dateEmpruntValueLabel,
            summarySection.dateRetourPrevValueLabel,
            saveBtn,
            cancelBtn,
            memberSection.searchField,
            documentSection.searchField,
            documentsTable);

        // Vider les champs de recherche dès qu'on appui sur 

        //Lignes ajoutées pour permettre une liste deroulante pour la selection de Adherent et Document
        setupAdherentAutoComplete(memberSection,empruntController);
        setupDocumentAutoComplete(documentSection, empruntController);

    // plus tard, quand tu feras la recherche adhérent/document,
    // tu pourras appeler :
    //    empruntController.setSelectedAdherent(adherentTrouve);
    //    empruntController.setSelectedDocument(documentTrouve);

    //============Research Adhérent =====//
    memberSection.searchBtn.setOnAction(e -> {
        String query = memberSection.searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            empruntController.showError("Veuillez saisir un ID ou un nom d'adhérent.");
            return;
        }

        Adherent found = null;
        for (Adherent a : bibliothequeManager.getList_Adherent()) {
            if (a.getID_Adherent().toLowerCase().equals(query) ||
                a.getNom().toLowerCase().contains(query)) {
                found = a;
                break;
            }
        }

        if (found == null) {
            empruntController.showError("Aucun adhérent trouvé pour : " + query);
        } else {
            // on met à jour la sélection dans le contrôleur
            empruntController.setSelectedAdherent(found);
            // et on met à jour les infos dans la section adhérent
            memberSection.idValueLabel.setText(found.getID_Adherent());
            memberSection.nomPrenomValueLabel.setText(found.getNom() + " " + found.getPrenom());
            memberSection.empruntsActuelsValueLabel.setText(found.List_Emprunt.size() + "/5");
            memberSection.statutPenaliteValueLabel.setText(
                found.getPenalite() > 0 ? "Pénalisé" : "Aucune"
            );
        }
    });

     // ==== Researche Document ====//
    documentSection.searchBtn.setOnAction(e -> {
        String query = documentSection.searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            empruntController.showError("Veuillez saisir un titre ou un ISBN.");
            return;
        }

        Document found = null;
        for (Document d : bibliothequeManager.getList_Document()) { // adapte le getter
            if (d.getTitre().toLowerCase().contains(query) ||
                d.getISBN_ISSN().toLowerCase().equals(query)) {
                found = d;
                break;
            }
        }

        if (found == null) {
            empruntController.showError("Aucun document trouvé pour : " + query);
        } else {
            empruntController.setSelectedDocument(found);

            documentSection.titreValueLabel.setText(found.getTitre());
            documentSection.auteurValueLabel.setText(found.getAuteur());
            documentSection.editeurValueLabel.setText(found.getEditeur());
            documentSection.typeValueLabel.setText(found.getType());
            documentSection.statutValueLabel.setText(
                found.getEstDisponible() ? "Disponible" : "Emprunté"
            );
        }
    });
    
    content.getChildren().addAll(
            titreLabel,
            memberSection.root,
            documentSection.root,
            summarySection.root,
            buttons
    );
    
    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    tab.setContent(scrollPane);
    
    return tab;
}
    
    
    private MemberSelectionComponents createMemberSelectionSection() {
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
        ///////////Modifier pour retourner AdherentSelectionComponents/////////
        
    /* info.add(createInfoLabel("ID :", "-"), 0, 0);
        info.add(createInfoLabel("Nom/Prénom :", "-"), 1, 0);
        info.add(createInfoLabel("Emprunts actuels :", "0/5"), 2, 0);
        info.add(createInfoLabel("Statut Pénalité :", "Aucune"), 3, 0);
        
        section.getChildren().addAll(title, searchBar, info);
        return section;*/

    VBox idBox = createInfoLabel("ID :", "-");
    VBox nomPrenomBox = createInfoLabel("Nom/Prénom :", "-");
    VBox empruntsBox = createInfoLabel("Emprunts actuels :", "0/5");
    VBox penaliteBox = createInfoLabel("Statut Pénalité :", "Aucune");

    Label idValueLabel = (Label) idBox.getChildren().get(1);
    Label nomPrenomValueLabel = (Label) nomPrenomBox.getChildren().get(1);
    Label empruntsValueLabel = (Label) empruntsBox.getChildren().get(1);
    Label penaliteValueLabel = (Label) penaliteBox.getChildren().get(1);

    info.add(idBox, 0, 0);
    info.add(nomPrenomBox, 1, 0);
    info.add(empruntsBox, 2, 0);
    info.add(penaliteBox, 3, 0);
    
    section.getChildren().addAll(title, searchBar, info);
    return new MemberSelectionComponents(
        section,
        searchField,
        searchBtn,
        idValueLabel,
        nomPrenomValueLabel,
        empruntsValueLabel,
        penaliteValueLabel
    );
}
    
    
    private DocumentSelectionComponents createDocumentSelectionSection() {
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

        //////Modifier pour permettre la selectionn de document ///////////
        
        /*info.add(createInfoLabel("Titre :", "-"), 0, 0);
        info.add(createInfoLabel("Auteur :", "-"), 1, 0);
        info.add(createInfoLabel("Editeur :", "-"), 2, 0);
        info.add(createInfoLabel("Type :", "-"), 3, 0);
        info.add(createInfoLabel("Statut :", "Disponible"), 4, 0);
        
        section.getChildren().addAll(title, searchBar, info);
        return section;*/

    VBox titreBox = createInfoLabel("Titre :", "-");
    VBox auteurBox = createInfoLabel("Auteur :", "-");
    VBox editeurBox = createInfoLabel("Editeur :", "-");
    VBox typeBox = createInfoLabel("Type :", "-");
    VBox statutBox = createInfoLabel("Statut :", "Disponible");

    Label titreValueLabel = (Label) titreBox.getChildren().get(1);
    Label auteurValueLabel = (Label) auteurBox.getChildren().get(1);
    Label editeurValueLabel = (Label) editeurBox.getChildren().get(1);
    Label typeValueLabel = (Label) typeBox.getChildren().get(1);
    Label statutValueLabel = (Label) statutBox.getChildren().get(1);
    
    info.add(titreBox, 0, 0);
    info.add(auteurBox, 1, 0);
    info.add(editeurBox, 2, 0);
    info.add(typeBox, 3, 0);
    info.add(statutBox, 4, 0);
    
    section.getChildren().addAll(title, searchBar, info);
    return new DocumentSelectionComponents(
        section,
        searchField,
        searchBtn,
        titreValueLabel,
        auteurValueLabel,
        editeurValueLabel,
        typeValueLabel,
        statutValueLabel
    );
    }
    
    //private VBox createLoanSummarySection() {
    private LoanSummaryComponents createLoanSummarySection(){ 
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: azure; -fx-padding: 15; -fx-border-color: #60a5fa; -fx-border-radius: 5;");
        
        Label title = new Label("Résumé de l'Emprunt");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(10);
        
        /*info.add(createInfoLabel("Document :", "-"), 0, 0);
        info.add(createInfoLabel("Adhérent :", "-"), 1, 0);
        info.add(createInfoLabel("Date d'emprunt :", "-"), 2, 0);
        info.add(createInfoLabel("Retour prévu :", "-"), 3, 0);*/

    Label docValue = new Label("-");
    Label adherentValue = new Label("-");
    Label dateEmpValue = new Label("-");
    Label datePrevValue = new Label("-");

    info.add(createInfoLabel("Document :", "-").getChildren().get(0), 0, 0); // on réutilise seulement le style du label
    info.add(wrapValueLabel(docValue), 0, 1);

    info.add(createInfoLabel("Adhérent :", "-").getChildren().get(0), 1, 0);
    info.add(wrapValueLabel(adherentValue), 1, 1);

    info.add(createInfoLabel("Date d'emprunt :", "-").getChildren().get(0), 2, 0);
    info.add(wrapValueLabel(dateEmpValue), 2, 1);

    info.add(createInfoLabel("Retour prévu :", "-").getChildren().get(0), 3, 0);
    info.add(wrapValueLabel(datePrevValue), 3, 1);
        
        section.getChildren().addAll(title, info);
        //return section;
        return new LoanSummaryComponents(section, docValue, adherentValue, dateEmpValue, datePrevValue);
    }

    
    // ==================== RETURN TAB ====================

private Tab createReturnTab() {
    Tab tab = new Tab("Enregistrer un retour");
    
    VBox content = new VBox(15);
    content.setPadding(new Insets(20));

    // 🔥 Création du contrôleur de retour
//RetourController retourController = new RetourController(bibliothequeManager);

    
    Label titreLabel = new Label("↩ Enregistrer un retour");
    titreLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

    // 1) Nouvelle section : sélection d'un emprunt avec auto-complétion
    EmpruntSelectionComponents empruntSection = createEmpruntSelectionSection();

    // 2) Table des emprunts
    TableView<Emprunt> table = createBorrowingsTable();

    // Quand on clique une ligne dans la table, on met à jour la sélection
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal == null) return;

        empruntSection.selectedEmprunt = newVal;

        empruntSection.idEmpruntValueLabel.setText(String.valueOf(newVal.getID_Emprunt()));
        empruntSection.documentValueLabel.setText(
            newVal.document.getISBN_ISSN() + " - " + newVal.document.getTitre()
        );
        empruntSection.adherentValueLabel.setText(
            newVal.adherent.getID_Adherent() + " - " +
            newVal.adherent.getNom() + " " + newVal.adherent.getPrenom()
        );
        empruntSection.dateEmpruntValueLabel.setText(String.valueOf(newVal.getDate_Emprunt()));
        empruntSection.dateRetourPrevValueLabel.setText(String.valueOf(newVal.getDate_RetourPrevue()));
        empruntSection.statutValueLabel.setText(newVal.getStatut_Emprunt() ? "En cours" : "Clôturé");
        // on informe le controler 
    retourController.setSelectedEmprunt(newVal);
    });

    // 3) Formulaire de retour (version sans TextArea, avec Label date + Combo état)
    VBox returnForm = createReturnForm();
    
    // 4) Pénalité (inchangé)
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
    
    // 5) Boutons
    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER_RIGHT);
    Button cancelBtn = new Button("Annuler");
    cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db;");
    Button saveBtn = new Button("Enregistrer le Retour");
    saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");
    buttons.getChildren().addAll(cancelBtn, saveBtn);

    //controleur pour enregistrer un retour //////////
    retourController =  new RetourController(bibliothequeManager, saveBtn, cancelBtn, penaltyAmount, table, documentsTable,
                                            adherentsTable);
        /*new RetourController(
        bibliothequeManager,
        saveBtn,
        cancelBtn,
        penaltyAmount,
        table,
        documentsTable);*/
    
    
    content.getChildren().addAll(
        titreLabel,
        table,  // liste des emprunts
        empruntSection.root,  // fiche de l'emprunt sélectionnés
        returnForm,           // infos de retour
        penaltyBox,
        buttons
    );
    
    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    tab.setContent(scrollPane);
    
    return tab;
}


    
    private TableView<Emprunt> createBorrowingsTable() {
        TableView<Emprunt> table = new TableView<>();
        
        TableColumn<Emprunt, String> idCol = new TableColumn<>("ID Emprunt");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getID_Emprunt())));
        
        TableColumn<Emprunt, String> docCol = new TableColumn<>("Document: ISBN-ISSN");
        docCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().document.getISBN_ISSN()));
        docCol.setPrefWidth(200);

        TableColumn<Emprunt, String> docCol2 = new TableColumn<>("Document: Titre");
        docCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().document.getTitre()));
        docCol2.setPrefWidth(200);
        
        TableColumn<Emprunt, String> memberCol = new TableColumn<>("Adhérent: ID");
        memberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().adherent.getID_Adherent()));
        memberCol.setPrefWidth(150);

        TableColumn<Emprunt, String> memberCol2 = new TableColumn<>("Adhérent:Nom");
        memberCol2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().adherent.getNom()+"  "+cellData.getValue().adherent.getPrenom()));
        memberCol2.setPrefWidth(150);
        
        TableColumn<Emprunt, String> borrowDateCol = new TableColumn<>("Date Emprunt");
        borrowDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate_Emprunt())));
        borrowDateCol.setPrefWidth(150);
        
        TableColumn<Emprunt, String> dueDateCol = new TableColumn<>("Date Retour Prévue");
        dueDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate_RetourPrevue())));
        dueDateCol.setPrefWidth(150);
        TableColumn<Emprunt, String> realDateCol = new TableColumn<>("Date Retour Réelle");
        realDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate_RetourReelle())));
        realDateCol.setPrefWidth(150);
        
        table.getColumns().addAll(Arrays.asList(idCol, docCol,docCol2, memberCol,memberCol2, borrowDateCol, dueDateCol,realDateCol));
        
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
    
    Label formTitle = new Label("Informations de Retour");
    formTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
    
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    // ===== LABEL Date de retour (pas éditable) =====
    Label dateLabel = new Label("Date du retour :");
    dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

    Label dateRetourValueLabel = new Label(LocalDate.now().toString());
    dateRetourValueLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
    dateRetourValueLabel.setStyle("-fx-text-fill: #1e40af;");

    // ===== Choix de l'état du document =====
    ComboBox<String> etatCombo = new ComboBox<>();
    etatCombo.getItems().addAll("En bon état", "Usé");
    etatCombo.setPromptText("État du document");
    Label etatLabel = new Label("État du document :");
    etatLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

    grid.add(dateLabel, 0, 0);
    grid.add(dateRetourValueLabel, 0, 1);

    grid.add(etatLabel, 1, 0);
    grid.add(etatCombo, 1, 1);



    form.getChildren().addAll(formTitle, grid);
    return form;
    }
    private EmpruntSelectionComponents createEmpruntSelectionSection() {
    VBox section = new VBox(10);
    section.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
    
    Label title = new Label("Sélection de l'Emprunt");
    title.setFont(Font.font("System", FontWeight.BOLD, 14));
    
    // Barre de recherche
    HBox searchBar = new HBox(10);
    TextField searchField = new TextField();
    searchField.setPromptText("ID Emprunt, Document, Adhérent ou Date d'emprunt...");
    searchField.setPrefWidth(400);
    Button searchBtn = new Button("Rechercher");
    searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white;");
    searchBar.getChildren().addAll(searchField, searchBtn);
    
    // Zone d'infos
    GridPane info = new GridPane();
    info.setHgap(20);
    info.setVgap(10);
    info.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");
    
    VBox idEmpBox    = createInfoLabel("ID Emprunt :", "-");
    VBox docBox      = createInfoLabel("Document :", "-");
    VBox adhBox      = createInfoLabel("Adhérent :", "-");
    VBox dateEmpBox  = createInfoLabel("Date d'emprunt :", "-");
    VBox datePrevBox = createInfoLabel("Retour prévu :", "-");
    VBox statutBox   = createInfoLabel("Statut :", "-");
    
    Label idEmpValue      = (Label) idEmpBox.getChildren().get(1);
    Label docValue        = (Label) docBox.getChildren().get(1);
    Label adhValue        = (Label) adhBox.getChildren().get(1);
    Label dateEmpValue    = (Label) dateEmpBox.getChildren().get(1);
    Label datePrevValue   = (Label) datePrevBox.getChildren().get(1);
    Label statutValue     = (Label) statutBox.getChildren().get(1);
    
    info.add(idEmpBox,    0, 0);
    info.add(docBox,      1, 0);
    info.add(adhBox,      2, 0);
    info.add(dateEmpBox,  0, 1);
    info.add(datePrevBox, 1, 1);
    info.add(statutBox,   2, 1);
    
    section.getChildren().addAll(title, searchBar, info);
    
    EmpruntSelectionComponents comp = new EmpruntSelectionComponents(
        section,
        searchField,
        searchBtn,
        idEmpValue,
        docValue,
        adhValue,
        dateEmpValue,
        datePrevValue,
        statutValue
    );

    // ====== Menu contextuel pour la liste déroulante ======
    ContextMenu suggestions = new ContextMenu();

    // Auto-complétion quand on tape
    searchField.textProperty().addListener((obs, oldText, newText) -> {
        if (newText == null || newText.isBlank()) {
            suggestions.hide();
            comp.selectedEmprunt = null;
            idEmpValue.setText("-");
            docValue.setText("-");
            adhValue.setText("-");
            dateEmpValue.setText("-");
            datePrevValue.setText("-");
            statutValue.setText("-");
            return;
        }

        String lower = newText.toLowerCase();

        // On filtre sur : id emprunt, doc titre/ISBN, adhérent id/nom, date emprunt
        var matches = bibliothequeManager.getList_Emprunt()
            .stream()
            .filter(Emprunt::getStatut_Emprunt) // seulement les emprunts en cours
            .filter(emp -> {
                String idEmpStr   = String.valueOf(emp.getID_Emprunt());
                String docIsbn    = emp.document.getISBN_ISSN().toLowerCase();
                String docTitre   = emp.document.getTitre().toLowerCase();
                String adhId      = emp.adherent.getID_Adherent().toLowerCase();
                String adhNom     = (emp.adherent.getNom() + " " + emp.adherent.getPrenom()).toLowerCase();
                String dateEmpStr = emp.getDate_Emprunt() != null ? emp.getDate_Emprunt().toString() : "";

                return idEmpStr.contains(lower)
                    || docIsbn.contains(lower)
                    || docTitre.contains(lower)
                    || adhId.contains(lower)
                    || adhNom.contains(lower)
                    || dateEmpStr.contains(lower);
            })
            .limit(10)
            .toList();

        if (matches.isEmpty()) {
            suggestions.hide();
            return;
        }

        suggestions.getItems().clear();
        for (Emprunt emp : matches) {
            String label = emp.getID_Emprunt()
                + " | " + emp.document.getISBN_ISSN() + " - " + emp.document.getTitre()
                + " | " + emp.adherent.getID_Adherent() + " - " 
                + emp.adherent.getNom() + " " + emp.adherent.getPrenom()
                + " | " + emp.getDate_Emprunt();

            MenuItem item = new MenuItem(label);
            item.setOnAction(ev -> {
                // on remplit le champ avec l'ID de l'emprunt
                searchField.setText(String.valueOf(emp.getID_Emprunt()));

                // on met à jour l'affichage
                comp.selectedEmprunt = emp;
                idEmpValue.setText(String.valueOf(emp.getID_Emprunt()));
                docValue.setText(emp.document.getISBN_ISSN() + " - " + emp.document.getTitre());
                adhValue.setText(emp.adherent.getID_Adherent() + " - " +
                                emp.adherent.getNom() + " " + emp.adherent.getPrenom());
                dateEmpValue.setText(String.valueOf(emp.getDate_Emprunt()));
                datePrevValue.setText(String.valueOf(emp.getDate_RetourPrevue()));
                statutValue.setText(emp.getStatut_Emprunt() ? "En cours" : "Clôturé");

                retourController.setSelectedEmprunt(emp);

                suggestions.hide();
            });

            suggestions.getItems().add(item);
        }

        if (!suggestions.isShowing()) {
            suggestions.show(searchField, Side.BOTTOM, 0, 0);
        }
    });

    // Bouton "Rechercher" classique : prend le premier match
    searchBtn.setOnAction(e -> {
        String q = searchField.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            comp.selectedEmprunt = null;
            idEmpValue.setText("-");
            docValue.setText("-");
            adhValue.setText("-");
            dateEmpValue.setText("-");
            datePrevValue.setText("-");
            statutValue.setText("-");
            return;
        }

        Emprunt found = bibliothequeManager.getList_Emprunt()
            .stream()
            .filter(Emprunt::getStatut_Emprunt)
            .filter(emp -> {
                String idEmpStr   = String.valueOf(emp.getID_Emprunt());
                String docIsbn    = emp.document.getISBN_ISSN().toLowerCase();
                String docTitre   = emp.document.getTitre().toLowerCase();
                String adhId      = emp.adherent.getID_Adherent().toLowerCase();
                String adhNom     = (emp.adherent.getNom() + " " + emp.adherent.getPrenom()).toLowerCase();
                String dateEmpStr = emp.getDate_Emprunt() != null ? emp.getDate_Emprunt().toString() : "";

                return idEmpStr.contains(q)
                    || docIsbn.contains(q)
                    || docTitre.contains(q)
                    || adhId.contains(q)
                    || adhNom.contains(q)
                    || dateEmpStr.contains(q);
            })
            .findFirst()
            .orElse(null);

        if (found == null) {
            comp.selectedEmprunt = null;
            idEmpValue.setText("Aucun emprunt trouvé");
            docValue.setText("-");
            adhValue.setText("-");
            dateEmpValue.setText("-");
            datePrevValue.setText("-");
            statutValue.setText("-");
        } else {
            comp.selectedEmprunt = found;
            idEmpValue.setText(String.valueOf(found.getID_Emprunt()));
            docValue.setText(found.document.getISBN_ISSN() + " - " + found.document.getTitre());
            adhValue.setText(found.adherent.getID_Adherent() + " - " +
                            found.adherent.getNom() + " " + found.adherent.getPrenom());
            dateEmpValue.setText(String.valueOf(found.getDate_Emprunt()));
            datePrevValue.setText(String.valueOf(found.getDate_RetourPrevue()));
            statutValue.setText(found.getStatut_Emprunt() ? "En cours" : "Clôturé");
        }
    });

    return comp;
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



    // ====== Barre de recherche + filtrage pour la table des Adhérents ======
private AdherentSearchComponents createAdherentSearchBox(TableView<Adherent> table) {
    VBox searchBox = new VBox(10);
    searchBox.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; "
            + "-fx-border-color: #e5e7eb; -fx-border-radius: 5;");
    
    HBox searchBar = new HBox(10);
    searchBar.setAlignment(Pos.CENTER_LEFT);
    
    ComboBox<String> searchType = new ComboBox<>();
    searchType.getItems().addAll("Nom", "ID Adhérent");
    searchType.setValue("Nom");
    searchType.setPrefWidth(150);
    
    TextField searchField = new TextField();
    searchField.setPromptText("Entrez le nom ou l'ID...");
    searchField.setPrefWidth(300);
    
    Button searchBtn = new Button("Rechercher");
    searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
    
    searchBar.getChildren().addAll(
        new Label("Rechercher par :"),
        searchType,
        new Label("Texte :"),
        searchField,
        searchBtn
    );
    
    searchBox.getChildren().add(searchBar);

    // ===== Logique de filtrage =====
    // on crée une vue filtrée de la liste d'adhérents
    FilteredList<Adherent> filteredData =
        new FilteredList<>(bibliothequeManager.getList_Adherent(), a -> true);

    // puis on branche ça à la table (avec tri)
    SortedList<Adherent> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(table.comparatorProperty());
    table.setItems(sortedData);

    // Fonction qui met à jour le prédicat du filtre
    Runnable updateFilter = () -> {
        String type = searchType.getValue();
        String text = searchField.getText();
        String lower = text == null ? "" : text.toLowerCase().trim();

        if (lower.isEmpty()) {
            // si champ vide → on affiche tout
            filteredData.setPredicate(a -> true);
            return;
        }

        filteredData.setPredicate(a -> {
            if (a == null) return false;

            if ("Nom".equals(type)) {
                String nomComplet = (a.getNom() + " " + a.getPrenom()).toLowerCase();
                return nomComplet.contains(lower);
            } else if ("ID Adhérent".equals(type)) {
                return a.getID_Adherent() != null
                        && a.getID_Adherent().toLowerCase().contains(lower);
            }
            return true;
        });
    };

    // Filtrage dynamique quand on tape
    searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());

    // Et aussi quand on change le type de recherche
    searchType.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());

    // Et si on clique sur le bouton "Rechercher"
    searchBtn.setOnAction(e -> updateFilter.run());

    return new AdherentSearchComponents(searchBox, searchType, searchField, searchBtn);
}


// ====== Barre de recherche + filtrage pour la table des Documents ======
private DocumentSearchComponents createDocumentSearchBox(TableView<Document> table) {
    VBox searchBox = new VBox(10);
    searchBox.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; "
            + "-fx-border-color: #e5e7eb; -fx-border-radius: 5;");
    
    HBox searchBar = new HBox(10);
    searchBar.setAlignment(Pos.CENTER_LEFT);
    
    ComboBox<String> searchType = new ComboBox<>();
    searchType.getItems().addAll("Titre", "Auteur", "ISBN/ISSN", "Type");
    searchType.setValue("Titre");
    searchType.setPrefWidth(150);
    
    TextField searchField = new TextField();
    searchField.setPromptText("Entrez votre recherche...");
    searchField.setPrefWidth(300);
    
    Button searchBtn = new Button("Rechercher");
    searchBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white; -fx-font-weight: bold;");
    
    searchBar.getChildren().addAll(
        new Label("Rechercher par :"),
        searchType,
        new Label("Texte :"),
        searchField,
        searchBtn
    );
    
    searchBox.getChildren().add(searchBar);

    // ===== Logique de filtrage =====
    FilteredList<Document> filteredData =
        new FilteredList<>(bibliothequeManager.getList_Document(), d -> true);

    SortedList<Document> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(table.comparatorProperty());
    table.setItems(sortedData);

    Runnable updateFilter = () -> {
        String type = searchType.getValue();
        String text = searchField.getText();
        String lower = (text == null) ? "" : text.toLowerCase().trim();

        if (lower.isEmpty()) {
            filteredData.setPredicate(d -> true); // afficher tout
            return;
        }

        filteredData.setPredicate(d -> {
            if (d == null) return false;

            switch (type) {
                case "Titre":
                    return d.getTitre() != null &&
                        d.getTitre().toLowerCase().contains(lower);

                case "Auteur":
                    return d.getAuteur() != null &&
                        d.getAuteur().toLowerCase().contains(lower);

                case "ISBN/ISSN":
                    return d.getISBN_ISSN() != null &&
                        d.getISBN_ISSN().toLowerCase().contains(lower);

                case "Type":
                    return d.getType() != null &&
                        d.getType().toLowerCase().contains(lower);
            }
            return true;
        });
    };

    // Filtrage dynamique au clavier
    searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());

    // Et quand on change le type
    searchType.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());

    // Et sur clic bouton
    searchBtn.setOnAction(e -> updateFilter.run());

    return new DocumentSearchComponents(searchBox, searchType, searchField, searchBtn);
}



     /// Helper 
    private VBox wrapValueLabel(Label valueLabel) {
    VBox box = new VBox(5);
    valueLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
    box.getChildren().add(valueLabel);
    return box;
}

    //Methode d'auto-Completion pour les Adherents afin de permettre à ce que la barre de recheche propose une liste correspondant à ce qu'on a renseigné dans le champ
    private void setupAdherentAutoComplete(MemberSelectionComponents memberSection,AjouterEmprunt empruntController) {

    ContextMenu suggestions = new ContextMenu();

    memberSection.searchField.textProperty().addListener((obs, oldText, newText) -> {
        if (newText == null || newText.isBlank()) {
            suggestions.hide();
            empruntController.setSelectedAdherent(null);
            memberSection.idValueLabel.setText("-");
            memberSection.nomPrenomValueLabel.setText("-");
            memberSection.empruntsActuelsValueLabel.setText("0/5");
            memberSection.statutPenaliteValueLabel.setText("Aucune");
            return;
        }

        String lower = newText.toLowerCase();
        var matches = bibliothequeManager.getList_Adherent()
                .stream()
                .filter(a ->
                        a.getID_Adherent().toLowerCase().contains(lower) ||
                        a.getNom().toLowerCase().contains(lower) ||
                        (a.getNom() + " " + a.getPrenom()).toLowerCase().contains(lower)
                )
                .limit(10)
                .toList();

        if (matches.isEmpty()) {
            suggestions.hide();
            return;
        }

        suggestions.getItems().clear();
        for (Adherent a : matches) {
            String label = a.getID_Adherent() + " - " + a.getNom() + " " + a.getPrenom();
            MenuItem item = new MenuItem(label);
            item.setOnAction(ev -> {
                // remplir le champ
                memberSection.searchField.setText(a.getID_Adherent());
                // mettre à jour les labels
                memberSection.idValueLabel.setText(a.getID_Adherent());
                memberSection.nomPrenomValueLabel.setText(a.getNom() + " " + a.getPrenom());
                memberSection.empruntsActuelsValueLabel.setText(a.List_Emprunt.size() + "/5");
                memberSection.statutPenaliteValueLabel.setText(
                        a.getPenalite() > 0 ? "Pénalisé" : "Aucune"
                );
                // informer le contrôleur
                empruntController.setSelectedAdherent(a);

                suggestions.hide();
            });
            suggestions.getItems().add(item);
        }

        if (!suggestions.isShowing()) {
            suggestions.show(memberSection.searchField, Side.BOTTOM, 0, 0);
        }
    });

    // optionnel : le bouton Rechercher fait une recherche "classique"
    memberSection.searchBtn.setOnAction(e -> {
        String query = memberSection.searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            empruntController.showError("Veuillez saisir un ID ou un nom d'adhérent.");
            return;
        }
        Adherent found = bibliothequeManager.getList_Adherent().stream()
                .filter(a -> a.getID_Adherent().toLowerCase().equals(query) ||
                            a.getNom().toLowerCase().contains(query))
                .findFirst()
                .orElse(null);
        if (found == null) {
            empruntController.showError("Aucun adhérent trouvé pour : " + query);
        } else {
            memberSection.searchField.setText(found.getID_Adherent());
            memberSection.idValueLabel.setText(found.getID_Adherent());
            memberSection.nomPrenomValueLabel.setText(found.getNom() + " " + found.getPrenom());
            memberSection.empruntsActuelsValueLabel.setText(found.List_Emprunt.size() + "/5");
            memberSection.statutPenaliteValueLabel.setText(
                    found.getPenalite() > 0 ? "Pénalisé" : "Aucune"
            );
            empruntController.setSelectedAdherent(found);
        }
    });
}
private void setupDocumentAutoComplete(DocumentSelectionComponents documentSection,
                                    AjouterEmprunt empruntController) {

    ContextMenu suggestions = new ContextMenu();

    documentSection.searchField.textProperty().addListener((obs, oldText, newText) -> {
        if (newText == null || newText.isBlank()) {
            suggestions.hide();
            empruntController.setSelectedDocument(null);
            documentSection.titreValueLabel.setText("-");
            documentSection.auteurValueLabel.setText("-");
            documentSection.editeurValueLabel.setText("-");
            documentSection.typeValueLabel.setText("-");
            documentSection.statutValueLabel.setText("Disponible");
            return;
        }

        String lower = newText.toLowerCase();
        var matches = bibliothequeManager.getList_Document()
                .stream()
                .filter(d ->
                        d.getTitre().toLowerCase().contains(lower) ||
                        d.getISBN_ISSN().toLowerCase().contains(lower)
                )
                .limit(10)
                .toList();

        if (matches.isEmpty()) {
            suggestions.hide();
            return;
        }

        suggestions.getItems().clear();
        for (Document d : matches) {
            String label = d.getISBN_ISSN() + " - " + d.getTitre();
            MenuItem item = new MenuItem(label);
            item.setOnAction(ev -> {
                documentSection.searchField.setText(d.getISBN_ISSN());

                documentSection.titreValueLabel.setText(d.getTitre());
                documentSection.auteurValueLabel.setText(d.getAuteur());
                documentSection.editeurValueLabel.setText(d.getEditeur());
                documentSection.typeValueLabel.setText(d.getType());
                documentSection.statutValueLabel.setText(
                        d.getEstDisponible() ? "Disponible" : "Emprunté"
                );

                empruntController.setSelectedDocument(d);
                suggestions.hide();
            });
            suggestions.getItems().add(item);
        }

        if (!suggestions.isShowing()) {
            suggestions.show(documentSection.searchField, Side.BOTTOM, 0, 0);
        }
    });

    documentSection.searchBtn.setOnAction(e -> {
        String query = documentSection.searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            empruntController.showError("Veuillez saisir un titre ou un ISBN.");
            return;
        }
        Document found = bibliothequeManager.getList_Document().stream()
                .filter(d -> d.getTitre().toLowerCase().contains(query) ||
                            d.getISBN_ISSN().toLowerCase().equals(query))
                .findFirst()
                .orElse(null);
        if (found == null) {
            empruntController.showError("Aucun document trouvé pour : " + query);
        } else {
            documentSection.searchField.setText(found.getISBN_ISSN());
            documentSection.titreValueLabel.setText(found.getTitre());
            documentSection.auteurValueLabel.setText(found.getAuteur());
            documentSection.editeurValueLabel.setText(found.getEditeur());
            documentSection.typeValueLabel.setText(found.getType());
            documentSection.statutValueLabel.setText(
                    found.getEstDisponible() ? "Disponible" : "Emprunté"
            );
            empruntController.setSelectedDocument(found);
        }
    });
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
    
    private static class LoanSummaryComponents {
    VBox root;
    Label docValueLabel;
    Label adherentValueLabel;
    Label dateEmpruntValueLabel;
    Label dateRetourPrevValueLabel;

    LoanSummaryComponents(VBox root,
        Label docValueLabel,
        Label adherentValueLabel,
        Label dateEmpruntValueLabel,
        Label dateRetourPrevValueLabel) {
        this.root = root;
        this.docValueLabel = docValueLabel;
        this.adherentValueLabel = adherentValueLabel;
        this.dateEmpruntValueLabel = dateEmpruntValueLabel;
        this.dateRetourPrevValueLabel = dateRetourPrevValueLabel;
    }
}

////Méthodes ajoutées pour permettre d'enregitrer un emprunt///////////
private static class MemberSelectionComponents {
    VBox root;
    TextField searchField;
    Button searchBtn;
    Label idValueLabel;
    Label nomPrenomValueLabel;
    Label empruntsActuelsValueLabel;
    Label statutPenaliteValueLabel;

    MemberSelectionComponents(VBox root,
                            TextField searchField,
                            Button searchBtn,
                            Label idValueLabel,
                            Label nomPrenomValueLabel,
                            Label empruntsActuelsValueLabel,
                            Label statutPenaliteValueLabel) {
        this.root = root;
        this.searchField = searchField;
        this.searchBtn = searchBtn;
        this.idValueLabel = idValueLabel;
        this.nomPrenomValueLabel = nomPrenomValueLabel;
        this.empruntsActuelsValueLabel = empruntsActuelsValueLabel;
        this.statutPenaliteValueLabel = statutPenaliteValueLabel;
    }
}
//Classes de selection de Document et Adherent pour enregistrer un emprunt 

private static class DocumentSelectionComponents {
    VBox root;
    TextField searchField;
    Button searchBtn;
    Label titreValueLabel;
    Label auteurValueLabel;
    Label editeurValueLabel;
    Label typeValueLabel;
    Label statutValueLabel;

    DocumentSelectionComponents(VBox root,
                                TextField searchField,
                                Button searchBtn,
                                Label titreValueLabel,
                                Label auteurValueLabel,
                                Label editeurValueLabel,
                                Label typeValueLabel,
                                Label statutValueLabel) {
        this.root = root;
        this.searchField = searchField;
        this.searchBtn = searchBtn;
        this.titreValueLabel = titreValueLabel;
        this.auteurValueLabel = auteurValueLabel;
        this.editeurValueLabel = editeurValueLabel;
        this.typeValueLabel = typeValueLabel;
        this.statutValueLabel = statutValueLabel;
    }
}

// calsse de la selection emprunt 

// ====== Components pour la sélection d'un emprunt ======
private static class EmpruntSelectionComponents {
    VBox root;
    TextField searchField;
    Button searchBtn;
    Label idEmpruntValueLabel;
    Label documentValueLabel;
    Label adherentValueLabel;
    Label dateEmpruntValueLabel;
    Label dateRetourPrevValueLabel;
    Label statutValueLabel;

    Emprunt selectedEmprunt;

    EmpruntSelectionComponents(
        VBox root,
        TextField searchField,
        Button searchBtn,
        Label idEmpruntValueLabel,
        Label documentValueLabel,
        Label adherentValueLabel,
        Label dateEmpruntValueLabel,
        Label dateRetourPrevValueLabel,
        Label statutValueLabel
    ) {
        this.root = root;
        this.searchField = searchField;
        this.searchBtn = searchBtn;
        this.idEmpruntValueLabel = idEmpruntValueLabel;
        this.documentValueLabel = documentValueLabel;
        this.adherentValueLabel = adherentValueLabel;
        this.dateEmpruntValueLabel = dateEmpruntValueLabel;
        this.dateRetourPrevValueLabel = dateRetourPrevValueLabel;
        this.statutValueLabel = statutValueLabel;
    }
}




// ====== Components pour la recherche d'Adhérents ======
private static class AdherentSearchComponents {
    VBox root;
    ComboBox<String> searchType;
    TextField searchField;
    Button searchBtn;

    AdherentSearchComponents(VBox root,
                            ComboBox<String> searchType,
                            TextField searchField,
                            Button searchBtn) {
        this.root = root;
        this.searchType = searchType;
        this.searchField = searchField;
        this.searchBtn = searchBtn;
    }
}

// ====== Components pour la recherche de Documents ======
private static class DocumentSearchComponents {
    VBox root;
    ComboBox<String> searchType;
    TextField searchField;
    Button searchBtn;

    DocumentSearchComponents(VBox root,
                            ComboBox<String> searchType,
                            TextField searchField,
                            Button searchBtn) {
        this.root = root;
        this.searchType = searchType;
        this.searchField = searchField;
        this.searchBtn = searchBtn;
    }
}



    
}
