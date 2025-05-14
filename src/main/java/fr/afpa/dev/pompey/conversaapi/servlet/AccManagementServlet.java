package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
import fr.afpa.dev.pompey.conversaapi.service.SignalementsService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import jakarta.json.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static fr.afpa.dev.pompey.conversaapi.utilitaires.AlertMsg.ERRORUSERNOTEXIST;
import static fr.afpa.dev.pompey.conversaapi.utilitaires.AlertMsg.INTERNALSERVERERROR;

@Slf4j
@WebServlet(name = "AccManagementServlet", value = "/accManagement")
public class AccManagementServlet extends HttpServlet {
    private transient UserService userService;
    private transient MessagesPriveeService messagesPriveeService;
    private transient SignalementsService signalementsService;

    @Override
    public void init() {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());

            String jwt = jsonObject.getString("jwt");

            this.userService = new UserService(Role.UTILISATEUR);

            User user;

            if (jwt != null) {
                user = userService.get(JWTutils.VerificationJWT(jwt).getId());
                if (user != null) {
                    log.info("JWT valide");
                } else {
                    log.error("JWT invalide");
                    SendJSON.Error(response, "jwtInvalide");
                    return;
                }
            } else {
                log.error("JWT vide");
                SendJSON.Error(response, "jwtInvalide");
                return;
            }

            if(user.getRole().equals("admin")) {
                log.info("Admin logged in");
            }else{
                log.error("User is not admin");
                SendJSON.Error(response, "NotAdmin");
                return;
            }

            this.signalementsService = new SignalementsService(Role.SUPERADMIN);
            this.messagesPriveeService = new MessagesPriveeService(Role.SUPERADMIN);
            this.userService = new UserService(Role.SUPERADMIN);

