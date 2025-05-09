package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.Amis;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.AmisService;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import io.jsonwebtoken.Claims;
import jakarta.json.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.IOException;
import java.util.List;

import static fr.afpa.dev.pompey.conversaapi.securite.JWTutils.VerificationJWT;
import static fr.afpa.dev.pompey.conversaapi.utilitaires.AlertMsg.ERRORSERVER;

@Slf4j
@WebServlet(name = "AmisServlet", value = "/amis")
public class AmisServlet extends HttpServlet {
    private transient AmisService amisService;
    private transient UserService userService;

    @Override
    public void init() {
        this.amisService = new AmisService(Role.UTILISATEUR);
        this.userService = new UserService(Role.UTILISATEUR);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());

            User user = userService.get(VerificationJWT(jsonObject.getString("jwt")).getId());

            //Verifie si l'utilisateur appartient à un groupe de message privées

            if (user == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, "userNotFound");
                return;
            }

            if (jsonObject.containsKey("method")) {
                String method = jsonObject.getString("method");
                JsonObject objects = jsonObject.getJsonObject("objects");

                if (method.equals("GetListFriends")) {
                    if (user == null) {
                        log.error("L'utilisateur n'existe pas");
                        SendJSON.Error(response, "userNotFound");
                        return;
                    } else {
                        //Récupere la liste des amis et les mettre en jsonarray
                        log.info("L'utilisateur existe");
                        List<Amis> amisList = amisService.findById(user.getId());
                        List<Amis> amiRequest = amisService.findAllFriendsRequestById(user.getId());

                        JsonArrayBuilder amisBuilder = Json.createArrayBuilder();

                        for (Amis ami : amisList) {
                            amisBuilder.add(Json.createObjectBuilder()
                                    .add("idGroupeMessagesPrives", ami.getIdGroupeMessagesPrives())
                                    .add("statut", ami.getStatut().toString())
                                    .add("dateDemande", ami.getDateDemande().toString())
                                    .add("userIdAmiDe", ami.getUserIdAmiDe())
                                    .add("userIdDemandeur", ami.getUserIdDemandeur())
                                    .add("username", ami.getUser().getName())
                                    .add("userId", ami.getUser().getId()));
                        }

                        JsonArrayBuilder demandesBuilder = Json.createArrayBuilder();

                        for (Amis ami : amiRequest) {
                            demandesBuilder.add(Json.createObjectBuilder()
                                    .add("idGroupeMessagesPrives", ami.getIdGroupeMessagesPrives())
                                    .add("statut", ami.getStatut().toString())
                                    .add("dateDemande", ami.getDateDemande().toString())
                                    .add("userIdAmiDe", ami.getUserIdAmiDe())
                                    .add("userIdDemandeur", ami.getUserIdDemandeur())
                                    .add("username", ami.getUser().getName())
                                    .add("userId", ami.getUser().getId()));
                        }
                        JsonObject globalJson = Json.createObjectBuilder()
                                .add("amis", amisBuilder)
                                .add("demandes", demandesBuilder)
                                .build();

                        SendJSON.GlobalJSON(response, globalJson);
                    }

                } else if (method.equals("SearchAndAddFriends")) {
                    if (objects.containsKey("type")) {
                        String type = objects.getString("type");
                        String action = objects.getString("action");
                        if (type.equals("friendSearchForm")) {
                            if (action.equals("search")) {
                                //Recherche d'amis dans la liste de l'utilisateur
                                //Je récupère l'id de l'utilisateur puis je cherche le nom dans sa liste des amis qui correspond à la demande de l'utilisateur
                                String nomRechercher = objects.getString("username", "");
                                List<Amis> amisRechercher = amisService.TrouverUnAmis(nomRechercher, user.getId());
                                //Puis j'envoye à l'utilisateur la liste des amis qui correspondent à la recherche

                                JsonArrayBuilder amisRechercherBuilder = Json.createArrayBuilder();

                                for (Amis ami : amisRechercher) {
                                    amisRechercherBuilder.add(Json.createObjectBuilder()
                                            .add("idGroupeMessagesPrives", ami.getIdGroupeMessagesPrives())
                                            .add("statut", ami.getStatut().toString())
                                            .add("dateDemande", ami.getDateDemande().toString())
                                            .add("userIdAmiDe", ami.getUserIdAmiDe())
                                            .add("userIdDemandeur", ami.getUserIdDemandeur())
                                            .add("username", ami.getUser().getName())
                                            .add("userId", ami.getUser().getId()));
                                }

                                JsonObject jsonRechercher = Json.createObjectBuilder()
                                        .add("amisRechercher", amisRechercherBuilder)
                                        .build();
                                log.info(jsonRechercher.toString());
                                SendJSON.GlobalJSON(response, jsonRechercher);

                            } else if (action.equals("add")) {
                                log.info("friendSearchForm: " + objects);
                                // Ajout d'amis
                                String usernamePourRechercherId = objects.getString("username", "");

                                //Jecherche l'id de l'utilisateur qui correspond au nom d'utilisateur

                                User userRechercherID = userService.findByName(usernamePourRechercherId);
                                log.info("ID de l'utilisateur recherché: " + userRechercherID.getId());
                                if (userRechercherID == null) {
                                    log.error("L'utilisateur n'existe pas");
                                    SendJSON.Error(response, "userNotFound");
                                    return;
                                }

                                Amis demanderEnAmis = new Amis(user.getId(), userRechercherID.getId());
                                boolean isAdd = false;
                                isAdd = amisService.add(demanderEnAmis);
                                log.info("Demande d'amis envoyée: " + isAdd);
                                if (isAdd) {
                                    //Je renvoie un message de succès
                                    SendJSON.Success(response, "AskFriendRequestSend");
                                } else {
                                    //Je renvoie un message d'erreur
                                    SendJSON.Error(response, "Error");
                                }

                            } else {
                                //Annoncer l'erreur
                                log.error("friendSearchForm: Erreur dans la requête");
                                SendJSON.Error(response, "Error");
                            }
                        } else if (type.equals("friendRequestResponse")) {
                            log.info("friendRequestResponse: " + objects);
                            Integer idGroupeMessagesPrivee = Integer.valueOf(objects.getString("idGroupeMessagesPrivee", ""));
                            //Verifie si idGroupeMessagesPrivee existe dans la table Amis
                            Amis amisFindGroupeMP = amisService.getIdGroupeMessagesPrivee(idGroupeMessagesPrivee);

                            if (action.equals("yes")) {
                                log.info("friendRequestResponse.yes: " + objects);
                                Amis amis = new Amis(amisFindGroupeMP.getIdGroupeMessagesPrives(), amisFindGroupeMP.getUserIdDemandeur(), user.getId());
                                boolean isAccept = false;
                                isAccept = amisService.update(amis);

                                //Je renvoie un message de succès
                                if (isAccept) {
                                    SendJSON.Success(response, "AcceptFriendRequest");
                                } else {
                                    SendJSON.Error(response, "ErrorOfAcceptFriendRequest");
                                }
                            } else if (action.equals("no")) {
                                log.info("friendRequestResponse.no: " + objects);
                                Amis amis = new Amis(amisFindGroupeMP.getIdGroupeMessagesPrives(), amisFindGroupeMP.getUserIdDemandeur(), user.getId());
                                boolean isRefus = false;
                                isRefus = amisService.delete(amis);

                                //Je renvoie un message de succès
                                if (isRefus) {
                                    SendJSON.Success(response, "RefuseFriendRequest");
                                } else {
                                    SendJSON.Error(response, "ErrorOfRefuseFriendRequest");
                                }
                            } else {
                                SendJSON.Error(response, ERRORSERVER);
                            }
                        } else {
                            SendJSON.Error(response, ERRORSERVER);
                        }
                    } else {
                        SendJSON.Error(response, ERRORSERVER);
                    }
                }else if(method.equals("getAllAmis")){
                    List<Amis> amisList = amisService.findById(user.getId());

                    JsonArrayBuilder amisBuilder = Json.createArrayBuilder();

                    for (Amis ami : amisList) {
                        amisBuilder.add(Json.createObjectBuilder()
                                .add("idGroupeMessagesPrives", ami.getIdGroupeMessagesPrives())
                                .add("statut", ami.getStatut().toString())
                                .add("username", ami.getUser().getName())
                                .add("userId", ami.getUser().getId()));
                    }

                    JsonObject globalJson = Json.createObjectBuilder()
                            .add("getAllAmis", amisBuilder)
                            .build();

                    SendJSON.GlobalJSON(response, globalJson);
                }
            }
        } catch (Exception e) {
            log.error("doPost: Erreur dans la requête: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Erreur dans la requête");
        }

    }

    @Override
    public void destroy() {

    }
}