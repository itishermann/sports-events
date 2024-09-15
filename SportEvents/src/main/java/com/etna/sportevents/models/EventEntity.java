package com.etna.sportevents.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
public class EventEntity {

    public EventEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_event")
    private Integer id;

    @Column(nullable = false, length = 255, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 255, name = "event_title")
    private String title;

    @Column(nullable = false, length = 255, name = "event_location")
    private String location;

    @Column(nullable = false, columnDefinition = "DATETIME", name = "event_date")
    private Date dateStart;

    @Column(nullable = false, columnDefinition = "DATETIME", name = "event_end")
    private Date dateEnd;

    @Column(nullable = false)
    private Integer max_participants;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne
    @JoinColumn(name="id_sport", nullable=false)
    private SportEntity sport;

    @ManyToOne
    @JoinColumn(name="id_organizer", nullable=false)
    private UserEntity organizer;

    @OneToMany(mappedBy="event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationEntity> registrations;

    @OneToMany(mappedBy="event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> comments;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME", name = "creation_date")
    private Date creationDate = new Date();

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME" , name = "updated_date")
    private Date updatedDate = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public UserEntity getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserEntity organizer) {
        this.organizer = organizer;
    }

    public Set<RegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<RegistrationEntity> registrations) {
        this.registrations = registrations;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventEntity)) return false;
        EventEntity event = (EventEntity) o;
        return Objects.equals(getUuid(), event.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        return "EventEntity{" +
            "id=" + id +
            ", uuid=" + uuid +
            ", title='" + title + '\'' +
            ", location='" + location + '\'' +
            ", dateStart=" + dateStart +
            ", dateEnd=" + dateEnd +
            ", max_participants=" + max_participants +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", creationDate=" + creationDate +
            ", updatedDate=" + updatedDate +
            '}';
    }
}
