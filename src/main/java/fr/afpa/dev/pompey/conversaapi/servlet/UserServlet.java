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
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import jakarta.json.*;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet pour les utilisateurs.
 */
@WebServlet("/api/user")
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
        List<User> users = userService.getAllUsers();
        HttpSession session = request.getSession();
        String csrfToken = (String) session.getAttribute("csrfToken");

        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = Json.createObjectBuilder()
            .add("csrfToken", csrfToken)
                    .add("users", usersToJson(users))
                            .build();

        response.getWriter().write(usersToJson(users));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String csrfToken = (String) session.getAttribute("csrfToken");

        //Lire le JSON envoyé par le clint
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();

        //Extraire le token CSRF du JSON
        String csrfTokenRecu = extraireCsrfToken(json);

        if(csrfToken.equals(csrfTokenRecu)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF Token Invalide");
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

    // Méthode pour extraire le token CSRF du JSON envoyé
    private String extraireCsrfToken(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.getString("csrfToken", null);
        }
    }


}
