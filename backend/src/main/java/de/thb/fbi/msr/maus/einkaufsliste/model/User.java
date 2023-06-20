package de.thb.fbi.msr.maus.einkaufsliste.model;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String email;

    // its insecure but for this usecase its enough
    private String password;

    public User() {
        // TODO Auto-generated constructor stub
    }

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public boolean equals(Object other) {
        if(!(other instanceof User)) return false;
        return ((User) other).getId() == this.getId();
    }
}

