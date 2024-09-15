package com.etna.sportevents.payloads.response;

import java.util.Date;


public class Registration {

    public Registration() {
    }

    public Registration(Integer id, Date date, Event event, User user, Date creationDate, Date updatedDate) {
        this.id = id;
        this.date = date;
        this.event = event;
        this.user = user;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
    }

    private Integer id;
    private Date date;

    private Event event;

    private User user;
    private Date creationDate;
    private Date updatedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
