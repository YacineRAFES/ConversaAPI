package fr.afpa.dev.pompey.securite;

import com.password4j.Hash;
import com.password4j.Password;

public class Securite {
    public static String hashPassword(String password) {
        Hash hash = Password.hash(password)
                .addPepper(PoivreToken.POIVRE)
                .withBcrypt();

        return hash.getResult();
    }
}
