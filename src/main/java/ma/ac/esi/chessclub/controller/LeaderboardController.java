package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.service.LeaderboardService;

import java.io.IOException;

/**
 * Contrôleur du leaderboard.
 * GET /leaderboard → affiche le classement et le joueur du mois
 */
@WebServlet("/leaderboard")
public class LeaderboardController extends HttpServlet {

    private final LeaderboardService leaderboardService = new LeaderboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Classement complet
        request.setAttribute("leaderboard",    leaderboardService.getLeaderboard());
        // Joueur du mois
        request.setAttribute("playerOfMonth",  leaderboardService.getPlayerOfMonth());

        request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
    }
}
