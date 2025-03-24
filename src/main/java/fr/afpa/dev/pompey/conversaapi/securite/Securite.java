package fr.afpa.dev.pompey.conversaapi.securite;

import com.password4j.Hash;
import com.password4j.HashChecker;
import com.password4j.Password;
import com.password4j.SaltGenerator;

import static com.password4j.Password.check;

public class Securite {
    private static final String salt = "salt";
//    Pour plus d'info : https://github.com/Password4j/password4j
//    hashPassword("password", hash)
    /**
     * Hash le mot de passe
     * @param password le mot de passe à hasher
     * @return le hash du mot de passe
     */
    public static String hashPassword(String password) {
        try {
            Hash hash = Password.hash(password)
                        .addRandomSalt()
                        .addPepper(PoivreToken.POIVRE)
                        .withArgon2();

            return hash.getResult();
        }catch (Exception e){
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }
//    checkPassword("password", hash)
    /**
     * Vérifie si le mot de passe correspond au hash
     * @param password le mot de passe à vérifier
     * @param hash le hash à vérifier
     * @return true si le mot de passe correspond au hash, false sinon
     */
    public static boolean checkPassword(String password, String hash) {
        try {
            return Password.check(password, hash)
                    .addPepper(PoivreToken.POIVRE)
                    .withArgon2();
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la vérification du mot de passe");
        }
    }
}
