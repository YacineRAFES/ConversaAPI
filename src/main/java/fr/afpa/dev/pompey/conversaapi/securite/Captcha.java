package fr.afpa.dev.pompey.conversaapi.securite;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Captcha {

    public static boolean verif(String captcha) throws IOException {
        //SECRET_KEY
        String SECRET_KEY = System.getenv("SECRET_KEY_CAPTCHA");
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
        String params = "secret=" + URLEncoder.encode(SECRET_KEY, StandardCharsets.UTF_8) + "&response=" + URLEncoder.encode(captcha, StandardCharsets.UTF_8);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Convertir la réponse JSON en objet Java
        try(JsonReader jsonReader = Json.createReader(new StringReader(response.toString()))) {
            JsonObject jsonObject = jsonReader.readObject();
            System.out.println("Depuis Captcha.verif: " + jsonObject);
            return jsonObject.getBoolean("success");
        }catch (Exception e) {
            System.out.println("Erreur de parsing Captcha JSON: " + e.getMessage());
            return false;
        }
    }
}
