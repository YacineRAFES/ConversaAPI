package fr.afpa.dev.pompey;

import static fr.afpa.dev.pompey.securite.Securite.hashPassword;

public class Main {
    public static void main(String[] args) {
        System.out.println(hashPassword("password"));
    }
}