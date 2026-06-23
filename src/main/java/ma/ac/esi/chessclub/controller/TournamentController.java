package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.Tournament;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.TournamentService;
import ma.ac.esi.chessclub.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/tournaments")
public class TournamentController extends HttpServlet {

    private final TournamentService tournamentService = new TournamentService();
    private final UserService       userService       = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam     = request.getParameter("id");
        String statusParam = request.getParameter("status");
        String action      = request.getParameter("action");

        if ("detail".equals(action) || (idParam != null && !idParam.isEmpty())) {
            int id = Integer.parseInt(idParam != null ? idParam : "0");
            Tournament tournament = tournamentService.getTournamentWithMatches(id);
            if (tournament == null) {
                response.sendError(404, "Tournoi introuvable");
                return;
            }
            List<User> participants = userService.getParticipants(id);
            request.setAttribute("tournament", tournament);
            request.setAttribute("participants", participants);
            request.setAttribute("matches", tournament.getMatches());
            request.getRequestDispatcher("/WEB-INF/views/tournament-detail.jsp")
                   .forward(request, response);
        } else {
            if (statusParam != null && !statusParam.isEmpty()) {
                request.setAttribute("tournaments",
                    tournamentService.getTournamentsByStatus(statusParam));
            } else {
                request.setAttribute("tournaments",
                    tournamentService.getAllTournaments());
            }
            request.getRequestDispatcher("/WEB-INF/views/tournaments.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if ("join".equals(action)) {
            int tournamentId = Integer.parseInt(request.getParameter("tournamentId"));
            boolean ok = tournamentService.addParticipant(tournamentId, user.getId());
            if (ok) {
                session.setAttribute("successMessage", "Inscription reussie !");
            } else {
                session.setAttribute("errorMessage", "Impossible de s'inscrire a ce tournoi.");
            }
            response.sendRedirect(request.getContextPath() + "/tournaments?action=detail&id=" + tournamentId);
        } else {
            response.sendRedirect(request.getContextPath() + "/tournaments");
        }
    }
}
