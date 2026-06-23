package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.Badge;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour les badges.
 */
public class BadgeRepository {

    public List<Badge> getAllBadges() {
        List<Badge> list = new ArrayList<>();
        String sql = "SELECT * FROM badges ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BadgeRepository] Erreur getAllBadges: " + e.getMessage());
        }
        return list;
    }

    public Badge getBadgeByName(String name) {
        String sql = "SELECT * FROM badges WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[BadgeRepository] Erreur getBadgeByName: " + e.getMessage());
        }
        return null;
    }

    public Badge getBadgeById(int id) {
        String sql = "SELECT * FROM badges WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[BadgeRepository] Erreur getBadgeById: " + e.getMessage());
        }
        return null;
    }

    private Badge mapRow(ResultSet rs) throws SQLException {
        Badge b = new Badge();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setDescription(rs.getString("description"));
        b.setIcon(rs.getString("icon"));
        b.setPuzzleCountThreshold((Integer) rs.getObject("puzzle_count_threshold"));
        b.setTournamentWinsThreshold((Integer) rs.getObject("tournament_wins_threshold"));
        b.setConsecutiveDaysThreshold((Integer) rs.getObject("consecutive_days_threshold"));
        b.setMatchCountThreshold((Integer) rs.getObject("match_count_threshold"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
