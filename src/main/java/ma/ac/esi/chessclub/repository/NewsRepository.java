package ma.ac.esi.chessclub.repository;

import ma.ac.esi.chessclub.model.News;
import ma.ac.esi.chessclub.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour les actualités du club.
 */
public class NewsRepository {

    public List<News> getLatestNews(int limit) {
        List<News> list = new ArrayList<>();
        String sql = "SELECT n.*, u.username as author_name FROM news n " +
                     "LEFT JOIN users u ON n.author_id = u.id " +
                     "WHERE n.is_published = TRUE " +
                     "ORDER BY n.published_at DESC LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[NewsRepository] Erreur getLatestNews: " + e.getMessage());
        }
        return list;
    }

    public News getNewsById(int id) {
        String sql = "SELECT n.*, u.username as author_name FROM news n " +
                     "LEFT JOIN users u ON n.author_id = u.id WHERE n.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[NewsRepository] Erreur getNewsById: " + e.getMessage());
        }
        return null;
    }

    public int insertNews(News news) {
        String sql = "INSERT INTO news (title, content, author_id, is_published, published_at) " +
                     "VALUES (?, ?, ?, TRUE, CURRENT_TIMESTAMP) RETURNING id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, news.getTitle());
            stmt.setString(2, news.getContent());
            stmt.setInt(3, news.getAuthorId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("[NewsRepository] Erreur insertNews: " + e.getMessage());
        }
        return -1;
    }

    public boolean deleteNews(int id) {
        String sql = "DELETE FROM news WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[NewsRepository] Erreur deleteNews: " + e.getMessage());
        }
        return false;
    }

    private News mapRow(ResultSet rs) throws SQLException {
        News n = new News();
        n.setId(rs.getInt("id"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setAuthorId(rs.getInt("author_id"));
        n.setAuthorName(rs.getString("author_name"));
        n.setPublishedAt(rs.getTimestamp("published_at"));
        n.setUpdatedAt(rs.getTimestamp("updated_at"));
        n.setPublished(rs.getBoolean("is_published"));
        return n;
    }
}
