package Controler;

import Model.Adherent;
import Model.ArgumentException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PayerPenaliteControler {

    private final Adherent adherent;
    private final TextField penaliteField;  // champ affichant la pénalité actuelle (readonly)
    private final TextField sommeField;     // champ où on saisit la somme à payer
    private final Button payerButton;

    public PayerPenaliteControler(Adherent adherent,
                                TextField penaliteField,
                                TextField sommeField,
                                Button payerButton) {
        this.adherent = adherent;
        this.penaliteField = penaliteField;
        this.sommeField = sommeField;
        this.payerButton = payerButton;

        // Afficher la pénalité actuelle au lancement
        majAffichagePenalite();

        initHandlers();
    }

    private void initHandlers() {
        payerButton.setOnAction(e -> handlePayer());
    }

    private void handlePayer() {
        String texteSomme = sommeField.getText().trim().replace(',', '.');

        if (texteSomme.isEmpty()) {
            showError("Veuillez saisir un montant à payer.");
            return;
        }

        double somme;
        try {
            somme = Double.parseDouble(texteSomme);
        } catch (NumberFormatException ex) {
            showError("Montant invalide. Saisissez un nombre (ex : 3.50).");
            return;
        }

        if (somme <= 0) {
            showError("Le montant à payer doit être strictement positif.");
            return;
        }

        double penaliteActuelle = adherent.getPenalite();
        if (penaliteActuelle <= 0) {
            showError("Cet adhérent n'a aucune pénalité à payer.");
            return;
        }

        if (somme > penaliteActuelle) {
            showError("Le montant ne peut pas dépasser la pénalité actuelle (" 
                    + String.format("%.2f", penaliteActuelle) + ").");
            return;
        }
        try {//  Appel de ta méthode métier
        // (le nom est celui que tu m'as donné : PayerPenalite)
        adherent.payerPenalite(somme);
            
        } catch (ArgumentException e) {
            showError(e.getMessage());
        }
         // Mise à jour du champ de pénalité
        majAffichagePenalite();
        sommeField.clear();

        Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("Paiement effectué");
        ok.setHeaderText(null);
        ok.setContentText("Le paiement de " + String.format("%.2f", somme) 
                + " a bien été enregistré.\nNouvelle pénalité : "
                + String.format("%.2f", adherent.getPenalite()));
        ok.showAndWait();
    }

    private void majAffichagePenalite() {
        penaliteField.setText(String.format("%.2f", adherent.getPenalite()));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
