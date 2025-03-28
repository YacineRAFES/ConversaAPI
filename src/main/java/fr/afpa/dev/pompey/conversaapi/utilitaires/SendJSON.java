package fr.afpa.dev.pompey.conversaapi.utilitaires;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SendJSON {
    /**
     * Convertit un message error en JSON et l'envoie à la réponse HTTP.
     * {"status": "error", "message": "Votre Message"}
     *
     * @param response La réponse HTTP.
     * @param message  Le message à envoyer.
     * @throws JsonException
     */
    public static void Error(HttpServletResponse response, String message) throws JsonException {
        try {
            // Définition des headers HTTP
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Code 400 pour erreur

            // Création de l'objet JSON
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", message)
                    .build();

            // Écriture de la réponse JSON
            response.getWriter().write(jsonResponse.toString());

            // S'assurer que la réponse est bien flushée
            response.getWriter().flush();
        } catch (IOException e) {
            throw new JsonException("Erreur dans la méthode SendJSON.Error : " + e.getMessage());
        }
    }

    /**
     * Convertit un message success en JSON et l'envoie à la réponse HTTP.
     * {"status": "success", "message": "Votre Message"}
     *
     * @param response La réponse HTTP.
     * @param message  Le message à envoyer.
     * @throws JsonException
     */
    public static void Success(HttpServletResponse response, String message) throws JsonException {
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Je construis un objet JSON avec le message de succès.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", message)
                    .build();

            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.Success : " + e.getMessage());
        }
    }

    /**
     * Convertit un token en JSON et l'envoie à la réponse HTTP.
     * {"status": "typeToken", "message": "votreToken"}
     *
     * @param response      La réponse HTTP.
     * @param typeToken     Le type de token à envoyer.
     * @param token         Le token à envoyer.
     * @throws JsonException
     */
    public static void Token(HttpServletResponse response, String typeToken, String token) throws JsonException {
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Je construis un objet JSON avec le token.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add(typeToken, token)
                    .build();

            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.Token : " + e.getMessage());
        }
    }
}
