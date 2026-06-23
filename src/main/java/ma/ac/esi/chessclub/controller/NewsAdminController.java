package ma.ac.esi.chessclub.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.service.NewsService;

import java.io.IOException;

@WebServlet({"/admin/news/create", "/admin/news/delete"})
public class NewsAdminController extends HttpServlet {

    private final NewsService newsService = new NewsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireAdmin(request, response)) return;
        request.getRequestDispatcher("/WEB-INF/views/admin/news-create.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireAdmin(request, response)) return;

        String path = request.getServletPath();

        if ("/admin/news/delete".equals(path)) {
            int id = Integer.parseInt(request.getParameter("newsId"));
            newsService.deleteNews(id);
            request.getSession().setAttribute("successMessage", "Actualite supprimee.");
            response.sendRedirect(request.getContextPath() + "/news");
            return;
        }

        // POST /admin/news/create
        User admin  = (User) request.getSession().getAttribute("user");
        String title   = request.getParameter("title");
        String content = request.getParameter("content");

        int newsId = newsService.publishNews(title, content, admin.getId());

        if (newsId > 0) {
            request.getSession().setAttribute("successMessage", "Actualite publiee !");
            response.sendRedirect(request.getContextPath() + "/news");
        } else {
            request.setAttribute("error", "Erreur lors de la publication.");
            request.getRequestDispatcher("/WEB-INF/views/admin/news-create.jsp")
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
