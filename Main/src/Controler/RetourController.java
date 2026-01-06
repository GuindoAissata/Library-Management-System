package Controler;

import java.time.LocalDate;
import java.util.Locale;

import Model.Document;

import Model.Adherent;
import Model.ArgumentException;
import Model.BibliothequeManager;
import Model.Emprunt;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class RetourController {

    private final BibliothequeManager manager;
    private final Button saveBtn;
    private final Button cancelBtn;
    private final Label penaltyAmountLabel;
    private final TableView<Emprunt> empruntsTable;
    // ajouté pour permettre le refresh
    private final TableView<Document> documentsTable;
    private final TableView<Adherent> adherentsTable;

    // L'emprunt actuellement sélectionné (via la liste déroulante ou le clic sur la table)
    private Emprunt selectedEmprunt;

    public RetourController(BibliothequeManager manager,
                                    Button saveBtn,
                                    Button cancelBtn,
                                    Label penaltyAmountLabel,
                                    TableView<Emprunt> empruntsTable,
                                    TableView<Document> documentsTable,
                                    TableView<Adherent> adherentsTable) {
        this.manager = manager;
        this.saveBtn = saveBtn;
        this.cancelBtn = cancelBtn;
        this.penaltyAmountLabel = penaltyAmountLabel;
        this.empruntsTable = empruntsTable;
        this.documentsTable = documentsTable;
        this.adherentsTable = adherentsTable;

        initHandlers();
    }

    // Appelé depuis l'interface quand l'utilisateur choisit un emprunt
    public void setSelectedEmprunt(Emprunt e) {
        this.selectedEmprunt = e;
    }

    private void initHandlers() {
        saveBtn.setOnAction(e -> handleSave());
        cancelBtn.setOnAction(e -> handleCancel());
    }

    private void handleSave() {
        if (selectedEmprunt == null) {
            showError("Veuillez d'abord sélectionner un emprunt en cours.");
            return;
        }

        try {
            // Appel de ta logique métier
            boolean ok = manager.Enregistrer_Retour(selectedEmprunt);
            if (!ok) {
                showError("Le retour n'a pas pu être enregistré.");
                return;
            }

            // Met à jour la pénalité affichée
            double penalite = selectedEmprunt.getPenalite();
            penaltyAmountLabel.setText(String.format(Locale.FRANCE, "%.2f €", penalite));

            // Rafraîchir la table (statut, date retour réelle, dispo doc, etc.)
            // Rafraichir les tables après le retour 
            if (empruntsTable != null) {empruntsTable.refresh();}
            if(documentsTable !=null){documentsTable.refresh();}
            if(adherentsTable !=null){adherentsTable.refresh();}



            showInfo("Retour enregistré",
                    "Le retour de l'emprunt " + selectedEmprunt.getID_Emprunt() +
                    " a été enregistré avec succès (" + LocalDate.now() + ").");

                    


            // On pourrait aussi remettre selectedEmprunt à null si tu veux forcer une nouvelle sélection
            // selectedEmprunt = null;

        } catch (ArgumentException ex) {
            showError("Erreur lors de l'enregistrement du retour : " + ex.getMessage());
        }
    }

    private void handleCancel() {
        // Comportement simple : on remet juste la pénalité visuelle à 0,00 €
        penaltyAmountLabel.setText(String.valueOf(selectedEmprunt.adherent.getPenalite())+ "€");
        // On pourrait aussi désélectionner l'emprunt
        // selectedEmprunt = null;
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
