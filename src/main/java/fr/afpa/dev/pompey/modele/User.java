package fr.afpa.dev.pompey.modele;

import java.sql.Date;
import java.time.LocalDate;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String role;
    private Date date;

//    CONSTRUCTEURS
    public User(){

    }

    public User(int id, String name, String password, String email, String role, Date date) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
        this.role = role;
    }

    public User(int id){
        this.id = id;
    }

//    GETTER ET SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
