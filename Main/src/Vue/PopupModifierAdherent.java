package Vue;

import Model.Adherent;
import Model.BibliothequeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

import Controler.PayerPenaliteControler;

public class PopupModifierAdherent {

    public static void afficher(BibliothequeManager manager,Adherent adherent, Consumer<Adherent> onSave) {
        Stage stage = new Stage();
        stage.setTitle("Modifier l'adhérent");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        root.setMinHeight(Region.USE_PREF_SIZE);


        Label title = new Label("Modifier l'adhérent");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // ID adhérent (code fonctionnel) - en lecture seule
        Label idLabel = new Label(adherent.getID_Adherent());
        idLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

        // Champs modifiables
        TextField nomField = new TextField(adherent.getNom());
        TextField prenomField = new TextField(adherent.getPrenom());
        TextField mailField = new TextField(adherent.getMail());
        TextField contactField = new TextField(adherent.getContact());

        nomField.setPromptText("Nom");
        prenomField.setPromptText("Prénom");
        mailField.setPromptText("Email");
        contactField.setPromptText("Téléphone");

        grid.add(new Label("ID Adhérent :"), 0, 0);
        grid.add(idLabel,                  1, 0);

        grid.add(new Label("Nom :"),       0, 1);
        grid.add(nomField,                 1, 1);

        grid.add(new Label("Prénom :"),    0, 2);
        grid.add(prenomField,              1, 2);

        grid.add(new Label("Email :"),     0, 3);
        grid.add(mailField,                1, 3);

        grid.add(new Label("Téléphone :"), 0, 4);
        grid.add(contactField,             1, 4);

         // Champ affichant la pénalité actuelle (non éditable)
        TextField penaliteField = new TextField();
        penaliteField.setEditable(false);
        penaliteField.setMaxWidth(120);

        // Champ pour saisir la somme à payer
        TextField sommePayerField = new TextField();
        sommePayerField.setPromptText("Montant en €");
        sommePayerField.setMaxWidth(120);

        Button payerBtn = new Button("Payer pénalité");
        payerBtn.setStyle("-fx-background-color: #f97316; -fx-text-fill: white; -fx-font-weight: bold;");

         // Placement dans la grille
        grid.add(new Label("Pénalité actuelle :"), 0, 5);
        grid.add(penaliteField,                   1, 5);

        grid.add(new Label("Somme à acquitter :"), 0, 6);
        grid.add(sommePayerField,                  1, 6);

        grid.add(payerBtn,                         1, 7);
        // le controleur gère toute la logique 
        new PayerPenaliteControler(adherent, penaliteField, sommePayerField, payerBtn);
        



        // Boutons enregistrer ou Annuler 
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Annuler");
        Button saveBtn   = new Button("Enregistrer");

        cancelBtn.setOnAction(e -> stage.close());
        saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String nouveauNom     = nomField.getText().trim();
            String nouveauPrenom  = prenomField.getText().trim();
            String nouveauMail    = mailField.getText().trim();
            String nouveauContact = contactField.getText().trim();

            if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty()
                    || nouveauMail.isEmpty() || nouveauContact.isEmpty()) {
                showError("Tous les champs sont obligatoires.");
                return;
            }
            if ( nouveauContact!=null && (!nouveauContact.matches("^[0-9]+$") || nouveauContact.length()<10 || nouveauContact.length()>10)) {
                showError("Contact invalide");
                return;
            }
            if(nouveauMail !=null && !nouveauMail.matches("^[a-zA-Z][a-zA-Z0-9]*(?:\\.[a-zA-Z][a-zA-Z0-9]*)?@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$")){ 
                showError("Adresse mail invalide");
                return;
            }
            for (Adherent a : manager.getList_Adherent()) {
            if(nouveauMail.equalsIgnoreCase(a.getMail()) && adherent.getID_Adherent() != a.getID_Adherent() ){
                showError("Un compte avec un ID different existe déjà avec cete adresse ");
            return;}}

            try {
                // setters susceptibles de faire des vérifications (regex mail, téléphone, etc.)
                adherent.setNom(nouveauNom);
                adherent.setPrenom(nouveauPrenom);
                adherent.setMail(nouveauMail);
                adherent.setContact(nouveauContact);

                if (onSave != null) {
                    onSave.accept(adherent);
                }

                stage.close();
            } catch (IllegalArgumentException ex) {
                showError("Erreur : " + ex.getMessage());
            }
        });

        buttons.getChildren().addAll(cancelBtn, saveBtn);

        root.getChildren().addAll(title, grid, buttons);

    ScrollPane scrollPane = new ScrollPane(root);
    scrollPane.setFitToWidth(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    root.setMinHeight(Region.USE_PREF_SIZE);
    Scene scene = new Scene(scrollPane, 480, 350);
    stage.setScene(scene);
    stage.setResizable(true);
    stage.showAndWait();

    }

    private static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
