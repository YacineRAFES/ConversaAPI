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
public class CORSFilter implements Filter {

    // Définir les origines autorisées (remplace par ton domaine)
    private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:8090");


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String origin = request.getHeader("Origin");

        // Vérifie si l'origine est autorisée
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            log.info("Accès autorisé depuis l'origine: {}", origin);
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            log.warn("Tentative d'accès non autorisée depuis l'origine: {}", origin);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès interdit");
            return;
        }

        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Autoriser les cookies et sessions

        // Autoriser les en-têtes personnalisés
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // Si c'est une requête OPTIONS (préflight), on renvoie directement la réponse
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}