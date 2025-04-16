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
    // https://github.com/jwtk/jjwt?tab=readme-ov-file#creating-a-jwt

    private static final String SECRET_KEY = getCLE_PRIVEE();
    private static final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(int id, String email, String username, String role) {

        return Jwts.builder()
                .issuer("ConversaAPI") // Identifiant de l’émetteur
                .subject(username)
                .claim("id", id)
                .claim("email", email)
                .claim("name", username)
                .claim("roles", role)
                .issuedAt(new java.util.Date(System.currentTimeMillis())) // Date d’émission
                .expiration(new java.util.Date(System.currentTimeMillis() + 60 * 60)) // Expiration (1h)
                .signWith(secretKey)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * Récupère les informations de l'utilisateur à partir du token
     * @param token le token à décoder
     * @return les informations de l'utilisateur
     */
    public static Claims getUserInfoFromToken(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.error("Erreur lors de la récupération des informations de l'utilisateur à partir du token : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des informations de l'utilisateur à partir du token");
        }
    }

}
