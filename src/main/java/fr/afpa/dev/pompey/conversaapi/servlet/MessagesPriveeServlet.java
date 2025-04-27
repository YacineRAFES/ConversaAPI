//package fr.afpa.dev.pompey.conversaapi.servlet;
//
//import fr.afpa.dev.pompey.conversaapi.emuns.Role;
//import fr.afpa.dev.pompey.conversaapi.modele.Amis;
//import fr.afpa.dev.pompey.conversaapi.modele.MessagesPrivee;
//import fr.afpa.dev.pompey.conversaapi.modele.User;
//import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
//import fr.afpa.dev.pompey.conversaapi.service.AmisService;
//import fr.afpa.dev.pompey.conversaapi.service.MessagesPriveeService;
//import fr.afpa.dev.pompey.conversaapi.service.UserService;
//import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
//import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
//import io.jsonwebtoken.Claims;
//import jakarta.json.Json;
//import jakarta.json.JsonObject;
//import jakarta.json.JsonReader;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.util.List;
//
//@Slf4j
//@WebServlet("/MessagesPrivee")
//public class MessagesPriveeServlet extends HttpServlet {
//    private transient MessagesPriveeService messagesPriveeService;
//    private transient UserService userService;
//    private transient AmisService amisService;
//    @Override
//    public void init() {
//        this.messagesPriveeService = new MessagesPriveeService(Role.UTILISATEUR);
//        this.userService = new UserService(Role.UTILISATEUR);
//        this.amisService = new AmisService(Role.UTILISATEUR);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            JsonReader jsonReader = Json.createReader(request.getInputStream());
//            JsonObject jsonObject = jsonReader.readObject();
//            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());
//            String jwt = jsonObject.getString("jwt");
//            int idGroupeMessagesPrivee = Integer.parseInt(jsonObject.getString("idGroupeMessagesPrivee"));
//            String type = jsonObject.getString("type");
//            //Verification JWT
//            if (jwt != null) {
//                if (JWTutils.validateToken(jwt)) {
//                    log.info("JWT valide");
//                } else {
//                    log.error("JWT invalide");
//                    SendJSON.Error(response, "jwtInvalide");
//                    return;
//                }
//            } else {
//                log.error("JWT vide");
//                SendJSON.Error(response, "jwtInvalide");
//                return;
//            }
//
//            //Recuperation de l'ID de l'utilisateur
//            Claims claims = JWTutils.getUserInfoFromToken(jwt);
//            User user;
//            if (claims == null) {
//                log.error("Token claims null");
//                SendJSON.Error(response, "invalidToken");
//                return;
//            }
//
//            Integer id = claims.get("id", Integer.class);
//            String email = claims.get("email", String.class);
//            String username = claims.get("name", String.class);
//            String role = claims.get("roles", String.class);
//            if (id == null || email == null || username == null || role == null) {
//                log.error("Données manquantes dans le token");
//                SendJSON.Error(response, "missingDataInToken");
//                return;
//            }
//            user = userService.get(id);
//            if (user == null) {
//                log.error("Utilisateur non trouvé");
//                SendJSON.Error(response, "userNotFound");
//                return;
//            }
//
//            if(type.equals("getAllMessages")) {
//                //Recuperation de tous les messages privés
//                List<MessagesPrivee> messagesPrivee = messagesPriveeService.getAllMessagesPriveeByIdUser(user.getId());
//                if (messagesPrivee == null) {
//                    log.error("Aucun message trouvé");
//                    SendJSON.Error(response, "noMessageFound");
//                    return;
//                }
//                //Envoi des messages privés en JSON
//                SendJSON.MessagesPrivee(response, messagesPrivee);
//                return;
//            }
//
//            //Recupereration de l'ID du groupe de messages privés dans la bdd Amis
//            Amis GroupeMessagesPrivee = amisService.getIdGroupeMessagesPrivee(idGroupeMessagesPrivee);
//            if (GroupeMessagesPrivee == null) {
//                log.error("Groupe de messages privés non trouvé");
//                SendJSON.Error(response, "groupeMessagesPriveeNotFound");
//                return;
//            }
//
//
//            //Recupere le message et add le message dans la bdd
//            MessagesPrivee messagesPrivee = new MessagesPrivee(
//
//            );
//
//
//
//
//
//
//
//
//        }catch (Exception e) {
//
//        }
//    }
//
//}