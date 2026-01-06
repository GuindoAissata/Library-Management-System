package Controler;

import Model.BibliothequeManager;
import Model.Document;
import Model.Livre;
import Model.Magazine;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class AjouterDocumentController {

    private final BibliothequeManager manager;

    private final ComboBox<String> typeBox;
    private final TextField isbnField;
    private final TextField titleField;
    private final TextField authorField;
    private final TextField editorField;
    private final TextField nbPagesField;
    private final TextField periodiciteField;
    private final TextField numeroField;
    private final Button addBtn;

    public AjouterDocumentController(
            BibliothequeManager manager,
            ComboBox<String> typeBox,
            TextField isbnField,
            TextField titleField,
            TextField authorField,
            TextField editorField,
            TextField nbPagesField,
            TextField periodiciteField,
            TextField numeroField,
            Button addBtn
    ) {
        this.manager = manager;
        this.typeBox = typeBox;
        this.isbnField = isbnField;
        this.titleField = titleField;
        this.authorField = authorField;
        this.editorField = editorField;
        this.nbPagesField = nbPagesField;
        this.periodiciteField = periodiciteField;
        this.numeroField = numeroField;
        this.addBtn = addBtn;

        initHandler();
    }

    private void initHandler() {
        addBtn.setOnAction(e -> handleAdd());
    }

    private void handleAdd() {
        String type = typeBox.getValue();
        String isbn = isbnField.getText();
        String titre = titleField.getText();
        String auteur = authorField.getText();
        String editeur = editorField.getText();

        if (type == null) {
            showError("Veuillez sélectionner un type de document.");
            return;
        }
        if (isbn.isBlank() || titre.isBlank()|| editeur.isBlank()) {
            showError("Tous les champs généraux doivent être remplis.");
            return;
        }

        try {
            Document doc;

            if (type.equals("Livre")) {

                if (nbPagesField.getText().isBlank()) {
                    showError("Veuillez indiquer le nombre de pages.");
                    return;
                }
                if(auteur.isBlank()){
                    showError("Veuillez renseigner l'auteur");
                    return;
                }

                int nbPages = Integer.parseInt(nbPagesField.getText());

                doc = new Livre(isbn,titre,editeur,auteur, nbPages);
            }

            else { // MAGAZINE

                if (periodiciteField.getText().isBlank() || numeroField.getText().isBlank()) {
                    showError("Veuillez remplir numéro et périodicité.");
                    return;
                }

                int numero = Integer.parseInt(numeroField.getText());
                String periodicite = periodiciteField.getText();

                doc = new Magazine(isbn,titre,editeur,periodicite, numero);
            }

            // Ajout via le manager (DAO + ObservableList)
            manager.AddDocument(doc);

            showInfo("Document ajouté: ","Le document a été enregistré avec succès !");
            clearFields();

        } catch (NumberFormatException ex) {
            showError("Nombre invalide dans les champs numériques.");
        } catch (Exception ex) {
            showError("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        editorField.clear();
        nbPagesField.clear();
        periodiciteField.clear();
        numeroField.clear();
        typeBox.setValue(null);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(null);
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
