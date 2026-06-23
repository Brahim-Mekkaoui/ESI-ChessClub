package ma.ac.esi.chessclub.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * Utilitaire de hachage des mots de passe.
 *
 * [INNOVATION] Utilise SHA-256 avec salt aléatoire pour sécuriser les mots de passe.
 * Format stocké : "{salt}:{hash}" pour permettre la vérification.
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_BITS = 128;

    /**
     * Hache un mot de passe avec un salt aléatoire.
     * Retourne une chaîne au format : "salt:hash"
     *
     * @param plainPassword le mot de passe en clair
     * @return le hash formaté salt:hash
     */
    public static String hashPassword(String plainPassword) {
        try {
            // Générer un salt aléatoire
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            String salt = toHex(saltBytes);

            // Hacher le mot de passe avec le salt
            String hash = hash(salt + plainPassword);

            // Retourner salt:hash
            return salt + ":" + hash;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme de hachage indisponible : " + ALGORITHM, e);
        }
    }

    /**
     * Vérifie un mot de passe contre un hash stocké.
     *
     * @param plainPassword le mot de passe en clair à vérifier
     * @param storedHash    le hash stocké au format "salt:hash"
     * @return true si le mot de passe est correct
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        try {
            if (storedHash == null || !storedHash.contains(":")) {
                return false;
            }

            String[] parts = storedHash.split(":", 2);
            String salt = parts[0];
            String expectedHash = parts[1];

            String actualHash = hash(salt + plainPassword);
            return expectedHash.equals(actualHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme de hachage indisponible : " + ALGORITHM, e);
        }
    }

    // ============================================================
    // Méthodes privées
    // ============================================================

    private static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hashBytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return toHex(hashBytes);
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        String hex = bi.toString(16);
        // Padding pour garder la longueur constante
        int paddingLength = (bytes.length * 2) - hex.length();
        if (paddingLength > 0) {
            return "0".repeat(paddingLength) + hex;
        }
        return hex;
    }

    /**
     * Validation de la complexité d'un mot de passe.
     * Règles : min 8 caractères, au moins 1 lettre et 1 chiffre.
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit  = password.chars().anyMatch(Character::isDigit);
        return hasLetter && hasDigit;
    }
}
