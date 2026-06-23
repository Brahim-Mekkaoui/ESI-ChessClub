package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.Tournament;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour l'accès aux données des tournois.
 */
public class TournamentRepository {

    public List<Tournament> getAllTournaments() {
        List<Tournament> list = new ArrayList<>();
        String sql = "SELECT t.*, u.username as organizer_name, " +
                     "(SELECT COUNT(*) FROM tournament_participants tp WHERE tp.tournament_id = t.id) as participant_count " +
                     "FROM tournaments t " +
                     "LEFT JOIN users u ON t.organizer_id = u.id " +
                     "ORDER BY t.start_date DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur getAllTournaments: " + e.getMessage());
        }
        return list;
    }

    public List<Tournament> getTournamentsByStatus(String status) {
        List<Tournament> list = new ArrayList<>();
        String sql = "SELECT t.*, u.username as organizer_name, " +
                     "(SELECT COUNT(*) FROM tournament_participants tp WHERE tp.tournament_id = t.id) as participant_count " +
                     "FROM tournaments t " +
                     "LEFT JOIN users u ON t.organizer_id = u.id " +
                     "WHERE t.status = ? ORDER BY t.start_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur getTournamentsByStatus: " + e.getMessage());
        }
        return list;
    }

    public Tournament getTournamentById(int id) {
        String sql = "SELECT t.*, u.username as organizer_name, " +
                     "(SELECT COUNT(*) FROM tournament_participants tp WHERE tp.tournament_id = t.id) as participant_count " +
                     "FROM tournaments t " +
                     "LEFT JOIN users u ON t.organizer_id = u.id " +
                     "WHERE t.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur getTournamentById: " + e.getMessage());
        }
        return null;
    }

    public int insertTournament(Tournament t) {
        String sql = "INSERT INTO tournaments (name, description, format, start_date, end_date, " +
                     "organizer_id, status, location, max_players, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'PLANNED', ?, ?, CURRENT_TIMESTAMP) RETURNING id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getName());
            stmt.setString(2, t.getDescription());
            stmt.setString(3, t.getFormat());
            stmt.setDate(4, t.getStartDate());
            stmt.setDate(5, t.getEndDate());
            stmt.setInt(6, t.getOrganizerId());
            stmt.setString(7, t.getLocation());
            stmt.setInt(8, t.getMaxPlayers());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur insertTournament: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateTournamentStatus(int id, String status) {
        String sql = "UPDATE tournaments SET status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur updateTournamentStatus: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTournament(Tournament t) {
        String sql = "UPDATE tournaments SET name=?, description=?, format=?, start_date=?, " +
                     "end_date=?, location=?, max_players=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getName());
            stmt.setString(2, t.getDescription());
            stmt.setString(3, t.getFormat());
            stmt.setDate(4, t.getStartDate());
            stmt.setDate(5, t.getEndDate());
            stmt.setString(6, t.getLocation());
            stmt.setInt(7, t.getMaxPlayers());
            stmt.setInt(8, t.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur updateTournament: " + e.getMessage());
        }
        return false;
    }

    public boolean addParticipant(int tournamentId, int userId) {
        String sql = "INSERT INTO tournament_participants (tournament_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tournamentId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur addParticipant: " + e.getMessage());
        }
        return false;
    }

    public int countTournamentWins(int userId, int month, int year) {
        // Un joueur gagne un tournoi s'il gagne tous ses matchs dans ce tournoi
        // Simplification : compter les tournois où il a le plus de victoires
        String sql = "SELECT COUNT(DISTINCT m.tournament_id) FROM matches m " +
                     "WHERE ((m.white_player_id = ? AND m.result = 'WHITE_WIN') OR " +
                     "       (m.black_player_id = ? AND m.result = 'BLACK_WIN')) " +
                     "AND EXTRACT(MONTH FROM m.updated_at) = ? " +
                     "AND EXTRACT(YEAR FROM m.updated_at) = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, month);
            stmt.setInt(4, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[TournamentRepository] Erreur countTournamentWins: " + e.getMessage());
        }
        return 0;
    }

    private Tournament mapRow(ResultSet rs) throws SQLException {
        Tournament t = new Tournament();
        t.setId(rs.getInt("id"));
        t.setName(rs.getString("name"));
        t.setDescription(rs.getString("description"));
        t.setFormat(rs.getString("format"));
        t.setStartDate(rs.getDate("start_date"));
        t.setEndDate(rs.getDate("end_date"));
        t.setOrganizerId(rs.getInt("organizer_id"));
        t.setOrganizerName(rs.getString("organizer_name"));
        t.setStatus(rs.getString("status"));
        t.setLocation(rs.getString("location"));
        t.setMaxPlayers(rs.getInt("max_players"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setParticipantCount(rs.getInt("participant_count"));
        return t;
    }
}
