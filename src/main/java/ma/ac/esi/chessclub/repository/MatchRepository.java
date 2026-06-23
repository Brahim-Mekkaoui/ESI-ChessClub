package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.Match;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour l'accès aux données des matchs.
 */
public class MatchRepository {

    public List<Match> getMatchesByTournament(int tournamentId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT m.*, " +
                     "wp.username as white_player_name, bp.username as black_player_name " +
                     "FROM matches m " +
                     "LEFT JOIN users wp ON m.white_player_id = wp.id " +
                     "LEFT JOIN users bp ON m.black_player_id = bp.id " +
                     "WHERE m.tournament_id = ? ORDER BY m.round_number, m.id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tournamentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur getMatchesByTournament: " + e.getMessage());
        }
        return list;
    }

    public List<Match> getMatchesByUser(int userId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT m.*, t.name as tournament_name, " +
                     "wp.username as white_player_name, bp.username as black_player_name " +
                     "FROM matches m " +
                     "LEFT JOIN tournaments t ON m.tournament_id = t.id " +
                     "LEFT JOIN users wp ON m.white_player_id = wp.id " +
                     "LEFT JOIN users bp ON m.black_player_id = bp.id " +
                     "WHERE m.white_player_id = ? OR m.black_player_id = ? " +
                     "ORDER BY m.match_date DESC NULLS LAST";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur getMatchesByUser: " + e.getMessage());
        }
        return list;
    }

    public Match getMatchById(int id) {
        String sql = "SELECT m.*, " +
                     "wp.username as white_player_name, bp.username as black_player_name " +
                     "FROM matches m " +
                     "LEFT JOIN users wp ON m.white_player_id = wp.id " +
                     "LEFT JOIN users bp ON m.black_player_id = bp.id " +
                     "WHERE m.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur getMatchById: " + e.getMessage());
        }
        return null;
    }

    public int insertMatch(Match match) {
        String sql = "INSERT INTO matches (tournament_id, white_player_id, black_player_id, " +
                     "result, match_date, round_number, notes, created_at) " +
                     "VALUES (?, ?, ?, 'NOT_PLAYED', ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, match.getTournamentId());
            stmt.setInt(2, match.getWhitePlayerId());
            stmt.setInt(3, match.getBlackPlayerId());
            stmt.setDate(4, match.getMatchDate());
            stmt.setInt(5, match.getRoundNumber());
            stmt.setString(6, match.getNotes());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur insertMatch: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateMatchResult(int matchId, String result) {
        String sql = "UPDATE matches SET result=?, match_date=CURRENT_DATE, " +
                     "updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, result);
            stmt.setInt(2, matchId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur updateMatchResult: " + e.getMessage());
        }
        return false;
    }

    public int countMatchesPlayed(int userId) {
        String sql = "SELECT COUNT(*) FROM matches WHERE " +
                     "(white_player_id = ? OR black_player_id = ?) AND result != 'NOT_PLAYED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur countMatchesPlayed: " + e.getMessage());
        }
        return 0;
    }

    public int countMatchesWon(int userId) {
        String sql = "SELECT COUNT(*) FROM matches WHERE " +
                     "(white_player_id = ? AND result = 'WHITE_WIN') OR " +
                     "(black_player_id = ? AND result = 'BLACK_WIN')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur countMatchesWon: " + e.getMessage());
        }
        return 0;
    }

    public int countMatchesWonThisMonth(int userId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM matches WHERE " +
                     "((white_player_id = ? AND result = 'WHITE_WIN') OR " +
                     " (black_player_id = ? AND result = 'BLACK_WIN')) " +
                     "AND EXTRACT(MONTH FROM updated_at) = ? AND EXTRACT(YEAR FROM updated_at) = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, month);
            stmt.setInt(4, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[MatchRepository] Erreur countMatchesWonThisMonth: " + e.getMessage());
        }
        return 0;
    }

    private Match mapRow(ResultSet rs) throws SQLException {
        Match m = new Match();
        m.setId(rs.getInt("id"));
        m.setTournamentId(rs.getInt("tournament_id"));
        m.setWhitePlayerId(rs.getInt("white_player_id"));
        m.setWhitePlayerName(rs.getString("white_player_name"));
        m.setBlackPlayerId(rs.getInt("black_player_id"));
        m.setBlackPlayerName(rs.getString("black_player_name"));
        m.setResult(rs.getString("result"));
        m.setMatchDate(rs.getDate("match_date"));
        m.setRoundNumber(rs.getInt("round_number"));
        m.setNotes(rs.getString("notes"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        // tournament_name peut ne pas être présent dans tous les contextes
        try { m.setTournamentName(rs.getString("tournament_name")); } catch (SQLException ignored) {}
        return m;
    }
}
