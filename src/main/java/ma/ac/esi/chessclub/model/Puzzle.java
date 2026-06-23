package ma.ac.esi.chessclub.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entité représentant un puzzle d'échecs (récupéré depuis l'API Lichess).
 */
public class Puzzle {

    private int id;
    private String fenPosition;     // Position en notation FEN
    private String bestMove;        // Premier coup de la solution (ex: e2e4)
    private String allMoves;        // Toute la ligne de solution (séparée par des espaces)
    private int difficulty;         // 1-9 (échelle Lichess)
    private String puzzleIdLichess; // ID du puzzle sur Lichess
    private String theme;           // OPENING, TACTIC, ENDGAME, etc.
    private boolean fetchedFromApi;
    private Date puzzleDate;        // Date du puzzle (1 puzzle par jour)
    private Timestamp createdAt;

    // ============================================================
    // Constructeurs
    // ============================================================

    public Puzzle() {
        this.fetchedFromApi = false;
        this.difficulty = 5;
    }

    public Puzzle(String fenPosition, String bestMove, int difficulty) {
        this();
        this.fenPosition = fenPosition;
        this.bestMove = bestMove;
        this.difficulty = difficulty;
    }

    // ============================================================
    // Méthodes utilitaires
    // ============================================================

    /**
     * Retourne l'URL du puzzle sur Lichess pour référence.
     */
    public String getLichessUrl() {
        if (puzzleIdLichess != null && !puzzleIdLichess.isEmpty()) {
            return "https://lichess.org/training/" + puzzleIdLichess;
        }
        return null;
    }

    /**
     * Retourne une description de la difficulté.
     */
    public String getDifficultyLabel() {
        if (difficulty <= 2) return "Débutant";
        if (difficulty <= 4) return "Intermédiaire";
        if (difficulty <= 6) return "Avancé";
        if (difficulty <= 8) return "Expert";
        return "Grand Maître";
    }

    /**
     * Retourne la couleur CSS de la difficulté.
     */
    public String getDifficultyColor() {
        if (difficulty <= 2) return "success";
        if (difficulty <= 4) return "info";
        if (difficulty <= 6) return "warning";
        return "danger";
    }

    /**
     * Vérifie si un coup soumis est correct.
     * Compare en ignorant la casse.
     */
    public boolean isCorrectMove(String move) {
        if (move == null || bestMove == null) return false;
        return bestMove.toLowerCase().trim().equals(move.toLowerCase().trim());
    }

    // ============================================================
    // Getters et Setters
    // ============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFenPosition() { return fenPosition; }
    public void setFenPosition(String fenPosition) { this.fenPosition = fenPosition; }

    public String getBestMove() { return bestMove; }
    public void setBestMove(String bestMove) { this.bestMove = bestMove; }

    public String getAllMoves() { return allMoves; }
    public void setAllMoves(String allMoves) { this.allMoves = allMoves; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public String getPuzzleIdLichess() { return puzzleIdLichess; }
    public void setPuzzleIdLichess(String puzzleIdLichess) { this.puzzleIdLichess = puzzleIdLichess; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public boolean isFetchedFromApi() { return fetchedFromApi; }
    public void setFetchedFromApi(boolean fetchedFromApi) { this.fetchedFromApi = fetchedFromApi; }

    public Date getPuzzleDate() { return puzzleDate; }
    public void setPuzzleDate(Date puzzleDate) { this.puzzleDate = puzzleDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
