package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.emuns.Role;
import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Regex;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.json.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import static fr.afpa.dev.pompey.conversaapi.securite.Securite.hashPassword;

/**
 * Servlet pour les utilisateurs.
 */
@Slf4j
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private transient UserService userService;

    /**
     * Initialise la servlet.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        this.userService = new UserService(Role.UTILISATEUR);
    }

    /**
     * Récupère tous les utilisateurs et les renvoie en JSON.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            log.info("DEMANDE DE CREATION D'UN UTILISATEUR");
            JsonReader jsonReader = Json.createReader(request.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            log.info(String.valueOf(jsonObject));

            String username = jsonObject.getString("username", "");
            String email = jsonObject.getString("email", "");
            String password1 = jsonObject.getString("password1", "");
            String password2 = jsonObject.getString("password2", "");


            // Verifie les champs ne sont pas vides
            if(username.trim().isEmpty() && email.trim().isEmpty() && password1.trim().isEmpty() && password2.trim().isEmpty()) {
                log.error("Les champs sont vides");
                SendJSON.Error(response, "emptyField");
                return;
            }

            List<User> users = userService.getAll();

            // Vérifie la longueur des champs
            log.info("Verifie les longueurs de l'utilisateur et l'email");
            if (username.length() > 50 && email.length() > 50) {
                log.info("le champs username respecte la longueur du caractère");
                SendJSON.Error(response, "lengthInvalid");
                return;
            }
            log.info("Verification de la longueur : OK");

            // Vérifie le format de l'email
            log.info("Verifie le format de l'email");
            if (!email.matches(Regex.EMAIL)) {
                log.error("L'email est invalide lors de la REGEX.EMAIL");
                SendJSON.Error(response, "emailInvalid");
                return;
            }
            log.info("Verification de l'email : OK");

                // Vérifie si l'utilisateur existe déjà
            log.info("Verifie si l'utilisateur et email existe déjà");
            for (User user : users) {
                if (user.getName().equals(username)) {
                    log.error("Le nom d'utilisateur existe déjà");
                    SendJSON.Error(response, "userAlreadyExists");
                    return;
                }
                if (user.getEmail().equals(email)) {
                    log.error("L'email existe déjà");
                    SendJSON.Error(response, "userAlreadyExists");
                    return;
                }
            }
            log.info("Verification de l'utilisateur et email : OK");



            // Verifie le mot de passe
            log.info("Verifie le mot de passe et la confirmation");
            if(!password1.equals(password2)) {
                log.error("Les mots de passe ne correspondent pas");
                SendJSON.Error(response, "passwordInvalid");
                return;
            }
            log.info("Verification de la correspondance du mot de passe : OK");

            // Vérifie les critères du mot de passe si oui, on le hash
            log.info("Verifie les critères du mot de passe");
            if(!password1.matches(Regex.PASSWORD)) {
                log.error("le mot de passe ne respect pas le critère");
                SendJSON.Error(response, "passwordInvalid");
                return;
            }
            log.info("Verification du mot de passe : OK");

            // Hash le mot de passe
            log.info("Hash le mot de passe");
            String pwHash = hashPassword(password1);
            log.info(String.valueOf(pwHash.getBytes().length));
            log.info(pwHash);


            User user = new User(username, pwHash, email, "user", Date.valueOf(LocalDate.now()), true);

            userService.add(user);
            log.info("L'utilisateur a été ajouté avec succès");
            SendJSON.Success(response, "userCreated");

        }catch(JsonException e){
            log.error("Erreur JSON détectée", e);
        }catch (Exception e){
            log.error("Erreur inattendue", e);
            SendJSON.Error(response, "errorServer");
        }
    }
}