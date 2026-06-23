package ma.ac.esi.chessclub.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * Entité représentant un utilisateur du club d'échecs.
 * Contient les données de la table "users" plus les statistiques enrichies.
 */
public class User {

    // --- Champs persistés en base de données ---
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String chessLevel;   // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    private String bio;
    private String profilePictureUrl;
    private String role;         // ADMIN, MEMBER
    private int eloRating;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // --- Statistiques enrichies (non persistées directement) ---
    private int puzzlesSolved;
    private int matchesPlayed;
    private int matchesWon;
    private int tournamentsWon;
    private List<Badge> badges = new ArrayList<>();

    // ============================================================
    // Constructeurs
    // ============================================================

    public User() {
        this.eloRating = 1200;
        this.role = "MEMBER";
        this.isActive = true;
    }

    public User(String username, String email, String firstName, String lastName, String chessLevel) {
        this();
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.chessLevel = chessLevel;
    }

    // ============================================================
    // Méthodes utilitaires
    // ============================================================

    /**
     * Retourne le nom complet de l'utilisateur.
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        }
        return username;
    }

    /**
     * Vérifie si l'utilisateur est un administrateur.
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    /**
     * Calcule le score mensuel pour le classement "Joueur du mois".
     * Formule : (tournois*10) + (matchs gagnés*2) + (puzzles*0.5) + (badges*5)
     */
    public int calculateMonthlyScore() {
        return (tournamentsWon * 10)
             + (matchesWon * 2)
             + (puzzlesSolved / 2)
             + (badges.size() * 5);
    }

    // ============================================================
    // Getters et Setters
    // ============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getChessLevel() { return chessLevel; }
    public void setChessLevel(String chessLevel) { this.chessLevel = chessLevel; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getEloRating() { return eloRating; }
    public void setEloRating(int eloRating) { this.eloRating = eloRating; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getPuzzlesSolved() { return puzzlesSolved; }
    public void setPuzzlesSolved(int puzzlesSolved) { this.puzzlesSolved = puzzlesSolved; }

    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public int getMatchesWon() { return matchesWon; }
    public void setMatchesWon(int matchesWon) { this.matchesWon = matchesWon; }

    public int getTournamentsWon() { return tournamentsWon; }
    public void setTournamentsWon(int tournamentsWon) { this.tournamentsWon = tournamentsWon; }

    public List<Badge> getBadges() { return badges; }
    public void setBadges(List<Badge> badges) { this.badges = badges; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
