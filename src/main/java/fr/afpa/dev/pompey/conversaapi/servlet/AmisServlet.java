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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());

            String jwt = jsonObject.getString("jwt", "");

            // Vérification du JWT
            if (JWTutils.validateToken(jwt)) {
                log.info("JWT valide");
            } else {
                log.error("JWT invalide");
                SendJSON.Error(response, "jwtInvalide");
                return;
            }

            Claims claims = JWTutils.getUserInfoFromToken(jwt);
            User user;
            if (claims != null) {
                Integer id = Integer.valueOf(claims.get("id", String.class));
                log.info("Après le claims, ID USER : " + id);
                String email = claims.get("email", String.class);
                log.info("Après le claims, EMAIL USER : " + email);
                String username = claims.get("name", String.class);
                log.info("Après le claims, USERNAME USER : " + username);
                String role = claims.get("roles", String.class);
                log.info("Après le claims, ROLE USER : " + role);

                //Verifie si l'utilisateur existe
                user = userService.get(id);
                if (user == null) {
                    log.error("L'utilisateur n'existe pas");
                    SendJSON.Error(response, "userNotFound");
                    return;
                } else {
                    //Récupere la liste des amis et les mettre en jsonarray
                    log.info("L'utilisateur existe");

                    List<Amis> amisList = amisService.findById(id);

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    for (Amis ami : amisList) {
                        JsonObject amiJson = Json.createObjectBuilder()
                                .add("idGroupeMessagesPrives", ami.getIdGroupeMessagesPrives())
                                .add("statut", ami.getStatut().toString())
                                .add("dateDemande", ami.getDateDemande().toString())
                                .add("userIdAmiDe", ami.getUserIdAmiDe())
                                .add("userIdDemandeur", ami.getUserIdDemandeur())
                                .add("username", ami.getUser().getName())
                                .add("userId", ami.getUser().getId())
                                .build();

                        arrayBuilder.add(amiJson);
                    }

                    JsonArray amisJsonArray = arrayBuilder.build();

                    SendJSON.OnlyInArray(response, amisJsonArray);

                }
            }
        }catch(Exception e){
            log.error("Erreur dans la requête: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Erreur dans la requête");
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void destroy() {

    }
}