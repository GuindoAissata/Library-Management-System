package Vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Authentification - SGEB");

        // --- Layout principal ---
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("Connexion au système");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        TextField userField = new TextField();
        userField.setPromptText("Identifiant");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        form.add(new Label("Identifiant :"), 0, 0);
        form.add(userField,               1, 0);
        form.add(new Label("Mot de passe :"), 0, 1);
        form.add(passwordField,              1, 1);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button loginBtn = new Button("Se connecter");
        loginBtn.setStyle("-fx-background-color: MIDNIGHTBLUE; -fx-text-fill: white;");
        buttons.getChildren().add(loginBtn);

        root.getChildren().addAll(title, form, buttons);

        Scene scene = new Scene(root, 400, 220);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // ======= Action du bouton =======
        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passwordField.getText().trim();

            if (LoginService.authenticate(username, password)) {
                // Connexion OK on ouvre l’interface principale
                Interface mainApp = new Interface();
                try {
                    mainApp.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Impossible de lancer l'application principale.");
                    return;
                }
                // on ferme la fenêtre de login
                primaryStage.close();
            } else {
                showError("Identifiant ou mot de passe incorrect.");
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
