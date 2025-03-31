package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Regex;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.json.*;
import jakarta.servlet.http.HttpSession;
import jdk.jfr.Timespan;
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

        // Verifie les champs ne sont pas vides
        if(!username.isEmpty() || !email.isEmpty() || !password1.isEmpty() || !password2.isEmpty()) {
            log.info("Les champs ne sont pas vides");
        }else{
            log.error("Les champs sont vides");
            SendJSON.Error(response, "emptyField");
            return;
        }

        // Vérifie la longueur des champs
        if (username.length() < 50 && email.length() < 50) {
            log.info("les champs respectent la longueur du caractère");
            // Vérifie si l'utilisateur existe déjà
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                if (user.getName().equals(username) && user.getEmail().equals(email)) {
                    log.error("L'utilisateur existe déjà");
                    SendJSON.Error(response, "userExists");
                    return;
                }else{
                    log.info("L'utilisateur n'existe pas");
                }
            }
        }else{
            log.error("Les champs dépassent la longueur maximale");
            SendJSON.Error(response, "lengthInvalid");
            return;
        }

        // Vérifie le format de l'email
        if (email.matches(Regex.EMAIL)) {
            log.info("L'email est valide");
        }else{
            SendJSON.Error(response, "emailInvalid");
            return;
        }

        // Verifie si le mot de passe
        String pwHash = null;
        if(password1.equals(password2)) {
            log.info("Les mots de passe correspondent");

            if(password1.matches(Regex.PASSWORD)) {
                pwHash = hashPassword(password1);
                System.out.println(pwHash.getBytes().length);
                System.out.println(pwHash);
            }else{
                log.error("le mot de passe ne respect pas le critère");
                SendJSON.Error(response, "passwordInvalid");
                return;
            }
        }else{
            log.error("Les mots de passe ne correspondent pas");
            SendJSON.Error(response, "passwordInvalid");
            return;
        }
        // TODO: PROBLEME AVEC LA DATE
        User user = new User(username, pwHash, email, "user", Date.valueOf(LocalDate.now()));
        try{
            userService.addUser(user);
            log.info("L'utilisateur a été ajouté avec succès");
        }catch(Exception e){
            log.error("Erreur lors de l'ajout de l'utilisateur", e);
            SendJSON.Error(response, "userNotAdded");
            throw new ServletException(e.getMessage());
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