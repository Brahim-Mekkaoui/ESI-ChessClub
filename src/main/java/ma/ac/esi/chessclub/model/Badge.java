package ma.ac.esi.chessclub.model;

import java.sql.Timestamp;

/**
 * Entité représentant un badge (récompense).
 */
public class Badge {
    private int id;
    private String name;
    private String description;
    private String icon;  // Emoji ou URL icône
    private Integer puzzleCountThreshold;
    private Integer tournamentWinsThreshold;
    private Integer consecutiveDaysThreshold;
    private Integer matchCountThreshold;
    private Timestamp createdAt;

    public Badge() {}

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getPuzzleCountThreshold() { return puzzleCountThreshold; }
    public void setPuzzleCountThreshold(Integer puzzleCountThreshold) { this.puzzleCountThreshold = puzzleCountThreshold; }

    public Integer getTournamentWinsThreshold() { return tournamentWinsThreshold; }
    public void setTournamentWinsThreshold(Integer tournamentWinsThreshold) { this.tournamentWinsThreshold = tournamentWinsThreshold; }

    public Integer getConsecutiveDaysThreshold() { return consecutiveDaysThreshold; }
    public void setConsecutiveDaysThreshold(Integer consecutiveDaysThreshold) { this.consecutiveDaysThreshold = consecutiveDaysThreshold; }

    public Integer getMatchCountThreshold() { return matchCountThreshold; }
    public void setMatchCountThreshold(Integer matchCountThreshold) { this.matchCountThreshold = matchCountThreshold; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
