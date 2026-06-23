package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.service.AuthService;

import java.io.IOException;
import java.util.Map;

/**
 * Contrôleur d'inscription.
 * GET  /register → affiche le formulaire d'inscription
 * POST /register → traite l'inscription
 */
@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si déjà connecté, rediriger
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username   = request.getParameter("username");
        String email      = request.getParameter("email");
        String password   = request.getParameter("password");
        String firstName  = request.getParameter("firstName");
        String lastName   = request.getParameter("lastName");
        String chessLevel = request.getParameter("chessLevel");

        Map<String, Object> result = authService.register(
            username, email, password, firstName, lastName, chessLevel
        );

        boolean success = (Boolean) result.getOrDefault("success", false);

        if (success) {
            // Succès → rediriger vers login avec message
            HttpSession session = request.getSession();
            session.setAttribute("successMessage",
                "Inscription réussie ! Connectez-vous maintenant.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Échec → rester sur register avec erreur
            request.setAttribute("error", result.get("error"));
            // Pré-remplir les champs
            request.setAttribute("username",   username);
            request.setAttribute("email",      email);
            request.setAttribute("firstName",  firstName);
            request.setAttribute("lastName",   lastName);
            request.setAttribute("chessLevel", chessLevel);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
