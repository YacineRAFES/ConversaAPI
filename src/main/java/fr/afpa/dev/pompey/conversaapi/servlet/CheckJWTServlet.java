package fr.afpa.dev.pompey.conversaapi.servlet;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(name = "CheckJWTServlet", value = "/CheckJWT")
public class CheckJWTServlet extends HttpServlet {

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
            if (jwt != null) {
                if (JWTutils.validateToken(jwt)) {
                    log.info("JWT valide");
                    SendJSON.Success(response, "jwtValide");
                } else {
                    log.error("JWT invalide");
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
        //Verifier le JWT

    }
}