package sgeb.ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sgeb.model.Livre;
import sgeb.exception.AdherentException;
import sgeb.exception.DocumentException;
import sgeb.model.Adherent;
import sgeb.model.Document;
import sgeb.model.Genre;

public class MainApplication extends Application {

    /*
    Control : boutons et zones de textes
    Layout : agencement des control sur la page
    Scene : 
    Stage : fenêtre où s'affiche notre application
    Event : que se passe-t-il quand je clique / manipule un control



    Layout
    HBox : arranges its content nodes horizontally in a single row
    HBox : --------------------------- vertically in a single column
    StackPlane : place its content nodes in a back-to-front single stack
    FlowPane : arranges its content nodes in either a horizontal or vertical "flow", wrapping at the specified width/height boundaries
    GridPane : enables the developper to create a flexible grid of rows and colums in which to lay out content nodes
    BorderPane : lays out its content nodes in the top, bottom, right, left or center region

    Stage methods
    primStage.show();
    primStage.setScene(sc);
    primStage.setTitle(value);
    primStage.getTitle();
    primStage.setWidth(value);
    primStage.setHeight(value);
    primStage.getWidth();
    primStage.getHeight();
    primStage.setFullScreen(true);
    primStage.isFullScreen();
    primStage.close();
    
    */


    private ObservableList<Adherent> listeDesAdherents;



