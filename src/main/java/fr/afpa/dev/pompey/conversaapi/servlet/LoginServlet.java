package fr.afpa.dev.pompey.conversaapi.servlet;
import com.password4j.Password;
import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.securite.Captcha;
import fr.afpa.dev.pompey.conversaapi.securite.JWTutils;
import fr.afpa.dev.pompey.conversaapi.securite.Securite;
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
    private static final String CSRFTOKEN = "csrfToken";
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();
            String csrfToken = (String) session.getAttribute(CSRFTOKEN);

            if (csrfToken == null) {
                csrfToken = UUID.randomUUID().toString();
                session.setAttribute(CSRFTOKEN, csrfToken);
            }

            log.info(csrfToken);

            SendJSON.Token(response, CSRFTOKEN, csrfToken);
        }catch (JsonException e){
            log.error("Erreur JSON détectée", e);
        }catch (Exception e){
            log.error("Erreur inattendue", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info("JSON RECU depuis: "+jsonObject + Utils.getNameClass());

            String email = jsonObject.getString("email", "");
            String password = jsonObject.getString("password", "");
            String captcha = jsonObject.getString("cf-turnstile-response", "");

            log.info("Captcha : " + captcha);

            boolean isCaptchaValid = Captcha.verif(captcha);
            if (!isCaptchaValid) {
                log.error("Captcha invalide");
                SendJSON.Error(response, "captchaInvalid");
                return;
            }
            log.info("Captcha valide");

            if(email.trim().isEmpty() && password.trim().isEmpty()) {
                log.error("Les champs sont vides");
                SendJSON.Error(response, "emptyField");
                return;
            }
            log.info("Champs valides");

            List<User> users = userService.getAllUsers();
            User userFind = null;
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    log.info("Utilisateur trouvé: " + user);
                    userFind = user;
                }else{
                    log.info("Utilisateur non trouvé: " + email);
                    SendJSON.Error(response, AlertMsg.ErreurIdentifiant);
                    return;
                }
            }
            log.info("Utilisateur trouvé: " + userFind);

            if (userFind == null) {
                log.error("Utilisateur non trouvé");
                SendJSON.Error(response, AlertMsg.ErreurIdentifiant);
                return;
            }
            log.info("Utilisateur valide");

            if(!checkPassword(password, userFind.getPassword())) {
                log.error("Mot de passe incorrect");
                SendJSON.Error(response, AlertMsg.ErreurIdentifiant);
                return;
            }
            log.info("Mot de passe correct");

            //Generation du token JWT
            String jwtToken = JWTutils.generateToken(userFind.getId(), userFind.getName(), userFind.getEmail(), userFind.getRole());
            log.info("Token JWT généré: " + jwtToken);
            //Envoi du token JWT
            SendJSON.LoginUser(response, jwtToken);

        }catch(JsonException e){
            log.error("Erreur JSON détectée", e);
        }catch (Exception e){
            log.error("Erreur inattendue", e);
        }
    }
}