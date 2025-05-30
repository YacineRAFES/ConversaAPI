package fr.afpa.dev.pompey.conversaapi.servlet;
import com.password4j.Password;
import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.utilitaires.AlertMsg;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static fr.afpa.dev.pompey.conversaapi.securite.Securite.checkPassword;
@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService(Role.UTILISATEUR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON REÇU depuis: {}{}", jsonObject, Utils.getNameClass());

            String email = jsonObject.getString("email", "").trim();
            String password = jsonObject.getString("password", "").trim();

            if (email.isEmpty() || password.isEmpty()) {
                log.error("Champs email ou mot de passe vides");
                SendJSON.Error(response, "emptyField");
                return;
            }

            log.info("Champs valides");

            User userFind = userService.trouveParEmail(email);

            if (userFind == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, "userNotFound");
                return;
            }

            log.info("Utilisateur trouvé: {}", userFind);

            // Verifie si le mdp saisie et le mdp hashé est correct
            if (!checkPassword(password, userFind.getPassword())) {
                log.error("Mot de passe incorrect");
                SendJSON.Error(response, AlertMsg.INVALIDCREDENTIALS);
                return;
            }

            // Generation JWT
            String jwtToken = JWTutils.generateToken(
                    userFind.getId(),
                    userFind.getEmail(),
                    userFind.getName(),
                    userFind.getRole()
            );

            log.info("Token JWT généré: {}", jwtToken);

            SendJSON.LoginUser(response, jwtToken, userFind.getId().toString(), userFind.getName());

        } catch (JsonException e) {
            log.error("Erreur JSON détectée", e);
            SendJSON.Error(response, "jsonError");
        } catch (Exception e) {
            log.error("Erreur inattendue", e);
            SendJSON.Error(response, "serverError");
        }
    }

}