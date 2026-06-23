package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.Puzzle;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour les puzzles d'échecs.
 */
public class PuzzleRepository {

    public Puzzle getPuzzleByDate(LocalDate date) {
        String sql = "SELECT * FROM puzzles WHERE puzzle_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur getPuzzleByDate: " + e.getMessage());
        }
        return null;
    }

    public Puzzle getPuzzleById(int id) {
        String sql = "SELECT * FROM puzzles WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur getPuzzleById: " + e.getMessage());
        }
        return null;
    }

    public List<Puzzle> getRecentPuzzles(int limit) {
        List<Puzzle> list = new ArrayList<>();
        String sql = "SELECT * FROM puzzles ORDER BY puzzle_date DESC LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur getRecentPuzzles: " + e.getMessage());
        }
        return list;
    }

    public int insertPuzzle(Puzzle puzzle) {
        String sql = "INSERT INTO puzzles (fen_position, best_move, all_moves, difficulty, " +
                     "puzzle_id_lichess, theme, fetched_from_api, puzzle_date, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) " +
                     "ON CONFLICT (puzzle_date) DO UPDATE SET " +
                     "fen_position=EXCLUDED.fen_position, best_move=EXCLUDED.best_move, " +
                     "all_moves=EXCLUDED.all_moves, difficulty=EXCLUDED.difficulty, " +
                     "puzzle_id_lichess=EXCLUDED.puzzle_id_lichess, theme=EXCLUDED.theme, " +
                     "fetched_from_api=EXCLUDED.fetched_from_api " +
                     "RETURNING id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, puzzle.getFenPosition());
            stmt.setString(2, puzzle.getBestMove());
            stmt.setString(3, puzzle.getAllMoves());
            stmt.setInt(4, puzzle.getDifficulty());
            stmt.setString(5, puzzle.getPuzzleIdLichess());
            stmt.setString(6, puzzle.getTheme());
            stmt.setBoolean(7, puzzle.isFetchedFromApi());
            stmt.setDate(8, puzzle.getPuzzleDate());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur insertPuzzle: " + e.getMessage());
        }
        return -1;
    }

    public int countSolvedPuzzles(int userId) {
        String sql = "SELECT COUNT(*) FROM user_puzzle_stats WHERE user_id = ? AND solved = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur countSolvedPuzzles: " + e.getMessage());
        }
        return 0;
    }

    public int countSolvedPuzzlesThisMonth(int userId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM user_puzzle_stats WHERE user_id = ? AND solved = TRUE " +
                     "AND EXTRACT(MONTH FROM date_solved) = ? AND EXTRACT(YEAR FROM date_solved) = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur countSolvedPuzzlesThisMonth: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Compte le nombre de jours consécutifs où l'utilisateur a résolu un puzzle.
     */
    public int countConsecutiveDays(int userId) {
        String sql = "SELECT DISTINCT DATE(date_solved) as solve_date FROM user_puzzle_stats " +
                     "WHERE user_id = ? AND solved = TRUE ORDER BY solve_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            int consecutive = 0;
            LocalDate expected = LocalDate.now();
            while (rs.next()) {
                LocalDate solveDate = rs.getDate("solve_date").toLocalDate();
                if (solveDate.equals(expected) || solveDate.equals(expected.minusDays(1))) {
                    consecutive++;
                    expected = solveDate.minusDays(1);
                } else {
                    break;
                }
            }
            return consecutive;
        } catch (SQLException e) {
            System.err.println("[PuzzleRepository] Erreur countConsecutiveDays: " + e.getMessage());
        }
        return 0;
    }

    private Puzzle mapRow(ResultSet rs) throws SQLException {
        Puzzle p = new Puzzle();
        p.setId(rs.getInt("id"));
        p.setFenPosition(rs.getString("fen_position"));
        p.setBestMove(rs.getString("best_move"));
        p.setAllMoves(rs.getString("all_moves"));
        p.setDifficulty(rs.getInt("difficulty"));
        p.setPuzzleIdLichess(rs.getString("puzzle_id_lichess"));
        p.setTheme(rs.getString("theme"));
        p.setFetchedFromApi(rs.getBoolean("fetched_from_api"));
        p.setPuzzleDate(rs.getDate("puzzle_date"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
