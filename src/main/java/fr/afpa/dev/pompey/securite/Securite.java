package fr.afpa.dev.pompey.securite;

import com.password4j.Hash;
import com.password4j.HashChecker;
import com.password4j.Password;
import com.password4j.SaltGenerator;

import static com.password4j.Password.check;

public class Securite {
    public static String hashPassword(String password) {
        try {
            Hash hash = Password.hash(password)
                    .addSalt(SaltGenerator.generate())
                    .addPepper(PoivreToken.POIVRE)
                    .withBcrypt();

            return hash.getResult();
        }catch (Exception e){
            throw new RuntimeException("Erreur lors du hashage du mot de passe");
        }

    }
//TODO A FAIRE
    public static boolean checkPassword(String password, String hash) {
        try {
            HashChecker hashChecker = check(password, hash);
            return hashChecker.check();
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la vérification du mot de passe");
        }
    }
}
