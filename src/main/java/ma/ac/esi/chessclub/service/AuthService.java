package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.repository.UserRepository;
import ma.ac.esi.chessclub.util.PasswordUtil;
import ma.ac.esi.chessclub.util.ValidationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Service d'authentification et d'inscription.
 * Contient toute la logique de sécurité : vérification, validation, hachage.
 */
public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    // ============================================================
    // Authentification
    // ============================================================

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
     *
     * @param username le nom d'utilisateur
     * @param password le mot de passe en clair
     * @return l'objet User si authentifié, null sinon
     */
    public User authenticate(String username, String password) {
        if (username == null || password == null) return null;

        // Récupérer l'utilisateur depuis la DB
        User user = userRepository.getUserByUsername(username.trim());
        if (user == null) return null;

        // Vérifier le mot de passe haché
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return null;
        }

        // Ne pas exposer le hash dans la session
        user.setPasswordHash(null);
        return user;
    }

    // ============================================================
    // Inscription
    // ============================================================

    /**
     * Inscrit un nouvel utilisateur après validation complète.
     *
     * @param username    nom d'utilisateur
     * @param email       adresse email
     * @param password    mot de passe en clair
     * @param firstName   prénom
     * @param lastName    nom de famille
     * @param chessLevel  niveau d'échecs
     * @return une Map contenant "success" (boolean) et éventuellement "error" (String)
     */
    public Map<String, Object> register(String username, String email, String password,
                                         String firstName, String lastName, String chessLevel) {
        Map<String, Object> result = new HashMap<>();

        // --- Validation des champs ---
        if (!ValidationUtil.isValidUsername(username)) {
            result.put("success", false);
            result.put("error", "Nom d'utilisateur invalide (3-50 caractères alphanumériques ou _)");
            return result;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            result.put("success", false);
            result.put("error", "Adresse email invalide");
            return result;
        }

        if (!PasswordUtil.isValidPassword(password)) {
            result.put("success", false);
            result.put("error", "Le mot de passe doit contenir au moins 8 caractères avec une lettre et un chiffre");
            return result;
        }

        if (!ValidationUtil.isNotEmpty(firstName)) {
            result.put("success", false);
            result.put("error", "Le prénom est obligatoire");
            return result;
        }

        if (!ValidationUtil.isValidChessLevel(chessLevel)) {
            result.put("success", false);
            result.put("error", "Niveau d'échecs invalide");
            return result;
        }

        // --- Vérification unicité ---
        if (userRepository.usernameExists(username)) {
            result.put("success", false);
            result.put("error", "Ce nom d'utilisateur est déjà pris");
            return result;
        }

        if (userRepository.emailExists(email)) {
            result.put("success", false);
            result.put("error", "Cette adresse email est déjà utilisée");
            return result;
        }

        // --- Création de l'utilisateur ---
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setFirstName(firstName.trim());
        user.setLastName(lastName != null ? lastName.trim() : "");
        user.setChessLevel(chessLevel);
        user.setRole("MEMBER");

        int newId = userRepository.insertUser(user);
        if (newId > 0) {
            result.put("success", true);
            result.put("userId", newId);
        } else {
            result.put("success", false);
            result.put("error", "Erreur lors de la création du compte. Réessayez.");
        }

        return result;
    }
}
