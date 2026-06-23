package ma.ac.esi.chessclub.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * Entité représentant un tournoi d'échecs.
 */
public class Tournament {

    private int id;
    private String name;
    private String description;
    private String format;        // ROUND_ROBIN, SWISS, SINGLE_ELIMINATION
    private Date startDate;
    private Date endDate;
    private int organizerId;
    private String organizerName; // enrichi
    private String status;        // PLANNED, ONGOING, FINISHED
    private String location;
    private int maxPlayers;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Données enrichies
    private List<Match> matches = new ArrayList<>();
    private int participantCount;

    // ============================================================
    // Constructeurs
    // ============================================================

    public Tournament() {
        this.status = "PLANNED";
        this.format = "ROUND_ROBIN";
        this.maxPlayers = 16;
    }

    // ============================================================
    // Méthodes utilitaires
    // ============================================================

    public boolean isPlanned()  { return "PLANNED".equals(status); }
    public boolean isOngoing()  { return "ONGOING".equals(status); }
    public boolean isFinished() { return "FINISHED".equals(status); }

    public String getStatusLabel() {
        switch (status != null ? status : "") {
            case "PLANNED":  return "Planifié";
            case "ONGOING":  return "En cours";
            case "FINISHED": return "Terminé";
            default:         return status;
        }
    }

    public String getFormatLabel() {
        switch (format != null ? format : "") {
            case "ROUND_ROBIN":        return "Round Robin";
            case "SWISS":              return "Système Suisse";
            case "SINGLE_ELIMINATION": return "Élimination Directe";
            default:                   return format;
        }
    }

    // ============================================================
    // Getters et Setters
    // ============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getOrganizerId() { return organizerId; }
    public void setOrganizerId(int organizerId) { this.organizerId = organizerId; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }

    public int getParticipantCount() { return participantCount; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }

    /** Retourne la date de début formatée (dd/MM/yyyy) pour les JSP */
    public String getFormattedStartDate() {
        if (startDate == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(startDate);
    }

    /** Retourne la date de fin formatée (dd/MM/yyyy) pour les JSP */
    public String getFormattedEndDate() {
        if (endDate == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(endDate);
    }
}
