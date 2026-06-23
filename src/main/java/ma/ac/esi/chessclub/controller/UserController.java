package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.MatchService;
import ma.ac.esi.chessclub.service.UserService;
import ma.ac.esi.chessclub.service.BadgeService;

import java.io.IOException;

@WebServlet("/user")
public class UserController extends HttpServlet {

    private final UserService  userService  = new UserService();
    private final MatchService matchService = new MatchService();
    private final BadgeService badgeService = new BadgeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String idParam = request.getParameter("id");
        int targetId = (idParam != null && !idParam.isEmpty())
            ? Integer.parseInt(idParam)
            : currentUser.getId();

        User profileUser = userService.getUserWithStats(targetId);
        if (profileUser == null) {
            response.sendError(404, "Utilisateur introuvable");
            return;
        }

        // Flash messages
        String success = (String) session.getAttribute("successMessage");
        String error   = (String) session.getAttribute("errorMessage");
        if (success != null) { request.setAttribute("success", success); session.removeAttribute("successMessage"); }
        if (error   != null) { request.setAttribute("error",   error);   session.removeAttribute("errorMessage"); }

        request.setAttribute("profileUser",   profileUser);
        request.setAttribute("recentMatches", matchService.getMatchesByUser(targetId));
        request.setAttribute("userBadges",    badgeService.getUserBadgesWithDetails(targetId));
        request.setAttribute("isOwnProfile",  targetId == currentUser.getId());

        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        currentUser.setFirstName(request.getParameter("firstName"));
        currentUser.setLastName(request.getParameter("lastName"));
        currentUser.setChessLevel(request.getParameter("chessLevel"));
        currentUser.setBio(request.getParameter("bio"));
        currentUser.setEmail(request.getParameter("email"));
        String pic = request.getParameter("profilePictureUrl");
        if (pic != null && !pic.isEmpty()) currentUser.setProfilePictureUrl(pic);

        userService.updateUser(currentUser);
        session.setAttribute("user", currentUser);
        session.setAttribute("successMessage", "Profil mis a jour !");

        response.sendRedirect(request.getContextPath() + "/user");
    }
}
