package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.News;
import ma.ac.esi.chessclub.repository.NewsRepository;
import ma.ac.esi.chessclub.util.ValidationUtil;

import java.util.List;

/**
 * Service métier pour les actualités du club.
 */
public class NewsService {

    private final NewsRepository newsRepository = new NewsRepository();

    public List<News> getLatestNews(int limit) {
        return newsRepository.getLatestNews(limit);
    }

    public News getNewsById(int id) {
        return newsRepository.getNewsById(id);
    }

    /**
     * Publie une nouvelle actualité avec validation.
     * @return l'ID créé, ou -1 si invalide
     */
    public int publishNews(String title, String content, int authorId) {
        if (!ValidationUtil.isNotEmpty(title) || title.length() > 200) return -1;
        if (!ValidationUtil.isNotEmpty(content)) return -1;

        News news = new News();
        news.setTitle(title.trim());
        news.setContent(content.trim());
        news.setAuthorId(authorId);

        return newsRepository.insertNews(news);
    }

    public boolean deleteNews(int id) {
        return newsRepository.deleteNews(id);
    }
}
