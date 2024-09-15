package com.etna.sportevents.payloads.response;


import java.util.Date;

public class Event {
    private Integer id;
    private String title;
    private String location;
    private Date dateStart;
    private Date dateEnd;
    private Integer max_participants;
    private Double latitude;
    private Double longitude;
    private Sport sport = new Sport();
    private User organizer = new User();
    private Date creationDate;
    private Date updatedDate;

    public Event() {
    }

    public Event(Integer id, String title, String location, Date dateStart, Date dateEnd, Integer max_participants, Double latitude, Double longitude, Sport sport, User organizer, Date creationDate, Date updatedDate) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.max_participants = max_participants;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sport = sport;
        this.organizer = organizer;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getMax_participants() {
        return max_participants;
    }

    public void setMax_participants(Integer max_participants) {
        this.max_participants = max_participants;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
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