            log.info("Clés disponibles dans le JSON : " + jsonObject.keySet());
            String action = jsonObject.getString("action");
            if (action.equals("getAllUser")) {
                //Récupère tous les utilisateurs
                log.info("Récupération de tous les utilisateurs");
                List<User> utilisateursList = userService.getAllOnlyUserAndModo();

                JsonArrayBuilder utilisateursBuilder = Json.createArrayBuilder();

                for (User utilisateur : utilisateursList) {
                    JsonObjectBuilder utilisateurJson = Json.createObjectBuilder()
                            .add("userId", utilisateur.getId())
                            .add("userName", utilisateur.getName());
                    utilisateursBuilder.add(utilisateurJson);
                }

                JsonObject userJson = Json.createObjectBuilder()
                        .add("userId", user.getId())
                        .add("userName", user.getName())
                        .add("userEmail", user.getEmail())
                        .add("userRole", user.getRole())
                        .build();

                JsonObject globalJson = Json.createObjectBuilder()
                        .add("users", utilisateursBuilder)
                        .add("user", userJson)
                        .build();

                SendJSON.GlobalJSON(response, globalJson);

            }else if(action.equals("getUser")) {
                //Récupère les informations grâce un IdUser
                log.info("getUser enclenchée.");

                int idUser = Integer.parseInt(jsonObject.getString("userId"));
                User getuser = new User(
                        idUser
                );
                User getUser = userService.getIdUser(getuser);

                //Je vérifie si l'utilisateur existe dans la BDD
                if(!verifyIfUserExist(response, idUser)){
                    SendJSON.Error(response, ERRORUSERNOTEXIST);
                    return;
                }

                //Récupère tous les utilisateurs
                log.info("Récupération de tous les utilisateurs");
                List<User> utilisateursList = userService.getAllOnlyUserAndModo();
                JsonArrayBuilder utilisateursBuilder = Json.createArrayBuilder();
                for (User utilisateur : utilisateursList) {
                    JsonObjectBuilder utilisateurJson = Json.createObjectBuilder()
                            .add("userId", utilisateur.getId())
                            .add("userName", utilisateur.getName());
                    utilisateursBuilder.add(utilisateurJson);
                }

                //Récupère l'utilisateur grâce à IdUser
                log.info("Récupération de l'utilisateur grâce à IdUser");
                JsonObject getUserBuilder = Json.createObjectBuilder()
                        .add("userId", getUser.getId())
                        .add("userName", getUser.getName())
                        .add("userDate", getUser.getDate().toString())
                        .add("userEmail", getUser.getEmail())
                        .add("userRole", getUser.getRole())
                        .add("userIsValid", getUser.isValide())
                        .build();

                JsonObject globalJson = Json.createObjectBuilder()
                        .add("getAllUser", utilisateursBuilder)
                        .add("getUser", getUserBuilder)
                        .build();

                SendJSON.SuccessWithObject(response, "getUser", "usr", globalJson);

            }else if(action.equals("modifUser")) {
                //Modification de l'utilisateur
                log.info("modifUser enclenchée");

                int IdUser = Integer.parseInt(jsonObject.getString("userId"));

                //Je vérifie si l'utilisateur existe dans la BDD
                if(!verifyIfUserExist(response, IdUser)){
                    SendJSON.Error(response, ERRORUSERNOTEXIST);
                    return;
                }

                String email = jsonObject.getString("userEmail");
                String roles = jsonObject.getString("userRole");
                String nom = jsonObject.getString("userName");
                if(email == null && nom == null && roles == null) {
                    SendJSON.Error(response, "Champs vide");
                    return;
                }

                User userUpdate = new User(
                        IdUser,
                        nom,
                        email,
                        roles,
                        jsonObject.getBoolean("userIsValid")
                );

                boolean confirmation = userService.modifyByAdmin(userUpdate);

                if (confirmation) {
                    User getUser = userService.getIdUser(userUpdate);
                    //Récupère tous les utilisateurs
                    log.info("Récupération de tous les utilisateurs");
                    List<User> utilisateursList = userService.getAllOnlyUserAndModo();
                    JsonArrayBuilder utilisateursBuilder = Json.createArrayBuilder();
                    for (User utilisateur : utilisateursList) {
                        JsonObjectBuilder utilisateurJson = Json.createObjectBuilder()
                                .add("userId", utilisateur.getId())
                                .add("userName", utilisateur.getName());
                        utilisateursBuilder.add(utilisateurJson);
                    }

                    //Récupère l'utilisateur grâce à IdUser
                    log.info("Récupération de l'utilisateur grâce à IdUser");
                    JsonObject getUserBuilder = Json.createObjectBuilder()
                            .add("userId", getUser.getId())
                            .add("userName", getUser.getName())
                            .add("userDate", getUser.getDate().toString())
                            .add("userEmail", getUser.getEmail())
                            .add("userRole", getUser.getRole())
                            .add("userIsValid", getUser.isValide())
                            .build();

                    JsonObject globalJson = Json.createObjectBuilder()
                            .add("getAllUser", utilisateursBuilder)
                            .add("getUser", getUserBuilder)
                            .build();

                    SendJSON.SuccessWithObject(response, "userModified", "usr", globalJson);
                }else{
                    SendJSON.Error(response, INTERNALSERVERERROR);
                }
            }else if(action.equals("sendAskResetPassword")) {
                //TODO: A faire pour envoyer un mail de réinialisation de mdp

                //Modification de l'utilisateur
                log.info("sendAskResetPassword enclenchée");

                int IdUser = Integer.parseInt(jsonObject.getString("userId"));

                //Je vérifie si l'utilisateur existe dans la BDD
                if(!verifyIfUserExist(response, IdUser)){
                    return;
                }

                User userFind = userService.get(IdUser);

//                boolean confirmation = sendEmail();

//                if (confirmation) {
//                    SendJSON.Success(response, "userDeleted");
//                }else{
//                    SendJSON.Error(response, INTERNALSERVERERROR);
//                }
            } else if (action.equals("deleteAccount")) {
                //Supprimer un compte utilisateur
                log.info("deleteAccount enclenchée");

                int IdUser = Integer.parseInt(jsonObject.getString("userId"));

                //Je vérifie si l'utilisateur existe dans la BDD
                if(!verifyIfUserExist(response, IdUser)){
                    SendJSON.Error(response, ERRORUSERNOTEXIST);
                    return;
                }

                log.info("deleteaccount : " + IdUser);

                User userDelete = new User(
                        IdUser
                );

                boolean confirmation = userService.delete(userDelete);

                if (confirmation) {
                    //Récupère tous les utilisateurs
                    log.info("Récupération de tous les utilisateurs");
                    List<User> utilisateursList = userService.getAllOnlyUserAndModo();
                    JsonArrayBuilder utilisateursBuilder = Json.createArrayBuilder();
                    for (User utilisateur : utilisateursList) {
                        JsonObjectBuilder utilisateurJson = Json.createObjectBuilder()
                                .add("userId", utilisateur.getId())
                                .add("userName", utilisateur.getName());
                        utilisateursBuilder.add(utilisateurJson);
                    }

                    JsonObject globalJson = Json.createObjectBuilder()
                            .add("getAllUser", utilisateursBuilder)
                            .build();

                    SendJSON.SuccessWithObject(response, "userDeleted", "usr", globalJson);
                }

            } else{
                SendJSON.Error(response, "actionNotFound");
            }
        }catch (Exception e) {
            log.error("Erreur : " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    public void destroy() {

    }

    private boolean verifyIfUserExist(HttpServletResponse response, Integer IdUser){
        User userVerified = userService.get(IdUser);
        if(userVerified == null){
            SendJSON.Error(response, "userIsNull");
            return false;
        }
        return true;
    }
}