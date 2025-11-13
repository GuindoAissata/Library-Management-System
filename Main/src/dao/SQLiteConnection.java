package sgeb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String URL = "jdbc:sqlite:./data/Bibliotheque.db";

    public static Connection getConnection() throws SQLException {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Connexion SQLite établie avec succès !");
            return conn;
        }
        catch(ClassNotFoundException e){
            System.err.println("Erreur de chargement du driver : " + e.getMessage());
            return null;
        }
        catch(SQLException e){
            System.err.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}

