package com.etna.sportevents.payloads.request;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

public class CreateEventPayload {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Location is required")
    @Size(min = 1, max = 255, message = "Location must be between 1 and 255 characters")
    private String location;

    @Future(message = "Date start must be in the future")
    private Date dateStart;

    @Future(message = "Date end must be in the future")
    private Date dateEnd;

    @NotNull(message = "Max participants is required")
    @Min(value = 1, message = "Max participants must be greater than 1")
    private Integer maxParticipants;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Sport is required")
    private Integer sportId;

    public CreateEventPayload(String title, String location, Date dateStart, Date dateEnd, Integer maxParticipants, Double latitude, Double longitude, Integer sportId) {
        this.title = title;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.maxParticipants = maxParticipants;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sportId = sportId;
    }

    public CreateEventPayload() {
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

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
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

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateEventPayload)) return false;
        CreateEventPayload that = (CreateEventPayload) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getDateStart(), that.getDateStart()) && Objects.equals(getDateEnd(), that.getDateEnd()) && Objects.equals(getMaxParticipants(), that.getMaxParticipants()) && Objects.equals(getLatitude(), that.getLatitude()) && Objects.equals(getLongitude(), that.getLongitude()) && Objects.equals(getSportId(), that.getSportId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getLocation(), getDateStart(), getDateEnd(), getMaxParticipants(), getLatitude(), getLongitude(), getSportId());
    }

    @Override
    public String toString() {
        return "CreateEventPayload{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", maxParticipants=" + maxParticipants +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", sportId=" + sportId +
                '}';
    }
}
