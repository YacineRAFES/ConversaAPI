package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.*;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
import fr.afpa.dev.pompey.conversaapi.service.SignalementsService;
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

import static fr.afpa.dev.pompey.conversaapi.securite.JWTutils.VerificationJWT;

@Slf4j
@WebServlet("/MessagesPrivee")
public class MessagesPriveeServlet extends HttpServlet {
    private transient MessagesPriveeService messagesPriveeService;
    private transient UserService userService;
    private transient AmisService amisService;
    private transient SignalementsService signalementsService;

    @Override
    public void init() {
        this.messagesPriveeService = new MessagesPriveeService(Role.UTILISATEUR);
        this.userService = new UserService(Role.UTILISATEUR);
        this.amisService = new AmisService(Role.UTILISATEUR);
        this.signalementsService = new SignalementsService(Role.UTILISATEUR);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());
            String type = jsonObject.getString("type");
            User user = userService.get(VerificationJWT(jsonObject.getString("jwt")).getId());

            //Verifie si l'utilisateur appartient à un groupe de message privées

            if (user == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, "userNotFound");
                return;
            }

            if (type.equals("getAllMessages")) {
                //Recuperation de tous les messages privés
                log.info("Appel : getAllMessages");
                List<MessagesPrivee> messagesPrivee = messagesPriveeService.getAllMessagesPriveeByIdUser(user.getId());

                if (!messagesPrivee.isEmpty()) {
                    log.info("Messages privés trouvés : " + messagesPrivee.size());
                    //Envoi des messages privés en JSON
                    JsonArrayBuilder messagesPriveeBuilder = Json.createArrayBuilder();

                    for (MessagesPrivee messagesPrivee1 : messagesPrivee) {
                        messagesPriveeBuilder.add(Json.createObjectBuilder()
                                .add("groupe_messages_prives", Json.createObjectBuilder()
                                        .add("id_groupe_messages_prives", messagesPrivee1.getIdGroupeMessagesPrives())
                                        .add("message_prive", Json.createObjectBuilder()
                                                .add("message", messagesPrivee1.getMessage())
                                                .add("id_message_prive", messagesPrivee1.getId())
                                                .add("date", messagesPrivee1.getDate().toString())
                                                .add("user", Json.createObjectBuilder()
                                                        .add("id_user", messagesPrivee1.getUser().getId())
                                                        .add("username", messagesPrivee1.getUser().getName())
                                                )
                                        )
                                )
                        );
                    }

                    SendJSON.SuccessWithJsonArrayBuilder(response, "getAllMessages", "getAllMsg", messagesPriveeBuilder);
                } else {
                    log.error("Aucun message trouvé");
                    SendJSON.Success(response, "aucunMessageTrouve");
                }

            } else if (type.equals("sendMessages")) {
                log.info("Appel : sendMessages");

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

                if (!user.getId().equals(idUserRecup) && !user.getName().equals(usernameRecup)) {
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

                //Recuperation de tous les messages privés
                log.info("Appel : getAllMessages");
                List<MessagesPrivee> getAllMessagesPrivee = messagesPriveeService.getAllMessagesPriveeByIdUser(user.getId());

                if (!getAllMessagesPrivee.isEmpty()) {
                    log.info("Messages privés trouvés : " + getAllMessagesPrivee.size());
                    //Envoi des messages privés en JSON
                    JsonArrayBuilder messagesPriveeBuilder = Json.createArrayBuilder();

                    for (MessagesPrivee messagesPrivee1 : getAllMessagesPrivee) {
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

                }else {
                    log.error("Aucun message trouvé");
                    SendJSON.Success(response, "aucunMessageTrouve");
                }
            }else if(type.equals("signaler")){

                log.info("Appel : signaler");
                Integer idMessagePriveeRecup = jsonObject.getInt("idMessage");

                //Verifie si idMessagePriveeRecup et messageRecup ne sont pas vide
                if (idMessagePriveeRecup == null) {
                    log.error("Un des champs est vide");
                    SendJSON.Error(response, "idUserVide");
                    return;
                }

                //Verifie si idMessagePriveeRecup existe
                MessagesPrivee mpExist = messagesPriveeService.findById(idMessagePriveeRecup);

                if(mpExist != null){
                    log.info("Message privé trouvé avec l'ID : " + mpExist.getId());

                    //Création d'une instanciation de l'objet message privé

                    Signalements signalements = new Signalements(
                            mpExist,
                            user
                    );

                    //Ajout du message privé au signalement
                    boolean confirmation = signalementsService.add(signalements);

                    if(confirmation){
                        log.info("Signalement d'un message confirmé " + mpExist.getId());
                        SendJSON.Success(response, "messageSignaler");
                    }else{
                        log.error("Erreur lors de l'ajout du signalement " + mpExist.getId());
                        SendJSON.Error(response, "erreurAjoutSignalement");
                    }

                }


            }else if(type.equals("supprimer")){
                log.info("Appel : supprimer");
                Integer idMessagePriveeRecup = jsonObject.getInt("idMessage");

                //Verifie si idMessagePriveeRecup ne sont pas vide
                if (idMessagePriveeRecup == null) {
                    log.error("Un des champs est vide");
                    SendJSON.Error(response, "idUserVide");
                    return;
                }

                //Verifie si idMessagePriveeRecup existe
                MessagesPrivee mpExist = messagesPriveeService.findById(idMessagePriveeRecup);

                if(mpExist != null){
                    log.info("Message privé trouvé avec l'ID : " + mpExist.getId());

                    //Création d'une instanciation de l'objet message privé
                    MessagesPrivee messagesPrivee = new MessagesPrivee(
                            mpExist.getId()
                    );

                    //Ajout du message privé au signalement
                    MessagesPrivee mp = messagesPriveeService.supprimer(messagesPrivee);
                    log.info("Message privé supprimé avec l'ID : " + mp.getId());

                    SendJSON.Success(response, "messageSupprimer");
                }
            }else {
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