package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("csrfToken", csrfToken)
                .build();

        response.getWriter().write(jsonResponse.toString());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lire le JSON envoyé par le client
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb);
        String json = sb.toString();
        System.out.println("JSON reçu: " + json);

        log.info("JSON reçu: " + json);

        if (json.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le corps de la requête est vide");
            return;
        }

        //Afficher json
        System.out.println(json);

        User newUser = extraireUtilisateurDepuisJson(json);
        if (newUser == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données utilisateur invalides");
            return;
        }
        //        User newUser = new parse;
    }

    /**
     * Convertit une liste d'utilisateurs en JSON.
     *
     * @param users La liste d'utilisateurs.
     * @return La liste d'utilisateurs en JSON.
     */
    private String usersToJson(List<User> users) {
        try{
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (User user : users) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", user.getId())
                        .add("name", user.getName())
                        .add("password", user.getPassword())
                        .add("email", user.getEmail())
                        .add("role", user.getRole())
                        .add("date", user.getDate().toString()));
            }
            return arrayBuilder.build().toString();
        }catch (JsonException e){
            throw new JsonException("Erreur lors de la lecture du serveur");
        }
    }

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
