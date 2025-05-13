package fr.afpa.dev.pompey.conversaapi.modele;

import fr.afpa.dev.pompey.conversaapi.exception.RegexException;
import fr.afpa.dev.pompey.conversaapi.securite.Securite;
import fr.afpa.dev.pompey.conversaapi.utilitaires.Regex;
import fr.afpa.dev.pompey.conversaapi.exception.SaisieException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.time.LocalDate;

@Slf4j
public class User {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private String role;
    private Date date;
    private boolean valide;

//    CONSTRUCTEURS
    public User(){

    }

    public User(Integer id, String name, Date date, String email, String role){
        this.id = id;
        this.name = name;
        this.date = date;
        this.email = email;
        this.role = role;
    }

    public User(Integer id, String name, Date date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    /**
     * Prend un nom d'utilisateur
     * @param name
     */
    public User(String name){
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param password
     * @param email
     * @param role
     * @param date
     */
    public User(Integer id, String name, String password, String email, String role, Date date) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
        this.role = role;
    }

    /**
     * @param id
     * @param name
     */
    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Integer id, String name, Date date, String email, String role, boolean valide) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.email = email;
        this.role = role;
        this.valide = valide;
    }

    /**
     * @param id
     * @param name
     * @param password
     * @param email
     * @param role
     * @param date
     */
    public User(Integer id, String name, String password, String email, String role, Date date, boolean valide) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
        this.role = role;
        this.valide = valide;
    }

    /** Création d'un utilisateur
     * @param name
     * @param password
     * @param email
     * @param role
     * @param date
     */
    public User(String name, String password, String email, String role, Date date, boolean valide) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
        this.role = role;
        this.valide = valide;
    }

    /**
     * Modification un compte par un utilisateur
     * @param id
     * @param name
     * @param password
     * @param email
     */
    public User(Integer id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    /**
     * @param name
     * @param password
     * @param email
     * @param role
     */
    public User(String name, String password, String email, String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Utiliser pour modifier un compte d'utilisateur par un SuperAdmin
     * @param id
     * @param name
     * @param email
     * @param role
     * @param valide
     */
    public User(Integer id, String name, String email, String role, boolean valide) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.valide = valide;
    }

    /**
     * Prend un id d'utilisateur
     * @param id
     */
    public User(Integer id){
        this.id = id;
    }

//    GETTER ET SETTER

    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
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
    public void setName(String name) throws RegexException, SaisieException {
        if (name == null || name.isEmpty()) {
            throw new SaisieException("Le nom ne doit pas être vide ou null");
        } else if (!name.trim().matches(Regex.USERNAME)) {
            log.info(name);
            throw new RegexException("Le nom ne corresponds pas");
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
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws SaisieException {
        if (email == null || email.isEmpty()) {
            throw new SaisieException("L'email ne doit pas être vide ou null");
        } else if (!email.matches(Regex.EMAIL)) {
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
        } else if (date.toLocalDate().isAfter(LocalDate.now())) {
            throw new SaisieException("La date ne corresponds pas");
        }
        this.date = date;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) throws SaisieException {
        this.valide = valide;
    }
}
