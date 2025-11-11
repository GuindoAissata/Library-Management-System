package sgeb.dao;

import java.sql.Connection;
import java.sql.Statement;

public class InitDatabase {
    public static void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS documents (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titre TEXT NOT NULL,
                contenu TEXT,
                auteur TEXT,
                date_creation TEXT
            );
            """;
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table documents prête !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