    public static void main(String[] args){
        launch();

    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: skyblue");

        

        // Titre de la page d'accueil
        Label titrePage = new Label("Menu principal");
        titrePage.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Creer differents boutons
        Button btnCatalogue = new Button("Accéder au catalogue");
        btnCatalogue.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
        Button btnListeEmprunts = new Button("Accéder à la liste des \ndocuments empruntés");
        btnListeEmprunts.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
        Button btnListeAdherents = new Button("Accéder à la liste des \nadhérents");
        btnListeAdherents.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");


        // Boite contenant les boutons
        VBox cadreBoutons = new VBox(20, btnCatalogue, btnListeEmprunts, btnListeAdherents);
        cadreBoutons.setPadding(new Insets(20));
        cadreBoutons.setStyle("-fx-alignment: center;"+
            "-fx-background-color: lightyellow;"+
            "-fx-border-color: lightgray;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius:20;" +
            "-fx-backgroung-radius:20");

        // Creer un menu principal et le positionner dans la fenêtre
        VBox menuPrincipal = new VBox(30, titrePage, cadreBoutons);
        menuPrincipal.setPadding(new Insets(50));
        menuPrincipal.setStyle("-fx-background-color: white;" +
                                " -fx-padding: 10; " +
                                " -fx-border-color: gray;" +
                                "-fx-background-radius:20;" + // donner coin arrondis
                                "-fx-border-color: gray;" + 
                                "-fx-border-radius:20;" +
                                " -fx-border-width: 2");

        menuPrincipal.setMaxWidth(500);
        menuPrincipal.setMaxHeight(500);
        menuPrincipal.setAlignment(Pos.CENTER);

        root.setCenter(menuPrincipal);





        // ACCEDER AU CATALOGUE

        btnCatalogue.setOnAction(e -> {
            // Créer le nouveau layout pour la page catalogue
            BorderPane pageCatalogue = new BorderPane();
            pageCatalogue.setStyle("-fx-bacground-color: white");


            // Barre de recherche à mettre en haut de la page
            HBox barreRecherche = new HBox(20);
            barreRecherche.setPadding(new Insets(15));
            barreRecherche.setAlignment(Pos.CENTER_LEFT);

            Label lblRecherche = new Label("Recherche par :");
            lblRecherche.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            ComboBox<String> cbCritere = new ComboBox<>(); // Pour faire un menu deroulant
            cbCritere.setStyle("-fx-font-size: 16px");
            cbCritere.getItems().addAll("Titre","Auteur", "ISBN", "ID", "Genre");
            cbCritere.setValue("Titre"); // valeur par défaut

            // Champ de texte pour que l'utilisateur puisse écrire
            TextField tfRecherche = new TextField();
            tfRecherche.setPromptText("Recherche dans la base de données de la bibliothèque");
            tfRecherche.setPrefWidth(600);
            tfRecherche.setStyle("fx-font-size: 16px");

            Button btnRechercher = new Button("Rechercher");
            btnRechercher.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");


            // Mettre tout ça ensemble
            HBox zoneTexteBouton = new HBox(5, tfRecherche, btnRechercher);
            zoneTexteBouton.setAlignment(Pos.CENTER_LEFT);

            barreRecherche.getChildren().addAll(lblRecherche, cbCritere, zoneTexteBouton);
            pageCatalogue.setTop(barreRecherche);



            // Zone centrale : colonne gauche et droite
            HBox centre = new HBox(20);
            centre.setPadding(new Insets(20));

            // Colonne gauche : liste catalogue sous forme de table
            VBox gauche = new VBox(10);
            gauche.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            gauche.setPrefWidth(1200);
            gauche.setMaxWidth(Double.MAX_VALUE);
            Label titreCatalogue = new Label("Catalogue des documents");
            titreCatalogue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // AJOUTER UNE TABLEVIEW pour le catalogue
            // TableView<Document> tableDoc = new TableView<>();
            gauche.getChildren().addAll(titreCatalogue /*, table */);


            // Colonne droite : détail du document sélectionné
            VBox droite = new VBox(10);
            droite.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            droite.setAlignment(Pos.TOP_CENTER);
            droite.setPrefWidth(400);
            droite.setMaxWidth(Double.MAX_VALUE);
            Label lbldetailDoc = new Label("Détails");
            lbldetailDoc.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            droite.getChildren().add(lbldetailDoc);


            centre.getChildren().addAll(gauche, droite);
            pageCatalogue.setCenter(centre);



            // Boutons en pied de page
            HBox piedPage = new HBox(20);
            piedPage.setPadding(new Insets(15));
            piedPage.setAlignment(Pos.CENTER);

            Button btnAjouter = new Button("Enregistrer un nouveau document");
            btnAjouter.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnMaj = new Button("Mettre à jour un document");
            btnMaj.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnRetirer = new Button("Retirer un document");
            btnRetirer.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnRetour = new Button("Retour au menu principal");
            btnRetour.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
           
           

            btnRetour.setOnAction(retourMenuPrincipal -> {
                root.setCenter(menuPrincipal);
                root.setBottom(null); // faire disparaitre le pied de page
            }); 

            btnRechercher.setOnAction(recherche ->{
                

            });


            piedPage.getChildren().addAll(btnAjouter, btnMaj, btnRetirer, btnRetour);
            pageCatalogue.setBottom(piedPage);


            // Remplacer le centre du BorderPane par la pageCatalogue
            root.setCenter(pageCatalogue);

        });



        // ACCEDER A LA LISTE DES EMPRUNTS
        btnListeEmprunts.setOnAction(e -> {
            // Créer le nouveau layout pour la page catalogue
            BorderPane pageListeEmprunts = new BorderPane();
            pageListeEmprunts.setStyle("-fx-bacground-color: white");

            // Zone centrale : colonne gauche et droite
            HBox centre = new HBox(20);
            centre.setPadding(new Insets(20));

            // Colonne gauche : liste catalogue sous forme de table
            VBox gauche = new VBox(10);
            gauche.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            gauche.setPrefWidth(1200);
            gauche.setMaxWidth(Double.MAX_VALUE);
            Label titreListeEmprunts = new Label("Emprunts");
            titreListeEmprunts.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // AJOUTER UNE TABLEVIEW pour la liste d'adhérents
            // TableView<Adherent> tableAdh = new TableView<>();
            gauche.getChildren().addAll(titreListeEmprunts /*, table */);


            // Colonne droite : détail du document sélectionné
            VBox droite = new VBox(10);
            droite.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            droite.setAlignment(Pos.TOP_CENTER);
            droite.setPrefWidth(400);
            droite.setMaxWidth(Double.MAX_VALUE);
            Label lbldetailEmp = new Label("Détails");
            lbldetailEmp.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            droite.getChildren().add(lbldetailEmp);


            centre.getChildren().addAll(gauche, droite);
            pageListeEmprunts.setCenter(centre);



            // Boutons en pied de page
            HBox piedPage = new HBox(20);
            piedPage.setPadding(new Insets(15));
            piedPage.setAlignment(Pos.CENTER);

            
            Button btnAjouter = new Button("Enregistrer un nouvel emprunt");
            btnAjouter.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnRetourDoc = new Button("Enregistrer un retour");
            btnRetourDoc.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnEnCours = new Button("Afficher les emprunts en cours");
            btnEnCours.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnEnRetard = new Button("Afficher les documents en retard de rendu");
            btnEnRetard.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnRetour = new Button("Retour au menu principal");
            btnRetour.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
 


            btnRetour.setOnAction(retourMenuPrincipal -> {
                root.setCenter(menuPrincipal);
                root.setBottom(null); // faire disparaitre le pied de page
            }); 


            piedPage.getChildren().addAll(btnAjouter, btnRetourDoc, btnEnCours, btnEnRetard, btnRetour);
            pageListeEmprunts.setBottom(piedPage);


            // Remplacer le centre du BorderPane par la pageCatalogue
            root.setCenter(pageListeEmprunts);

        });





        
        





        // ACCEDER A LA LISTE DES ADHERENTS

        btnListeAdherents.setOnAction(e -> {
            // Créer le nouveau layout pour la page catalogue
            BorderPane pageListeAdherents = new BorderPane();
            pageListeAdherents.setStyle("-fx-bacground-color: white");


            // Barre de recherche à mettre en haut de la page
            HBox barreRecherche = new HBox(20);
            barreRecherche.setPadding(new Insets(15));
            barreRecherche.setAlignment(Pos.CENTER_LEFT);

            Label lblRecherche = new Label("Recherche par :");
            lblRecherche.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            ComboBox<String> cbCritere = new ComboBox<>(); // Pour faire un menu deroulant
            cbCritere.setStyle("-fx-font-size: 16px");
            cbCritere.getItems().addAll("ID Adhérent","NOM");
            cbCritere.setValue("NOM"); // valeur par défaut

            // Champ de texte pour que l'utilisateur puisse écrire
            TextField tfRecherche = new TextField();
            tfRecherche.setPromptText("Recherche dans la base de données de la bibliothèque");
            tfRecherche.setPrefWidth(600);
            tfRecherche.setStyle("fx-font-size: 16px");

            Button btnRechercher = new Button("Rechercher");
            btnRechercher.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");


            // Mettre tout ça ensemble
            HBox zoneTexteBouton = new HBox(5, tfRecherche, btnRechercher);
            zoneTexteBouton.setAlignment(Pos.CENTER_LEFT);

            barreRecherche.getChildren().addAll(lblRecherche, cbCritere, zoneTexteBouton);
            pageListeAdherents.setTop(barreRecherche);




            // Zone centrale : colonne gauche et droite
            HBox centre = new HBox(20);
            centre.setPadding(new Insets(20));

            // Colonne gauche : liste catalogue sous forme de table
            VBox gauche = new VBox(10);
            gauche.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            gauche.setPrefWidth(1200);
            gauche.setMaxWidth(Double.MAX_VALUE);
            Label titreListeAdherents = new Label("Adhérents");
            titreListeAdherents.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // AJOUTER UNE TABLEVIEW pour la liste d'adhérents
            // =========================
            // TableView des adhérents
            // =========================
            TableView<Adherent> tableAdherents = new TableView<>();

            TableColumn<Adherent, Integer> colID = new TableColumn<>("ID");
            colID.setCellValueFactory(new PropertyValueFactory<>("id_adherent"));

            TableColumn<Adherent, String> colNom = new TableColumn<>("NOM");
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

            TableColumn<Adherent, String> colPrenom = new TableColumn<>("PRENOM");
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            TableColumn<Adherent, String> colEmail = new TableColumn<>("EMAIL");
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<Adherent, String> colTelephone = new TableColumn<>("TELEPHONE");
            colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

            TableColumn<Adherent, Double> colMontantPenalite = new TableColumn<>("MONTANT PENALITE");
            colMontantPenalite.setCellValueFactory(new PropertyValueFactory<>("montantPenalite"));

            

            tableAdherents.getColumns().addAll(colID, colNom, colPrenom, colEmail, colTelephone, colMontantPenalite);


            try{
                listeDesAdherents = FXCollections.observableArrayList(
                        new Adherent("PHICHITH", "Nouannapha", "nophi@dauphine.eu", "0102030405"),
                        new Adherent("GUINDO", "Aissata", "aguindo@dauphine.eu", "0101010101"));
                tableAdherents.setItems(listeDesAdherents); // Pour afficher la liste des adherents dans le tableau
            }
            catch(AdherentException a){
                System.out.println(a.getMessage());
            }


            gauche.getChildren().addAll(titreListeAdherents, tableAdherents);


            // Colonne droite : détail du document sélectionné
            VBox droite = new VBox(10);
            droite.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");
            droite.setAlignment(Pos.TOP_CENTER);
            droite.setPrefWidth(400);
            droite.setMaxWidth(Double.MAX_VALUE);
            Label lbldetailAdh = new Label("Détails");
            lbldetailAdh.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            droite.getChildren().add(lbldetailAdh);


            centre.getChildren().addAll(gauche, droite);
            pageListeAdherents.setCenter(centre);



            // Boutons en pied de page
            HBox piedPage = new HBox(20);
            piedPage.setPadding(new Insets(15));
            piedPage.setAlignment(Pos.CENTER);

            Button btnAjouter = new Button("Enregistrer un nouvel adhérent");
            btnAjouter.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnMaj = new Button("Mettre à jour les informations \nd'un adhérent");
            btnMaj.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnPenalite = new Button("Afficher les adhérents ayant un \nretard de retour de document(s)");
            btnPenalite.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            Button btnAdherentHistorique = new Button("Afficher l'historique des \nemprunts de l'adhérent");
            btnAdherentHistorique.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");
            
            Button btnRetour = new Button("Retour au menu principal");            
            btnRetour.setStyle("-fx-font-size: 16 px; -fx-font-weight: bold");

            btnRetour.setOnAction(retourMenuPrincipal -> {
                root.setCenter(menuPrincipal);
                root.setBottom(null); // faire disparaitre le pied de page
            }); 


            piedPage.getChildren().addAll(btnAjouter, btnMaj, btnPenalite, btnAdherentHistorique, btnRetour);
            pageListeAdherents.setBottom(piedPage);


            // Remplacer le centre du BorderPane par la pageCatalogue
            root.setCenter(pageListeAdherents);

        });


































        // Scene 
        Scene scene = new Scene(root);
        primStage.setScene(scene);
        
        // Adapter la fenetre a la taille de l'ecran
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primStage.setX(screenBounds.getMinX());
        primStage.setY(screenBounds.getMinY());
        primStage.setWidth(screenBounds.getWidth());
        primStage.setHeight(screenBounds.getHeight());
        primStage.setResizable(false);

        primStage.setTitle("SGEB - Bibliothèque");
        primStage.show();
}


