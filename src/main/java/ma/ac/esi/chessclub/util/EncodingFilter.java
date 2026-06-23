package ma.ac.esi.chessclub.util;

import jakarta.servlet.*;
import java.io.IOException;

/**
 * Filtre HTTP qui force l'encodage UTF-8 sur toutes les requêtes et réponses.
 * Déclaré dans web.xml avec le mapping "/*".
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
