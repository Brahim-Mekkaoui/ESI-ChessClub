package ma.ac.esi.chessclub.model;

import java.sql.Timestamp;

/**
 * Entité représentant l'attribution d'un badge à un utilisateur.
 */
public class UserBadge {
    private int id;
    private int userId;
    private int badgeId;
    private Timestamp dateEarned;

    // Badge enrichi (join)
    private Badge badge;

    public UserBadge() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBadgeId() { return badgeId; }
    public void setBadgeId(int badgeId) { this.badgeId = badgeId; }

    public Timestamp getDateEarned() { return dateEarned; }
    public void setDateEarned(Timestamp dateEarned) { this.dateEarned = dateEarned; }

    public Badge getBadge() { return badge; }
    public void setBadge(Badge badge) { this.badge = badge; }

    public String getFormattedDate() {
        if (dateEarned == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(dateEarned);
    }
}
