package Controler;

import Model.BibliothequeManager;
import Model.Document;
import Vue.PopupSupprimer;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;


public class SupprimerControler {


    private final BibliothequeManager manager;
    private final TableView<Document> table;
    private final TableColumn<Document, String> statusCol;

    public SupprimerControler(BibliothequeManager manager,
                                    TableView<Document> table,
                                TableColumn<Document, String> statusCol) {
        this.manager = manager;
        this.table = table;
        this.statusCol = statusCol;
    }

    /**
     * Lance le popup de confirmation et supprime le document si confirmé.
     */
    public void confirmerEtSupprimer(Document doc) {

        PopupSupprimer.afficherPopupSuppression(doc, () -> {
            // 1) Tentative de suppression côté métier
            boolean ok = manager.DeleteDocument(doc);

            if (!ok) {
                // Doc non supprimé (sans doute encore emprunté)
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Suppression impossible");
                alert.setHeaderText(null);
                alert.setContentText(
                    "Impossible de supprimer ce document.\n" +
                    "Vérifiez qu'il n'est pas emprunté."
                );
                alert.showAndWait();
                return false;
            }

            // 2) La liste Observable a été mise à jour par DeleteDocument
            // On force juste le rafraîchissement de la TableView
        statusCol.setCellValueFactory(cellData ->
    new SimpleStringProperty(cellData.getValue().getStatutAffichage())
);

        statusCol.setPrefWidth(120); 
            table.refresh();
            return true;
        });
    }
}
