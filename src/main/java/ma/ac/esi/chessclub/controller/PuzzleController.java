package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.Puzzle;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.PuzzleService;

import java.io.IOException;

/**
 * Contrôleur du puzzle quotidien.
 * GET  /puzzle → affiche le puzzle du jour
 * POST /puzzle → soumet la réponse
 */
@WebServlet("/puzzle")
public class PuzzleController extends HttpServlet {

    private final PuzzleService puzzleService = new PuzzleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier la connexion
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Charger le puzzle du jour
        Puzzle puzzle = puzzleService.getDailyPuzzle();
        request.setAttribute("puzzle", puzzle);

        // Vérifier si l'utilisateur a déjà résolu ce puzzle
        if (puzzle != null) {
            boolean alreadySolved = puzzleService.hasSolvedTodayPuzzle(user.getId(), puzzle.getId());
            request.setAttribute("alreadySolved", alreadySolved);
        }

        request.getRequestDispatcher("/WEB-INF/views/puzzle.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier la connexion
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        int    puzzleId  = Integer.parseInt(request.getParameter("puzzleId"));
        String move      = request.getParameter("move");
        int    timeTaken = 0;
        try {
            timeTaken = Integer.parseInt(request.getParameter("timeTaken"));
        } catch (NumberFormatException ignored) {}

        // Vérifier la réponse
        boolean correct = puzzleService.submitAnswer(user.getId(), puzzleId, move, timeTaken);

        // Préparer la vue
        Puzzle puzzle = puzzleService.getDailyPuzzle();
        request.setAttribute("puzzle",       puzzle);
        request.setAttribute("submitted",    true);
        request.setAttribute("correct",      correct);
        request.setAttribute("submittedMove", move);
        request.setAttribute("alreadySolved", correct);

        if (correct) {
            request.setAttribute("feedbackMessage", "🎉 Excellent ! Vous avez trouvé le bon coup !");
        } else {
            request.setAttribute("feedbackMessage", "❌ Ce n'est pas le bon coup. Réessayez !");
        }

        request.getRequestDispatcher("/WEB-INF/views/puzzle.jsp").forward(request, response);
    }
}
