package ma.ac.esi.chessclub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitaire de connexion à la base de données PostgreSQL.
 *
 * Configuration : Modifiez URL, USER et PASSWORD selon votre environnement.
 * Ces valeurs peuvent aussi être lues depuis web.xml (context-param).
 */
public class DBUtil {

    // ============================================================
    // Configuration de la connexion
    // Modifiez ces constantes selon votre environnement PostgreSQL
    // ============================================================
    private static final String URL      = "jdbc:postgresql://localhost:5432/chessclub_db";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "postgres";

    // Chargement du driver au démarrage
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL introuvable. Ajoutez le JAR PostgreSQL au classpath.", e);
        }
    }

    /**
     * Retourne une connexion JDBC à la base de données.
     * Utilise try-with-resources pour fermer automatiquement.
     *
     * @return une connexion active
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Ferme une connexion sans lever d'exception.
     * Utile dans les blocs finally.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[DBUtil] Erreur fermeture connexion : " + e.getMessage());
            }
        }
    }
}
