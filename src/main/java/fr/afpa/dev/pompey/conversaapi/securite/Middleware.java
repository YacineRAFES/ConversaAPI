package fr.afpa.dev.pompey.conversaapi.securite;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebFilter("/*")
public class Middleware implements Filter {

    List<String> routesAProteger = List.of(
            "/home"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI(); // Récupère l'URL demandée

        // Si l'URL est dans la liste des routes à protéger
        if (routesAProteger.contains(uri)) {
            String token = request.getHeader("Authorization");

            // Vérifie si le token est présent et commence par "Bearer "
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);  // On extrait le token sans "Bearer "

                // Validation du token JWT
                if (JWTutils.validateToken(token)) {
                    // Si le token est valide, on passe à la requête suivante
                    chain.doFilter(request, response);
                    return;
                }
            }

            // Si le token est absent ou invalide, renvoyer une erreur ou rediriger
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
            response.getWriter().write("Token JWT invalide ou manquant");
            return;
        }

        // Si l'URL n'est pas protégée, on continue sans filtrage
        chain.doFilter(request, response);
    }

}
