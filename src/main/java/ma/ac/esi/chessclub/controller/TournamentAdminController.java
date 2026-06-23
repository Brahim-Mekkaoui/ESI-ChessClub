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

@WebServlet({"/admin/tournament/create", "/admin/tournament/edit",
             "/admin/tournament/status", "/admin/tournament/generate"})
public class TournamentAdminController extends HttpServlet {

    private final TournamentService tournamentService = new TournamentService();
    private final UserService       userService       = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireAdmin(request, response)) return;
        String path = request.getServletPath();

        if ("/admin/tournament/create".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/admin/tournament-create.jsp")
                   .forward(request, response);
        } else if ("/admin/tournament/edit".equals(path)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Tournament t = tournamentService.getTournamentWithMatches(id);
                request.setAttribute("tournament", t);
                List<User> participants = userService.getParticipants(id);
                request.setAttribute("participants", participants);
            }
            request.getRequestDispatcher("/WEB-INF/views/admin/tournament-edit.jsp")
                   .forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/tournaments");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireAdmin(request, response)) return;
        User admin = (User) request.getSession().getAttribute("user");
        String path = request.getServletPath();

        if ("/admin/tournament/status".equals(path)) {
            int id = Integer.parseInt(request.getParameter("tournamentId"));
            String status = request.getParameter("status");
            tournamentService.updateStatus(id, status);
            request.getSession().setAttribute("successMessage", "Statut mis a jour !");
            response.sendRedirect(request.getContextPath() + "/admin/tournament/edit?id=" + id);
            return;
        }

        if ("/admin/tournament/generate".equals(path)) {
            int id = Integer.parseInt(request.getParameter("tournamentId"));
            tournamentService.generateRoundRobinMatches(id);
            request.getSession().setAttribute("successMessage", "Parties generees !");
            response.sendRedirect(request.getContextPath() + "/tournaments?action=detail&id=" + id);
            return;
        }

        // POST /admin/tournament/create
        String name        = request.getParameter("name");
        String description = request.getParameter("description");
        String format      = request.getParameter("format");
        String startDate   = request.getParameter("startDate");
        String endDate     = request.getParameter("endDate");
        String location    = request.getParameter("location");
        int maxPlayers = 8;
        try { maxPlayers = Integer.parseInt(request.getParameter("maxPlayers")); } catch (Exception ignored) {}

        int tournamentId = tournamentService.createTournament(
            name, description, format, startDate, endDate, location, admin.getId(), maxPlayers
        );

        if (tournamentId > 0) {
            request.getSession().setAttribute("successMessage", "Tournoi cree avec succes !");
            response.sendRedirect(request.getContextPath() + "/tournaments");
        } else {
            request.setAttribute("error", "Erreur lors de la creation du tournoi.");
            request.getRequestDispatcher("/WEB-INF/views/admin/tournament-create.jsp")
                   .forward(request, response);
        }
    }

    private boolean requireAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acces refuse.");
            return false;
        }
        return true;
    }
}
