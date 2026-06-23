package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.Badge;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour les badges attribués aux utilisateurs.
 */
public class UserBadgeRepository {

    /**
     * Récupère la liste des badges d'un utilisateur (avec infos du badge).
     */
    public List<Badge> getBadgesForUser(int userId) {
        List<Badge> list = new ArrayList<>();
        String sql = "SELECT b.* FROM badges b " +
                     "INNER JOIN user_badges ub ON b.id = ub.badge_id " +
                     "WHERE ub.user_id = ? ORDER BY ub.date_earned DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Badge b = new Badge();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setDescription(rs.getString("description"));
                b.setIcon(rs.getString("icon"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur getBadgesForUser: " + e.getMessage());
        }
        return list;
    }

    /**
     * Vérifie si un utilisateur possède déjà un badge spécifique.
     */
    public boolean hasBadge(int userId, int badgeId) {
        String sql = "SELECT COUNT(*) FROM user_badges WHERE user_id = ? AND badge_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, badgeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur hasBadge: " + e.getMessage());
        }
        return false;
    }

    /**
     * Attribue un badge à un utilisateur.
     */
    public boolean awardBadge(int userId, int badgeId) {
        String sql = "INSERT INTO user_badges (user_id, badge_id, date_earned) VALUES (?, ?, CURRENT_TIMESTAMP) " +
                     "ON CONFLICT (user_id, badge_id) DO NOTHING";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, badgeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur awardBadge: " + e.getMessage());
        }
        return false;
    }

    public int countBadgesEarned(int userId) {
        String sql = "SELECT COUNT(*) FROM user_badges WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur countBadgesEarned: " + e.getMessage());
        }
        return 0;
    }

    public int countBadgesEarnedThisMonth(int userId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM user_badges WHERE user_id = ? " +
                     "AND EXTRACT(MONTH FROM date_earned) = ? AND EXTRACT(YEAR FROM date_earned) = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur countBadgesEarnedThisMonth: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retourne la liste des UserBadge (avec infos badge enrichies) pour un utilisateur.
     */
    public List<ma.ac.esi.chessclub.model.UserBadge> getUserBadgesWithDetails(int userId) {
        List<ma.ac.esi.chessclub.model.UserBadge> list = new ArrayList<>();
        String sql = "SELECT ub.id, ub.user_id, ub.badge_id, ub.date_earned, " +
                     "b.name, b.description, b.icon " +
                     "FROM user_badges ub JOIN badges b ON ub.badge_id = b.id " +
                     "WHERE ub.user_id = ? ORDER BY ub.date_earned DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ma.ac.esi.chessclub.model.UserBadge ub = new ma.ac.esi.chessclub.model.UserBadge();
                ub.setId(rs.getInt("id"));
                ub.setUserId(rs.getInt("user_id"));
                ub.setBadgeId(rs.getInt("badge_id"));
                ub.setDateEarned(rs.getTimestamp("date_earned"));
                Badge b = new Badge();
                b.setId(rs.getInt("badge_id"));
                b.setName(rs.getString("name"));
                b.setDescription(rs.getString("description"));
                b.setIcon(rs.getString("icon"));
                ub.setBadge(b);
                list.add(ub);
            }
        } catch (SQLException e) {
            System.err.println("[UserBadgeRepository] Erreur getUserBadgesWithDetails: " + e.getMessage());
        }
        return list;
    }
}
