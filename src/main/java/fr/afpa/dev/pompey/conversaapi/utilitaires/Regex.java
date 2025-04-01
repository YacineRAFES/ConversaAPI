package fr.afpa.dev.pompey.conversaapi.utilitaires;

public class Regex {
    private Regex() {}
    public static String NAME = "^[a-zA-ZàáâäçèéêëìíîïñòóôöùúûüÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜ'\\-\\s]+$";
    public static String EMAIL = "^(?=.{1,64}@)[\\p{L}0-9\\+_-]+(\\.[\\p{L}0-9\\+_-]+)*@"
            + "[^-][\\p{L}0-9\\+-]+(\\.[\\p{L}0-9\\+-]+)*(\\.[\\p{L}]{2,})$";
    public static String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static String USERNAME = "^[a-zA-Z0-9](?!.*[_.-]{2})[a-zA-Z0-9_.-]{6,50}[a-zA-Z0-9]$";

    //TODO: REGEX A FAIRE
}
