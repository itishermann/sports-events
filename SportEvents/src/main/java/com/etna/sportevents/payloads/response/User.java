package com.etna.sportevents.payloads.response;

import com.etna.sportevents.models.*;

import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String email;
    private UserRole role;
    private Date creationDate;
    private Date updatedDate;

    public User() {
    }

    public User(Integer id, String username, String email, UserRole role, Date creationDate, Date updatedDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
