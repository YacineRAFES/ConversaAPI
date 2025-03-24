package fr.afpa.dev.pompey.conversaapi.utilitaires;

public class Utils {
    public static String getNameClass() {
        return " Nom de la classe : "+Thread.currentThread().getStackTrace()[2].getClassName();
    }
}
