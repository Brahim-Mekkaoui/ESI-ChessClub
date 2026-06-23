package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.service.LeaderboardService;
import ma.ac.esi.chessclub.service.NewsService;

import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private final NewsService newsService = new NewsService();
    private final LeaderboardService leaderboardService = new LeaderboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("latestNews", newsService.getLatestNews(3));
        request.setAttribute("playerOfMonth", leaderboardService.getPlayerOfMonth());

        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}