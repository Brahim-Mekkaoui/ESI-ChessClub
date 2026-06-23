package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.MatchService;

import java.io.IOException;

/**
 * Contrôleur pour l'enregistrement des résultats de matchs (ADMIN).
 * POST /MatchController → enregistre le résultat d'un match
 */
@WebServlet("/match")
public class MatchController extends HttpServlet {

    private final MatchService matchService = new MatchService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérification admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs.");
            return;
        }

        // Récupérer les paramètres
        int    matchId      = Integer.parseInt(request.getParameter("matchId"));
        String result       = request.getParameter("result");
        int    tournamentId = Integer.parseInt(request.getParameter("tournamentId"));

        // Enregistrer le résultat (met à jour ELO automatiquement)
        boolean success = matchService.recordResult(matchId, result);

        if (success) {
            session.setAttribute("successMessage",
                "Résultat enregistré ! Les ratings ELO ont été mis à jour.");
        } else {
            session.setAttribute("errorMessage", "Erreur lors de l'enregistrement du résultat.");
        }

        response.sendRedirect(request.getContextPath() + "/tournaments?action=detail&id=" + tournamentId);
    }
}
