package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.Signalements;
import fr.afpa.dev.pompey.conversaapi.modele.User;
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
            this.userService = new UserService(Role.UTILISATEUR);
            User user = userService.get(VerificationJWT(jsonObject.getString("jwt")).getId());
            String action = jsonObject.getString("action");

            if(user.getRole().equals("admin")) {
                log.info("Admin logged in");
            }else{
                log.error("User is not admin");
                SendJSON.Error(response, "NotAdmin");
                return;
            }

            this.signalementsService = new SignalementsService(Role.SUPERADMIN);
            if (action.equals("getAllSignalements")) {
                log.info("Récupération de tous les signalements");

                List<Signalements> signalementsList = signalementsService.getAll();
                JsonArrayBuilder signalementsBuilder = Json.createArrayBuilder();

                for (Signalements signalement : signalementsList) {
                    JsonObjectBuilder signalementJson = Json.createObjectBuilder()
                            .add("messageId", signalement.getMessagesPrivee().getId())
                            .add("messageTexte", signalement.getMessagesPrivee().getMessage())
                            .add("messageDate", signalement.getMessagesPrivee().getDate().toString())
                            .add("messageGroupeId", signalement.getMessagesPrivee().getIdGroupeMessagesPrives())
                            .add("emetteurId", signalement.getMessagesPrivee().getUser().getId())
                            .add("emetteurNom", signalement.getMessagesPrivee().getUser().getName())
                            .add("emetteurDateInscription", signalement.getMessagesPrivee().getUser().getDate().toString())
                            .add("emetteurEmail", signalement.getMessagesPrivee().getUser().getEmail())
                            .add("emetteurRole", signalement.getMessagesPrivee().getUser().getRole())
                            .add("utilisateurSignaleId", signalement.getUser().getId())
                            .add("dateSignalement", signalement.getDate().toString())
                            .add("raison", signalement.getRaison());

                    signalementsBuilder.add(signalementJson);
                }

                JsonObject globalJson = Json.createObjectBuilder()
                        .add("signalements", signalementsBuilder)
                        .build();

                SendJSON.GlobalJSON(response, globalJson);
            }

        }catch (Exception e) {
            log.error("Erreur dans la récupération de l'utilisateur: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

    }

    @Override
    public void destroy() {

    }
}