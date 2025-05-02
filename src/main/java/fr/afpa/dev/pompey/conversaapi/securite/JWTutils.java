package fr.afpa.dev.pompey.conversaapi.securite;

import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.utilitaires.SendJSON;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;

import static fr.afpa.dev.pompey.conversaapi.utilitaires.Config.getCLE_PRIVEE;

// TODO: A REVOIR
@Slf4j
public class JWTutils {

    private static Key getSecretKey() {
        String secret = getCLE_PRIVEE();

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("La clé secrète est invalide ou non chargée");
        }

        log.info("Clé utilisée pour le JWT : " + secret);
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public static String generateToken(int id, String email, String username, String role) {
        return Jwts.builder()
                .issuer("ConversaAPI")
                .subject(username)
                .claim("id", id)
                .claim("email", email)
                .claim("name", username)
                .claim("roles", role)
                .issuedAt(new java.util.Date(System.currentTimeMillis()))
                .expiration(new java.util.Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1h
                .signWith(getSecretKey())
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Erreur de validation du token : " + e.getMessage());
            return false;
        }
    }

    public static Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des informations de l'utilisateur : " + e.getMessage());
            throw new RuntimeException("Token invalide");
        }
    }

    public static User VerificationJWT(HttpServletResponse response, String jwt) throws SaisieException, RegexException {
        //Verification JWT
        if (jwt != null) {
            if (JWTutils.validateToken(jwt)) {
                log.info(Utils.getNameClass() + "JWT valide");
            } else {
                log.error(Utils.getNameClass() + "JWT invalide");
                SendJSON.Error(response, "jwtInvalide");
                return null;
            }
        } else {
            log.error(Utils.getNameClass() + "JWT vide");
            SendJSON.Error(response, "jwtInvalide");
            return null;
        }



        //Recuperation de l'ID de l'utilisateur
        Claims claims = JWTutils.getUserInfoFromToken(jwt);
        if (claims == null) {
            log.error("Token claims null");
            SendJSON.Error(response, "invalidToken");
            return null;
        }

        Integer id = claims.get("id", Integer.class);
        String email = claims.get("email", String.class);
        String username = claims.get("name", String.class);
        String role = claims.get("roles", String.class);
        if (id == null || email == null || username == null || role == null) {
            log.error("Données manquantes dans le token");
            SendJSON.Error(response, "missingDataInToken");
            return null;
        }

        return new User(
                id
        );

    }
}

