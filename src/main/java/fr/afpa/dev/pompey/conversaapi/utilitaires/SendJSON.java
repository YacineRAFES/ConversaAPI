package fr.afpa.dev.pompey.conversaapi.utilitaires;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendJSON {
    private SendJSON() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Envoye un message error en JSON et l'envoie à la réponse HTTP.
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
     * Envoye un message success en JSON et l'envoie à la réponse HTTP.
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
     * Envoye un message success en JSON et l'envoie à la réponse HTTP.
     * {"status": "success", "objects" : "Votre Message"}
     *
     * @param response La réponse HTTP.
     * @param jsonObject  L'objet a envoyer.
     * @throws JsonException
     */
    public static void SuccessWithJsonArrayBuilder(HttpServletResponse response, String message, String key, JsonArrayBuilder jsonObject) throws JsonException {
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Je construis un objet JSON avec le message de succès.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", message)
                    .add(key, jsonObject)
                    .build();

            log.info(jsonResponse.toString());
            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.SuccessWithObject : " + e.getMessage());
        }
    }

    /**
     * Envoye un message success en JSON et l'envoie à la réponse HTTP.
     * {"status": "success", "objects" : "Votre Message"}
     *
     * @param response La réponse HTTP.
     * @param jsonObject  L'objet a envoyer.
     * @throws JsonException
     */
    public static void SuccessWithObject(HttpServletResponse response, String message, String key, JsonObject jsonObject) throws JsonException {
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Je construis un objet JSON avec le message de succès.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", message)
                    .add(key, jsonObject)
                    .build();

            log.info(jsonResponse.toString());
            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.SuccessWithObject : " + e.getMessage());
        }
    }

    /**
     * Envoye un token en JSON et l'envoie à la réponse HTTP.
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

    /**
     * Envoie un message de succès de connexion en JSON et envoie JWT à la réponse HTTP.
     * {"status": "success", "message": "loginSuccess"}
     *
     * @param response La réponse HTTP.
     * @param token    Le token JWT à envoyer.
     * @throws JsonException
     */
    public static void LoginUser(HttpServletResponse response, String token, String iduser, String username) throws JsonException {
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Authorization", "Bearer " + token);

            // Je construis un objet JSON avec le token JWT.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "success")
                    .add("message", "loginSuccess")
                    .add("user", Json.createObjectBuilder()
                            .add("iduser", iduser)
                            .add("username", username)
                            .build())
                    .build();

            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.JWT : " + e.getMessage());
        }
    }

    //Envoye sous forme de tableau
    public static void OnlyInArray(HttpServletResponse response, JsonArray jsonArray) throws JsonException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonArray.toString());
        } catch (Exception e) {
            throw new JsonException("Erreur dans OnlyInArray : " + e.getMessage());
        }
    }

    public static void GlobalJSON(HttpServletResponse response, JsonObject jsonObject) throws JsonException {
        //TODO:  A REVOIR
        try {
            // Je définis le type de contenu de la réponse HTTP.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Je construis un objet JSON avec le message de succès.
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "success")
                    .add("objects", jsonObject)
                    .build();

            // J'écris la réponse JSON.
            response.getWriter().write(jsonResponse.toString());
            log.info("jsonResponse : " + jsonResponse);
        } catch (Exception e) {
            throw new JsonException("Erreur dans la méthode SendJSON.Success : " + e.getMessage());
        }
    }




}
