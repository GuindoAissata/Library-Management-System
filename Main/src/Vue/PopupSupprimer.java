package Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Supplier;

public class PopupSupprimer {

    /**
     * @param element   l’élément qu’on supprime (pour info, si tu veux afficher des détails plus tard)
     * @param onConfirm fonction appelée quand l’utilisateur clique sur "Supprimer".
     *                  Elle doit retourner true si la suppression a bien réussi, false sinon.
     */
    public static <T> void afficherPopupSuppression(T element,
                                                    Supplier<Boolean> onConfirm) {

        Stage popupStage = new Stage();
        popupStage.setTitle("Confirmation de suppression");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");

        Label messageLabel = new Label("Voulez-vous vraiment supprimer cet élément ?");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label warningLabel = new Label("Cette action est irréversible !");
        warningLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        warningLabel.setTextFill(Color.RED);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);

        Button annulerBtn = new Button("Annuler");
        annulerBtn.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-padding: 10 30; -fx-font-size: 14;");
        annulerBtn.setOnAction(e -> popupStage.close());

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-font-weight: bold;");
        supprimerBtn.setOnAction(e -> {
            boolean ok = onConfirm.get();   //la logique métier décide
            if (ok) {
                afficherMessageSucces("Suppression effectuée avec succès !");
                popupStage.close();
            }
            // si ok == false : la méthode appelante affiche déjà le message d'erreur
        });

        buttonsBox.getChildren().addAll(annulerBtn, supprimerBtn);
        layout.getChildren().addAll(messageLabel, warningLabel, buttonsBox);

        Scene scene = new Scene(layout, 400, 300);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    private static void afficherMessageSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
