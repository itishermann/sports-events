package com.etna.sportevents.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class CommentEntity {

    public CommentEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_comment")
    private Integer id;

    @Column(nullable = false, length = 255, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 255, name = "comment")
    private String comment;

    @Column(nullable = false, name = "rating")
    private Float rating;

    @Column(nullable = false, name = "is_approved", columnDefinition = "boolean default false")
    private Boolean approved;

    @ManyToOne
    @JoinColumn(name="id_event", nullable=false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name="id_commenter", nullable=false)
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
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
        if (!(o instanceof CommentEntity)) return false;
        CommentEntity comment = (CommentEntity) o;
        return Objects.equals(getUuid(), comment.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
            "id=" + id +
            ", uuid=" + uuid +
            ", comment='" + comment + '\'' +
            ", rating=" + rating +
            ", approved=" + approved +
            ", creationDate=" + creationDate +
            ", updatedDate=" + updatedDate +
            '}';
    }
}
