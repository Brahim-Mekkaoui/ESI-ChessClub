package ma.ac.esi.chessclub.util;

import java.util.regex.Pattern;

/**
 * Utilitaire de validation des données saisies par l'utilisateur.
 * Centralise toutes les règles de validation de l'application.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern USERNAME_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_]{3,50}$");

    /**
     * Vérifie si une chaîne est non-nulle et non-vide.
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Vérifie si un email est valide.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Vérifie si un username est valide.
     * Règles : 3-50 caractères alphanumériques ou underscore.
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Vérifie si un niveau d'échecs est valide.
     */
    public static boolean isValidChessLevel(String level) {
        return level != null && (
            level.equals("BEGINNER") ||
            level.equals("INTERMEDIATE") ||
            level.equals("ADVANCED") ||
            level.equals("EXPERT")
        );
    }

    /**
     * Vérifie si un format de tournoi est valide.
     */
    public static boolean isValidTournamentFormat(String format) {
        return format != null && (
            format.equals("ROUND_ROBIN") ||
            format.equals("SWISS") ||
            format.equals("SINGLE_ELIMINATION")
        );
    }

    /**
     * Échapper les caractères HTML pour prévenir les injections XSS.
     * [INNOVATION] Protection XSS intégrée.
     */
    public static String escapeHtml(String input) {
        if (input == null) return "";
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }

    /**
     * Tronque une chaîne à une longueur maximale.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    /**
     * Retourne une valeur par défaut si la chaîne est nulle ou vide.
     */
    public static String defaultIfEmpty(String value, String defaultValue) {
        return isNotEmpty(value) ? value : defaultValue;
    }
}
