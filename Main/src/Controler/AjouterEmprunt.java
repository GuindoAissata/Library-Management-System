package Controler;

import Model.Adherent;
import Model.BibliothequeManager;
import Model.Document;
import Model.Emprunt;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.*;

import java.time.LocalDate;

public class AjouterEmprunt {

    private final BibliothequeManager manager;
    private Adherent selectedAdherent;
    private Document selectedDocument;

    private final Label docSummaryLabel;
    private final Label adherentSummaryLabel;
    private final Label dateEmpruntLabel;
    private final Label dateRetourPrevLabel;
    private final Button saveBtn;
    private final Button cancelBtn;
    //Ajouté pour permettre un clear des champs de recherche après enregistrement 
    private final TextField memberSearchField;
    private final TextField documentSearchField;

    // Ajouté pour permettre le refresh dès que un document est emprunté
    private final TableView<Document> documentsTable;

    public AjouterEmprunt(BibliothequeManager manager,
                        Label docSummaryLabel,
                        Label adherentSummaryLabel,
                        Label dateEmpruntLabel,
                        Label dateRetourPrevLabel,
                        Button saveBtn,
                        Button cancelBtn,
                        TextField memberSearchField, TextField documentSearchField,
                        TableView<Document> documentsTable) {
        this.manager = manager;
        this.docSummaryLabel = docSummaryLabel;
        this.adherentSummaryLabel = adherentSummaryLabel;
        this.dateEmpruntLabel = dateEmpruntLabel;
        this.dateRetourPrevLabel = dateRetourPrevLabel;
        this.saveBtn = saveBtn;
        this.cancelBtn = cancelBtn;
        this.memberSearchField = memberSearchField;
        this.documentSearchField = documentSearchField;
        this.documentsTable = documentsTable;

        initHandlers();
        setSearchFieldListeners();// on l'a ajouté pour faire un clear des champs de rechecche après un précédent emprunt
    }

    private void initHandlers() {
        saveBtn.setOnAction(e -> handleSave());
        cancelBtn.setOnAction(e -> handleCancel());
    }

    public void setSelectedAdherent(Adherent adherent) {
        this.selectedAdherent = adherent;
        if (adherent != null) {
            adherentSummaryLabel.setText(adherent.getNom() + " " + adherent.getPrenom());
        } else {
            adherentSummaryLabel.setText("-");
        }
    }

    public void setSelectedDocument(Document document) {
        this.selectedDocument = document;
        if (document != null) {
            docSummaryLabel.setText(document.getTitre());
            LocalDate suggestDate = LocalDate.now().plusMonths(1);
            dateRetourPrevLabel.setText(suggestDate.toString());
        } else {
            docSummaryLabel.setText("-");
        }
    }

    private void handleSave() {
        if (selectedAdherent == null) {
            showError("Veuillez d'abord sélectionner un adhérent.");
            return;
        }
        if (selectedDocument == null) {
            showError("Veuillez d'abord sélectionner un document.");
            return;
        }
        if (!selectedDocument.getEstDisponible()) {
            showError("Ce document est déjà emprunté.");
            return;
        }

        try {
            Emprunt emprunt = new Emprunt(selectedAdherent, selectedDocument);

            // Enregistrement en BD + en mémoire
            manager.Enregistrer_Emprunt(selectedDocument,selectedAdherent) ;

            // mettre à jour la dispo du document dans l'appli
            selectedDocument.setEstDisponible(false);

            // Mise à jour du résumé
            LocalDate dEmp = emprunt.getDate_Emprunt();
            LocalDate dPrev = emprunt.getDate_RetourPrevue();

            dateEmpruntLabel.setText(dEmp.toString());
            dateRetourPrevLabel.setText(dPrev.toString());

            showInfo("Emprunt enregistré",
                    "Adhérent : " + selectedAdherent.getNom() + " " + selectedAdherent.getPrenom() +
            "\nDocument : " + selectedDocument.getTitre());

            // Ensuite nettoyer les deux champs de recherche
            memberSearchField.clear();
            documentSearchField.clear();
            dateEmpruntLabel.setText("-");
            dateRetourPrevLabel.setText("-");
            documentsTable.refresh();// pour mettre à jour la tableview dans document
            

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Erreur lors de l'enregistrement de l'emprunt : " + ex.getMessage());
        }
    }

    private void setSearchFieldListeners() {
    // Quand on tape dans la recherche adhérent
    memberSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
        adherentSummaryLabel.setText("-");
        selectedAdherent = null;
    });

    // Quand on tape dans la recherche document
    documentSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
        docSummaryLabel.setText("-");
        selectedDocument = null; 
    });}



    private void handleCancel() {
        setSelectedAdherent(null);
        setSelectedDocument(null);
        dateEmpruntLabel.setText("-");
        dateRetourPrevLabel.setText("-");
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
