package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
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

import static fr.afpa.dev.pompey.conversaapi.securite.JWTutils.VerificationJWT;

@Slf4j
@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
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

            String action = jsonObject.getString("action");
            if (action.equals("getAllSignalements")) {
                log.info("Récupération de tous les signalements");

                List<Signalements> signalementsList = signalementsService.getAll();
                JsonArrayBuilder signalementsBuilder = Json.createArrayBuilder();

                for (Signalements signalement : signalementsList) {
                    JsonObjectBuilder signalementJson = Json.createObjectBuilder()
                            .add("messageId", signalement.getMessagesPrivee().getId());
                    signalementsBuilder.add(signalementJson);
                }

                JsonObject userJson = Json.createObjectBuilder()
                        .add("userId", user.getId())
                        .add("userName", user.getName())
                        .add("userEmail", user.getEmail())
                        .add("userRole", user.getRole())
                        .build();


                JsonObject globalJson = Json.createObjectBuilder()
                        .add("signalements", signalementsBuilder)
                        .add("user", userJson)
                        .build();

                SendJSON.GlobalJSON(response, globalJson);

            }else if(action.equals("getSignalement")) {
                Integer messageId = Integer.valueOf(jsonObject.getString("messageId"));
                Signalements signalements = signalementsService.getByIdMP(messageId);

                JsonObject signalementsBuilder = Json.createObjectBuilder()
                        .add("messageId", signalements.getMessagesPrivee().getId())
                        .add("messageTexte", signalements.getMessagesPrivee().getMessage())
                        .add("messageDate", signalements.getMessagesPrivee().getDate().toString())
                        .add("messageGroupeId", signalements.getMessagesPrivee().getIdGroupeMessagesPrives())
                        .add("emetteurId", signalements.getMessagesPrivee().getUser().getId())
                        .add("emetteurNom", signalements.getMessagesPrivee().getUser().getName())
                        .add("emetteurDateInscription", signalements.getMessagesPrivee().getUser().getDate().toString())
                        .add("emetteurEmail", signalements.getMessagesPrivee().getUser().getEmail())
                        .add("emetteurRole", signalements.getMessagesPrivee().getUser().getRole())
                        .add("utilisateurIdSignale", signalements.getUser().getId())
                        .add("utilisateurNomSignale", signalements.getUser().getName())
                        .add("dateSignalement", signalements.getDate().toString())
                        .add("raison", signalements.getRaison())
                        .build();

                SendJSON.SuccessWithObject(response, "signalementOK", "signalements", signalementsBuilder);
            }

        }catch (Exception e) {
            log.error("Erreur dans la récupération de l'utilisateur: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

    }
}