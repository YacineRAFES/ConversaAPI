package fr.afpa.dev.pompey.modele;

import fr.afpa.dev.pompey.securite.Securite;
import fr.afpa.dev.pompey.utilitaires.Regex;
import fr.afpa.dev.pompey.exception.SaisieException;

import java.sql.Date;
import java.time.LocalDate;

public class User {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private String role;
    private Date date;

//    CONSTRUCTEURS
    public User(){

    }

    public User(Integer id, String name, String password, String email, String role, Date date) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
        this.role = role;
    }

    public User(Integer id){
        this.id = id;
    }

//    GETTER ET SETTER

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws SaisieException {
        if (id == null) {
            throw new SaisieException("L'id ne doit pas être vide ou null");
        } else if (id <= 0) {
            throw new SaisieException("L'id ne doit pas être négatif");
        }
        this.id = id;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) throws SaisieException {
        if (name == null || name.isEmpty()) {
            throw new SaisieException("Le nom ne doit pas être vide ou null");
        } else if (!name.matches(Regex.REGEX_NAME)) {
            throw new SaisieException("Le nom ne corresponds pas");
        }
        this.name = name;
    }

    /**
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) throws SaisieException {
        if (password == null || password.isEmpty()) {
            throw new SaisieException("Le mot de passe ne doit pas être vide ou null");
        } else if (!password.matches(Regex.REGEX_PASSWORD)) {
            throw new SaisieException("Le mot de passe ne respecte pas les critères");
        }
        this.password = Securite.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws SaisieException {
        if (email == null || email.isEmpty()) {
            throw new SaisieException("L'email ne doit pas être vide ou null");
        } else if (!email.matches(Regex.REGEX_EMAIL)) {
            throw new SaisieException("L'email ne corresponds pas");
        }
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) throws SaisieException {
        if (role == null || role.isEmpty()) {
            throw new SaisieException("Le role ne doit pas être vide ou null");
        }
        this.role = role;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) throws SaisieException {
        if (date == null) {
            throw new SaisieException("La date ne doit pas être vide ou null");
        } else if (!date.toLocalDate().equals(LocalDate.now())) {
            throw new SaisieException("La date ne corresponds pas");
        }
        this.date = date;
    }
}
