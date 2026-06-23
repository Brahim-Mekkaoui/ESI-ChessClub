package ma.ac.esi.chessclub.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entité représentant un match entre deux joueurs dans un tournoi.
 */
public class Match {

    private int id;
    private int tournamentId;
    private String tournamentName; // enrichi
    private int whitePlayerId;
    private String whitePlayerName; // enrichi
    private int blackPlayerId;
    private String blackPlayerName; // enrichi
    private String result;          // WHITE_WIN, BLACK_WIN, DRAW, NOT_PLAYED
    private Date matchDate;
    private int roundNumber;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ============================================================
    // Constructeurs
    // ============================================================

    public Match() {
        this.result = "NOT_PLAYED";
    }

    // ============================================================
    // Méthodes utilitaires
    // ============================================================

    public boolean isPlayed() {
        return !"NOT_PLAYED".equals(result);
    }

    public String getResultLabel() {
        switch (result != null ? result : "") {
            case "WHITE_WIN":  return "Victoire Blancs";
            case "BLACK_WIN":  return "Victoire Noirs";
            case "DRAW":       return "Nulle";
            case "NOT_PLAYED": return "Non joué";
            default:           return result;
        }
    }

    /**
     * Vérifie si l'utilisateur donné a gagné ce match.
     */
    public boolean isWonBy(int userId) {
        return (whitePlayerId == userId && "WHITE_WIN".equals(result))
            || (blackPlayerId == userId && "BLACK_WIN".equals(result));
    }

    // ============================================================
    // Getters et Setters
    // ============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTournamentId() { return tournamentId; }
    public void setTournamentId(int tournamentId) { this.tournamentId = tournamentId; }

    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }

    public int getWhitePlayerId() { return whitePlayerId; }
    public void setWhitePlayerId(int whitePlayerId) { this.whitePlayerId = whitePlayerId; }

    public String getWhitePlayerName() { return whitePlayerName; }
    public void setWhitePlayerName(String whitePlayerName) { this.whitePlayerName = whitePlayerName; }

    public int getBlackPlayerId() { return blackPlayerId; }
    public void setBlackPlayerId(int blackPlayerId) { this.blackPlayerId = blackPlayerId; }

    public String getBlackPlayerName() { return blackPlayerName; }
    public void setBlackPlayerName(String blackPlayerName) { this.blackPlayerName = blackPlayerName; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public Date getMatchDate() { return matchDate; }
    public void setMatchDate(Date matchDate) { this.matchDate = matchDate; }

    public int getRoundNumber() { return roundNumber; }
    public void setRoundNumber(int roundNumber) { this.roundNumber = roundNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    /** Alias pour les JSP */
    public String getWhiteName() { return whitePlayerName != null ? whitePlayerName : "Joueur " + whitePlayerId; }
    public String getBlackName() { return blackPlayerName != null ? blackPlayerName : "Joueur " + blackPlayerId; }
}
