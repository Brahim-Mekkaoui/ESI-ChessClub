package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.AuthService;

import java.io.IOException;

/**
 * Contrôleur de connexion.
 * GET  /login → affiche le formulaire login
 * POST /login → traite la connexion
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private final AuthService authService = new AuthService();

    // ============================================================
    // GET : Afficher la page de connexion
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si déjà connecté, rediriger vers l'accueil
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // ============================================================
    // POST : Traiter la connexion
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Authentification
        User user = authService.authenticate(username, password);

        if (user != null) {
            // Créer la session et y stocker l'utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("successMessage", "Bienvenue, " + user.getUsername() + " !");

            // Redirection vers l'accueil
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // Échec : rester sur la page de login avec un message d'erreur
            request.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            request.setAttribute("username", username); // Pré-remplir le champ
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
