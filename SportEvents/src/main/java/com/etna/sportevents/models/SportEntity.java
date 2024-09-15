package com.etna.sportevents.models;


import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sports")
public class SportEntity {

    public SportEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_sport")
    private Integer id;

    @Column(nullable = false, length = 255, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @OneToMany(mappedBy="sport", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<EventEntity> events;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(Set<EventEntity> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SportEntity)) return false;
        SportEntity sport = (SportEntity) o;
        return Objects.equals(getUuid(), sport.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        return "Sport{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }
}
