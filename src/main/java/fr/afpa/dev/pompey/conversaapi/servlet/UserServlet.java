package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.json.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import static fr.afpa.dev.pompey.conversaapi.securite.Securite.hashPassword;

/**
 * Servlet pour les utilisateurs.
 */
@Slf4j
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService;

    /**
     * Initialise la servlet.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    /**
     * Récupère tous les utilisateurs et les renvoie en JSON.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String csrfToken = (String) session.getAttribute("csrfToken");

        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }

        log.info(csrfToken);

//        SendJSON.Error(response, "Erreur du serveur");
        SendJSON.Token(response, "csrfToken", csrfToken);

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        JsonObject jsonResponse = Json.createObjectBuilder()
//                .add("csrfToken", csrfToken)
//                .build();
//
//        response.getWriter().write(jsonResponse.toString());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lire le JSON envoyé par le client
        JsonReader jsonReader = Json.createReader(request.getInputStream());
        JsonObject jsonObject = jsonReader.readObject();

        String username = jsonObject.getString("user", "");
        String email = jsonObject.getString("email", "");
        String password1 = jsonObject.getString("password1", "");
        String password2 = jsonObject.getString("password2", "");

        if(password1.equals(password2)) {
            log.info("Les mots de passe correspondent");
            String pwHash = hashPassword(password1);
        }else{
            log.error("Les mots de passe ne correspondent pas");
            SendJSON.Error(response, "passwordInvalid");
        }

    }

    /**
     * Convertit une liste d'utilisateurs en JSON.
     *
     * @param users La liste d'utilisateurs.
     * @return La liste d'utilisateurs en JSON.
     */
//    private String usersToJson(List<User> users) {
//        try{
//            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//            for (User user : users) {
//                arrayBuilder.add(Json.createObjectBuilder()
//                        .add("id", user.getId())
//                        .add("name", user.getName())
//                        .add("password", user.getPassword())
//                        .add("email", user.getEmail())
//                        .add("role", user.getRole())
//                        .add("date", user.getDate().toString()));
//            }
//            return arrayBuilder.build().toString();
//        }catch (JsonException e){
//            throw new JsonException("Erreur lors de la lecture du serveur");
//        }
//    }

    /**
     * Méthode pour extraire le token CSRF du JSON envoyé
     * @param json
     * @return
     */
    private String extraireCsrfToken(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.getString("csrfToken", null);
        }
    }

    /**
     * Extraire
     * @param json
     * @return
     */
    private User extraireUtilisateurDepuisJson(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();

            String username = jsonObject.getString("user", "");
            String email = jsonObject.getString("email", "");
            String password1 = jsonObject.getString("password1", "");
            String password2 = jsonObject.getString("password2", "");

            return new User(username, email, password1, password2); // Adapte selon ton constructeur
        } catch (Exception e) {
            log.error("Erreur lors de la lecture du JSON", e);
            return null;
        }
    }



}