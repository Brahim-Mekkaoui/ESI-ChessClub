package ma.ac.esi.chessclub.model;

import java.sql.Timestamp;

/**
 * Entité représentant les statistiques d'un utilisateur sur un puzzle.
 */
public class UserPuzzleStats {
    private int id;
    private int userId;
    private int puzzleId;
    private boolean solved;
    private int timeTakenSeconds;
    private Timestamp dateSolved;
    private int attempts;
    private Timestamp createdAt;

    public UserPuzzleStats() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPuzzleId() { return puzzleId; }
    public void setPuzzleId(int puzzleId) { this.puzzleId = puzzleId; }

    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }

    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    public Timestamp getDateSolved() { return dateSolved; }
    public void setDateSolved(Timestamp dateSolved) { this.dateSolved = dateSolved; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
