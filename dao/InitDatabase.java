package sgeb.dao;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class InitDatabase {

    public static void init() {
        try {
            // Lire le fichier SQL
            String sql = Files.readString(Paths.get("data/Bibliotheque.sql"));

            // Ouvrir la connexion
            try (Connection conn = SQLiteConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                // Découper le fichier en commandes SQL séparées par ;
                for (String command : sql.split(";")) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }

                System.out.println("Tables Adhérents, Emprunts et Catalogue prêtes !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
