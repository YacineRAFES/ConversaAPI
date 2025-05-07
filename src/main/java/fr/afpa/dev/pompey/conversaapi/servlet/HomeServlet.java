package fr.afpa.dev.pompey.conversaapi.servlet;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
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

import static fr.afpa.dev.pompey.conversaapi.securite.JWTutils.VerificationJWT;

@Slf4j
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private transient UserService userService;

    @Override
    public void init() {
        this.userService = new UserService(Role.UTILISATEUR);


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: " + jsonObject + Utils.getNameClass());
            String jwt = jsonObject.getString("jwt");
            log.info(Utils.getNameClass() + " jwt : " + jwt);

            User user = userService.get(VerificationJWT(jsonObject.getString("jwt")).getId());

            if(user == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, "userNotFound");
                return;
            }

        }catch (Exception e){

        }
    }

}