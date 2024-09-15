package com.etna.sportevents.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {

    public RegistrationEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_registration")
    private Integer id;

    @Column(nullable = false, length = 255, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, columnDefinition = "DATETIME", name = "registration_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="id_event", nullable=false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name="id_participant", nullable=false)
    private UserEntity user;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationEntity)) return false;
        RegistrationEntity that = (RegistrationEntity) o;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", date=" + date +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
