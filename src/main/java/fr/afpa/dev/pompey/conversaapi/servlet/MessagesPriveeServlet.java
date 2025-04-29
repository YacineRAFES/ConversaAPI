package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
import fr.afpa.dev.pompey.conversaapi.modele.StatutAmitie;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import io.jsonwebtoken.Claims;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@WebServlet("/MessagesPrivee")
public class MessagesPriveeServlet extends HttpServlet {
    private transient MessagesPriveeService messagesPriveeService;
    private transient UserService userService;
    private transient AmisService amisService;

    @Override
    public void init() {
        this.messagesPriveeService = new MessagesPriveeService(Role.UTILISATEUR);
        this.userService = new UserService(Role.UTILISATEUR);
        this.amisService = new AmisService(Role.UTILISATEUR);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());
            String jwt = jsonObject.getString("jwt");
            String type = jsonObject.getString("type");
            log.info(Utils.getNameClass() + " type : " + type);
            log.info(Utils.getNameClass() + " jwt : " + jwt);

            //Verification JWT
            if (jwt != null) {
                if (JWTutils.validateToken(jwt)) {
                    log.info(Utils.getNameClass() + "JWT valide");
                } else {
                    log.error(Utils.getNameClass() + "JWT invalide");
                    SendJSON.Error(response, "jwtInvalide");
                    return;
                }
            } else {
                log.error(Utils.getNameClass() + "JWT vide");
                SendJSON.Error(response, "jwtInvalide");
                return;
            }



            //Recuperation de l'ID de l'utilisateur
            Claims claims = JWTutils.getUserInfoFromToken(jwt);
            User user;
            if (claims == null) {
                log.error("Token claims null");
                SendJSON.Error(response, "invalidToken");
                return;
            }

            Integer id = claims.get("id", Integer.class);
            String email = claims.get("email", String.class);
            String username = claims.get("name", String.class);
            String role = claims.get("roles", String.class);
            if (id == null || email == null || username == null || role == null) {
                log.error("Données manquantes dans le token");
                SendJSON.Error(response, "missingDataInToken");
                return;
            }
            user = userService.get(id);
            if (user == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, "userNotFound");
                return;
            }

            if (type.equals("getAllMessages")) {
                //Recuperation de tous les messages privés
                log.info("1. Recuperation de tous les messages privés");
                List<MessagesPrivee> messagesPrivee = messagesPriveeService.getAllMessagesPriveeByIdUser(user.getId());
                //Envoi des messages privés en JSON
                JsonArrayBuilder messagesPriveeBuilder = Json.createArrayBuilder();

                for (MessagesPrivee messagesPrivee1 : messagesPrivee) {
                    messagesPriveeBuilder.add(Json.createObjectBuilder()
                            .add("id", messagesPrivee1.getId())
                            .add("message", messagesPrivee1.getMessage())
                            .add("idGroupeMessagesPrives", messagesPrivee1.getIdGroupeMessagesPrives())
                            .add("date", messagesPrivee1.getDate().toString())
                            .add("user", Json.createObjectBuilder()
                                    .add("id", messagesPrivee1.getUser().getId())
                                    .add("username", messagesPrivee1.getUser().getName())
                            )
                    );
                }

                JsonObject globalJson = Json.createObjectBuilder()
                        .add("getAllMessages", messagesPriveeBuilder)
                        .build();

                SendJSON.GlobalJSON(response, globalJson);
                return;
            } else if (type.equals("sendMessages")) {
                log.info("2. Envoi d'un message privé");

                Integer idGroupeMessagesPriveeRecup = jsonObject.getInt("idGroupeMessagesPrivee");
                String messageRecup = jsonObject.getString("message");
                Integer idUserRecup = Integer.valueOf(jsonObject.getString("iduser"));
                String usernameRecup = jsonObject.getString("username");

                //Verifie si username et iduser ne sont pas vide
                if (idUserRecup == null ||
                        usernameRecup == null || usernameRecup.isEmpty() ||
                        idGroupeMessagesPriveeRecup == null ||
                        messageRecup == null || messageRecup.isEmpty()) {
                    log.error("Un des champs est vide");
                    SendJSON.Error(response, "idUserVide");
                    return;
                }

                if (!id.equals(idUserRecup) && !username.equals(usernameRecup)) {
                    log.error("L'utilisateur ne peut pas s'envoyer un message");
                    SendJSON.Error(response, "userCannotSendMessageToSelf");
                    return;
                }

                //Si user appartient un groupe de message privées
                Amis userAppartientUnGroupe = new Amis(user.getId(), idGroupeMessagesPriveeRecup, StatutAmitie.AMI);
                if (!amisService.siIdUserAppartientAUnGroupe(userAppartientUnGroupe)) {
                    log.error("L'utilisateur n'appartient pas à un groupe de messages privés");
                    SendJSON.Error(response, "userNotInGroup");
                    return;
                }

                //Création du message privé
                MessagesPrivee messagesPrivee = new MessagesPrivee(
                        messageRecup,
                        user,
                        idGroupeMessagesPriveeRecup
                );

                //Ajout du message privé
                int idMessagePrivee = messagesPriveeService.add(messagesPrivee);
                log.info("Message privé ajouté avec l'ID : " + idMessagePrivee);

            } else {
                log.error("Type de message non reconnu");
                SendJSON.Error(response, "typeMessageInconnu");
                return;
            }

        } catch (Exception e) {
            log.error("Erreur dans MessagesPriveeServlet : ", e);
            SendJSON.Error(response, "internalError");
        }
    }

}