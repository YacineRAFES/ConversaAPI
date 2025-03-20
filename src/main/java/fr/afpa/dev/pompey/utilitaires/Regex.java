package fr.afpa.dev.pompey.utilitaires;

public class Regex {
    public static final String REGEX_NAME = "^[a-zA-ZàáâäçèéêëìíîïñòóôöùúûüÀÁÂÄÇÈÉÊËÌÍÎÏÑÒÓÔÖÙÚÛÜ'\\-\\s]+$";
    public static final String REGEX_EMAIL = "^(?=.{1,64}@)[\\p{L}0-9\\+_-]+(\\.[\\p{L}0-9\\+_-]+)*@"
            + "[^-][\\p{L}0-9\\+-]+(\\.[\\p{L}0-9\\+-]+)*(\\.[\\p{L}]{2,})$";

    //TODO: REGEX A FAIRE
}
