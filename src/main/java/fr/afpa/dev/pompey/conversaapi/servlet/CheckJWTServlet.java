package fr.afpa.dev.pompey.conversaapi.servlet;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
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
import java.util.Objects;

import static fr.afpa.dev.pompey.conversaapi.utilitaires.Utils.getNameClass;

@Slf4j
@WebServlet(name = "CheckJWTServlet", value = "/CheckJWT")
public class CheckJWTServlet extends HttpServlet {
    private transient UserService userService;

    @Override
    public void init() {
        this.userService = new UserService(Role.UTILISATEUR);
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + getNameClass());
            String jwt = jsonObject.getString("jwt");
            log.info(getNameClass() + " jwt : " + jwt);
            if (jwt != null) {
                User user = userService.get(JWTutils.VerificationJWT(jwt).getId());
                if (user != null) {
                    log.info("JWT valide");
                    JsonObject userJson = Json.createObjectBuilder()
                            .add("userId", user.getId())
                            .add("userName", user.getName())
                            .add("userEmail", user.getEmail())
                            .add("userRole", user.getRole())
                            .build();

                    SendJSON.SuccessWithObject(response, "jwtValide", "user", userJson);
                } else {
                    log.error("{}JWT invalide", getNameClass());
                    SendJSON.Error(response, "jwtInvalide");
                    throw new ServletException("JWT invalide");
                }
            } else {
                log.error("JWT vide");
                SendJSON.Error(response, "jwtInvalide");
                throw new ServletException("JWT invalide");
            }
        }catch (Exception e) {
            //Gérer l'erreur
            log.error("Erreur lors de la vérification du JWT: " + e.getMessage());
            SendJSON.Error(response, "ErrorServer");
            throw new ServletException("ErrorServer");
        }
    }
}