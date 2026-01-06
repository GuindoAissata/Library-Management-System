package Controler;

import Model.BibliothequeManager;
import Model.Document;
import Vue.PopupModifierDocument;
import javafx.scene.control.TableView;

public class ModifierDocumentControler {

    private final BibliothequeManager manager;
    private final TableView<Document> table;

    public ModifierDocumentControler(BibliothequeManager manager,
                                    TableView<Document> table) {
        this.manager = manager;
        this.table = table;
    }

    public void ouvrirPopupModification(Document doc) {
        PopupModifierDocument.afficher(doc, dModifie -> {
            // 1) Maj en base
            manager.updateDocument(dModifie); // à créer si pas encore fait

            // 2) La liste Observable contient déjà la même instance,
            // donc un simple refresh suffit pour mettre à jour la TableView
            table.refresh();
        });
    }
}
