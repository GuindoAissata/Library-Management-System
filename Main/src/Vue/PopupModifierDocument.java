package Vue;

import Model.Document;
import Model.Livre;
import Model.Magazine;
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

public class PopupModifierDocument {

    public static void afficher(Document doc, Consumer<Document> onSave) {
        Stage stage = new Stage();
        stage.setTitle("Modifier le document");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Modifier le document");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs communs
        TextField titreField   = new TextField(doc.getTitre());
        TextField editeurField = new TextField(doc.getEditeur());
        TextField codeField    = new TextField(doc.getISBN_ISSN());

        titreField.setPromptText("Titre");
        editeurField.setPromptText("Éditeur");
        codeField.setEditable(false); // on ne modifie pas ISBN/ISSN

        grid.add(new Label("Titre :"),      0, 0);
        grid.add(titreField,               1, 0);
        grid.add(new Label("Éditeur :"),   0, 1);
        grid.add(editeurField,             1, 1);
        grid.add(new Label("ISBN/ISSN :"), 0, 2);
        grid.add(codeField,                1, 2);

        // Boutons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Annuler");
        Button saveBtn   = new Button("Enregistrer");

        cancelBtn.setOnAction(e -> stage.close());
        saveBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        // === CAS LIVRE ===
        if (doc instanceof Livre livre) {
            TextField auteurField = new TextField(livre.getAuteur());
            TextField nbPageField = new TextField(String.valueOf(livre.getNombre_page()));

            auteurField.setPromptText("Auteur");
            nbPageField.setPromptText("Nombre de pages");

            grid.add(new Label("Auteur :"), 0, 3);
            grid.add(auteurField,           1, 3);
            grid.add(new Label("Nb pages :"), 0, 4);
            grid.add(nbPageField,            1, 4);

            // Handler spécifique Livre
            saveBtn.setOnAction(e -> {
                try {
                    String nouveauTitre   = titreField.getText().trim();
                    String nouvelEditeur = editeurField.getText().trim();
                    String nouvAuteur    = auteurField.getText().trim();
                    String nbStr         = nbPageField.getText().trim();

                    if (nouveauTitre.isEmpty() || nouvelEditeur.isEmpty()
                            || nouvAuteur.isEmpty() || nbStr.isEmpty()) {
                        showError("Tous les champs sont obligatoires.");
                        return;
                    }

                    int nbPages = Integer.parseInt(nbStr);
                    if (nbPages <= 0) {
                        showError("Le nombre de pages doit être > 0.");
                        return;
                    }

                    // Mise à jour de l'objet
                    doc.settitre(nouveauTitre);
                    doc.setediteur(nouvelEditeur);
                    livre.setAuteur(nouvAuteur);
                    livre.setNombre_page(nbPages);

                    if (onSave != null) {
                        onSave.accept(doc);
                    }

                    stage.close();
                } catch (NumberFormatException ex) {
                    showError("Veuillez saisir un nombre de pages valide.");
                }
            });

        // === CAS MAGAZINE ===
        } else if (doc instanceof Magazine magazine) {
            TextField periodiciteField = new TextField(magazine.getPeriodicite());
            TextField numeroField      = new TextField(String.valueOf(magazine.getNumero()));

            periodiciteField.setPromptText("Périodicité");
            numeroField.setPromptText("Numéro");

            grid.add(new Label("Périodicité :"), 0, 3);
            grid.add(periodiciteField,          1, 3);
            grid.add(new Label("Numéro :"),     0, 4);
            grid.add(numeroField,               1, 4);

            // Handler spécifique Magazine
            saveBtn.setOnAction(e -> {
                try {
                    String nouveauTitre   = titreField.getText().trim();
                    String nouvelEditeur = editeurField.getText().trim();
                    String nouvPer       = periodiciteField.getText().trim();
                    String numStr        = numeroField.getText().trim();

                    if (nouveauTitre.isEmpty() || nouvelEditeur.isEmpty()
                            || nouvPer.isEmpty() || numStr.isEmpty()) {
                        showError("Tous les champs sont obligatoires.");
                        return;
                    }

                    int numero = Integer.parseInt(numStr);
                    if (numero <= 0) {
                        showError("Le numéro doit être > 0.");
                        return;
                    }

                    doc.settitre(nouveauTitre);
                    doc.setediteur(nouvelEditeur);
                    magazine.setPeriodicite(nouvPer);
                    magazine.setNumero(numero);

                    if (onSave != null) {
                        onSave.accept(doc);
                    }

                    stage.close();
                } catch (NumberFormatException ex) {
                    showError("Veuillez saisir un numéro valide.");
                }
            });

        // === CAS AUTRE TYPE (au cas où) ===
        } else {
            saveBtn.setOnAction(e -> {
                String nouveauTitre   = titreField.getText().trim();
                String nouvelEditeur = editeurField.getText().trim();

                if (nouveauTitre.isEmpty() || nouvelEditeur.isEmpty()) {
                    showError("Le titre et l'éditeur sont obligatoires.");
                    return;
                }

                doc.settitre(nouveauTitre);
                doc.setediteur(nouvelEditeur);

                if (onSave != null) {
                    onSave.accept(doc);
                }
                stage.close();
            });
        }

        buttons.getChildren().addAll(cancelBtn, saveBtn);

        root.getChildren().addAll(title, grid, buttons);

        Scene scene = new Scene(root, 450, 320);
        stage.setScene(scene);
        stage.setResizable(false);
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
