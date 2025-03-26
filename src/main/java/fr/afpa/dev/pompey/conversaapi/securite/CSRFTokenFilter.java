package fr.afpa.dev.pompey.conversaapi.securite;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.UUID;

@WebFilter("/*")
@Slf4j
public class CSRFTokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Filtre CSRF exécuté avant UserServlet");
//        // Convertir en HttpServletRequest/HttpServletResponse
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        log.info("filtre csrf appele");
        HttpSession session = httpRequest.getSession(false);

        // Vérifier uniquement pour les requêtes sensibles (POST, PUT, DELETE)
        String method = httpRequest.getMethod();
        log.info(method);
        if (session != null
                && (method.equalsIgnoreCase("POST")
                || method.equalsIgnoreCase("PUT")
                || method.equalsIgnoreCase("DELETE"))) {
            // Récupérer le token CSRF envoyé par le client
            log.info("REQUETE: {} ", requestURI);
            request.getParameterMap().forEach((key, value) -> log.info("REQUETE PARAMETER key: {} value: {}", key, value));
            String csrfTokenFromClient = extractCsrfTokenFromJson(httpRequest);

            // Récupérer le token CSRF stocké dans la session
            String csrfTokenFromServer = (String) session.getAttribute("csrfToken");
            log.info("filtre csrf verification");
            log.info("csrf requete: {}", csrfTokenFromClient);
            log.info("csrf session: {}", csrfTokenFromServer);
            // Validation
            if (csrfTokenFromClient == null || !csrfTokenFromClient.equals(csrfTokenFromServer)) {
                // Rejet si le token est invalide ou absent
                log.info("filtre csrf invalide ou absent");
                if (!httpResponse.isCommitted()) {
                    // Envoyer une réponse d'erreur 403 Forbidden
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                } else {
                    // Si la réponse est déjà engagée, réinitialisez la réponse
                    httpResponse.reset();
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                }
                return;
            }
        } else if (session != null && (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD"))) {
            log.info("request uri : {}", requestURI);
            String uuidStr = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", uuidStr);
            request.setAttribute("csrfToken", uuidStr);
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String extractCsrfTokenFromJson(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        if (sb.isEmpty()) {
            return null;
        }

        try (JsonReader jsonReader = Json.createReader(new StringReader(sb.toString()))) {
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.getString("csrfToken", null);
        } catch (Exception e) {
            log.error("Erreur lors de la lecture du JSON", e);
            return null;
        }
    }
}

