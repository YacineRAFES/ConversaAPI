package fr.afpa.dev.pompey.conversaapi.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Scanner;

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
}

