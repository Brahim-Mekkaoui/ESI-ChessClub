package ma.ac.esi.chessclub.model;

import java.sql.Timestamp;

/**
 * Entité représentant une actualité du club.
 */
public class News {
    private int id;
    private String title;
    private String content;
    private int authorId;
    private String authorName; // enrichi (join avec users)
    private Timestamp publishedAt;
    private Timestamp updatedAt;
    private boolean isPublished;

    public News() {
        this.isPublished = true;
    }

    // Méthodes utilitaires
    public String getShortContent(int maxLength) {
        if (content == null) return "";
        if (content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public Timestamp getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Timestamp publishedAt) { this.publishedAt = publishedAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }

    public String getFormattedDate() {
        if (publishedAt == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(publishedAt);
    }
}
