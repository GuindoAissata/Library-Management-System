package Controler;

import Model.Adherent;
import Model.BibliothequeManager;
import Vue.PopupModifierAdherent;
import javafx.scene.control.TableView;

public class ModifierAdherentControler {

    private final BibliothequeManager manager;
    private final TableView<Adherent> table;

    public ModifierAdherentControler(BibliothequeManager manager,
                                    Adherent adherent,
                                    TableView<Adherent> table) {
        this.manager = manager;
        this.table = table;

        PopupModifierAdherent.afficher(manager,adherent, this::onSave);
    }

    private void onSave(Adherent adherentModifie) {
        // Méthode côté manager pour persister dans la BD
        manager.updateAdherent(adherentModifie);

        // Rafraîchir la table
        if (table != null) {
            table.refresh();
        }
    }
}
