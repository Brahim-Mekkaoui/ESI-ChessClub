package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.service.NewsService;

import java.io.IOException;

/**
 * Contrôleur des actualités (lecture).
 * GET /news → affiche les 10 dernières actualités
 */
@WebServlet("/news")
public class NewsController extends HttpServlet {

    private final NewsService newsService = new NewsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 10 dernières actualités publiées
        request.setAttribute("newsList", newsService.getLatestNews(10));
        request.getRequestDispatcher("/WEB-INF/views/news.jsp").forward(request, response);
    }
}
