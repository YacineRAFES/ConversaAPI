package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Regex;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = (String) request.getAttribute("requestBody");
        if (requestBody != null && !requestBody.isEmpty()) {
            try (JsonReader jsonReader = Json.createReader(new StringReader(requestBody))) {
                JsonObject jsonObject = jsonReader.readObject();
                System.out.println("JSON reçu: " + jsonObject);

                String username = jsonObject.getString("user", null);
                String email = jsonObject.getString("email", null);
                String password1 = jsonObject.getString("password1", null);
                String password2 = jsonObject.getString("password2", null);

                System.out.println(username);
                System.out.println(email);
                System.out.println(password1);
                System.out.println(password2);

                if (username == null || email == null || password1 == null || password2 == null) {
                    System.out.println("Informations manquantes sur " + Utils.getNameClass());
                    JsonObject jsonResponse = Json.createObjectBuilder()
                            .add("status", "error")
                            .add("message", "Informations manquantes")
                            .build();

                    response.getWriter().write(jsonResponse.toString());
                    response.getWriter().flush();
                    response.getWriter().close();
                    throw new RuntimeException("Informations manquantes");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JsonObject jsonResponse = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "Informations manquantes")
                        .build();

                response.getWriter().write(jsonResponse.toString());
                response.getWriter().flush();
                response.getWriter().close();
                throw new RuntimeException("Informations manquantes");
            }
        } else {
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", "Informations manquantes")
                    .build();

            response.getWriter().write(jsonResponse.toString());
            response.getWriter().flush();
            response.getWriter().close();
            throw new RuntimeException("Informations manquantes");
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Definit le type de la réponse en JSON
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//
//        // Récupère le JSON de la requête "request"
//        try (JsonReader jsonReader = Json.createReader(request.getInputStream())) {
//            JsonObject jsonObject = jsonReader.readObject();
//            System.out.println("JSON reçu: " + jsonObject);
//
//            String username = jsonObject.getString("user", null);
//            String email = jsonObject.getString("email", null);
//            String password1 = jsonObject.getString("password1", null);
//            String password2 = jsonObject.getString("password2", null);
//
//            System.out.println(username);
//            System.out.println(email);
//            System.out.println(password1);
//            System.out.println(password2);
//
//            //1.VERIFICATION DES CHAMPS DE SAISIE
//            if (username.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
//                System.out.println("Informations manquantes " + Utils.getNameClass());
//                JsonObject jsonResponse = Json.createObjectBuilder()
//                        .add("status", "error")
//                        .add("message", "Informations manquantes")
//                        .build();
//
//                response.getWriter().write(jsonResponse.toString());
//                response.getWriter().flush();
//                response.getWriter().close();
//                throw new RuntimeException("Informations manquantes");
//            }
//            //2.MOT DE PASSE IDENTIQUE
////            if(password1.equals(password2)) {
////                System.out.println("Les mots de passe sont identiques");
////                String pw = password1;
////                if(pw.matches(Regex.REGEX_PASSWORD)) {
////                    System.out.println("Le mot de passe est valide");
////                    List<User> users = userService.getAllUsers();
////                    //Comparaison des utilisateurs si il a le meme email
////                    for(User user : users) {
////                        if(user.getEmail().equals(email)) {
////                            System.out.println("L'email existe déjà");
////                            JsonObject jsonResponse = Json.createObjectBuilder()
////                                    .add("status", "error")
////                                    .add("message", "L'email existe déjà")
////                                    .build();
////
////                            response.getWriter().write(jsonResponse.toString());
////                            response.getWriter().flush();
////                            response.getWriter().close();
////                            throw new RuntimeException("L'email existe déjà");
////                        }
////                    }
////                }else{
////                    System.out.println("Le mot de passe n'est pas valide");
////                    JsonObject jsonResponse = Json.createObjectBuilder()
////                            .add("status", "error")
////                            .add("message", "Le mot de passe n'est pas valide")
////                            .build();
////
////                    response.getWriter().write(jsonResponse.toString());
////                    response.getWriter().flush();
////                    response.getWriter().close();
////                    throw new RuntimeException("Le mot de passe n'est pas valide");
////                }
////            }else{
////                System.out.println("Les mots de passe ne sont pas identiques");
////                JsonObject jsonResponse = Json.createObjectBuilder()
////                        .add("status", "error")
////                        .add("message", "Les mots de passe ne sont pas identiques")
////                        .build();
////
////                response.getWriter().write(jsonResponse.toString());
////                response.getWriter().flush();
////                response.getWriter().close();
////                MsgErrorToJSON(response, "Les mots de passe ne sont pas identiques");
////
////                throw new RuntimeException("Les mots de passe ne sont pas identiques");
////            }
////            //3.VERIFICATION DE L'EXISTENCE DE L'UTILISATEUR
////            //4.CREATION DE L'UTILISATEUR
////            //5.RETOUR DE LA REPONSE
////            JsonObject jsonResponse = Json.createObjectBuilder()
////                    .add("status", "success")
////                    .add("message", "Utilisateur créé avec succès")
////                    .build();
////
////            response.getWriter().write(jsonResponse.toString());
//        } catch (Exception e) {
//            e.printStackTrace();  // Ajout pour voir l'erreur en console
//            try {
//                JsonObject jsonResponse = Json.createObjectBuilder()
//                        .add("status", "error")
//                        .add("message", "error")
//                        .build();
//
//                response.getWriter().write(jsonResponse.toString());
//                response.getWriter().flush();
//                response.getWriter().close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }
//
////        if (username == null || email == null || password1 == null || password2 == null) {
////            System.out.println("username and email are null");
////        }
////
////        User user = new User(username, email, password1, password2);
//
//
//    }

    /**
     * Envoye un message d'erreur en JSON pour l'application web.
     * @param response
     * @param message
     * @throws IOException
     */
    private void MsgErrorToJSON(HttpServletResponse response, String message) throws IOException {
        try {
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("status", "error")
                    .add("message", message)
                    .build();

            response.getWriter().write(jsonResponse.toString());
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            throw new IOException("Erreur dans la méthode MsgErrorToJSON : " + e.getMessage());
        }
    }
}
