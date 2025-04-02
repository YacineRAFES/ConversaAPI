//package fr.afpa.dev.pompey.conversaapi.securite;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import javax.crypto.SecretKey;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.Date;
//
//import static fr.afpa.dev.pompey.conversaapi.utilitaires.Config.getCLE_PRIVEE;
//
//public class JWTutils {
//    // TODO: A FAIRE
//    // https://github.com/jwtk/jjwt?tab=readme-ov-file#creating-a-jwt
//
//
//
//
//
//
//    public static String generateToken(String username) {
//        return Jwts.builder()                     // (1)
//
//                .subject("Bob")                             // (3) JSON Claims, or
//                //.content(aByteArray, "text/plain")        //     any byte[] content, with media type
//
////                .signWith(signingKey)                       // (4) if signing, or
//                //.encryptWith(key, keyAlg, encryptionAlg)  //     if encrypting
//
//                .compact();
//    }
//
//    public static Claims verifyToken(String token) throws Exception {
//        return Jwts.parser()
//                .verifyWith(publicKey) // Verification de la signature
//                .build()
//                .parseSignedClaims(token);
//    }
//
////    private static PublicKey getPublicKey() throws Exception {
////        byte[] publicKeyBytes = Base64.getDecoder().decode(getCLE_PRIVEE());
////        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
////        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
////        return keyFactory.generatePublic(keySpec);
////    }
//}
