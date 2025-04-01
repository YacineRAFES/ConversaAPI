package fr.afpa.dev.pompey.conversaapi.servlet;
import fr.afpa.dev.pompey.conversaapi.securite.Captcha;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
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
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() {
    
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonReader jsonReader = Json.createReader(request.getInputStream());
        JsonObject jsonObject = jsonReader.readObject();
        System.out.println(jsonObject);

        String email = jsonObject.getString("email", "");
        String password = jsonObject.getString("password", "");
        String captcha = jsonObject.getString("cf-turnstile-response", "");

        log.info("Captcha : " + captcha);

        //TODO: CAPTCHA: A REVOIR
        boolean isCaptchaValid = Captcha.verif(captcha);
        if (!isCaptchaValid) {
            log.error("Captcha invalide");
            SendJSON.Error(response, "captchaInvalid");
            throw new ServletException("captchaInvalid");
        } else {
            log.info("Captcha valide");
        }

    }
    
    @Override
    public void destroy() {
    
    }
}