    /*
    

    @Override
    public void start(Stage stage) {


       

        // =========================
        // TableView des livres
        // =========================
        TableView<Livre> tableLivres = new TableView<>();

        TableColumn<Livre, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(new PropertyValueFactory<>("id_document"));

        TableColumn<Livre, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));

        TableColumn<Livre, String> colAuteur = new TableColumn<>("Auteur");
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        TableColumn<Livre, String> colEditeur = new TableColumn<>("Éditeur");
        colEditeur.setCellValueFactory(new PropertyValueFactory<>("editeur"));

        TableColumn<Livre, Integer> colAnnee = new TableColumn<>("Année");
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));

        TableColumn<Livre, String> colGenre = new TableColumn<>("Genre");
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Livre, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        TableColumn<Livre, Integer> colPages = new TableColumn<>("Pages");
        colPages.setCellValueFactory(new PropertyValueFactory<>("nbPages"));

        tableLivres.getColumns().addAll(colID, colTitre, colAuteur, colEditeur,
                colAnnee, colGenre, colISBN, colPages);

        // =========================
        // Liste de livres test
        // =========================
        listeDesLivres = FXCollections.observableArrayList(
                new Livre("Harry Potter", "Gallimard", 1998, Genre.ROMAN, "9782070643028", "J.K. Rowling", 309),
                new Livre("Le Seigneur des Anneaux", "HarperCollins", 1954, Genre.ROMAN, "9780261102385", "J.R.R. Tolkien", 1178)
        );

        tableLivres.setItems(listeDesLivres);

        // =========================
        // Formulaire d'ajout / modification
        // =========================
        VBox formulaire = new VBox(10);
        formulaire.setPadding(new Insets(10));

        Label labelFormulaire = new Label("Ajouter un livre :");

        TextField champTitre = new TextField();
        champTitre.setPromptText("Titre");

        TextField champAuteur = new TextField();
        champAuteur.setPromptText("Auteur");

        TextField champEditeur = new TextField();
        champEditeur.setPromptText("Éditeur");

        TextField champAnnee = new TextField();
        champAnnee.setPromptText("Année");

        TextField champGenre = new TextField();
        champGenre.setPromptText("Genre");

        TextField champISBN = new TextField();
        champISBN.setPromptText("ISBN");

        TextField champPages = new TextField();
        champPages.setPromptText("Nombre de pages");

        Button boutonAjouter = new Button("Ajouter");

        formulaire.getChildren().addAll(labelFormulaire, champTitre, champAuteur, champEditeur,
                champAnnee, champGenre, champISBN, champPages, boutonAjouter);

        // =========================
        // Bouton Retour
        // =========================
        Button boutonRetour = new Button("Retour au menu");

        // =========================
        // Layout section Documents
        // =========================
        HBox sectionDocuments = new HBox(20);
        sectionDocuments.setPadding(new Insets(10));
        sectionDocuments.getChildren().addAll(tableLivres, formulaire);

        // Boutons Modifier / Supprimer
        HBox boutonsAction = new HBox(10);
        boutonsAction.setPadding(new Insets(10));
        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        boutonsAction.getChildren().addAll(btnModifier, btnSupprimer);

        // Documents View
        VBox documentsView = new VBox(10);
        documentsView.setPadding(new Insets(10));
        documentsView.getChildren().addAll(boutonRetour, sectionDocuments, boutonsAction);

        boutonRetour.setOnAction(e -> root.setCenter(menuPrincipal));

        // =========================
        // Action Ajouter
        // =========================
        boutonAjouter.setOnAction(e -> ajouterLivre());

        // =========================
        // Action Modifier
        // =========================
        btnModifier.setOnAction(e -> {
            Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
            if (livreSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un livre à modifier !");
                alert.showAndWait();
                return;
            }

            champTitre.setText(livreSelectionne.getTitre());
            champAuteur.setText(livreSelectionne.getAuteur());
            champEditeur.setText(livreSelectionne.getEditeur());
            champAnnee.setText(String.valueOf(livreSelectionne.getAnneePublication()));
            champGenre.setText(livreSelectionne.getGenre().name());
            champISBN.setText(livreSelectionne.getISBN());
            champPages.setText(String.valueOf(livreSelectionne.getNbPages()));

            boutonAjouter.setText("Valider modification");
            boutonAjouter.setOnAction(ev -> {
                try {
                    String titre = champTitre.getText().trim();
                    String auteur = champAuteur.getText().trim();
                    String editeur = champEditeur.getText().trim();
                    String genre = champGenre.getText().trim();
                    String isbn = champISBN.getText().trim();
                    int annee = Integer.parseInt(champAnnee.getText().trim());
                    int nbPages = Integer.parseInt(champPages.getText().trim());

                    if (titre.isEmpty() || auteur.isEmpty() || editeur.isEmpty() || genre.isEmpty() || isbn.isEmpty()) {
                        throw new IllegalArgumentException("Tous les champs doivent être remplis !");
                    }
                    if (!auteur.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                        throw new IllegalArgumentException("Le nom de l'auteur doit contenir uniquement des lettres !");
                    }

                    livreSelectionne.setTitre(titre);
                    livreSelectionne.setAuteur(auteur);
                    livreSelectionne.setEditeur(editeur);
                    livreSelectionne.setAnneePublication(annee);
                    livreSelectionne.setGenre(genre);
                    livreSelectionne.setISBN(isbn);
                    livreSelectionne.setNbPages(nbPages);

                    tableLivres.refresh();

                    boutonAjouter.setText("Ajouter");
                    champTitre.clear();
                    champAuteur.clear();
                    champEditeur.clear();
                    champAnnee.clear();
                    champGenre.clear();
                    champISBN.clear();
                    champPages.clear();

                    // Revenir à l'action Ajouter normale
                    boutonAjouter.setOnAction(ev2 -> ajouterLivre());

                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Année et nombre de pages doivent être des nombres !");
                    alert.showAndWait();
                } catch (IllegalArgumentException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                    alert.showAndWait();
                } catch {DocumentException ex}{
                    Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                    alert.showAndWait();

                }
            });
        });

        // =========================
        // Action Supprimer
        // =========================
        btnSupprimer.setOnAction(e -> {
            Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
            if (livreSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un livre à supprimer !");
                alert.showAndWait();
                return;
            }
            listeDesLivres.remove(livreSelectionne);
        });

        // =========================
        // Boutons menu principal
        // =========================
        btnDocuments.setOnAction(e -> root.setCenter(documentsView));

        btnAdherents.setOnAction(e -> {
            VBox adherentsView = new VBox(10);
            adherentsView.setPadding(new Insets(10));
            Button retourAdh = new Button("Retour au menu");
            retourAdh.setOnAction(ev -> root.setCenter(menuPrincipal));
            adherentsView.getChildren().addAll(new Label("Liste des adhérents (à compléter)"), retourAdh);
            root.setCenter(adherentsView);
        });

        btnEmprunts.setOnAction(e -> {
            VBox empruntsView = new VBox(10);
            empruntsView.setPadding(new Insets(10));
            Button retourEmp = new Button("Retour au menu");
            retourEmp.setOnAction(ev -> root.setCenter(menuPrincipal));
            empruntsView.getChildren().addAll(new Label("Liste des emprunts (à compléter)"), retourEmp);
            root.setCenter(empruntsView);
        });
    }

    private void ajouterLivre() {
        // Cette méthode sera remplacée par la vraie action Add dans start()
        // Elle peut être réécrite pour ajouter le livre comme précédemment
    }

        */


}
