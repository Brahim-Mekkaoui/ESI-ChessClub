package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.UserPuzzleStats;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;

/**
 * Repository pour les statistiques de puzzles par utilisateur.
 */
public class UserPuzzleStatsRepository {

    public UserPuzzleStats getStats(int userId, int puzzleId) {
        String sql = "SELECT * FROM user_puzzle_stats WHERE user_id = ? AND puzzle_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, puzzleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[UserPuzzleStatsRepository] Erreur getStats: " + e.getMessage());
        }
        return null;
    }

    /**
     * Enregistre ou met à jour la tentative d'un utilisateur sur un puzzle.
     */
    public boolean upsertStats(int userId, int puzzleId, boolean solved, int timeTaken) {
        String sql = "INSERT INTO user_puzzle_stats (user_id, puzzle_id, solved, time_taken_seconds, date_solved, attempts) " +
                     "VALUES (?, ?, ?, ?, CASE WHEN ? THEN CURRENT_TIMESTAMP ELSE NULL END, 1) " +
                     "ON CONFLICT (user_id, puzzle_id) DO UPDATE SET " +
                     "solved = GREATEST(user_puzzle_stats.solved, EXCLUDED.solved), " +
                     "attempts = user_puzzle_stats.attempts + 1, " +
                     "date_solved = CASE WHEN EXCLUDED.solved AND NOT user_puzzle_stats.solved THEN CURRENT_TIMESTAMP " +
                     "              ELSE user_puzzle_stats.date_solved END, " +
                     "time_taken_seconds = CASE WHEN EXCLUDED.solved THEN EXCLUDED.time_taken_seconds " +
                     "                     ELSE user_puzzle_stats.time_taken_seconds END";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, puzzleId);
            stmt.setBoolean(3, solved);
            stmt.setInt(4, timeTaken);
            stmt.setBoolean(5, solved);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserPuzzleStatsRepository] Erreur upsertStats: " + e.getMessage());
        }
        return false;
    }

    public boolean hasSolvedToday(int userId, int puzzleId) {
        String sql = "SELECT solved FROM user_puzzle_stats WHERE user_id = ? AND puzzle_id = ? AND solved = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, puzzleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[UserPuzzleStatsRepository] Erreur hasSolvedToday: " + e.getMessage());
        }
        return false;
    }

    private UserPuzzleStats mapRow(ResultSet rs) throws SQLException {
        UserPuzzleStats s = new UserPuzzleStats();
        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        s.setPuzzleId(rs.getInt("puzzle_id"));
        s.setSolved(rs.getBoolean("solved"));
        s.setTimeTakenSeconds(rs.getInt("time_taken_seconds"));
        s.setDateSolved(rs.getTimestamp("date_solved"));
        s.setAttempts(rs.getInt("attempts"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
