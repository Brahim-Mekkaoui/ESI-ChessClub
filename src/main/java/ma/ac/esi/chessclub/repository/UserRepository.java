package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour l'accès aux données des utilisateurs.
 * Toutes les requêtes SQL relatives à la table "users" sont ici.
 */
public class UserRepository {

    // ============================================================
    // Lecture
    // ============================================================

    /**
     * Récupère un utilisateur par son ID.
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ? AND is_active = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getUserById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère un utilisateur par son username.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getUserByUsername: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère un utilisateur par son email.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND is_active = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getUserByEmail: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère tous les membres actifs.
     */
    public List<User> getAllMembers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_active = TRUE ORDER BY elo_rating DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getAllMembers: " + e.getMessage());
        }
        return users;
    }

    /**
     * Récupère tous les membres (rôle MEMBER seulement) pour le leaderboard.
     */
    public List<User> getAllRegularMembers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_active = TRUE AND role = 'MEMBER' ORDER BY elo_rating DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getAllRegularMembers: " + e.getMessage());
        }
        return users;
    }

    /**
     * Vérifie si un username existe déjà.
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur usernameExists: " + e.getMessage());
        }
        return false;
    }

    /**
     * Vérifie si un email existe déjà.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur emailExists: " + e.getMessage());
        }
        return false;
    }

    // ============================================================
    // Écriture
    // ============================================================

    /**
     * Insère un nouvel utilisateur en base.
     * @return l'ID généré, ou -1 en cas d'erreur
     */
    public int insertUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, first_name, last_name, " +
                     "chess_level, role, elo_rating, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'MEMBER', 1200, CURRENT_TIMESTAMP) RETURNING id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getChessLevel());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur insertUser: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Met à jour le profil d'un utilisateur.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, chess_level=?, bio=?, " +
                     "elo_rating=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getChessLevel());
            stmt.setString(4, user.getBio());
            stmt.setInt(5, user.getEloRating());
            stmt.setInt(6, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur updateUser: " + e.getMessage());
        }
        return false;
    }

    /**
     * Met à jour uniquement le rating ELO d'un utilisateur.
     */
    public boolean updateEloRating(int userId, int newElo) {
        String sql = "UPDATE users SET elo_rating=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newElo);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur updateEloRating: " + e.getMessage());
        }
        return false;
    }

    // ============================================================
    // Mapping ResultSet → User
    // ============================================================

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setChessLevel(rs.getString("chess_level"));
        user.setBio(rs.getString("bio"));
        user.setProfilePictureUrl(rs.getString("profile_picture_url"));
        user.setRole(rs.getString("role"));
        user.setEloRating(rs.getInt("elo_rating"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }

    /**
     * Retourne la liste des participants inscrits à un tournoi.
     */
    public List<User> getParticipants(int tournamentId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM users u " +
                     "JOIN tournament_participants tp ON u.id = tp.user_id " +
                     "WHERE tp.tournament_id = ? ORDER BY u.elo_rating DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[UserRepository] Erreur getParticipants: " + e.getMessage());
        }
        return list;
    }
}
